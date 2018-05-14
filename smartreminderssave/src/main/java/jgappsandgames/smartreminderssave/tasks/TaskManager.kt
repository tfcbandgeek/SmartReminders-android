package jgappsandgames.smartreminderssave.tasks

// Java
import android.util.Log
import java.io.File
import java.io.IOException

// JSONObject
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

// Save Library
import jgappsandgames.smartreminderssave.utility.API
import jgappsandgames.smartreminderssave.utility.FileUtility
import jgappsandgames.smartreminderssave.utility.JSONUtility

/**
 * TaskManager
 * Created by joshua on 12/12/2017.
 */
class TaskManager {
    companion object {
        // Constants -------------------------------------------------------------------------------
        private const val FILENAME = "taskmanager.srj"

        private const val VERSION = "version"
        private const val META = "meta"
        private const val HOME = "home"
        private const val TASKS = "tasks"
        private const val ARCHIVED = "archived"
        private const val DELETED = "deleted"

        // Data ------------------------------------------------------------------------------------
        private var version: Int = 0
        @JvmField
        var meta: JSONObject = JSONObject()
        @JvmField
        var home: ArrayList<String> = ArrayList()
        @JvmField
        var tasks: ArrayList<String> = ArrayList()
        @JvmField
        var archived: ArrayList<String> = ArrayList()
        @JvmField
        var deleted: ArrayList<String> = ArrayList()

        // Management Methods ----------------------------------------------------------------------
        @JvmStatic
        fun create() {
            if (File(FileUtility.getApplicationDataDirectory(), FILENAME).exists()) load()

            home = ArrayList()
            tasks = ArrayList()
            archived = ArrayList()
            deleted = ArrayList()

            // API 11
            meta = JSONObject()
        }

        @JvmStatic
        fun forceCreate() {
            home = ArrayList()
            tasks = ArrayList()
            archived = ArrayList()
            deleted = ArrayList()

            // API 11
            meta = JSONObject()
        }

        @JvmStatic
        fun load() {
            try {
                loadJSON(JSONUtility.loadJSON(File(FileUtility.getApplicationDataDirectory(), FILENAME)))
            } catch (i: IOException) {
                i.printStackTrace()

                create()
                save()
            }

            if (deleted.size >= 50) deleted.removeAt(0)
        }

        @JvmStatic
        fun save() {
            JSONUtility.saveJSONObject(File(FileUtility.getApplicationDataDirectory(), FILENAME), saveJSON())
        }

        @JvmStatic
        fun clearTasks() {
            for (task in archived) deleteTask(Task(task))
            archived = ArrayList()
            save()
        }

        // JSONManagement Methods ------------------------------------------------------------------
        @JvmStatic
        fun loadJSON(data: JSONObject?) {
            if (data == null) {
                create()
                return
            }

            version = data.optInt(VERSION, API.RELEASE)

            home = ArrayList()
            tasks = ArrayList()
            archived = ArrayList()
            deleted = ArrayList()

            val h = data.optJSONArray(HOME)
            val t = data.optJSONArray(TASKS)
            val a = data.optJSONArray(ARCHIVED)
            val d = data.optJSONArray(DELETED)

            if (h != null && h.length() != 0)
                for (i in 0 until h.length())
                    if (!home.contains(h.optString(i))) home.add(h.optString(i))
            if (t != null && t.length() != 0)
                for (i in 0 until t.length())
                    if (!tasks.contains(t.optString(i))) tasks.add(t.optString(i))
            if (a != null && a.length() != 0)
                for (i in 0 until a.length())
                    if (!archived.contains(a.optString(i))) archived.add(a.optString(i))
            if (d != null && d.length() != 0)
                for (i in 0 until d.length())
                    if (!deleted.contains(d.optString(i))) deleted.add(d.optString(i))

            // API 11
            if (version >= API.MANAGEMENT) {
                meta = data.optJSONObject(META) ?: JSONObject()
            } else {
                meta = JSONObject()
            }
        }

        @JvmStatic
        fun saveJSON(): JSONObject {
            val data = JSONObject()

            try {
                data.put(VERSION, API.MANAGEMENT)

                val h = JSONArray()
                val t = JSONArray()
                val a = JSONArray()
                val d = JSONArray()

                for (task in home) h.put(task)
                for (task in tasks) t.put(task)
                for (task in archived) a.put(task)
                for (task in deleted) d.put(task)

                data.put(HOME, h)
                data.put(TASKS, t)
                data.put(ARCHIVED, a)
                data.put(DELETED, d)

                // API 11
                data.put(META, meta)
            } catch (j: JSONException) {
                j.printStackTrace()
            }

            return data
        }

        // Task Methods ----------------------------------------------------------------------------
        @JvmStatic
        fun archiveTask(task: Task) {
            task.markArchived()
            task.save()

            when {
                task.getParent() == "home" -> home.remove(task.getFilename())

                tasks.contains(task.getParent()) -> {
                    val parent = Task(task.getParent())
                    parent.removeChild(task.getFilename())
                    parent.save()
                }

                else -> {
                    try {
                        Log.v("TaskManager", "Archive Task Parent is NOT in the database")
                        val parent = Task(task.getParent())
                        parent.removeChild(task.getFilename())
                        parent.save()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            if (task.getType() == Task.TYPE_FLDR) {
                for (i in 0 until task.getChildren().size) archiveTask(Task(task.getChildren()[i]))
            }

            tasks.remove(task.getFilename())
            archived.add(task.getFilename())
            save()
        }

        @JvmStatic
        fun deleteTask(task: Task): Boolean {
            if (archived.contains(task.getFilename())) {
                task.delete()
                deleted.add(task.getFilename())
                archived.remove(task.getFilename())
                save()
                return true
            }

            return false
        }

        // Task Getters ----------------------------------------------------------------------------
        fun getTasks(tasks: ArrayList<String>): ArrayList<Task> {
            val r = ArrayList<Task>(tasks.size)
            for (i in 0 until tasks.size) r.add(Task(filename = tasks[i]))
            return r
        }

        fun getHome(): ArrayList<String> {
            return home
        }

        fun getHomeTasks(): ArrayList<Task> {
            return getTasks(home)
        }

        fun getAll(): ArrayList<String> {
            return tasks
        }

        fun getAllTasks(): ArrayList<Task> {
            return getTasks(tasks)
        }

        fun getArchived(): ArrayList<String> {
            return archived
        }

        fun getArchivedTasks(): ArrayList<Task> {
            return getTasks(archived)
        }

        fun getDeleted(): ArrayList<String> {
            return deleted
        }
    }
}
package jgappsandgames.smartreminderssave.tasks

// Java
import java.io.File
import java.io.IOException
import java.util.ArrayList

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
        private val FILENAME = "taskmanager.srj"

        private val VERSION = "version"
        private val META = "meta"
        private val HOME = "home"
        private val TASKS = "tasks"
        private val ARCHIVED = "archived"
        private val DELETED = "deleted"

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
                meta = data.optJSONObject(META)
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

            if (task.getParent() == "home") {
                home.remove(task.getFilename())
            } else if (tasks.contains(task.getParent())) {
                val parent = Task(task.getParent())
                parent.save()
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
    }
}
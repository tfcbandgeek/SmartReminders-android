package jgappsandgames.smartreminderssave.tasks

import jgappsandgames.smartreminderssave.utility.API
import jgappsandgames.smartreminderssave.utility.FileUtility
import jgappsandgames.smartreminderssave.utility.JSONUtility
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.util.ArrayList

/**
 * TaskManager
 * Created by joshua on 12/1/2017.
 */
class TaskManager {
    companion object {
        // Filepath Constant
        private val FILENAME = "taskmanager.srj"

        // Save Constants
        private val VERSION = "version"
        private val META = "meta"

        private val HOME = "home"
        private val TASKS = "tasks"
        private val ARCHIVED = "archived"
        private val DELETED = "deleted"

        // Data
        @JvmField
        var version: Int = 0
        @JvmField
        var meta: JSONObject? = null
        @JvmField
        var home: ArrayList<String> = ArrayList()
        @JvmField
        var tasks: ArrayList<String> = ArrayList()
        @JvmField
        var archived: ArrayList<String> = ArrayList()
        @JvmField
        var deleted: ArrayList<String> = ArrayList()

        // Management Methods
        @JvmStatic
        fun create() {
            version = API.MANAGEMENT

            home = ArrayList()
            tasks = ArrayList()
            archived = ArrayList()
            deleted = ArrayList()

            // Version 11
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

        // JSONManagement Methods
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

            // Version 11
            if (version >= API.MANAGEMENT) {
                meta = data.optJSONObject(META)
                if (meta == null) {
                    meta = JSONObject()
                }
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

                // Version 11
                data.put(META, meta)
            } catch (j: JSONException) {
                j.printStackTrace()
            }

            return data
        }

        @JvmStatic
        fun updateTasks(data: JSONObject) {
            val ha = ArrayList<String>()
            val ta = ArrayList<String>()
            val aa = ArrayList<String>()
            val da = ArrayList<String>()

            val h = data.optJSONArray(HOME)
            val t = data.optJSONArray(TASKS)
            val a = data.optJSONArray(ARCHIVED)
            val d = data.optJSONArray(DELETED)

            if (h != null && h.length() != 0)
                for (i in 0 until h.length())
                    if (!ha.contains(h.optString(i))) ha.add(h.optString(i))
            if (t != null && t.length() != 0)
                for (i in 0 until t.length())
                    if (!ta.contains(t.optString(i))) ta.add(t.optString(i))
            if (a != null && a.length() != 0)
                for (i in 0 until a.length())
                    if (!aa.contains(a.optString(i))) aa.add(a.optString(i))
            if (d != null && d.length() != 0)
                for (i in 0 until d.length())
                    if (!da.contains(d.optString(i))) da.add(d.optString(i))

            for (task in ha) {
                if (home.contains(task)) continue
                if (archived.contains(task) || deleted.contains(task)) continue
                home.add(task)
                if (!tasks.contains(task)) tasks.add(task)
            }

            for (task in ta) {
                if (tasks.contains(task)) continue
                if (archived.contains(task) || deleted.contains(task)) continue
                tasks.add(task)
            }

            for (task in aa) {
                if (archived.contains(task)) continue
                if (deleted.contains(task)) continue
                if (tasks.contains(task)) tasks.remove(task)
                archived.add(task)
            }

            for (task in da) {
                if (deleted.contains(task)) continue
                if (archived.contains(task)) archived.remove(task)
                if (tasks.contains(task)) tasks.remove(task)
            }
        }

        // Task Methods
        @JvmStatic
        fun archiveTask(task: Task) {
            task.markArchived()
            task.save()

            if (task.getParent() == "home") {
                home.remove(task.getFilename())
            } else if (tasks.contains(task.getParent())) {
                val parent = Task(task.getParent())
                parent.removeChild(task.getFilename())
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
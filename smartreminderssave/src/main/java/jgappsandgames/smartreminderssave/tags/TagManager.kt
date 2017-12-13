package jgappsandgames.smartreminderssave.tags

// Java
import java.io.File
import java.io.IOException
import java.util.ArrayList

// JSON
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

// Save Library
import jgappsandgames.smartreminderssave.tasks.Task
import jgappsandgames.smartreminderssave.tasks.TaskManager
import jgappsandgames.smartreminderssave.utility.API
import jgappsandgames.smartreminderssave.utility.FileUtility
import jgappsandgames.smartreminderssave.utility.JSONUtility

/**
 * TagManager
 * Created by joshua on 12/12/2017.
 */
class TagManager {
    companion object {
        // Constants
        private val FILENAME = "tagmanager.srj"

        private val VERSION = "version"
        private val META = "meta"
        private val TAGS = "tags"

        // Data
        private var version = 0
        @JvmField
        var meta: JSONObject = JSONObject()
        @JvmField
        var tags: ArrayList<String> = ArrayList()

        // Management Methods
        @JvmStatic
        fun create() {
            version = API.RELEASE
            tags = ArrayList()
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

        }

        @JvmStatic
        fun save() {
            JSONUtility.saveJSONObject(File(FileUtility.getApplicationDataDirectory(), FILENAME), toJSON())
        }

        // JSON Management Methods
        @JvmStatic
        fun loadJSON(data: JSONObject?) {
            if (data == null) {
                create()
                return
            }

            version = data.optInt(VERSION, API.RELEASE)
            val t = data.optJSONArray(TAGS)
            tags = ArrayList(t.length())
            for (i in 0 until t.length()) tags.add(t.optString(i))

            // API 11
            if (version >= API.MANAGEMENT) {
                meta = data.optJSONObject(META)
            } else {
                meta = JSONObject()
            }
        }

        @JvmStatic
        fun toJSON(): JSONObject {
            val data = JSONObject()
            val t = JSONArray()

            try {
                for (tag in tags) t.put(tag)

                data.put(VERSION, API.MANAGEMENT)
                data.put(TAGS, t)

                // API 11
                data.put(META, meta)
            } catch (j: JSONException) {
                j.printStackTrace()
            }

            return data
        }

        // TagManagement Methods
        @JvmStatic
        fun addTag(tag: String): Boolean {
            // Check to See if the Tag is equal to ""
            if (tag == "") return false

            // Check to See if the Tag is Already there
            if (tags.contains(tag)) return false

            // Add the Tag
            tags.add(tag)
            return true
        }

        @JvmStatic
        fun deleteTag(tag: String) {
            // Check to See if the Tag is equal to ""
            if (tag == "") return

            // Check to See if the Tag is Already there
            if (!tags.contains(tag)) return

            // Remove Tag
            tags.remove(tag)

            // Clear Tags from Tasks
            for (t in TaskManager.tasks!!) {
                val task = Task(t)
                task.removeTag(tag).save()
            }
        }

        @JvmStatic
        operator fun contains(tag: String): Boolean {
            return tags.contains(tag)
        }
    }
}
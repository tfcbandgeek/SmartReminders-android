package jgappsandgames.smartreminderssave.tags

import jgappsandgames.smartreminderssave.tasks.Task
import jgappsandgames.smartreminderssave.tasks.TaskManager
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
 * TagManager
 * Created by joshua on 12/1/2017.
 */
class TagManager {
    companion object {
        // Constants
        private val FILENAME = "tagmanager.srj"

        private val VERSION = "version"
        private val META = "meta"

        private val TAGS = "tags"

        // Data
        @JvmField
        var version: Int = 0
        @JvmField
        var meta: JSONObject? = null

        @JvmField
        var tags: ArrayList<String>? = null

        // Management Methods
        @JvmStatic
        fun create() {
            version = API.MANAGEMENT
            tags = ArrayList()

            // VERSION 11
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
            for (i in 0 until t.length()) tags!!.add(t.optString(i))

            // Version 11
            if (version >= API.MANAGEMENT) {
                meta = data.optJSONObject(META)
                if (meta == null) meta = JSONObject()
            } else {
                meta = JSONObject()
            }
        }

        @JvmStatic
        fun toJSON(): JSONObject {
            if (tags == null) tags = ArrayList()
            val data = JSONObject()
            val t = JSONArray()

            try {
                for (tag in tags!!) t.put(tag)

                data.put(VERSION, API.MANAGEMENT)
                data.put(TAGS, t)
                data.put(META, meta)
            } catch (j: JSONException) {
                j.printStackTrace()
            }

            return data
        }

        @JvmStatic
        fun addMissingTags(data: JSONObject) {
            if (tags == null) tags = ArrayList()

            val t = data.optJSONArray(TAGS)
            tags = ArrayList(t.length())
            for (i in 0 until t.length()) if (!tags!!.contains(t.optString(i))) tags!!.add(t.optString(i))
        }

        // TagManagement Methods
        @JvmStatic
        fun addTag(tag: String): Boolean {
            if (tags == null) tags = ArrayList()

            // Check to See if the Tag is equal to ""
            if (tag == "") return false

            // Check to See if the Tag is Already there
            if (tags!!.contains(tag)) return false

            // Add the Tag
            tags!!.add(tag)
            return true
        }

        @JvmStatic
        fun deleteTag(tag: String) {
            if (tags == null) return

            // Check to See if the Tag is equal to ""
            if (tag == "") return

            // Check to See if the Tag is Already there
            if (!tags!!.contains(tag)) return

            // Remove Tag
            tags!!.remove(tag)

            // Clear Tags from Tasks
            for (t in TaskManager.tasks) {
                val task = Task(t)
                task.removeTag(tag).save()
            }
        }

        @JvmStatic
        fun contains(tag: String): Boolean {
            if (tags == null) tags = ArrayList()
            return tags!!.contains(tag)
        }
    }
}
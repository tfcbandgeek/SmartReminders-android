package jgappsandgames.smartreminderssave.tags

// Java
import java.io.File
import java.util.ArrayList

// JSON
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

// Save Library
import jgappsandgames.smartreminderssave.tasks.TaskManager
import jgappsandgames.smartreminderssave.utility.API
import jgappsandgames.smartreminderssave.utility.FileUtility
import jgappsandgames.smartreminderssave.utility.JSONUtility

// Settings
import jgappsandgames.smartreminderssave.settings.SettingsManager

/**
 * TagManager
 * Created by joshua on 12/12/2017.
 */
class TagManager {
    companion object {
        // Constants -------------------------------------------------------------------------------
        private const val FILENAME = "tagmanager.srj"

        private const val VERSION = "version"
        private const val META = "meta"
        private const val TAGS = "tags"

        private const val VERSION_12 = "a"
        private const val META_12 = "b"
        private const val TAGS_12 = "c"

        // Data ------------------------------------------------------------------------------------
        private var version = 0
        @JvmField var meta: JSONObject = JSONObject()
        @JvmField val tags: ArrayList<String> = ArrayList()

        // Management Methods ----------------------------------------------------------------------
        @JvmStatic fun create() {
            if (File(FileUtility.getApplicationDataDirectory(), FILENAME).exists()) load()

            version = API.RELEASE
            tags.clear()
            meta = JSONObject()
        }

        @JvmStatic fun load() {
            val data = JSONUtility.loadJSONObject(File(FileUtility.getApplicationDataDirectory(), FILENAME))
            version = data.optInt(VERSION, API.RELEASE)

            if (version <= API.MANAGEMENT) {
                val t = data.optJSONArray(TAGS)
                tags.clear()
                for (i in 0 until t.length()) tags.add(t.optString(i))

                // API 11
                meta = if (version >= API.MANAGEMENT) data.optJSONObject(META)
                else JSONObject()
            }

            else {
                val t = data.optJSONArray(TAGS_12)
                tags.clear()
                for (i in 0 until t.length()) tags.add(t.optString(i))
                meta = data.optJSONObject(META_12)
            }

            tags.sort()
        }

        @JvmStatic fun save() {
            tags.sort()
            JSONUtility.saveJSONObject(File(FileUtility.getApplicationDataDirectory(), FILENAME), toJSON())
        }

        @JvmStatic fun toJSON(): JSONObject {
            val data = JSONObject()
            val t = JSONArray()

            try {
                if (SettingsManager.getUseVersion() <= 11) {
                    for (tag in tags) t.put(tag)

                    data.put(VERSION, API.MANAGEMENT)
                    data.put(TAGS, t)

                    // API 11
                    data.put(META, meta)
                } else {
                    for (tag in tags) t.put(tag)

                    data.put(VERSION, API.SHRINKING)
                    data.put(VERSION_12, API.SHRINKING)
                    data.put(META_12, meta)
                    data.put(TAGS_12, t)
                }
            } catch (j: JSONException) {
                j.printStackTrace()
            }

            return data
        }

        // Tag Management Methods ------------------------------------------------------------------
        @JvmStatic fun addTag(tag: String): Boolean {
            if (tag == "") return false

            return when {
                tags.size == 0 -> {
                    tags.add(tag)
                    tags.sort()
                    save()
                    true
                }

                tags.contains(tag) -> false

                else -> {
                    tags.add(tag)
                    tags.sort()
                    save()
                    true
                }
            }
        }

        @JvmStatic fun deleteTag(tag: String) {
            if (tag == "") return
            if (!tags.contains(tag)) return

            tags.remove(tag)
            save()

            val all = TaskManager.getAllTasks()
            for (t in all) {
                t.removeTag(tag).save()
                TaskManager.taskPool.returnPoolObject(t)
            }
        }

        @JvmStatic operator fun contains(tag: String): Boolean = tags.contains(tag)
    }
}
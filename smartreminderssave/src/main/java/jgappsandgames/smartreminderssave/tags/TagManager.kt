package jgappsandgames.smartreminderssave.tags

// Java
import jgappsandgames.smartreminderssave.settings.getUseVersion
import java.io.File

// JSON
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

// Save Library
import jgappsandgames.smartreminderssave.utility.*

// Settings
import jgappsandgames.smartreminderssave.tasks.getAllTasks
import jgappsandgames.smartreminderssave.tasks.returnTaskToPool

/**
 * TagManager
 * Created by joshua on 12/12/2017.
 */
// Constants -------------------------------------------------------------------------------
private const val FILENAME = "tagmanager.srj"

private const val VERSION = "version"
private const val META = "meta"
private const val TAGS = "tags"

private const val VERSION_12 = "a"
private const val META_12 = "b"
private const val TAGS_12 = "c"

// Data ------------------------------------------------------------------------------------
private var version = MANAGEMENT
@JvmField var meta: JSONObject = JSONObject()
private val tags: ArrayList<String> = ArrayList()

// Management Methods ----------------------------------------------------------------------
fun createTags() {
    if (getApplicationDataFile(FILENAME).exists()) loadTags()

    version = RELEASE
    tags.clear()
    meta = JSONObject()

    saveTags()
}

fun loadTags() {
    val data = loadJSONObject(File(getApplicationDataDirectory(), FILENAME))
    version = data.optInt(VERSION, RELEASE)

    if (version <= MANAGEMENT) {
        val t = data.optJSONArray(TAGS)
        tags.clear()
        for (i in 0 until t.length()) tags.add(t.optString(i))

        // API 11
        meta = if (version >= MANAGEMENT) data.optJSONObject(META)
        else JSONObject()
    }

    else {
        val t = data.optJSONArray(TAGS_12)
        tags.clear()
        for (i in 0 until t.length()) tags.add(t.optString(i))
        meta = data.optJSONObject(META_12)
    }

    tags.sort()
    saveTags()
}

fun saveTags() {
    tags.sort()
    saveJSONObject(File(getApplicationDataDirectory(), FILENAME), toJSON())
}

private fun toJSON(): JSONObject {
    val data = JSONObject()
    val t = JSONArray()

    try {
        if (getUseVersion() <= MANAGEMENT) {
            for (tag in tags) t.put(tag)

            data.put(VERSION, MANAGEMENT)
            data.put(TAGS, t)

            // API 11
            data.put(META, meta)
        } else {
            for (tag in tags) t.put(tag)

            data.put(VERSION, SHRINKING)
            data.put(VERSION_12, SHRINKING)
            data.put(META_12, meta)
            data.put(TAGS_12, t)
        }
    } catch (j: JSONException) {
        j.printStackTrace()
    }

    return data
}

// Tag Management Methods ------------------------------------------------------------------
fun addTag(tag: String): Boolean {
    if (tag == "") return false

    return when {
        tags.size == 0 -> {
            tags.add(tag)
            tags.sort()
            saveTags()
            true
        }

        tags.contains(tag) -> false

        else -> {
            tags.add(tag)
            tags.sort()
            saveTags()
            true
        }
    }
}

fun deleteTag(tag: String) {
    if (tag == "") return
    if (!tags.contains(tag)) return

    tags.remove(tag)
    saveTags()

    val all = getAllTasks()
    for (t in all) {
        t.removeTag(tag).save()
        returnTaskToPool(t)
    }
}

fun getTags(): ArrayList<String> {
    return tags
}

fun contains(tag: String): Boolean = tags.contains(tag)
package jgappsandgames.smartreminderssave.tags

// JSON
import org.json.JSONArray
import org.json.JSONObject

// Save
import jgappsandgames.smartreminderssave.utility.*

/**
 * TagManager
 * Created by joshu on 11/2/2017.
 */

// File Path
private val FILENAME = "tagmanager.srj"

// JSON Constants
private val VERSION = "version"
private val META = "meta" // New in 11

private val TAGS = "tags"

// Data
var version: Int? = null
var meta: JSONObject? = null // New in 11

var tags: ArrayList<String>? = null

// Management Methods
fun createTags() {
    version = MANAGEMENT
    meta = JSONObject() // New in 11

    tags = ArrayList()
}

fun loadTags() {
    val data = loadJSON(getApplicationFileDirectory(), FILENAME)

    if (data == JSONObject() || !data.has(VERSION)) {
        createTags()
        saveTags()
        return
    }

    version = data.optInt(VERSION, RELEASE)
    meta = if (version!! >= MANAGEMENT) data.optJSONObject(META)
    else JSONObject()

    var t = data.optJSONArray(TAGS)
    if (t == null) t = JSONArray()
    tags = ArrayList()
    for (i in 0 until t.length()) tags!!.add(t.optString(i, ""))
}

fun saveTags() {
    val data = JSONObject()
    val t = JSONArray()

    data.put(VERSION, MANAGEMENT)
    data.put(META, meta) // New in 11

    for (tag in tags!!) t.put(tag)
    data.put(TAGS, t)

    saveJSONObject(getApplicationFileDirectory(), FILENAME, data)
}
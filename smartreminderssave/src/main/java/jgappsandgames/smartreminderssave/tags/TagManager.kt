package jgappsandgames.smartreminderssave.tags

import jgappsandgames.smartreminderssave.utility.*
import org.json.JSONArray
import org.json.JSONObject

/**
 * TagManager
 * Created by joshu on 11/2/2017.
 */

// File Path
private val FILENAME = "tagmanager.srj"

// JSON Constants
private val VERSION = "version"
private val TAGS = "tags"

// Data
var version: Int? = null
var tags: ArrayList<String>? = null

// Management Methods
fun createTags() {
    version = MANAGEMENT
    tags = ArrayList()
}

fun loadTags() {
    val data = loadJSON(getApplicationFileDirectory(), FILENAME)

    version = data.optInt(VERSION, RELEASE)

    val t = data.optJSONArray(TAGS)
    tags = ArrayList()
    for (i in 0 .. t.length()) tags!!.add(t.optString(i, ""))
}

fun saveTags() {
    val data = JSONObject()
    val t = JSONArray()

    data.put(VERSION, version)
    for (tag in tags!!) t.put(tag)
    data.put(TAGS, t)

    saveJSONObject(getApplicationFileDirectory(), FILENAME, data)
}
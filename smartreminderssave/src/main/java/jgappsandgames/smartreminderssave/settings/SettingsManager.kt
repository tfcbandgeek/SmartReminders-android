package jgappsandgames.smartreminderssave.settings

// JSON
import org.json.JSONArray
import org.json.JSONObject

// Android OS
import android.os.Build

// Save
import jgappsandgames.smartreminderssave.utility.*

/**
 * SettingsManager
 * Created by joshuagarner on 11/1/17.
 */

// Filepath
private val FILENAME = "settings.srj"

// JSON Constants
private val VERSION = "version"
private val META = "meta"

private val USER_NAME = "user_name"
private val DEVICE_NAME = "device_name"
private val EXTERNAL_FILE = "external_file"
private val BACKUP_FILES = "backups"

private val SHORTCUT_TAG = "has_tag_shortcut"
private val SHORTCUT_STATUS = "has_status_shortcut"
private val SHORTCUT_PRIORITY = "has_priority_shortcut"
private val SHORTCUT_TODAY = "has_today_shortcut"
private val SHORTCUT_WEEK = "has_week_shortcut"
private val SHORTCUT_MONTH = "shortcut_month"
private val SHORTCUT_TASKS = "shortcut_tasks"

private val TUTORIAL = "has_done_tutorial"
private val SPLASH = "last_version_splash"

private val COMPLETED_TASK = "completed_task"
private val COMPLETED_CHECKPOINT = "completed_checkpoint"

// Data
var version: Int? = null
var meta: JSONObject? = null

var user_name: String? = null
var device_name: String? = null
var external_file: Boolean? = null
var backup_files: ArrayList<String>? = null

var shortcut_tag = false
var shortcut_status = false
var shortcut_priority = false
var shortcut_today = false
var shortcut_week = false
var shortcut_month = false
var shortcut_tasks: ArrayList<String>? = null

var tutorial = false
var splash = 0

var completed_tasks: Int? = null
var completed_checkpoints: Int? = null

fun createSettings() {
    version = MANAGEMENT

    user_name = ""
    device_name = Build.BRAND + " " + Build.MODEL + " " + Build.VERSION.SDK_INT
    external_file = false
    backup_files = ArrayList()

    shortcut_tasks = ArrayList()

    completed_tasks = 0
    completed_checkpoints = 0
}

fun loadSettings() {
    val data = loadJSON(getInternalFileDirectory(), FILENAME)
    if (data == JSONObject() || !data.has(VERSION)) {
        createSettings()
        saveSettings()
        return
    }

    version = data.optInt(VERSION, RELEASE)

    user_name = data.optString(USER_NAME, "")
    device_name = data.optString(DEVICE_NAME, "")
    external_file = data.optBoolean(EXTERNAL_FILE, false)

    shortcut_tag = data.optBoolean(SHORTCUT_TAG, false)
    shortcut_status = data.optBoolean(SHORTCUT_STATUS, false)
    shortcut_today = data.optBoolean(SHORTCUT_TODAY, false)
    shortcut_week = data.optBoolean(SHORTCUT_WEEK, false)
    shortcut_priority = data.optBoolean(SHORTCUT_PRIORITY, false)

    tutorial = data.optBoolean(TUTORIAL, false)
    splash = data.optInt(SPLASH, 0)

    if (version!! >= MANAGEMENT) {
        meta = data.optJSONObject(META)

        var b = data.optJSONArray(BACKUP_FILES)
        if (b == null) b = JSONArray()
        backup_files = ArrayList()
        for (i in 0 .. b.length()) backup_files!!.add(b.optString(i, ""))

        shortcut_month = data.optBoolean(SHORTCUT_MONTH, false)

        var t = data.optJSONArray(SHORTCUT_TASKS)
        if (t == null) t = JSONArray()
        shortcut_tasks = ArrayList()
        for (i in 0 .. t.length()) shortcut_tasks!!.add(t.optString(i, ""))

        completed_tasks = data.optInt(COMPLETED_TASK, 0)
        completed_checkpoints = data.optInt(COMPLETED_CHECKPOINT, 0)
    }
}

fun saveSettings() {
    val data = JSONObject()

    val b = JSONArray()
    for (file in backup_files!!) b.put(file)

    val t = JSONArray()
    for (task in shortcut_tasks!!) t.put(task)

    data.put(VERSION, MANAGEMENT)
    data.put(META, meta)

    data.put(USER_NAME, user_name)
    data.put(DEVICE_NAME, device_name)
    data.put(EXTERNAL_FILE, external_file)
    data.put(BACKUP_FILES, b)

    data.put(SHORTCUT_TAG, shortcut_tag)
    data.put(SHORTCUT_STATUS, shortcut_status)
    data.put(SHORTCUT_PRIORITY, shortcut_priority)
    data.put(SHORTCUT_TODAY, shortcut_today)
    data.put(SHORTCUT_WEEK, shortcut_week)
    data.put(SHORTCUT_MONTH, shortcut_month)

    data.put(TUTORIAL, tutorial)
    data.put(SPLASH, splash)

    saveJSONObject(getInternalFileDirectory(), FILENAME, data)
}
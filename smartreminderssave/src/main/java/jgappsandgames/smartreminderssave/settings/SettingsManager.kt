package jgappsandgames.smartreminderssave.settings

// Java
import java.io.File
import java.io.IOException

// Android OS
import android.os.Build

// JSON
import org.json.JSONException
import org.json.JSONObject

// Save
import jgappsandgames.smartreminderssave.utility.*

/**
 * SettingsManager
 * Created by joshua on 12/12/2017.
 */
// Constants -------------------------------------------------------------------------------
private const val FILENAME = "settings.srj"

// Save Data -------------------------------------------------------------------------------
private const val VERSION = "version"
private const val META = "meta"

private const val USER_NAME = "user_name"
private const val DEVICE_NAME = "device_name"
private const val USE_EXTERNAL_FILE = "external_file"

private const val HAS_TAG_SHORTCUT = "has_tag_shortcut"
private const val HAS_STATUS_SHORTCUT = "has_status_shortcut"
private const val HAS_PRIORITY_SHORTCUT = "has_priority_shortcut"
private const val HAS_TODAY_SHORTCUT = "has_today_shortcut"
private const val HAS_WEEK_SHORTCUT = "has_week_shortcut"
private const val HAS_MONTH_SHORTCUT = "has_month_shortcut"
private const val HAS_DONE_TUTORIAL = "has_done_tutorial"
private const val LAST_VERSION_SPLASH = "last_version_splash"

private const val VERSION_12 = "a"
private const val USE_VERSION_12 = "n"
private const val META_12 = "b"

private const val USER_NAME_12 = "c"
private const val DEVICE_NAME_12 = "d"

private const val USE_EXTERNAL_FILE_12 = "e"
private const val HAS_TAG_SHORTCUT_12 = "f"
private const val HAS_STATUS_SHORTCUT_12 = "g"
private const val HAS_PRIORITY_SHORTCUT_12 = "h"
private const val HAS_DAY_SHORTCUT_12 = "i"
private const val HAS_WEEK_SHORTCUT_12 = "j"
private const val HAS_MONTH_SHORTCUT_12 = "k"

private const val HAS_DONE_TUTORIAL_12 = "l"
private const val LAST_VERSION_12 = "m"

// Data ------------------------------------------------------------------------------------
private var version = 0
private var use_version = 11
private var meta = JSONObject()

private var user_name =""
private var device_name = ""
private var use_external_file = false

private var has_tag_shortcut = false
private var has_status_shortcut = false
private var has_priority_shortcut = false
private var has_today_shortcut = false
private var has_week_shortcut = false
private var has_month_shortcut = false
private var has_done_tutorial = false
private var last_version_splash = 0

// Management Methods ----------------------------------------------------------------------
fun createSettings() {
    if (File(getApplicationDataDirectory(), FILENAME).exists()) loadSettings()
    version = MANAGEMENT
    use_version = SHRINKING
    user_name = ""
    device_name = Build.BRAND + " " + Build.MODEL + " " + Build.VERSION.SDK_INT
    use_external_file = false
    has_tag_shortcut = false
    has_status_shortcut = false
    has_priority_shortcut = false
    has_today_shortcut = false
    has_week_shortcut = false
    has_month_shortcut = false
    has_done_tutorial = false
    last_version_splash = -1

    // API 11
    meta = JSONObject()
}

fun loadSettings() {
    val data: JSONObject
    try {
        data = loadJSONObject(File(getInternalFileDirectory(), FILENAME))
    } catch (e: IOException) {
        createSettings()
        saveSettings()
        return
    }

    version = data.optInt(VERSION, RELEASE)
    if (version < RELEASE) version = RELEASE

    if (version <= MANAGEMENT) {
        user_name = data.optString(USER_NAME)
        use_version = MANAGEMENT
        device_name = data.optString(DEVICE_NAME)
        use_external_file = data.optBoolean(USE_EXTERNAL_FILE)
        has_tag_shortcut = data.optBoolean(HAS_TAG_SHORTCUT)
        has_status_shortcut = data.optBoolean(HAS_STATUS_SHORTCUT, false)
        has_today_shortcut = data.optBoolean(HAS_TODAY_SHORTCUT, false)
        has_week_shortcut = data.optBoolean(HAS_WEEK_SHORTCUT, false)
        has_month_shortcut = data.optBoolean(HAS_MONTH_SHORTCUT, false)
        has_priority_shortcut = data.optBoolean(HAS_PRIORITY_SHORTCUT, false)
        has_done_tutorial = data.optBoolean(HAS_DONE_TUTORIAL)
        last_version_splash = data.optInt(LAST_VERSION_SPLASH)

        // API 11
        meta = if (version >= MANAGEMENT) data.optJSONObject(META)
        else JSONObject()
    }

    else {
        user_name = data.optString(USER_NAME_12, "")
        use_version = data.optInt(USE_VERSION_12, SHRINKING)
        device_name = data.optString(DEVICE_NAME_12, Build.BRAND + " " + Build.MODEL + " " + Build.VERSION.SDK_INT)
        use_external_file = data.optBoolean(USE_EXTERNAL_FILE_12, false)
        has_tag_shortcut = data.optBoolean(HAS_TAG_SHORTCUT_12, false)
        has_status_shortcut = data.optBoolean(HAS_STATUS_SHORTCUT_12, false)
        has_today_shortcut = data.optBoolean(HAS_DAY_SHORTCUT_12, false)
        has_week_shortcut = data.optBoolean(HAS_WEEK_SHORTCUT_12, false)
        has_month_shortcut = data.optBoolean(HAS_MONTH_SHORTCUT_12, false)
        has_priority_shortcut = data.optBoolean(HAS_PRIORITY_SHORTCUT_12, false)
        has_done_tutorial = data.optBoolean(HAS_DONE_TUTORIAL_12, false)
        last_version_splash = data.optInt(LAST_VERSION_12, 11)
        meta = data.optJSONObject(META_12)
    }
}

fun saveSettings() {
    val data = JSONObject()

    // Write to JSON
    if (use_version <= MANAGEMENT) {
        try {
            data.put(VERSION, MANAGEMENT)
            data.put(USER_NAME, user_name)
            data.put(DEVICE_NAME, device_name)
            data.put(USE_EXTERNAL_FILE, use_external_file)
            data.put(HAS_TAG_SHORTCUT, has_tag_shortcut)
            data.put(HAS_STATUS_SHORTCUT, has_status_shortcut)
            data.put(HAS_PRIORITY_SHORTCUT, has_priority_shortcut)
            data.put(HAS_TODAY_SHORTCUT, has_today_shortcut)
            data.put(HAS_WEEK_SHORTCUT, has_week_shortcut)
            data.put(HAS_MONTH_SHORTCUT, has_month_shortcut)
            data.put(HAS_DONE_TUTORIAL, has_done_tutorial)
            data.put(LAST_VERSION_SPLASH, last_version_splash)
            data.put(META, meta)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    } else {
        try {
            data.put(VERSION, SHRINKING)
            data.put(VERSION_12, SHRINKING)
            data.put(USE_VERSION_12, use_version)
            data.put(META_12, meta)

            data.put(USER_NAME_12, user_name)
            data.put(DEVICE_NAME_12, device_name)
            data.put(USE_EXTERNAL_FILE_12, use_external_file)

            data.put(HAS_TAG_SHORTCUT_12, has_tag_shortcut)
            data.put(HAS_STATUS_SHORTCUT_12, has_status_shortcut)
            data.put(HAS_PRIORITY_SHORTCUT_12, has_priority_shortcut)
            data.put(HAS_DAY_SHORTCUT_12, has_today_shortcut)
            data.put(HAS_WEEK_SHORTCUT_12, has_week_shortcut)
            data.put(HAS_MONTH_SHORTCUT_12, has_month_shortcut)

            data.put(HAS_DONE_TUTORIAL_12, has_done_tutorial)
            data.put(LAST_VERSION_12, last_version_splash)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    saveJSONObject(File(getInternalFileDirectory(), FILENAME), data)
}

// Getters ---------------------------------------------------------------------------------
fun getVersion(): Int = version

fun getUseVersion(): Int = use_version

fun getMeta(): JSONObject = meta

fun getUserName(): String = user_name

fun getDeviceName(): String = device_name

fun getUseExternal(): Boolean = use_external_file

fun hasTagShorcut(): Boolean = has_tag_shortcut

fun hasStatusShortcut(): Boolean = has_status_shortcut

fun hasPriorityShortcut(): Boolean = has_priority_shortcut

fun hasDayShortcut(): Boolean = has_today_shortcut

fun hasWeekShortcut(): Boolean = has_week_shortcut

fun hasMonthShortcut(): Boolean = has_month_shortcut

fun hasDoneTutorial(): Boolean = has_done_tutorial

fun getLastVersionSplash(): Int = last_version_splash

// Setters ---------------------------------------------------------------------------------
fun setVersion(_version: Int) {
    version = _version
    saveSettings()
}

fun setUseVersion(_useVersion: Int) {
    use_version = _useVersion
    saveSettings()
}

fun setMeta(_meta: JSONObject) {
    meta = _meta
    saveSettings()
}

fun setUserName(_userName: String) {
    user_name = _userName
    saveSettings()
}

fun setDeviceName(_deviceName: String) {
    device_name = _deviceName
    saveSettings()
}

fun setUseExternal(_useExternal: Boolean) {
    use_external_file = _useExternal
    saveSettings()
}

fun setTagShortcut(_useTagShortcut: Boolean) {
    has_tag_shortcut = _useTagShortcut
    saveSettings()
}

fun setStatusShortcut(_useStatusShortcut: Boolean) {
    has_status_shortcut = _useStatusShortcut
    saveSettings()
}

fun setPriorityShortcut(_usePriorityShortcut: Boolean) {
    has_priority_shortcut = _usePriorityShortcut
    saveSettings()
}

fun setDayShortcut(_useDayShortcut: Boolean) {
    has_today_shortcut = _useDayShortcut
    saveSettings()
}

fun setWeekShorcut(_useWeekShortcut: Boolean) {
    has_week_shortcut = _useWeekShortcut
    saveSettings()
}

fun setMonthShortcut(_useMonthShortcut: Boolean) {
    has_month_shortcut = _useMonthShortcut
    saveSettings()
}

fun completedTutorial() {
    has_done_tutorial = true
}

fun displayedSplash() {
    last_version_splash = 12
}
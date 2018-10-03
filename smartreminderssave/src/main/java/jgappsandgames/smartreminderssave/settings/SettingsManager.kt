package jgappsandgames.smartreminderssave.settings

// Java
import java.io.File
import java.io.IOException

// Android OS
import android.os.Build

// JSON
import org.json.JSONException
import org.json.JSONObject

// Save Library
import jgappsandgames.smartreminderssave.utility.API
import jgappsandgames.smartreminderssave.utility.FileUtility
import jgappsandgames.smartreminderssave.utility.JSONUtility

/**
 * SettingsManager
 * Created by joshua on 12/12/2017.
 */
class SettingsManager {
    companion object {
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
       @JvmStatic fun create() {
           if (File(FileUtility.getApplicationDataDirectory(), FILENAME).exists()) load()
           version = API.MANAGEMENT
           use_version = 12
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

        @JvmStatic fun load() {
            val data: JSONObject
            try {
                data = JSONUtility.loadJSONObject(File(FileUtility.getInternalFileDirectory(), FILENAME))
            } catch (e: IOException) {
                create()
                save()
                return
            }

            version = data.optInt(VERSION, API.RELEASE)
            if (version < API.RELEASE) version = API.RELEASE

            if (version <= 11) {
                user_name = data.optString(USER_NAME)
                use_version = 11
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
                meta = if (version >= API.MANAGEMENT) data.optJSONObject(META)
                else JSONObject()
            }

            else {
                user_name = data.optString(USER_NAME_12, "")
                use_version = data.optInt(USE_VERSION_12, 11)
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

        @JvmStatic fun save() {
            val data = JSONObject()

            // Write to JSON
            if (use_version <= 11) {
                try {
                    data.put(VERSION, API.MANAGEMENT)
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
                    data.put(VERSION, API.SHRINKING)
                    data.put(VERSION_12, API.SHRINKING)
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

            JSONUtility.saveJSONObject(File(FileUtility.getInternalFileDirectory(), FILENAME), data)
        }

        // Getters ---------------------------------------------------------------------------------
        @JvmStatic fun getVersion(): Int = version

        @JvmStatic fun getUseVersion(): Int = use_version

        @JvmStatic fun getMeta(): JSONObject = meta

        @JvmStatic fun getUserName(): String = user_name

        @JvmStatic fun getDeviceName(): String = device_name

        @JvmStatic fun getUseExternal(): Boolean = use_external_file

        @JvmStatic fun hasTagShorcut(): Boolean = has_tag_shortcut

        @JvmStatic fun hasStatusShortcut(): Boolean = has_status_shortcut

        @JvmStatic fun hasPriorityShortcut(): Boolean = has_priority_shortcut

        @JvmStatic fun hasDayShortcut(): Boolean = has_today_shortcut

        @JvmStatic fun hasWeekShortcut(): Boolean = has_week_shortcut

        @JvmStatic fun hasMonthShortcut(): Boolean = has_month_shortcut

        @JvmStatic fun hasDoneTutorial(): Boolean = has_done_tutorial

        @JvmStatic fun getLastVersionSplash(): Int = last_version_splash

        // Setters ---------------------------------------------------------------------------------
        @JvmStatic fun setVersion(_version: Int) {
            version = _version
            save()
        }

        @JvmStatic fun setUseVersion(_useVersion: Int) {
            use_version = _useVersion
            save()
        }

        @JvmStatic fun setMeta(_meta: JSONObject) {
            meta = _meta
            save()
        }

        @JvmStatic fun setUserName(_userName: String) {
            user_name = _userName
            save()
        }

        @JvmStatic fun setDeviceName(_deviceName: String) {
            device_name = _deviceName
            save()
        }

        @JvmStatic fun setUseExternal(_useExternal: Boolean) {
            use_external_file = _useExternal
            save()
        }

        @JvmStatic fun setTagShortcut(_useTagShortcut: Boolean) {
            has_tag_shortcut = _useTagShortcut
            save()
        }

        @JvmStatic fun setStatusShortcut(_useStatusShortcut: Boolean) {
            has_status_shortcut = _useStatusShortcut
            save()
        }

        @JvmStatic fun setPriorityShortcut(_usePriorityShortcut: Boolean) {
            has_priority_shortcut = _usePriorityShortcut
            save()
        }

        @JvmStatic fun setDayShortcut(_useDayShortcut: Boolean) {
            has_today_shortcut = _useDayShortcut
            save()
        }

        @JvmStatic fun setWeekShorcut(_useWeekShortcut: Boolean) {
            has_week_shortcut = _useWeekShortcut
            save()
        }

        @JvmStatic fun setMonthShortcut(_useMonthShortcut: Boolean) {
            has_month_shortcut = _useMonthShortcut
            save()
        }

        @JvmStatic fun completedTutorial() {
            has_done_tutorial = true
        }

        @JvmStatic fun displayedSplash() {
            last_version_splash = 12
        }
    }
}
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
        // Filepath
        private val FILENAME = "settings.srj"

        // Save Data
        private val VERSION = "version"
        private val META = "meta"

        private val USER_NAME = "user_name"
        private val DEVICE_NAME = "device_name"

        private val USE_EXTERNAL_FILE = "external_file"

        private val HAS_TAG_SHORTCUT = "has_tag_shortcut"
        private val HAS_STATUS_SHORTCUT = "has_status_shortcut"
        private val HAS_PRIORITY_SHORTCUT = "has_priority_shortcut"
        private val HAS_TODAY_SHORTCUT = "has_today_shortcut"
        private val HAS_WEEK_SHORTCUT = "has_week_shortcut"
        private val HAS_MONTH_SHORTCUT = "has_month_shortcut"

        private val HAS_DONE_TUTORIAL = "has_done_tutorial"
        private val LAST_VERSION_SPLASH = "last_version_splash"

        // Data
        private var version = 0
        @JvmField
        var meta = JSONObject()

        @JvmField
        var user_name =""
        @JvmField
        var device_name = ""

        @JvmField
        var use_external_file = false

        @JvmField
        var has_tag_shortcut = false
        @JvmField
        var has_status_shortcut = false
        @JvmField
        var has_priority_shortcut = false
        @JvmField
        var has_today_shortcut = false
        @JvmField
        var has_week_shortcut = false
        @JvmField
        var has_month_shortcut = false

        @JvmField
        var has_done_tutorial = false
        @JvmField
        var last_version_splash = 0

        // Management Methods
        @JvmStatic
        fun create() {
            if (File(FileUtility.getApplicationDataDirectory(), FILENAME).exists()) load()

            version = API.MANAGEMENT

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

        @JvmStatic
        fun forceCreate() {
            version = API.MANAGEMENT

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

        @JvmStatic
        fun load() {
            val data: JSONObject
            try {
                data = JSONUtility.loadJSON(File(FileUtility.getInternalFileDirectory(), FILENAME))
            } catch (e: IOException) {
                e.printStackTrace()

                create()
                save()
                return
            }

            version = data.optInt(VERSION, API.RELEASE)
            if (version < API.RELEASE) version = API.RELEASE

            user_name = data.optString(USER_NAME)
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
            if (version >= API.MANAGEMENT) {
                meta = data.optJSONObject(META)
            } else {
                meta = JSONObject()
            }
        }

        @JvmStatic
        fun save() {
            val data = JSONObject()

            // Write to JSON
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

            JSONUtility.saveJSONObject(File(FileUtility.getInternalFileDirectory(), FILENAME), data)
        }
    }
}
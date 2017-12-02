package jgappsandgames.smartreminderssave.theme

import jgappsandgames.smartreminderssave.utility.API
import jgappsandgames.smartreminderssave.utility.FileUtility
import jgappsandgames.smartreminderssave.utility.JSONUtility
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException

/**
 * ThemeManager
 * Created by joshu on 12/1/2017.
 */
class ThemeManager {
    companion object {
        // File Constants
        private val FILENAME = "thememanager.srj"

        // JSON Constants
        private val VERSION = "version"
        private val META = "meta"

        private val COLOR = "color"
        private val LIGHT = "light"

        // Color Constants
        @JvmField
        val BLUE = 1
        @JvmField
        val GREEN = 2
        @JvmField
        val RED = 3
        @JvmField
        val PURPLE = 4

        // Light Constants
        @JvmField
        val DARK = 1
        @JvmField
        val GREY = 2
        @JvmField
        val WHITE = 3

        // Data
        @JvmField
        var version = 0
        @JvmField
        var meta: JSONObject? = null
        @JvmField
        var color = 0
        @JvmField
        var light = 0

        // Management Methods
        @JvmStatic
        fun load() {
            val data: JSONObject?
            try {
                data = JSONUtility.loadJSON(File(FileUtility.getInternalFileDirectory(), FILENAME))
            } catch (e: IOException) {
                e.printStackTrace()

                create()
                save()

                return
            }

            if (data == null) {
                create()
                save()

                return
            }

            version = data.optInt(VERSION, API.RELEASE)
            color = data.optInt(COLOR, 1)
            light = data.optInt(LIGHT, 1)

            // Version 11
            if (version >= API.MANAGEMENT) {
                meta = data.optJSONObject(META)
                if (meta == null) meta = JSONObject()
            } else {
                meta = JSONObject()
            }
        }

        @JvmStatic
        fun create() {
            version = API.MANAGEMENT
            color = 1
            light = 1

            // Version 11
            meta = JSONObject()
        }

        @JvmStatic
        fun save() {
            val data = JSONObject()

            try {
                data.put(VERSION, API.MANAGEMENT)
                data.put(COLOR, color)
                data.put(LIGHT, light)

                // Version 11
                data.put(META, meta)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            JSONUtility.saveJSONObject(File(FileUtility.getInternalFileDirectory(), FILENAME), data)
        }
    }
}
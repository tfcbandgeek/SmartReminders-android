package jgappsandgames.smartreminderssave.theme

// Java
import java.io.File
import java.io.IOException

// JSON Library
import org.json.JSONException
import org.json.JSONObject

// Save Library
import jgappsandgames.smartreminderssave.utility.API
import jgappsandgames.smartreminderssave.utility.FileUtility
import jgappsandgames.smartreminderssave.utility.JSONUtility

/**
 * ThemeManager
 * Created by joshua on 12/12/2017.
 */
class ThemeManager {
    companion object {
        // Constants -------------------------------------------------------------------------------
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

        // Data ------------------------------------------------------------------------------------
        private var version = 0
        @JvmField
        var meta = JSONObject()
        @JvmField
        var color = 0
        @JvmField
        var light = 0

        // Management Methods ----------------------------------------------------------------------
        @JvmStatic
        fun create() {
            version = API.RELEASE
            color = 1
            light = 1

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
            color = data.optInt(COLOR, 1)
            light = data.optInt(LIGHT, 1)

            // API 11
            if (version >= API.RELEASE) {
                meta = data.optJSONObject(META)
            } else {
                meta = JSONObject()
            }
        }

        @JvmStatic
        fun save() {
            val data = JSONObject()

            try {
                data.put(VERSION, API.MANAGEMENT)
                data.put(META, meta)
                data.put(COLOR, color)
                data.put(LIGHT, light)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            JSONUtility.saveJSONObject(File(FileUtility.getInternalFileDirectory(), FILENAME), data)
        }
    }
}
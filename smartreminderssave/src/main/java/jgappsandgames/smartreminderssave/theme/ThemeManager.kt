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
        private const val FILENAME = "thememanager.srj"

        // JSON Constants
        private const val VERSION = "version"
        private const val META = "meta"
        private const val COLOR = "color"
        private const val LIGHT = "light"

        // Color Constants
        const val BLUE = 1
        const val GREEN = 2
        const val RED = 3
        const val PURPLE = 4

        // Light Constants
        const val DARK = 1
        const val GREY = 2
        const val WHITE = 3

        // Data ------------------------------------------------------------------------------------
        private var version = 0
        private var meta = JSONObject()
        private var color = 0
        private var light = 0

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
                data = JSONUtility.loadJSONObject(File(FileUtility.getInternalFileDirectory(), FILENAME))
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
            if (version >= API.RELEASE) meta = data.optJSONObject(META)
            else meta = JSONObject()
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

    // Getters -------------------------------------------------------------------------------------
    fun getMeta(): JSONObject {
        return meta
    }

    fun getColor(): Int {
        return color
    }

    fun getLight(): Int {
        return light
    }

    // Setters -------------------------------------------------------------------------------------
    fun setMeta(_meta: JSONObject) {
        meta = _meta
        save()
    }

    fun setColor(_color: Int) {
        color = _color
        save()
    }

    fun setLight(_light: Int) {
        light = _light
        save()
    }
}
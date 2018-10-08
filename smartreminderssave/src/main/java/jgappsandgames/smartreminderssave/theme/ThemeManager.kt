package jgappsandgames.smartreminderssave.theme

// Java
import jgappsandgames.smartreminderssave.utility.*
import java.io.File
import java.io.IOException

// JSON Library
import org.json.JSONException
import org.json.JSONObject

// Save Library

/**
 * ThemeManager
 * Created by joshua on 12/12/2017.
 */
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
fun createTheme() {
    version = RELEASE
    color = 1
    light = 1

    // API 11
    meta = JSONObject()
}

fun loadTheme() {
    val data: JSONObject
    try {
        data = loadJSONObject(File(getInternalFileDirectory(), FILENAME))
    } catch (e: IOException) {
        e.printStackTrace()

        createTheme()
        saveTheme()

        return
    }

    version = data.optInt(VERSION, RELEASE)
    color = data.optInt(COLOR, 1)
    light = data.optInt(LIGHT, 1)

    // API 11
    meta = if (version >= RELEASE) data.optJSONObject(META)
    else JSONObject()
}

fun saveTheme() {
    val data = JSONObject()

    try {
        data.put(VERSION, MANAGEMENT)
        data.put(META, meta)
        data.put(COLOR, color)
        data.put(LIGHT, light)
    } catch (e: JSONException) {
        e.printStackTrace()
    }

    saveJSONObject(File(getInternalFileDirectory(), FILENAME), data)
}

// Getters -------------------------------------------------------------------------------------
fun getMeta(): JSONObject = meta

fun getColor(): Int = color

fun getLight(): Int = light

// Setters -------------------------------------------------------------------------------------
fun setMeta(_meta: JSONObject) {
    meta = _meta
    saveTheme()
}

fun setColor(_color: Int) {
    color = _color
    saveTheme()
}

fun setLight(_light: Int) {
    light = _light
    saveTheme()
}
package jgappsandgames.smartreminderssave.theme

// JSON
import org.json.JSONObject

// Save
import jgappsandgames.smartreminderssave.utility.*

/**
 * ThemeManager
 * Created by joshuagarner on 11/2/17.
 */

// File Constant
private val FILENAME = "thememanager.srj"

// JSON Constants
private val VERSION = "version"
private val COLOR = "color"
private val LIGHT = "light"

// Color Constants
val BLUE = 1
val GREEN = 2
val RED = 3
val PURPLE = 4

// Light Constants
val DARK = 1
val GREY = 2
val WHITE = 3

// Data
var version = 0
var color = 0
var light = 0


// Management Methods
fun createTheme() {
    version = MANAGEMENT
    color = 1
    light = 1
}

fun loadTheme() {
    val data = loadJSON(getApplicationFileDirectory(), FILENAME)

    if (data == JSONObject() || !data.has(VERSION)) {
        createTheme()
        saveTheme()
        return
    }

    version = data.optInt(VERSION, RELEASE)
    color = data.optInt(COLOR, 1)
    light = data.optInt(LIGHT, 1)
}

fun saveTheme() {
    val data = JSONObject()

    data.put(VERSION, version)
    data.put(COLOR, color)
    data.put(LIGHT, light)

    saveJSONObject(getApplicationFileDirectory(), FILENAME, data)
}
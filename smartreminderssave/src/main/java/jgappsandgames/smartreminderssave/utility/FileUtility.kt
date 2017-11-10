package jgappsandgames.smartreminderssave.utility

// Java
import java.io.File

// Android OS
import android.content.Context
import android.os.Build
import android.os.Environment
import android.util.Log

// Save
import jgappsandgames.smartreminderssave.settings.external_file

/**
 * FileUtility
 * Created by joshuagarner on 11/1/17.
 *
 * Methods used to Load Files
 */

// Log ID
private val LOG = "FileUtility"

// FilePath
private val FILEPATH = ".smartreminders"

// FileObjects
private var data: File? = null
private var external: File? = null
private var cache: File? = null

// Load Filepaths
fun loadFilepaths(context: Context) {
    Log.d(LOG, "Loading Filepaths")

    data = File(context.filesDir, FILEPATH)
    cache = File(context.cacheDir, FILEPATH)

    if (!data!!.isDirectory) data!!.mkdirs()
    if (!cache!!.isDirectory) cache!!.mkdirs()

    Log.v(LOG, "Loading Filepaths finished")
}

// Check to See if it is the Apps First Run
fun isFirstRun(): Boolean {
    Log.d(LOG, "Checking For First Run")

    // Open the File
    if (data == null) throw RuntimeException("Filenames have yet to of been loaded")
    val file = File(data, "firstrun")

    // File exists, Return False
    if (file.isDirectory) return false

    // File Does Not Exist, Create It, Return True
    Log.d(LOG, "It is the First Run")
    file.mkdirs()
    return true
}

// Get the Internal App Directory (Useful for App Settings)
fun getInternalFileDirectory(): File {
    Log.d(LOG, "Get the Internal File Directory")
    if (data != null) return data!!
    throw RuntimeException("Filenames have yet to of been loaded")
}

// Get the Internal Cache Directory
fun getInternalCacheDirectory(): File {
    Log.d(LOG, "Get the Cache Directory")
    if (cache != null) return cache!!
    throw RuntimeException("Filenames have yet to of been loaded")
}

// Get the Application Save Directory
fun getApplicationFileDirectory(): File {
    Log.d(LOG, "Getting the Application File Directory")
    // Saving to the Internal Directory as per Settings
    if (!external_file!!) {
        Log.v(LOG, "Currntly Setup to save to the Internal Directory")
        if (data != null) return data!!
        throw RuntimeException("Filenames have yet to of been loaded")
    }

    Log.v(LOG, "Currently set to save to the external Directory")
    if (external != null) return external!!
    else {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) external = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), FILEPATH)
        else external = data
    }

    if (external != null) {
        if (external!!.isDirectory) return external!!
        else {
            Log.v(LOG, "Creating the External Save Directory")
            external!!.mkdirs()
            return external!!
        }
    }

    throw RuntimeException("Filepaths have yet to be loaded")
}
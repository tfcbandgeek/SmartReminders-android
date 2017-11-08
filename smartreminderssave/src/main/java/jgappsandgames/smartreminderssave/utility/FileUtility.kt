package jgappsandgames.smartreminderssave.utility

// Java
import java.io.File

// Android OS
import android.content.Context
import android.os.Build
import android.os.Environment

/**
 * FileUtility
 * Created by joshuagarner on 11/1/17.
 */

// FilePath
private val FILEPATH = ".smartreminders"

// FileObjects
private var data: File? = null
private var external: File? = null
private var cache: File? = null

// Load Filepaths
fun loadFilepaths(context: Context) {
    data = File(context.filesDir, FILEPATH)
    cache = File(context.cacheDir, FILEPATH)

    if (!data!!.isDirectory) data!!.mkdirs()
    if (!cache!!.isDirectory) cache!!.mkdirs()
}

// Check to See if it is the Apps First Run
fun isFirstRun(): Boolean {
    if (data == null) throw RuntimeException("Filenames have yet to of been loaded")
    val file = File(data, "firstrun")

    if (file.isDirectory) return false

    file.mkdirs()
    return true
}

// Get the Internal App Directory (Useful for App Settings)
fun getInternalFileDirectory(): File {
    if (data != null) return data!!
    throw RuntimeException("Filenames have yet to of been loaded")
}

// Get the Internal Cache Directory
fun getInternalCacheDirectory(): File {
    if (cache != null) return cache!!
    throw RuntimeException("Filenames have yet to of been loaded")
}

// Get the Application Save Directory
fun getApplicationFileDirectory(): File {
    if (external != null) return external!!
    else {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) external = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), FILEPATH)
        else external = data
    }

    if (external != null) {
        if (external!!.isDirectory) return external!!
        else {
            external!!.mkdirs()
            return external!!
        }
    }

    throw RuntimeException("Filepaths have yet to be loaded")
}
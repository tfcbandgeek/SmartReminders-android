package jgappsandgames.smartreminderssave.utility

// Java
import java.io.File

// Android OS
import android.content.Context
import android.os.Build
import android.os.Environment

// Save
import jgappsandgames.smartreminderssave.settings.getUseExternal

/**
 * FileUtilty
 * Created by joshua on 12/6/2017.
 */
// File Paths ------------------------------------------------------------------------------
private const val path = ".smartreminders"
private const val FIRST_RUN = "firstrun"

private var data: File? = null
private var external: File? = null
private var cache: File? = null

// FileUtility Control Methods -------------------------------------------------------------
fun isFirstRun(): Boolean {
    if (data!!.isDirectory) return false

    data!!.mkdirs()
    cache!!.mkdirs()
    return true
}

fun loadFilePaths(context: Context) {
    data = File(context.filesDir, path)
    cache = File(context.cacheDir, path)
}

// FileUtilityDirectory Getters ------------------------------------------------------------
fun getApplicationDataDirectory(): File {
    // Create File Object
    if (getUseExternal()) {
        if (external != null) return external!!

        external = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), path)
        else data

        // Create Directory
        if (!external!!.exists() || !external!!.isDirectory) external!!.mkdirs()

        return external!!
    }

    // Create Directory
    if (!data!!.exists() || !data!!.isDirectory) data!!.mkdirs()

    // Return the File
    return data!!
}

fun getApplicationDataFile(filename: String): File = File(getApplicationDataDirectory(), filename)

// Get the Internal App Directory (Useful for App Settings
fun getInternalFileDirectory(): File {
    // Create Directory
    if (!data!!.exists() || !data!!.isDirectory) data!!.mkdirs()

    // Return the File
    return data!!
}

fun getExternalFileDirectory(): File {
    external = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), path)
    else data

    // Create Directory
    if (!external!!.exists() || !external!!.isDirectory) external!!.mkdirs()

    return external!!
}

fun getApplicationCacheDirectory(): File {
    // Create Directory
    if (!cache!!.exists() || !cache!!.isDirectory) cache!!.mkdirs()

    // Return the File
    return cache!!
}

// FileUtility Move Methods ----------------------------------------------------------------
fun moveFolder(input: File, out: File) {
    out.deleteRecursively()
    File(data, FIRST_RUN)
    input.copyTo(out, true)
    input.deleteRecursively()
}

fun copyFolder(input: File, out: File) {
    out.deleteRecursively()
    File(data, FIRST_RUN)
    input.copyRecursively(out, true)
}
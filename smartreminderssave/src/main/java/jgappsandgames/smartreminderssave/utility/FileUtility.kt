package jgappsandgames.smartreminderssave.utility

// Java
import java.io.File

// Android OS
import android.content.Context
import android.os.Build
import android.os.Environment

// Save
import jgappsandgames.smartreminderssave.settings.SettingsManager

/**
 * FileUtilty
 * Created by joshua on 12/6/2017.
 */
class FileUtility {
    companion object {
        // File Paths
        private val path = ".smartreminders"

        private var data: File? = null
        private var external: File? = null
        private var cache: File? = null

        // Check to See if it is the Apps First Run
        @JvmStatic
        fun isFirstRun(): Boolean {
            if (data!!.isDirectory) return false


            data!!.mkdirs()
            cache!!.mkdirs()
            return true
        }

        // Load File Paths
        @JvmStatic
        fun loadFilePaths(context: Context) {
            data = File(context.filesDir, path)
            cache = File(context.cacheDir, path)
        }

        // Get The Directory Where the Data should be stored
        @JvmStatic
        fun getApplicationDataDirectory(): File {
            // Create File Object
            if (SettingsManager.use_external_file) {
                if (external != null) return external!!

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) external = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), path)
                else external = data

                // Create Directory
                if (!external!!.exists() || !external!!.isDirectory) external!!.mkdirs()

                return external!!
            }

            // Create Directory
            if (!data!!.exists() || !data!!.isDirectory) data!!.mkdirs()

            // Return the File
            return data!!

        }

        // Get the Internal App Directory (Useful for App Settings
        @JvmStatic
        fun getInternalFileDirectory(): File {
            // Create Directory
            if (!data!!.exists() || !data!!.isDirectory) data!!.mkdirs()

            // Return the File
            return data!!
        }

        @JvmStatic
        fun getExternalFileDirectory(): File {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) external = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), path)
            else external = data

            // Create Directory
            if (!external!!.exists() || !external!!.isDirectory) external!!.mkdirs()

            return external!!
        }

        // Get The Internal Cache Directory
        @JvmStatic
        fun getApplicationCacheDirectory(): File {
            // Create Directory
            if (!cache!!.exists() || !cache!!.isDirectory) cache!!.mkdirs()

            // Return the File
            return cache!!
        }

        @JvmStatic
        fun moveFolder(input: File, out: File) {
            out.deleteRecursively()
            File(data, "firstrun")
            input.copyTo(out, true)
            input.deleteRecursively()
        }

        @JvmStatic
        fun copyFolder(input: File, out: File) {
            out.deleteRecursively()
            File(data, "firstrun")
            input.copyRecursively(out, true)
        }
    }
}
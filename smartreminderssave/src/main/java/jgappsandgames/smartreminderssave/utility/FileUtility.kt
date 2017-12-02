package jgappsandgames.smartreminderssave.utility

import android.content.Context
import android.os.Build
import android.os.Environment
import jgappsandgames.smartreminderssave.settings.Settings
import java.io.File

/**
 * FileUtility
 * Created by joshua on 11/28/2017.
 */
class FileUtility {
    // File Paths
    companion object {
        private val path = ".smartreminders"

        private var data: File? = null
        private var external: File? = null
        private var cache: File? = null

        // Check to See if it is the Apps First Run
        @JvmStatic
        fun isFirstRun(): Boolean {
            val file = File(data, "firstrun")

            // It is not the First Run, Return False
            if (file.isDirectory) return false

            data!!.mkdirs()
            cache!!.mkdirs()

            file.mkdirs()
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
            if (Settings.use_external_file) {
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

        // Get the Internal App Directory (Useful for App Settings
        @JvmStatic
        fun getInternalFileDirectory(): File {
            // Create Directory
            if (!data!!.exists() || !data!!.isDirectory) data!!.mkdirs()

            // Return the File
            return data!!
        }

        // Get The Internal Cache Directory
        @JvmStatic
        fun getApplicationCacheDirectory(): File {
            // Create Directory
            if (!cache!!.exists() || !cache!!.isDirectory) cache!!.mkdirs()

            // Return the File
            return cache!!
        }
    }
}
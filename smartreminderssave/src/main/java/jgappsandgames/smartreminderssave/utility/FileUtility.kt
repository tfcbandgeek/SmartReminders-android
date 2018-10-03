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
        // File Paths ------------------------------------------------------------------------------
        private const val path = ".smartreminders"
        private const val FIRST_RUN = "firstrun"

        private var data: File? = null
        private var external: File? = null
        private var cache: File? = null

        // FileUtility Control Methods -------------------------------------------------------------
        @JvmStatic fun isFirstRun(): Boolean {
            if (data!!.isDirectory) return false


            data!!.mkdirs()
            cache!!.mkdirs()
            return true
        }

        @JvmStatic fun loadFilePaths(context: Context) {
            data = File(context.filesDir, path)
            cache = File(context.cacheDir, path)
        }

        // FileUtilityDirectory Getters ------------------------------------------------------------
        @JvmStatic fun getApplicationDataDirectory(): File {
            // Create File Object
            if (SettingsManager.getUseExternal()) {
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
        @JvmStatic fun getInternalFileDirectory(): File {
            // Create Directory
            if (!data!!.exists() || !data!!.isDirectory) data!!.mkdirs()

            // Return the File
            return data!!
        }

        @JvmStatic fun getExternalFileDirectory(): File {
            external = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), path)
                       else data

            // Create Directory
            if (!external!!.exists() || !external!!.isDirectory) external!!.mkdirs()

            return external!!
        }

        @JvmStatic fun getApplicationCacheDirectory(): File {
            // Create Directory
            if (!cache!!.exists() || !cache!!.isDirectory) cache!!.mkdirs()

            // Return the File
            return cache!!
        }

        // FileUtility Move Methods ----------------------------------------------------------------
        @JvmStatic fun moveFolder(input: File, out: File) {
            out.deleteRecursively()
            File(data, FIRST_RUN)
            input.copyTo(out, true)
            input.deleteRecursively()
        }

        @JvmStatic fun copyFolder(input: File, out: File) {
            out.deleteRecursively()
            File(data, FIRST_RUN)
            input.copyRecursively(out, true)
        }
    }
}
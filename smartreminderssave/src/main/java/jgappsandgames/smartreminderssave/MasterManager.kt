package jgappsandgames.smartreminderssave

// Java
import java.io.File

// Android OS
import android.content.Context

// OpenLog
import me.jgappsandgames.openlog.Config
import me.jgappsandgames.openlog.Exception
import me.jgappsandgames.openlog.Log

// Save Library
import jgappsandgames.smartreminderssave.settings.SettingsManager
import jgappsandgames.smartreminderssave.tags.TagManager
import jgappsandgames.smartreminderssave.tasks.TaskManager
import jgappsandgames.smartreminderssave.theme.ThemeManager
import jgappsandgames.smartreminderssave.utility.FileUtility

/**
 * MasterManager
 * Created by joshua on 12/10/2017.
 *
 * Manager Class For the Entire Save System
 */
class MasterManager {
    companion object {
        var log_loaded = false

        /**
         * Create
         *
         * Called to Possibly load the Log System, And Create the Application Data
         * @param context The Application Context
         */
        @Deprecated("To be Removed in 12")
        @JvmStatic
        fun create(context: Context) {
            if (!log_loaded) loadLog()

            Log.d("MasterManager", "Create Called")
            SettingsManager.create()
            TaskManager.create()
            TagManager.create()
            ThemeManager.create()

            save()
        }

        /**
         * Load
         *
         * Called to Possibly load the Log System, And Load the Application Data
         * @param context The Application context
         */
        @Deprecated("To be Removed in 12")
        @JvmStatic
        fun load(context: Context) {
            if (!log_loaded) loadLog()

            Log.d("MasterManager", "Load Called")
            SettingsManager.load()
            TaskManager.load()
            TagManager.load()
            ThemeManager.load()
        }

        /**
         * Create
         *
         * Called to Possibly load the Log System, And Create the Application Data
         * @param context The Application Context
         */
        @JvmStatic
        fun create() {
            if (!log_loaded) loadLog()

            Log.d("MasterManager", "Create Called")
            SettingsManager.create()
            TaskManager.create()
            TagManager.create()
            ThemeManager.create()

            save()
        }

        /**
         * Load
         *
         * Called to Possibly load the Log System, And Load the Application Data
         * @param context The Application context
         */
        @JvmStatic
        fun load() {
            if (!log_loaded) loadLog()

            Log.d("MasterManager", "Load Called")
            SettingsManager.load()
            TaskManager.load()
            TagManager.load()
            ThemeManager.load()
        }

        /**
         * Save
         *
         * Called to Save the Application Data
         */
        @JvmStatic
        fun save() {
            Log.d("MasterManager", "Save Called")
            SettingsManager.save()
            TaskManager.save()
            TagManager.save()
            ThemeManager.save()
        }

        /**
         * ClearSave
         *
         * Called to Clear All of the Save Files That Are No Longer Connected
         */
        @JvmStatic
        fun cleanSave() {
            Exception.e("MasterManager", "Clean Save Needs To Be Implemented")
        }

        /**
         * CleanCache
         *
         * Called to Clear the Entire Cache Directory, Then Reset the Log File
         */
        @JvmStatic
        fun cleanCache() {
            Exception.e("MasterManager", "Clean Cache Needs To Be Implemented")
            FileUtility.getApplicationCacheDirectory().deleteRecursively()
            loadLog()
        }

        // ---- ---- ---- ---- ---- ---- ---- Class Methods ---- ---- ---- ---- ---- ---- ----
        /**
         * loadLog
         *
         * Called to Load the Log File
         */
        private fun loadLog() {
            Config.getInstance()
                    .setFiles(File(FileUtility.getApplicationCacheDirectory(), "openlog"))
                    .setKeyLength(16)
                    .setDebug(true)
                    .setTimeStamp(true)
                    //.setSecondaryWriter(FileWriter.getInstance())

            Log.i("Smart Reminders Save", BuildConfig.VERSION_NAME)
        }
    }
}
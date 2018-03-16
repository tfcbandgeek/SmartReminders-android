package jgappsandgames.smartreminderssave

// Android OS
import android.content.Context

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
        @JvmStatic
        fun create(context: Context) {
            if (!log_loaded) loadLog()

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
        fun load(context: Context) {
            if (!log_loaded) loadLog()

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

        }

        /**
         * CleanCache
         *
         * Called to Clear the Entire Cache Directory, Then Reset the Log File
         */
        @JvmStatic
        fun cleanCache() {
            FileUtility.getApplicationCacheDirectory().deleteRecursively()
        }

        // ---- ---- ---- ---- ---- ---- ---- Class Methods ---- ---- ---- ---- ---- ---- ----
        /**
         * loadLog
         *
         * Called to Load the Log File
         */
        @Deprecated("Removed ASAP")
        private fun loadLog() {
        }
    }
}
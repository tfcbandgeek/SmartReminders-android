package jgappsandgames.smartreminderssave

// Android OS
import android.content.Context

// Save Library
import jgappsandgames.smartreminderssave.date.DateManager
import jgappsandgames.smartreminderssave.priority.PriorityManager
import jgappsandgames.smartreminderssave.settings.SettingsManager
import jgappsandgames.smartreminderssave.status.StatusManager
import jgappsandgames.smartreminderssave.tags.TagManager
import jgappsandgames.smartreminderssave.tasks.TaskManager
import jgappsandgames.smartreminderssave.theme.ThemeManager
import jgappsandgames.smartreminderssave.utility.FileUtility

/**
 * MasterManager
 * Created by joshua on 12/10/2017.
 * Last Updated 4/11/2018.
 *
 * Manager Class For the Entire Save System
 */
class MasterManager {
    companion object {
        /**
         * Create
         *
         * Called to Possibly load the Log System, And Create the Application Data
         * @param context The Application Context
         */
        @JvmStatic
        @Deprecated("To Be Removed in 12")
        fun create(context: Context) {
            SettingsManager.create()
            TaskManager.create()
            TagManager.create()
            ThemeManager.create()

            save()
        }

        /**
         * Create
         *
         * Called to Possibly load the Log System, And Create the Application Data
         */
        @JvmStatic
        fun create() {
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
        @Deprecated("To Be Removed in 12")
        fun load(context: Context) {
            SettingsManager.load()
            TaskManager.load()
            TagManager.load()
            ThemeManager.load()
            DateManager.create()
            PriorityManager.create()
            StatusManager.create()
        }

        /**
         * Load
         *
         * Called to Possibly load the Log System, And Load the Application Data
         */
        @JvmStatic
        fun load() {
            SettingsManager.load()
            TaskManager.load()
            TagManager.load()
            ThemeManager.load()
            DateManager.create()
            PriorityManager.create()
            StatusManager.create()
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
    }
}
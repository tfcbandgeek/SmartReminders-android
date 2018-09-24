package jgappsandgames.smartreminderssave

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
 * Last Updated 9/123/2018.
 *
 * Manager Class For the Entire Save System
 */
class MasterManager {
    companion object {
        @JvmStatic fun create() {
            SettingsManager.create()
            TaskManager.create()
            TagManager.create()
            ThemeManager.create()

            save()
        }

        @JvmStatic fun load() {
            SettingsManager.load()
            TaskManager.load()
            TagManager.load()
            ThemeManager.load()
            DateManager.create()
            PriorityManager.create()
            StatusManager.create()
        }

        @JvmStatic fun save() {
            SettingsManager.save()
            TaskManager.save()
            TagManager.save()
            ThemeManager.save()
        }

        @JvmStatic fun cleanSave() {}

        @JvmStatic fun cleanCache() = FileUtility.getApplicationCacheDirectory().deleteRecursively()
    }
}
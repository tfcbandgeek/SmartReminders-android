package jgappsandgames.smartreminderssave

import jgappsandgames.smartreminderssave.settings.Settings
import jgappsandgames.smartreminderssave.tags.TagManager
import jgappsandgames.smartreminderssave.tasks.TaskManager
import jgappsandgames.smartreminderssave.theme.ThemeManager

/**
 * Master Manager
 * Created by joshua on 11/28/2017.
 */
class MasterManager {
    companion object {
        fun create() {
            TaskManager.create()
            TagManager.create()
            ThemeManager.create()
            Settings.create()

            save()
        }

        fun load() {
            TaskManager.load()
            TagManager.load()
            ThemeManager.load()
            Settings.load()
        }

        fun save() {
            TaskManager.save()
            TagManager.save()
            ThemeManager.save()
            Settings.save()
        }
    }
}
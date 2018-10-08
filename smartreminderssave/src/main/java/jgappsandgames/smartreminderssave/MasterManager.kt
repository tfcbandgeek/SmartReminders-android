package jgappsandgames.smartreminderssave

// Save Library
import jgappsandgames.smartreminderssave.date.createDate
import jgappsandgames.smartreminderssave.priority.createPriority
import jgappsandgames.smartreminderssave.settings.createSettings
import jgappsandgames.smartreminderssave.settings.loadSettings
import jgappsandgames.smartreminderssave.settings.saveSettings
import jgappsandgames.smartreminderssave.status.createStatus
import jgappsandgames.smartreminderssave.tags.createTags
import jgappsandgames.smartreminderssave.tags.loadTags
import jgappsandgames.smartreminderssave.tags.saveTags
import jgappsandgames.smartreminderssave.tasks.createTasks
import jgappsandgames.smartreminderssave.tasks.loadTasks
import jgappsandgames.smartreminderssave.tasks.saveTasks
import jgappsandgames.smartreminderssave.theme.createTheme
import jgappsandgames.smartreminderssave.theme.loadTheme
import jgappsandgames.smartreminderssave.theme.saveTheme
import jgappsandgames.smartreminderssave.utility.getApplicationCacheDirectory

/**
 * MasterManager
 * Created by joshua on 12/10/2017.
 * Last Updated 9/123/2018.
 *
 * Manager Class For the Entire Save System
 */
fun createMaster() {
    createSettings()
    createTasks()
    createTags()
    createTheme()

    saveMaster()
}

fun loadMaster() {
    loadSettings()
    loadTasks()
    loadTags()
    loadTheme()
    createDate()
    createPriority()
    createStatus()
}

fun saveMaster() {
    saveSettings()
    saveTasks()
    saveTags()
    saveTheme()
}

fun cleanSave() {}

fun cleanCache() = getApplicationCacheDirectory().deleteRecursively()
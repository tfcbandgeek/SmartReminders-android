package jgappsandgames.smartreminderssave

import jgappsandgames.smartreminderssave.date.DateManager
import jgappsandgames.smartreminderssave.settings.Settings
import jgappsandgames.smartreminderssave.settings.createSettings
import jgappsandgames.smartreminderssave.settings.loadSettings
import jgappsandgames.smartreminderssave.settings.saveSettings
import jgappsandgames.smartreminderssave.tags.TagManager
import jgappsandgames.smartreminderssave.tasks.*
import jgappsandgames.smartreminderssave.theme.ThemeManager
import jgappsandgames.smartreminderssave.theme.createTheme
import jgappsandgames.smartreminderssave.theme.loadTheme
import jgappsandgames.smartreminderssave.theme.saveTheme

/**
 * MasterManager
 * Created by joshua on 10/30/17.
 */
fun create() {
    createSettings()
    createTheme()
    createTasks()
    TagManager.create()
    DateManager.create()

    saveSettings()
    saveTheme()
    saveTasks()
}

fun load() {
    loadSettings()
    loadTheme()
    loadTasks()
    TagManager.load()
    // DateManager.load()
}

fun save() {
    saveSettings()
    saveTheme()
    saveTasks()
}

fun lowMemory() {

}

fun lowStorage() {

}
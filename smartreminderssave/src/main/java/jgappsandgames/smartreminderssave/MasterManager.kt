package jgappsandgames.smartreminderssave

// Save
import jgappsandgames.smartreminderssave.daily.*
import jgappsandgames.smartreminderssave.date.createDates
import jgappsandgames.smartreminderssave.date.loadDates
import jgappsandgames.smartreminderssave.date.saveDates
import jgappsandgames.smartreminderssave.priority.createPriority
import jgappsandgames.smartreminderssave.priority.loadPriority
import jgappsandgames.smartreminderssave.priority.savePriorty
import jgappsandgames.smartreminderssave.settings.createSettings
import jgappsandgames.smartreminderssave.settings.loadSettings
import jgappsandgames.smartreminderssave.settings.saveSettings
import jgappsandgames.smartreminderssave.status.createStatus
import jgappsandgames.smartreminderssave.status.loadStatus
import jgappsandgames.smartreminderssave.status.saveStatus
import jgappsandgames.smartreminderssave.tags.createTags
import jgappsandgames.smartreminderssave.tags.loadTags
import jgappsandgames.smartreminderssave.tags.saveTags
import jgappsandgames.smartreminderssave.tasks.*
import jgappsandgames.smartreminderssave.theme.createTheme
import jgappsandgames.smartreminderssave.theme.loadTheme
import jgappsandgames.smartreminderssave.theme.saveTheme

/**
 * MasterManager
 * Created by joshua on 10/30/17.
 */
fun create() {
    // Settings
    createSettings()
    createTheme()

    // Tasks
    createTasks()

    // Sort
    createTags()
    createDates()
    createPriority()
    createStatus()

    // Task Manipulation
    createDaily()
    createWeekly()
    createMonthly()
    createYearly()

    // Save
    save()
}

fun load() {
    // Settings
    loadSettings()
    loadTheme()

    // Tasks
    loadTasks()

    // Sort
    loadTags()
    loadDates()
    loadPriority()
    loadStatus()

    // Task Manipulation
    loadDaily()
    loadWeekly()
    loadMonthly()
    loadYearly()
}

fun save() {
    // Settings
    saveSettings()
    saveTheme()

    // Tasks
    saveTasks()

    // Sort
    saveTags()
    saveDates()
    savePriorty()
    saveStatus()

    // Task Manipulation
    saveDaily()
    saveWeekly()
    saveMonthly()
    saveYearly()
}
package jgappsandgames.smartreminderssave.date

// Java
import java.util.Calendar

// Save
import jgappsandgames.smartreminderssave.tasks.Task

/**
 * Day
 * Created by joshua on 12/12/2017.
 */
class Day(val day: Calendar) {
    var tasks = ArrayList<Task>()

    fun addTask(task: Task) {
        if (!tasks.contains(task)) tasks.add(task)
    }

    fun removeTask(task: Task) {
        tasks.remove(task)
    }
}
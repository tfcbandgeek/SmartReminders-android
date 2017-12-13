package jgappsandgames.smartreminderssave.date

import jgappsandgames.smartreminderssave.tasks.Task
import java.util.*

/**
 * Day
 * Created by joshua on 12/12/2017.
 */
class Day(private val day: Calendar) {
    private var tasks = ArrayList<Task>()

    fun getDay(): Calendar {
        return day
    }

    fun addTask(task: Task) {
        if (!tasks.contains(task)) tasks.add(task)
    }

    fun removeTask(task: Task) {
        tasks.remove(task)
    }

    fun getTasks(): ArrayList<Task> {
        return tasks
    }
}
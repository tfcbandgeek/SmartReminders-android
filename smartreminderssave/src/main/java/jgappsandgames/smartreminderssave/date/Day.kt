package jgappsandgames.smartreminderssave.date

import jgappsandgames.smartreminderssave.tasks.Task
import java.util.*
import kotlin.collections.ArrayList

/**
 * Day
 * Created by joshua on 12/1/2017.
 */
class Day(i_day: Calendar) {
    private val day: Calendar = i_day
    private var tasks: ArrayList<Task> = ArrayList()

    fun getDay(): Calendar = day

    fun addTask(task: Task) {
        if (!tasks.contains(task)) tasks.add(task)
    }

    fun removeTask(task: Task) {
        tasks.remove(task)
    }

    fun getTasks(): ArrayList<Task> = tasks
}
package jgappsandgames.smartreminderssave.date

import jgappsandgames.smartreminderssave.tasks.Task
import java.util.*

/**
 * Month
 * Created by joshua on 12/1/2017.
 */
class Month(start: Calendar) {
    // Data
    private var days_in_month: Int
    private var month_starts_on: Int

    private val start: Calendar
    private var end: Calendar

    private var days: ArrayList<Day>?

    init {
        this.start = start.clone() as Calendar
        days_in_month = start.getMaximum(Calendar.DAY_OF_MONTH)
        month_starts_on = start.get(Calendar.DAY_OF_WEEK)

        days = ArrayList(days_in_month)
        for (i in 0 until days_in_month) {
            days!!.add(Day(start.clone() as Calendar))
            start.add(Calendar.DAY_OF_MONTH, 1)
        }
        end = days!![days_in_month - 1].getDay()
        end.add(Calendar.DAY_OF_MONTH, 1)
    }

    // Task Management Methods
    fun addTask(task: Task): Boolean {
        if (task.getDateDue().get(Calendar.YEAR) >= start.get(Calendar.YEAR)) {
            if (task.getDateDue().get(Calendar.DAY_OF_YEAR) >= start.get(Calendar.MONTH)) {
                if (task.getDateDue().get(Calendar.YEAR) <= end.get(Calendar.YEAR)) {
                    if (task.getDateDue().get(Calendar.MONTH) <= end.get(Calendar.MONTH)) {
                        for (day in days!!) {
                            if (day.getDay().get(Calendar.DAY_OF_MONTH) == task.getDateDue().get(Calendar.DAY_OF_MONTH)) {
                                day.addTask(task)
                                return true
                            }
                        }
                    }
                }
            }
        }

        return false
    }

    fun removeTask(task: Task): Boolean {
        if (task.getDateDue().get(Calendar.YEAR) >= start.get(Calendar.YEAR)) {
            if (task.getDateDue().get(Calendar.DAY_OF_YEAR) >= start.get(Calendar.MONTH)) {
                if (task.getDateDue().get(Calendar.YEAR) <= end.get(Calendar.YEAR)) {
                    if (task.getDateDue().get(Calendar.MONTH) <= end.get(Calendar.MONTH)) {
                        for (day in days!!) {
                            if (day.getDay().get(Calendar.DAY_OF_MONTH) == task.getDateDue().get(Calendar.DAY_OF_MONTH)) {
                                day.removeTask(task)
                                return true
                            }
                        }
                    }
                }
            }
        }

        return false
    }

    // Getters
    fun getDay(instance: Calendar): Day? {
        if (instance.get(Calendar.YEAR) >= start.get(Calendar.YEAR)) {
            if (instance.get(Calendar.DAY_OF_YEAR) >= start.get(Calendar.MONTH)) {
                if (instance.get(Calendar.YEAR) <= end.get(Calendar.YEAR)) {
                    if (instance.get(Calendar.MONTH) <= end.get(Calendar.MONTH)) {
                        for (day in days!!) {
                            if (day.getDay().get(Calendar.DAY_OF_MONTH) == instance.get(Calendar.DAY_OF_MONTH)) {
                                return day
                            }
                        }
                    }
                }
            }
        }

        // Todo: Return Specialized Day Class
        return null
    }

    fun getStart(): Calendar {
        return start
    }

    fun getEnd(): Calendar {
        return end
    }

    fun getAllTasks(): ArrayList<Task> {
        val tasks = ArrayList<Task>()

        if (days == null || days!!.size == 0) return tasks
        for (day in days!!) tasks.addAll(day.getTasks())
        return tasks
    }
}
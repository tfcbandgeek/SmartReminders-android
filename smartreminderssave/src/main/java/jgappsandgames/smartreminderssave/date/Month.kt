package jgappsandgames.smartreminderssave.date

// Java
import java.util.Calendar

// Save
import jgappsandgames.smartreminderssave.tasks.Task

/**
 * Month
 * Created by joshua on 12/12/2017.
 */
class Month(start: Calendar) {
    // Data ----------------------------------------------------------------------------------------
    private var days_in_month = start.getMaximum(Calendar.DAY_OF_MONTH)
    private var month_starts_on = start.get(Calendar.DAY_OF_WEEK)

    private val start = start.clone() as Calendar
    private var end: Calendar

    private var days: ArrayList<Day>

    // Constructers --------------------------------------------------------------------------------
    init {
        days = ArrayList(days_in_month)
        for (i in 0 until days_in_month) {
            days.add(Day(start.clone() as Calendar))
            start.add(Calendar.DAY_OF_MONTH, 1)
        }

        end = days[days_in_month - 1].day
        end.add(Calendar.DAY_OF_MONTH, 1)
    }

    // Task Management Methods ---------------------------------------------------------------------
    // TODO: CLEAN UP AL of the if stements here
    fun addTask(task: Task): Boolean {
        if (task.getDateDue()!!.get(Calendar.YEAR) >= start.get(Calendar.YEAR)) {
            if (task.getDateDue()!!.get(Calendar.DAY_OF_YEAR) >= start.get(Calendar.MONTH)) {
                if (task.getDateDue()!!.get(Calendar.YEAR) <= end.get(Calendar.YEAR)) {
                    if (task.getDateDue()!!.get(Calendar.MONTH) <= end.get(Calendar.MONTH)) {
                        for (day in days) {
                            if (day.day.get(Calendar.DAY_OF_MONTH) == task.getDateDue()!!.get(Calendar.DAY_OF_MONTH)) {
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
        if (task.getDateDue()!!.get(Calendar.YEAR) >= start.get(Calendar.YEAR)) {
            if (task.getDateDue()!!.get(Calendar.DAY_OF_YEAR) >= start.get(Calendar.MONTH)) {
                if (task.getDateDue()!!.get(Calendar.YEAR) <= end.get(Calendar.YEAR)) {
                    if (task.getDateDue()!!.get(Calendar.MONTH) <= end.get(Calendar.MONTH)) {
                        for (day in days) {
                            if (day.day.get(Calendar.DAY_OF_MONTH) == task.getDateDue()!!.get(Calendar.DAY_OF_MONTH)) {
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

    // Getters -------------------------------------------------------------------------------------
    fun getDay(instance: Calendar): Day? {
        if (instance.get(Calendar.YEAR) >= start.get(Calendar.YEAR)) {
            if (instance.get(Calendar.DAY_OF_YEAR) >= start.get(Calendar.MONTH)) {
                if (instance.get(Calendar.YEAR) <= end.get(Calendar.YEAR)) {
                    if (instance.get(Calendar.MONTH) <= end.get(Calendar.MONTH)) {
                        for (day in days) {
                            if (day.day.get(Calendar.DAY_OF_MONTH) == instance.get(Calendar.DAY_OF_MONTH)) {
                                return day
                            }
                        }
                    }
                }
            }
        }

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

        if (days.size == 0) return tasks
        for (day in days) tasks.addAll(day.tasks)
        return tasks
    }
}
package jgappsandgames.smartreminderssave.date

import jgappsandgames.smartreminderssave.tasks.Task
import java.util.*

/**
 * Week
 * Created by joshua on 12/1/2017.
 */
class Week(start: Calendar) {
    // Data
    private var sunday: Day
    private var monday: Day
    private var tuesday: Day
    private var wednesday: Day
    private var thursday: Day
    private var friday: Day
    private var saturday: Day

    init {
        sunday = Day(start.clone() as Calendar)

        start.add(Calendar.DAY_OF_WEEK, 1)
        monday = Day(start.clone() as Calendar)

        start.add(Calendar.DAY_OF_WEEK, 1)
        tuesday = Day(start.clone() as Calendar)

        start.add(Calendar.DAY_OF_WEEK, 1)
        wednesday = Day(start.clone() as Calendar)

        start.add(Calendar.DAY_OF_WEEK, 1)
        thursday = Day(start.clone() as Calendar)

        start.add(Calendar.DAY_OF_WEEK, 1)
        friday = Day(start.clone() as Calendar)

        start.add(Calendar.DAY_OF_WEEK, 1)
        saturday = Day(start.clone() as Calendar)
    }

    fun addTask(task: Task): Boolean {
        if (task.getDateDue().get(Calendar.YEAR) >= sunday.getDay().get(Calendar.YEAR)) {
            if (task.getDateDue().get(Calendar.DAY_OF_YEAR) >= sunday.getDay().get(Calendar.DAY_OF_YEAR)) {
                if (task.getDateDue().get(Calendar.YEAR) <= saturday.getDay().get(Calendar.YEAR)) {
                    if (task.getDateDue().get(Calendar.DAY_OF_YEAR) <= saturday.getDay().get(Calendar.DAY_OF_YEAR)) {
                        // Sunday
                        if (task.getDateDue().get(Calendar.DAY_OF_YEAR) == sunday.getDay().get(Calendar.DAY_OF_YEAR)) {
                            sunday.addTask(task)
                            return true
                        }

                        // Monday
                        if (task.getDateDue().get(Calendar.DAY_OF_YEAR) == monday.getDay().get(Calendar.DAY_OF_YEAR)) {
                            monday.addTask(task)
                            return true
                        }

                        // Tuesday
                        if (task.getDateDue().get(Calendar.DAY_OF_YEAR) == tuesday.getDay().get(Calendar.DAY_OF_YEAR)) {
                            tuesday.addTask(task)
                            return true
                        }

                        // Wednesday
                        if (task.getDateDue().get(Calendar.DAY_OF_YEAR) == wednesday.getDay().get(Calendar.DAY_OF_YEAR)) {
                            wednesday.addTask(task)
                            return true
                        }

                        // Thursday
                        if (task.getDateDue().get(Calendar.DAY_OF_YEAR) == thursday.getDay().get(Calendar.DAY_OF_YEAR)) {
                            thursday.addTask(task)
                            return true
                        }

                        // Friday
                        if (task.getDateDue().get(Calendar.DAY_OF_YEAR) == friday.getDay().get(Calendar.DAY_OF_YEAR)) {
                            friday.addTask(task)
                            return true
                        }

                        // Saturday
                        if (task.getDateDue().get(Calendar.DAY_OF_YEAR) == saturday.getDay().get(Calendar.DAY_OF_YEAR)) {
                            saturday.addTask(task)
                            return true
                        }
                    }
                }
            }
        }

        return false
    }

    fun removeTask(task: Task): Boolean {
        if (task.getDateDue().get(Calendar.YEAR) >= sunday.getDay().get(Calendar.YEAR)) {
            if (task.getDateDue().get(Calendar.DAY_OF_YEAR) >= sunday.getDay().get(Calendar.DAY_OF_YEAR)) {
                if (task.getDateDue().get(Calendar.YEAR) <= saturday.getDay().get(Calendar.YEAR)) {
                    if (task.getDateDue().get(Calendar.DAY_OF_YEAR) <= saturday.getDay().get(Calendar.DAY_OF_YEAR)) {
                        // Sunday
                        if (task.getDateDue().get(Calendar.DAY_OF_YEAR) == sunday.getDay().get(Calendar.DAY_OF_YEAR)) {
                            sunday.removeTask(task)
                            return true
                        }

                        // Monday
                        if (task.getDateDue().get(Calendar.DAY_OF_YEAR) == monday.getDay().get(Calendar.DAY_OF_YEAR)) {
                            monday.removeTask(task)
                            return true
                        }

                        // Tuesday
                        if (task.getDateDue().get(Calendar.DAY_OF_YEAR) == tuesday.getDay().get(Calendar.DAY_OF_YEAR)) {
                            tuesday.removeTask(task)
                            return true
                        }

                        // Wednesday
                        if (task.getDateDue().get(Calendar.DAY_OF_YEAR) == wednesday.getDay().get(Calendar.DAY_OF_YEAR)) {
                            wednesday.removeTask(task)
                            return true
                        }

                        // Thursday
                        if (task.getDateDue().get(Calendar.DAY_OF_YEAR) == thursday.getDay().get(Calendar.DAY_OF_YEAR)) {
                            thursday.removeTask(task)
                            return true
                        }

                        // Friday
                        if (task.getDateDue().get(Calendar.DAY_OF_YEAR) == friday.getDay().get(Calendar.DAY_OF_YEAR)) {
                            friday.removeTask(task)
                            return true
                        }

                        // Saturday
                        if (task.getDateDue().get(Calendar.DAY_OF_YEAR) == saturday.getDay().get(Calendar.DAY_OF_YEAR)) {
                            saturday.removeTask(task)
                            return true
                        }
                    }
                }
            }
        }

        return false
    }

    fun getDay(instance: Calendar): Day {
        when (instance.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> return sunday

            Calendar.MONDAY -> return monday

            Calendar.TUESDAY -> return tuesday

            Calendar.WEDNESDAY -> return wednesday

            Calendar.THURSDAY -> return thursday

            Calendar.FRIDAY -> return friday

            Calendar.SATURDAY -> return saturday
        }

        // Todo: Return Specialized Day Class
        return Day(Calendar.getInstance())
    }

    fun getStart(): Calendar {
        return sunday.getDay()
    }

    fun getEnd(): Calendar {
        return saturday.getDay()
    }

    fun getAllTasks(): ArrayList<Task> {
        val tasks = ArrayList<Task>()

        tasks.addAll(sunday.getTasks())
        tasks.addAll(monday.getTasks())
        tasks.addAll(tuesday.getTasks())
        tasks.addAll(wednesday.getTasks())
        tasks.addAll(thursday.getTasks())
        tasks.addAll(friday.getTasks())
        tasks.addAll(saturday.getTasks())

        return tasks
    }
}
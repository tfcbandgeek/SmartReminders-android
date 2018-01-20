package jgappsandgames.smartreminderssave.date

// Java
import java.util.Calendar

// Save
import jgappsandgames.smartreminderssave.tasks.Task

/**
 * Week
 * Created by joshua on 12/12/2017.
 */
class Week(start: Calendar) {
    // Data ----------------------------------------------------------------------------------------
    private var sunday = Day(start.clone() as Calendar)
    private var monday: Day
    private var tuesday: Day
    private var wednesday: Day
    private var thursday: Day
    private var friday: Day
    private var saturday: Day

    // Constructor(s) ------------------------------------------------------------------------------
    init {
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

    // Task Management Methods ---------------------------------------------------------------------
    fun addTask(task: Task): Boolean {
        if (task.getDateDue()!!.get(Calendar.YEAR) >= sunday.day.get(Calendar.YEAR)) {
            if (task.getDateDue()!!.get(Calendar.DAY_OF_YEAR) >= sunday.day.get(Calendar.DAY_OF_YEAR)) {
                if (task.getDateDue()!!.get(Calendar.YEAR) <= saturday.day.get(Calendar.YEAR)) {
                    if (task.getDateDue()!!.get(Calendar.DAY_OF_YEAR) <= saturday.day.get(Calendar.DAY_OF_YEAR)) {
                        // Sunday
                        if (task.getDateDue()!!.get(Calendar.DAY_OF_YEAR) == sunday.day.get(Calendar.DAY_OF_YEAR)) {
                            sunday.addTask(task)
                            return true
                        }

                        // Monday
                        if (task.getDateDue()!!.get(Calendar.DAY_OF_YEAR) == monday.day.get(Calendar.DAY_OF_YEAR)) {
                            monday.addTask(task)
                            return true
                        }

                        // Tuesday
                        if (task.getDateDue()!!.get(Calendar.DAY_OF_YEAR) == tuesday.day.get(Calendar.DAY_OF_YEAR)) {
                            tuesday.addTask(task)
                            return true
                        }

                        // Wednesday
                        if (task.getDateDue()!!.get(Calendar.DAY_OF_YEAR) == wednesday.day.get(Calendar.DAY_OF_YEAR)) {
                            wednesday.addTask(task)
                            return true
                        }

                        // Thursday
                        if (task.getDateDue()!!.get(Calendar.DAY_OF_YEAR) == thursday.day.get(Calendar.DAY_OF_YEAR)) {
                            thursday.addTask(task)
                            return true
                        }

                        // Friday
                        if (task.getDateDue()!!.get(Calendar.DAY_OF_YEAR) == friday.day.get(Calendar.DAY_OF_YEAR)) {
                            friday.addTask(task)
                            return true
                        }

                        // Saturday
                        if (task.getDateDue()!!.get(Calendar.DAY_OF_YEAR) == saturday.day.get(Calendar.DAY_OF_YEAR)) {
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
        if (task.getDateDue()!!.get(Calendar.YEAR) >= sunday.day.get(Calendar.YEAR)) {
            if (task.getDateDue()!!.get(Calendar.DAY_OF_YEAR) >= sunday.day.get(Calendar.DAY_OF_YEAR)) {
                if (task.getDateDue()!!.get(Calendar.YEAR) <= saturday.day.get(Calendar.YEAR)) {
                    if (task.getDateDue()!!.get(Calendar.DAY_OF_YEAR) <= saturday.day.get(Calendar.DAY_OF_YEAR)) {
                        // Sunday
                        if (task.getDateDue()!!.get(Calendar.DAY_OF_YEAR) == sunday.day.get(Calendar.DAY_OF_YEAR)) {
                            sunday.removeTask(task)
                            return true
                        }

                        // Monday
                        if (task.getDateDue()!!.get(Calendar.DAY_OF_YEAR) == monday.day.get(Calendar.DAY_OF_YEAR)) {
                            monday.removeTask(task)
                            return true
                        }

                        // Tuesday
                        if (task.getDateDue()!!.get(Calendar.DAY_OF_YEAR) == tuesday.day.get(Calendar.DAY_OF_YEAR)) {
                            tuesday.removeTask(task)
                            return true
                        }

                        // Wednesday
                        if (task.getDateDue()!!.get(Calendar.DAY_OF_YEAR) == wednesday.day.get(Calendar.DAY_OF_YEAR)) {
                            wednesday.removeTask(task)
                            return true
                        }

                        // Thursday
                        if (task.getDateDue()!!.get(Calendar.DAY_OF_YEAR) == thursday.day.get(Calendar.DAY_OF_YEAR)) {
                            thursday.removeTask(task)
                            return true
                        }

                        // Friday
                        if (task.getDateDue()!!.get(Calendar.DAY_OF_YEAR) == friday.day.get(Calendar.DAY_OF_YEAR)) {
                            friday.removeTask(task)
                            return true
                        }

                        // Saturday
                        if (task.getDateDue()!!.get(Calendar.DAY_OF_YEAR) == saturday.day.get(Calendar.DAY_OF_YEAR)) {
                            saturday.removeTask(task)
                            return true
                        }
                    }
                }
            }
        }

        return false
    }

    // Getters -------------------------------------------------------------------------------------
    fun getStart(): Calendar {
        return sunday.day
    }

    fun getEnd(): Calendar {
        return saturday.day
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

        return Day(Calendar.getInstance())
    }

    fun getAllTasks(): ArrayList<Task> {
        val tasks = ArrayList<Task>()

        tasks.addAll(sunday.tasks)
        tasks.addAll(monday.tasks)
        tasks.addAll(tuesday.tasks)
        tasks.addAll(wednesday.tasks)
        tasks.addAll(thursday.tasks)
        tasks.addAll(friday.tasks)
        tasks.addAll(saturday.tasks)

        return tasks
    }
}
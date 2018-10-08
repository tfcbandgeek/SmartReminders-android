package jgappsandgames.smartreminderssave.date

// Java
import java.util.Calendar

// Save Code
import jgappsandgames.smartreminderssave.tasks.Task
import jgappsandgames.smartreminderssave.tasks.getAllTasks

/**
 * DateManager
 * Created by joshua on 12/12/2017.
 *
 * Sorts Tasks Based on Their Date Due
 * TODO: Save Sorted Tasks
 * TODO: Improve Sort Efficiency
 * TODO: Split into Week & Month
 */
// Data ------------------------------------------------------------------------------------
private var weeks = ArrayList<KeyWeek>()
private var months = ArrayList<KeyMonth>()

// Management Methods ---------------------------------------------------------------------
fun createDate() {
    val tasks = ArrayList<Task>()
    for (task in getAllTasks()) if (task.getDateDue() != null) tasks.add(task)

    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)

    weeks = ArrayList()
    for (i in 0 .. 9) {
        weeks.add(KeyWeek(i, Week(calendar.clone() as Calendar)))
        calendar.add(Calendar.WEEK_OF_YEAR, 1)
    }

    for (task in tasks) {
        if (task.getDateDue()!!.before(weeks[0].week.getStart())) continue
        var i = 0
        var b = true

        while (b) {
            if (weeks[i].week.addTask(task)) b = false

            if (i > weeks.size - 10) {
                val t = Calendar.getInstance()
                t.add(Calendar.WEEK_OF_YEAR, weeks.size)
                weeks.add(KeyWeek(weeks.size, Week(t)))
            }

            if (i >= 52) b = false
            i++
        }
    }

    val marker = Calendar.getInstance()
    marker.set(Calendar.DAY_OF_MONTH, 1)
    marker.set(Calendar.HOUR_OF_DAY, 0)
    marker.set(Calendar.MINUTE, 0)
    marker.set(Calendar.SECOND, 1)

    months = ArrayList()
    for (i in 0..11) {
        months.add(KeyMonth(i, Month(marker.clone() as Calendar)))
        marker.add(Calendar.MONTH, 1)
    }

    for (task in tasks) {
        if (task.getDateDue()!!.before(months[0].month.getStart())) continue
        var i = 0
        var b = true

        while (b) {
            if (months[i].month.addTask(task)) b = false

            if (i > months.size - 2) {
                val t = Calendar.getInstance()
                t.add(Calendar.MONTH, months.size)
                months.add(KeyMonth(months.size, Month(t)))
            }

            if (i >= 24) b = false
            i++
        }
    }
}

// Getters ---------------------------------------------------------------------------------
fun getDayCount(): Int = getWeekCount() * 7

fun getDay(date_active: Calendar): Day {
    if (date_active.before(getWeek(0).getStart())) return Day(date_active)
    for (week in weeks) if (week.week.getStart().before(date_active) && week.week.getEnd().after(date_active)) return week.week.getDay(date_active)

    return Day(date_active)
}

fun getDayTasks(date_active: Calendar): ArrayList<Task> {
    if (date_active.before(getWeek(0).getStart())) return ArrayList()
    for (week in weeks) if (week.week.getStart().before(date_active) && week.week.getEnd().after(date_active)) return week.week.getDay(date_active).tasks

    return ArrayList()
}

fun getWeekCount(): Int = weeks.size

fun getWeek(week: Int): Week {
    for (w in weeks) if (w.key == week) return w.week

    // Todo: Return Special Case
    return Week(Calendar.getInstance())
}

fun getWeekTasks(week: Int): ArrayList<Task> {
    for (w in weeks) if (w.key == week) return w.week.getAllTasks()
    return ArrayList()
}

fun getMonthCount(): Int = months.size

fun getMonth(month: Int): Month {
    for (m in months) if (m.key == month) return m.month

    // Todo: Return Special Case
    return Month(Calendar.getInstance())
}

fun getMonthTasks(month: Int): ArrayList<Task> {
    for (m in months) if (m.key == month) return m.month.getAllTasks()
    return ArrayList()
}

// Classes ---------------------------------------------------------------------------------
private class KeyWeek(val key: Int, val week: Week)
private class KeyMonth(val key: Int, val month: Month)

/**
 * Day
 * Created by joshua on 12/12/2017.
 */
class Day(val day: Calendar) {
    var tasks = ArrayList<Task>()

    fun addTask(task: Task) {
        if (!tasks.contains(task)) tasks.add(task)
    }

    fun removeTask(task: Task) = tasks.remove(task)
}

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

    // Getters -------------------------------------------------------------------------------------
    fun getStart(): Calendar = sunday.day

    fun getEnd(): Calendar = saturday.day

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

/**
 * Month
 * Created by joshua on 12/12/2017.
 */
class Month(start: Calendar) {
    // Data ----------------------------------------------------------------------------------------
    private var daysInMonth = start.getMaximum(Calendar.DAY_OF_MONTH)
    private var monthStartsOn = start.get(Calendar.DAY_OF_WEEK)

    private val start = start.clone() as Calendar
    private var end: Calendar

    private var days: ArrayList<Day>

    // Constructors --------------------------------------------------------------------------------
    init {
        days = ArrayList(daysInMonth)
        for (i in 0 until daysInMonth) {
            days.add(Day(start.clone() as Calendar))
            start.add(Calendar.DAY_OF_MONTH, 1)
        }

        end = days[daysInMonth - 1].day
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

    fun getStart(): Calendar = start

    fun getEnd(): Calendar = end

    fun getAllTasks(): ArrayList<Task> {
        val tasks = ArrayList<Task>()

        if (days.size == 0) return tasks
        for (day in days) tasks.addAll(day.tasks)
        return tasks
    }
}
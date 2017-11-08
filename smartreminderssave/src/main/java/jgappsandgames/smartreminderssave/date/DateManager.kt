package jgappsandgames.smartreminderssave.date

import jgappsandgames.smartreminderssave.tasks.Task
import jgappsandgames.smartreminderssave.tasks.tasks
import jgappsandgames.smartreminderssave.utility.getApplicationFileDirectory
import jgappsandgames.smartreminderssave.utility.loadJSON
import jgappsandgames.smartreminderssave.utility.saveJSONObject
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

/**
 * DateManager
 * Created by joshua on 11/2/2017.
 */

// Filepath
private val FILENAME = "datemanager.srj"

// JSON Constants
private val VERSION = "version"

private val DAYS = "days"
private val DAYS_REVERSE = "days_reverse"
private val WEEKS = "weeks"
private val WEEKS_REVERSE = "weeks_reverse"
private val MONTHS = "months"
private val MONTHS_REVERSE = "months_reverse"

// Data
var version: Int? = null

var start: Calendar? = null

var days: ArrayList<Day>? = null
var days_reverse: ArrayList<Day>? = null
var weeks: ArrayList<Week>? = null
var weeks_reverse: ArrayList<Week>? = null
var months: ArrayList<Month>? = null
var months_reverse: ArrayList<Month>? = null

// Management Methods
fun createDates() {
    // Days
    days = ArrayList()
    days_reverse = ArrayList()

    start = Calendar.getInstance()
    start!!.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
    for (i in 0 .. 365) {
        days!!.add(Day(start!!.clone() as Calendar))
        start!!.add(Calendar.DAY_OF_MONTH, 1)
    }

    start = Calendar.getInstance()
    start!!.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
    start!!.add(Calendar.DAY_OF_MONTH, -1)
    for (i in 0 .. 7) {
        days!!.add(Day(start!!.clone() as Calendar))
        start!!.add(Calendar.DAY_OF_MONTH, -1)
    }

    if (tasks == null) throw RuntimeException("TaskManager should be loaded first")
    for (task in tasks!!) {
        val temp = Task(task)
        if (temp.type == Task.TYPE_TASK && temp.dateDue != null) addTask(temp)
    }

    // Weeks
    weeks = ArrayList()
    weeks_reverse = ArrayList()

    start = Calendar.getInstance()
    start!!.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)

    val a = start!!.clone() as Calendar
    a.add(Calendar.WEEK_OF_YEAR, -1)
    weeks_reverse!!.add(Week(a))

    for (i in 0 .. 52) {
        weeks!!.add(Week(start!!.clone() as Calendar))
        start!!.add(Calendar.WEEK_OF_YEAR, 1)
    }

    // Months
    months = ArrayList()
    months_reverse = ArrayList()

    start = Calendar.getInstance()
    start!!.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)

    val b = start!!.clone() as Calendar
    b.add(Calendar.MONTH, -1)
    months_reverse!!.add(Month(b))

    for (i in 0 .. 12) {
        months!!.add(Month(start!!.clone() as Calendar))
        start!!.add(Calendar.MONTH, 1)
    }
}

fun loadDates() {
    val data = loadJSON(getApplicationFileDirectory(), FILENAME)

    // Load JSON
    val d = data.optJSONArray(DAYS)
    val d_r = data.optJSONArray(DAYS_REVERSE)
    val w = data.optJSONArray(WEEKS)
    val w_r = data.optJSONArray(WEEKS_REVERSE)
    val m = data.optJSONArray(MONTHS)
    val m_r = data.optJSONArray(MONTHS_REVERSE)

    // Create Arrays
    days = ArrayList()
    days_reverse = ArrayList()
    weeks = ArrayList()
    weeks_reverse = ArrayList()
    months = ArrayList()
    months_reverse = ArrayList()

    // Load Data
    for (i in 0 .. d.length()) days!!.add(Day(d.optJSONObject(i)))
    for (i in 0 .. d_r.length()) days_reverse!!.add(Day(d_r.optJSONObject(i)))
    for (i in 0 .. w.length()) weeks!!.add(Week(w.optJSONObject(i)))
    for (i in 0 .. w_r.length()) weeks_reverse!!.add(Week(w_r.optJSONObject(i)))
    for (i in 0 .. m.length()) months!!.add(Month(m.optJSONObject(i)))
    for (i in 0 .. m_r.length()) months_reverse!!.add(Month(m_r.optJSONObject(i)))
}

fun saveDates() {
    // Create JSON Holders
    val data = JSONObject()

    val d = JSONArray()
    val d_r = JSONArray()
    val w = JSONArray()
    val w_r = JSONArray()
    val m = JSONArray()
    val m_r = JSONArray()

    // Fill JSONArrays
    for (i in 0 .. days!!.size) d.put(days!![i].toJSON())
    for (i in 0 .. days_reverse!!.size) d_r.put(days_reverse!![i].toJSON())
    for (i in 0 .. weeks!!.size) w.put(weeks!![i].toJSON())
    for (i in 0 .. weeks_reverse!!.size) w_r.put(weeks_reverse!![i].toJSON())
    for (i in 0 .. months!!.size) m.put(months!![i].toJSON())
    for (i in 0 .. months_reverse!!.size) m_r.put(months_reverse!![i].toJSON())

    // Fill Data
    data.put(DAYS, d)
    data.put(DAYS_REVERSE, d_r)
    data.put(WEEKS, w)
    data.put(WEEKS_REVERSE, w_r)
    data.put(MONTHS, m)
    data.put(MONTHS_REVERSE, m_r)

    // Save to File
    saveJSONObject(getApplicationFileDirectory(), FILENAME, data)
}

// Add A Task Methods
fun addTask(task: Task) {
    // The Task is not a folder or having an actual date so we do not care about it
    if (task.type != Task.TYPE_TASK || task.dateDue == null) return

    // The Task is in the Reverse ArrayList
    if (task.dateDue.before(days!![0].day)) addTaskBefore(task)

    // The Task is in the Main ArrayList
    else addTaskAfter(task)
}

private fun addTaskBefore(task: Task) {
    var i: Int = 0
    while (true) {
        // Try to Add it to a Day
        if (days_reverse!![i].addTask(task)) break

        // Approaching end of list, Add More Items (Before)
        if (i > days_reverse!!.size - 10) {
            val cal: Calendar = days_reverse!![days_reverse!!.size].day.clone() as Calendar
            cal.add(Calendar.DAY_OF_MONTH, -1)

            // Throw an Exception if the Date is Too Far into the Past (As Too Large of an Array will cause the App to Crash)
            val min = GregorianCalendar(2015, 1, 1, 0, 0, 0)
            if (cal.before(min)) throw RuntimeException("Currently Smart Reminders Does not Support dates beyond 1/1/2015")

            days_reverse!!.add(Day(cal))
        }

        // Increment the Counter(i)
        i++
    }
}

private fun addTaskAfter(task: Task) {
    var i: Int = 0
    while (true) {
        // Try to Add Task to A Day
        if (days!![i].addTask(task)) break

        // Approaching end of list, Add More Items (After)
        if (i > days!!.size - 10) {
            val cal: Calendar = days!![days!!.size].day.clone() as Calendar
            cal.add(Calendar.DAY_OF_MONTH, 1)

            // Throw an Exception if the Date is Too Far into the Future (As To Large of an Array will cause the App to Crash)
            val max = GregorianCalendar(2020, 1, 1, 0, 0, 0)
            if (max.before(cal)) throw RuntimeException("Currently Smart Reminders Does not support dates beyond 12/31/2019")

            days!!.add(Day(cal))
        }

        // Increment the Counter
        i++
    }
}

// Remove A Task Methods
fun removeTask(task: Task) {
    if (removeTaskAfter(task))
    else removeTaskBefore(task)
}


private fun removeTaskBefore(task: Task): Boolean {
    for (i in 0 .. days_reverse!!.size) if (days_reverse!![i].removeTask(task)) return true
    return false
}

private fun removeTaskAfter(task: Task): Boolean {
    for (i in 0 .. days!!.size) if (days!![i].removeTask(task)) return true
    return false
}

// Edit A Task Methods TODO: Make Method More Efficient
fun editTask(task: Task) {
    // Remove the Old Version of the Task
    removeTask(task)

    // Add the new Version of the Task
    addTask(task)
}

// Get A Day Methods
fun getDayTasks(day: Calendar): ArrayList<Task> {
    return getDay(day).tasks as ArrayList<Task>
}

fun getDay(day: Calendar): Day {
    if (day.before(days!![0].day)) return getDayBefore(day)
    return getDayAfter(day)
}

private fun getDayBefore(day: Calendar): Day {
    for (i in 0 .. days_reverse!!.size)
        if (days_reverse!![i].day.get(Calendar.YEAR) == day.get(Calendar.YEAR))
            if (days_reverse!![i].day.get(Calendar.DAY_OF_YEAR)  == day.get(Calendar.DAY_OF_YEAR)) return days_reverse!![i]
    return Day()
}

private fun getDayAfter(day: Calendar): Day {
    for (i in 0 .. days!!.size)
        if (days!![i].day.get(Calendar.YEAR) == day.get(Calendar.YEAR))
            if (days!![i].day.get(Calendar.DAY_OF_YEAR)  == day.get(Calendar.DAY_OF_YEAR)) return days!![i]
    return Day()
}

// Get A Week Methods
fun getWeekTasks(week: Calendar): ArrayList<Task> {
    return getWeek(week).allTasks as ArrayList<Task>
}

fun getWeek(week: Calendar): Week {
    if (week.before(weeks!![0].start)) return getWeekBefore(week)
    return getWeekAfter(week)
}

private fun getWeekBefore(week: Calendar): Week {
    for (i in 0 .. weeks_reverse!!.size)
        if (weeks_reverse!![i].start.get(Calendar.YEAR) == week.get(Calendar.YEAR))
            if (weeks_reverse!![i].start.get(Calendar.DAY_OF_YEAR)  == week.get(Calendar.DAY_OF_YEAR)) return weeks_reverse!![i]
    return Week()
}

private fun getWeekAfter(week: Calendar): Week {
    for (i in 0 .. weeks!!.size)
        if (weeks!![i].start.get(Calendar.YEAR) == week.get(Calendar.YEAR))
            if (weeks!![i].start.get(Calendar.DAY_OF_YEAR)  == week.get(Calendar.DAY_OF_YEAR)) return weeks!![i]
    return Week()
}

// Get A Month Methods
fun getMonthTasks(month: Calendar): ArrayList<Task> {
    return getMonth(month).allTasks as ArrayList<Task>
}

fun getMonth(month: Calendar): Month {
    if (month.before(months!![0].start)) return getMonthBefore(month)
    return getMonthAfter(month)
}

private fun getMonthBefore(month: Calendar): Month {
    for (i in 0 .. months_reverse!!.size)
        if (months_reverse!![i].start.get(Calendar.YEAR) == month.get(Calendar.YEAR))
            if (months_reverse!![i].start.get(Calendar.DAY_OF_YEAR)  == month.get(Calendar.DAY_OF_YEAR)) return months_reverse!![i]
    return Month()
}

private fun getMonthAfter(month: Calendar): Month {
    for (i in 0 .. months!!.size)
        if (months!![i].start.get(Calendar.YEAR) == month.get(Calendar.YEAR))
            if (months!![i].start.get(Calendar.DAY_OF_YEAR)  == month.get(Calendar.DAY_OF_YEAR)) return months!![i]
    return Month()
}
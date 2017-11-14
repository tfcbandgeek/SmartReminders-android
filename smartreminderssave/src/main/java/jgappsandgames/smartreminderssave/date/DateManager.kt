package jgappsandgames.smartreminderssave.date

// Java
import android.util.Log
import java.util.Calendar
import java.util.GregorianCalendar

// Kotlin
import kotlin.collections.ArrayList

// JSON
import org.json.JSONArray
import org.json.JSONObject

// Save
import jgappsandgames.smartreminderssave.tasks.Task
import jgappsandgames.smartreminderssave.tasks.tasks
import jgappsandgames.smartreminderssave.utility.getApplicationFileDirectory
import jgappsandgames.smartreminderssave.utility.loadJSON
import jgappsandgames.smartreminderssave.utility.saveJSONObject

/**
 * DateManager
 * Created by joshua on 11/2/2017.
 */

// Log Constant
private val LOG = "DateManager"

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
    Log.v(LOG, "createDates Called")

    // Days
    Log.v(LOG, "Run Through Days")
    days = ArrayList()
    days_reverse = ArrayList()

    Log.v(LOG, "Create Days For the Next Year")
    start = Calendar.getInstance()
    start!!.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
    for (i in 0 .. 365) {
        days!!.add(Day(start!!.clone() as Calendar))
        start!!.add(Calendar.DAY_OF_MONTH, 1)
    }

    Log.v(LOG, "Create Days for the Past Week")
    start = Calendar.getInstance()
    start!!.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
    start!!.add(Calendar.DAY_OF_MONTH, -1)
    for (i in 0 .. 7) {
        days!!.add(Day(start!!.clone() as Calendar))
        start!!.add(Calendar.DAY_OF_MONTH, -1)
    }

    Log.v(LOG, "Sort Through the Tasks to Add to the Days")
    if (tasks == null) throw RuntimeException("TaskManager should be loaded first")
    if (tasks!!.size != 0) {
        for (task in tasks!!) {
            val temp = Task(task)
            if (temp.type == Task.TYPE_TASK && temp.dateDue != null) addTask(temp)
        }
    }

    // Weeks
    Log.v(LOG, "Run Through The Weeks")
    weeks = ArrayList()
    weeks_reverse = ArrayList()

    start = Calendar.getInstance()
    start!!.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)

    Log.v(LOG, "Create Last Week")
    val a = start!!.clone() as Calendar
    a.add(Calendar.WEEK_OF_YEAR, -1)
    weeks_reverse!!.add(Week(a))

    Log.v(LOG, "Create Weeks for the Next Year")
    for (i in 0 .. 52) {
        weeks!!.add(Week(start!!.clone() as Calendar))
        start!!.add(Calendar.WEEK_OF_YEAR, 1)
    }

    // Months
    Log.v(LOG, "Run Through the Months")
    months = ArrayList()
    months_reverse = ArrayList()

    start = Calendar.getInstance()
    start!!.set(Calendar.DAY_OF_MONTH, 1)

    Log.v(LOG, "Create the Last Month")
    val b = start!!.clone() as Calendar
    b.add(Calendar.MONTH, -1)
    months_reverse!!.add(Month(b))

    Log.v(LOG, "Create Months for the Next Year")
    for (i in 0 .. 12) {
        months!!.add(Month(start!!.clone() as Calendar))
        start!!.add(Calendar.MONTH, 1)
    }

    Log.v(LOG, "createDates Done")
}

fun loadDates() {
    Log.d(LOG, "loadDates Called")
    val data = loadJSON(getApplicationFileDirectory(), FILENAME)

    if (data == JSONObject() || !data.has(VERSION)) {
        Log.v(LOG, "No Save File Found, Creating Dates")
        createDates()
        saveDates()
        return
    }

    // Load JSON
    Log.v(LOG, "Load JSON")
    val d = data.optJSONArray(DAYS)
    val d_r = data.optJSONArray(DAYS_REVERSE)
    val w = data.optJSONArray(WEEKS)
    val w_r = data.optJSONArray(WEEKS_REVERSE)
    val m = data.optJSONArray(MONTHS)
    val m_r = data.optJSONArray(MONTHS_REVERSE)

    // Create Arrays
    Log.v(LOG, "Load Arrays")
    days = ArrayList()
    days_reverse = ArrayList()
    weeks = ArrayList()
    weeks_reverse = ArrayList()
    months = ArrayList()
    months_reverse = ArrayList()

    // Load Data
    Log.v(LOG, "Load Data")
    for (i in 0 until d.length()) days!!.add(Day(d.optJSONObject(i)))
    for (i in 0 until d_r.length()) days_reverse!!.add(Day(d_r.optJSONObject(i)))
    for (i in 0 until w.length()) weeks!!.add(Week(w.optJSONObject(i)))
    for (i in 0 until w_r.length()) weeks_reverse!!.add(Week(w_r.optJSONObject(i)))
    for (i in 0 until m.length()) months!!.add(Month(m.optJSONObject(i)))
    for (i in 0 until m_r.length()) months_reverse!!.add(Month(m_r.optJSONObject(i)))

    Log.v(LOG, "loadDates Done")
}

fun saveDates() {
    Log.d(LOG, "saveDates")

    // Create JSON Holders
    Log.v(LOG, "Create JSON Holders")
    val data = JSONObject()

    val d = JSONArray()
    val d_r = JSONArray()
    val w = JSONArray()
    val w_r = JSONArray()
    val m = JSONArray()
    val m_r = JSONArray()

    // Fill JSONArrays
    Log.d(LOG, "Fill JSONArrays")
    if (days!!.size > 0) for (i in 0 until days!!.size) d.put(days!![i].toJSON())
    if (days_reverse!!.size > 0) for (i in 0 until days_reverse!!.size - 1) d_r.put(days_reverse!![i].toJSON())
    if (weeks!!.size > 0) for (i in 0 until weeks!!.size) w.put(weeks!![i].toJSON())
    if (weeks_reverse!!.size > 0) for (i in 0 until weeks_reverse!!.size) w_r.put(weeks_reverse!![i].toJSON())
    if (months!!.size > 0) for (i in 0 until months!!.size) m.put(months!![i].toJSON())
    if (months_reverse!!.size > 0) for (i in 0 until months_reverse!!.size) m_r.put(months_reverse!![i].toJSON())

    // Fill Data
    Log.v(LOG, "Fill Data")
    data.put(DAYS, d)
    data.put(DAYS_REVERSE, d_r)
    data.put(WEEKS, w)
    data.put(WEEKS_REVERSE, w_r)
    data.put(MONTHS, m)
    data.put(MONTHS_REVERSE, m_r)

    // Save to File
    Log.v(LOG, "Save to File")
    saveJSONObject(getApplicationFileDirectory(), FILENAME, data)

    Log.v(LOG, "saveDates Done")
}

// Add A Task Methods
fun addTask(task: Task) {
    Log.d(LOG, "addTask Called")

    // The Task is not a folder or having an actual date so we do not care about it
    if (task.type != Task.TYPE_TASK || task.dateDue == null) {
        Log.v(LOG, "It is Not a Task or it is a Task With No Date Due")
        return
    }

    // The Task is in the Reverse ArrayList
    if (task.dateDue.before(days!![0].day)) {
        Log.v(LOG, "The Task should be added to the Reverse Tag List")
        addTaskBefore(task)
    }

    // The Task is in the Main ArrayList
    else {
        Log.v(LOG, "The Task should be added to the Main Tag List")
        addTaskAfter(task)
    }

    Log.v(LOG, "addTask Done")
}

private fun addTaskBefore(task: Task) {
    Log.v(LOG, "addTaskBefore Called"
    )
    var i = 0
    while (true) {
        // Try to Add it to a Day
        if (days_reverse!![i].addTask(task)) break

        // Approaching end of list, Add More Items (Before)
        if (i > days_reverse!!.size - 10) {
            Log.i(LOG, "Add More Items to the List")
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

    Log.v(LOG, "addTaskBefore Done")
}

private fun addTaskAfter(task: Task) {
    Log.d(LOG, "addTaskAfter Called")

    var i = 0
    while (true) {
        // Try to Add Task to A Day
        if (days!![i].addTask(task)) break

        // Approaching end of list, Add More Items (After)
        if (i > days!!.size - 10) {
            Log.i(LOG, "Approaching the end of the List, Adding More Items")
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
    Log.v(LOG, "addTaskAfter Done")
}

// Remove A Task Methods
fun removeTask(task: Task) {
    Log.d(LOG, "removeTask Called")

    if (removeTaskAfter(task))
    else removeTaskBefore(task)

    Log.v(LOG, "removeTask Done")
}


private fun removeTaskBefore(task: Task): Boolean {
    Log.d(LOG, "removeTaskBefore Called")
    for (i in 0 until days_reverse!!.size) if (days_reverse!![i].removeTask(task)) return true
    return false
}

private fun removeTaskAfter(task: Task): Boolean {
    Log.d(LOG, "removeTaskAfter Called")
    for (i in 0 until days!!.size) if (days!![i].removeTask(task)) return true
    return false
}

// Edit A Task Methods TODO: Make Method More Efficient
fun editTask(task: Task) {
    Log.d(LOG, "editTask Called")

    // Remove the Old Version of the Task
    Log.v(LOG, "Remove Task")
    removeTask(task)

    // Add the new Version of the Task
    Log.v(LOG, "Add Task")
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
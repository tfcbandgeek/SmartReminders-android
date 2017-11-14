package jgappsandgames.smartreminderssave.status

// JSON
import jgappsandgames.smartreminderssave.tasks.TYPE_FLDR
import org.json.JSONArray
import org.json.JSONObject

// Save
import jgappsandgames.smartreminderssave.tasks.Task
import jgappsandgames.smartreminderssave.tasks.tasks
import jgappsandgames.smartreminderssave.utility.*

/**
 * StatusManager
 * Created by joshua on 11/6/2017.
 */

// FilePath
private val FILENAME = "statusmanager.srj"

// Save Constants
private val VERSION = "version"
private val META = "meta"

private val FOLDERS = "folders"
private val NO_DATE = "no_date"
private val NOT_YET_DUE = "not_yet_due"
private val OVERDUE = "overdue"
private val COMPLETED = "completed"

// Data
var version = RELEASE
var meta: JSONObject? = null

var folders: ArrayList<String>? = null
var no_date: ArrayList<String>? = null
var not_yet_done: ArrayList<String>? = null
var overdue: ArrayList<String>? = null
var completed: ArrayList<String>? = null

fun createStatus() {
    version = MANAGEMENT
    meta = JSONObject()

    folders = ArrayList()
    no_date = ArrayList()
    not_yet_done = ArrayList()
    overdue = ArrayList()
    completed = ArrayList()

    if (tasks == null) throw RuntimeException("TaskManager needs to be Loaded before any of the SortManagers are loaded")
    for (task in tasks!!) addTask(Task(task))
}

fun loadStatus() {
    val data = loadJSON(getApplicationFileDirectory(), FILENAME)

    if (data == JSONObject() || !data.has(VERSION)) {
        createStatus()
        saveStatus()
        return
    }

    version = data.optInt(VERSION, RELEASE)
    meta = data.optJSONObject(META)

    var f = data.optJSONArray(FOLDERS)
    if (f == null) f = JSONArray()
    var d = data.optJSONArray(NO_DATE)
    if (d == null) d = JSONArray()
    var n = data.optJSONArray(NOT_YET_DUE)
    if (n == null) n = JSONArray()
    var o = data.optJSONArray(OVERDUE)
    if (o == null) o = JSONArray()
    var c = data.optJSONArray(COMPLETED)
    if (c == null) c = JSONArray()

    folders = ArrayList()
    no_date = ArrayList()
    not_yet_done = ArrayList()
    overdue = ArrayList()
    completed = ArrayList()

    for (i in 0 until f.length()) folders!!.add(f.optString(i, ""))
    for (i in 0 until d.length()) no_date!!.add(d.optString(i, ""))
    for (i in 0 until n.length()) not_yet_done!!.add(n.optString(i, ""))
    for (i in 0 until o.length()) overdue!!.add(o.optString(i, ""))
    for (i in 0 until c.length()) completed!!.add(c.optString(i, ""))

    checkTasks()
}

fun saveStatus() {
    val data = JSONObject()
    val f = JSONArray()
    val d = JSONArray()
    val n = JSONArray()
    val o = JSONArray()
    val c = JSONArray()

    data.put(VERSION, MANAGEMENT)
    data.put(META, meta)

    for (s in folders!!) f.put(s)
    for (s in no_date!!) d.put(s)
    for (s in not_yet_done!!) n.put(s)
    for (s in overdue!!) o.put(s)
    for (s in completed!!) c.put(s)

    saveJSONObject(getApplicationFileDirectory(), FILENAME, data)
}

fun addTask(task: Task) {
    // Task is of Type folder
    if (task.getType() == TYPE_FLDR) folders!!.add(task.getFilename())

    // Task is Completed
    if (task.isCompleted()) completed!!.add(task.getFilename())

    // Task has no date
    if (task.getDateDue() == null) no_date!!.add(task.getFilename())

    // Task is Overdue
    if (task.isOverdue()) overdue!!.add(task.getFilename())

    // All others
    not_yet_done!!.add(task.getFilename())

    saveStatus()
}

fun editTask(task: Task) {
    // Remove the Old Version of the Task
    removeTask(task)

    // Add the New Version of the Task
    addTask(task)

    saveStatus()
}

fun removeTask(task: Task) {
    folders!!.remove(task.getFilename())
    no_date!!.remove(task.getFilename())
    not_yet_done!!.remove(task.getFilename())
    overdue!!.remove(task.getFilename())
    completed!!.remove(task.getFilename())

    saveStatus()
}

fun checkTasks() {
    val temp = ArrayList<String>()

    for (task in not_yet_done!!) {
        if (Task(task).isOverdue()) {
            temp.add(task)
            overdue!!.add(task)
        }
    }

    not_yet_done!!.removeAll(temp)
}
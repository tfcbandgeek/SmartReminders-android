package jgappsandgames.smartreminderssave.status

// JSON
import org.json.JSONArray
import org.json.JSONObject

// Save
import jgappsandgames.smartreminderssave.tasks.Task
import jgappsandgames.smartreminderssave.tasks.tasks
import jgappsandgames.smartreminderssave.utility.MANAGEMENT
import jgappsandgames.smartreminderssave.utility.RELEASE
import jgappsandgames.smartreminderssave.utility.getApplicationFileDirectory
import jgappsandgames.smartreminderssave.utility.loadJSON

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

    version = data.optInt(VERSION, RELEASE)
    meta = data.optJSONObject(META)

    val f = data.optJSONArray(FOLDERS)
    val d = data.optJSONArray(NO_DATE)
    val n = data.optJSONArray(NOT_YET_DUE)
    val o = data.optJSONArray(OVERDUE)
    val c = data.optJSONArray(COMPLETED)

    folders = ArrayList()
    no_date = ArrayList()
    not_yet_done = ArrayList()
    overdue = ArrayList()
    completed = ArrayList()

    for (i in 0 .. f.length()) folders!!.add(f.optString(i, ""))
    for (i in 0 .. d.length()) no_date!!.add(d.optString(i, ""))
    for (i in 0 .. n.length()) not_yet_done!!.add(n.optString(i, ""))
    for (i in 0 .. o.length()) overdue!!.add(o.optString(i, ""))
    for (i in 0 .. c.length()) completed!!.add(c.optString(i, ""))

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
}

fun addTask(task: Task) {
    // Task is of Type folder
    if (task.type == Task.TYPE_FLDR) folders!!.add(task.filename)

    // Task is Completed
    if (task.isCompleted) completed!!.add(task.filename)

    // Task has no date
    if (task.dateDue == null) no_date!!.add(task.filename)

    // Task is Overdue
    if (task.isOverdue) overdue!!.add(task.filename)

    // All others
    not_yet_done!!.add(task.filename)
}

fun editTask(task: Task) {
    // Remove the Old Version of the Task
    removeTask(task)

    // Add the New Version of the Task
    addTask(task)
}

fun removeTask(task: Task) {
    folders!!.remove(task.filename)
    no_date!!.remove(task.filename)
    not_yet_done!!.remove(task.filename)
    overdue!!.remove(task.filename)
    completed!!.remove(task.filename)
}

fun checkTasks() {
    val temp = ArrayList<String>()

    for (task in not_yet_done!!) {
        if (Task(task).isOverdue) {
            temp.add(task)
            overdue!!.add(task)
        }
    }

    not_yet_done!!.removeAll(temp)
}
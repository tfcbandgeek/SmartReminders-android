package jgappsandgames.smartreminderssave.priority

import jgappsandgames.smartreminderssave.tasks.Task
import jgappsandgames.smartreminderssave.tasks.tasks
import jgappsandgames.smartreminderssave.utility.MANAGEMENT
import jgappsandgames.smartreminderssave.utility.RELEASE
import jgappsandgames.smartreminderssave.utility.getApplicationFileDirectory
import jgappsandgames.smartreminderssave.utility.loadJSON
import org.json.JSONArray
import org.json.JSONObject

/**
 * PriorityManager
 * Created by joshua on 11/6/2017.
 */

// Filepath
private val FILENAME = "prioritymanager.srj"

// Save Constants
private val VERSION = "version"
private val META = "meta"

private val IGNORE = "ignore"
private val LOW = "low"
private val NORMAL = "normal"
private val HIGH = "high"
private val STAR = "star"

// Data
var version = RELEASE
var meta: JSONObject? = null

var ignore: ArrayList<String>? = null
var low: ArrayList<String>? = null
var normal: ArrayList<String>? = null
var high: ArrayList<String>? = null
var star: ArrayList<String>? = null

fun createPriority() {
    // Create File Header
    version = MANAGEMENT
    meta = JSONObject()

    // Create Arrays
    ignore = ArrayList()
    low = ArrayList()
    normal = ArrayList()
    high = ArrayList()
    star = ArrayList()

    // Organize Tasks
    if (tasks == null) throw RuntimeException("TaskManager must be loaded before any of the SortManagers")
    for (task in tasks!!) addTask(Task(task))
}

fun loadPriority() {
    val data = loadJSON(getApplicationFileDirectory(), FILENAME)

    // Load File Header
    version = data.optInt(VERSION, RELEASE)
    meta = data.optJSONObject(META)

    // Load Arrays
    val i = data.optJSONArray(IGNORE)
    val l = data.optJSONArray(LOW)
    val n = data.optJSONArray(NORMAL)
    val h = data.optJSONArray(HIGH)
    val s = data.optJSONArray(STAR)

    ignore = ArrayList()
    low = ArrayList()
    normal = ArrayList()
    high = ArrayList()
    star = ArrayList()

    for (a in 0 .. i.length()) ignore!!.add(i.optString(a, ""))
    for (a in 0 .. l.length()) low!!.add(l.optString(a, ""))
    for (a in 0 .. n.length()) normal!!.add(n.optString(a, ""))
    for (a in 0 .. h.length()) high!!.add(h.optString(a, ""))
    for (a in 0 .. s.length()) star!!.add(s.optString(a, ""))
}

fun savePriorty() {
    // Create All of the Neccessary JSON Values
    val data = JSONObject()
    val i = JSONArray()
    val l = JSONArray()
    val n = JSONArray()
    val h = JSONArray()
    val s = JSONArray()
}

// Task Management Methods
fun addTask(task: Task) {
    // Folders Have No Priority So Ignore Them
    if (task.type == Task.TYPE_FLDR) return

    // Ignored Tasks
    if (task.priority == 0 && ignore != null) ignore!!.add(task.filename)

    // Low Priority Tasks
    if (task.priority >= 1 && task.priority < 33 && low != null) low!!.add(task.filename)

    // Normal Priority Tasks
    if (task.priority >= 33 && task.priority < 67 && normal != null) normal!!.add(task.filename)

    // High Priority Tasks
    if (task.priority >= 67 && task.priority < 100 && high != null) high!!.add(task.filename)

    // Stared Tasks
    if (task.priority == 100 && star != null) star!!.add(task.filename)
}

fun editTask(task: Task) {
    // First Remove the Old Version Of the Task
    removeTask(task)

    // Add the New Version of the Task
    addTask(task)
}

fun removeTask(task: Task) {
    ignore!!.remove(task.filename)
    low!!.remove(task.filename)
    normal!!.remove(task.filename)
    high!!.remove(task.filename)
    star!!.remove(task.filename)
}
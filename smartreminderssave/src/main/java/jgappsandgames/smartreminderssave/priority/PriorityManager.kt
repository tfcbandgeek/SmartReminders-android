package jgappsandgames.smartreminderssave.priority

// JSON
import org.json.JSONArray
import org.json.JSONObject

// Save
import jgappsandgames.smartreminderssave.tasks.Task
import jgappsandgames.smartreminderssave.tasks.tasks
import jgappsandgames.smartreminderssave.utility.*

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

    if (data == JSONObject() || !data.has(VERSION)) {
        createPriority()
        savePriorty()
        return
    }

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

    for (a in 0 until i.length()) ignore!!.add(i.optString(a, ""))
    for (a in 0 until l.length()) low!!.add(l.optString(a, ""))
    for (a in 0 until n.length()) normal!!.add(n.optString(a, ""))
    for (a in 0 until h.length()) high!!.add(h.optString(a, ""))
    for (a in 0 until s.length()) star!!.add(s.optString(a, ""))
}

fun savePriorty() {
    // Create All of the Neccessary JSON Values
    val data = JSONObject()
    val i = JSONArray()
    val l = JSONArray()
    val n = JSONArray()
    val h = JSONArray()
    val s = JSONArray()

    // Save Header
    data.put(VERSION, MANAGEMENT)
    data.put(META, meta)

    // Save Tas Lists
    for (temp in ignore!!) i.put(temp)
    for (temp in low!!) l.put(temp)
    for (temp in normal!!) n.put(temp)
    for (temp in high!!) h.put(temp)
    for (temp in star!!) s.put(temp)

    data.put(IGNORE, i)
    data.put(LOW, l)
    data.put(NORMAL, n)
    data.put(HIGH, h)
    data.put(STAR, s)

    saveJSONObject(getApplicationFileDirectory(), FILENAME, data)
}

// Task Management Methods
fun addTask(task: Task) {
    // Folders Have No Priority So Ignore Them
    if (task.type == Task.TYPE_FLDR) return

    // Ignored Tasks
    if (task.priority == 0 && ignore != null) ignore!!.add(task.filename)

    // Low Priority Tasks
    if (task.priority in 1..32 && low != null) low!!.add(task.filename)

    // Normal Priority Tasks
    if (task.priority in 33..66 && normal != null) normal!!.add(task.filename)

    // High Priority Tasks
    if (task.priority in 67..99 && high != null) high!!.add(task.filename)

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
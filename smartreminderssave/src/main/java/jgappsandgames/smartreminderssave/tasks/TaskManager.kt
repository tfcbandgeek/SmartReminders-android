package jgappsandgames.smartreminderssave.tasks

// Android OS
import android.util.Log

// JSON
import org.json.JSONArray
import org.json.JSONObject

// Save
import jgappsandgames.smartreminderssave.utility.*

/**
 * TaskManager
 * Created by joshuagarner on 11/2/17.
 */

// Log Constants
private val LOG = "taskmanager"

// Filepath Constant
private val FILENAME = "taskmanager.srj"

// JSON Constants
private val VERSION = "version"
private val HOME = "home"
private val TASKS = "tasks"
private val ARCHIVED = "archived"
private val DELETED = "deleted"

// Data
var version: Int? = null

var home: ArrayList<String>? = null
var tasks: ArrayList<String>? = null
var archived: ArrayList<String>? = null
var deleted: ArrayList<String>? = null

// Management Methods
fun createTasks() {
    Log.d(LOG, "createTasks Create")
    version = MANAGEMENT

    home = ArrayList()
    tasks = ArrayList()
    archived = ArrayList()
    deleted = ArrayList()
    Log.v(LOG, "createTasks Done")
}

fun loadTasks() {
    Log.d(LOG, "loadTasks Create")
    val data = loadJSON(getApplicationFileDirectory(), FILENAME)

    Log.i(LOG, data.toString(4))
    if (data == JSONObject() || !data.has(VERSION)) {
        Log.v(LOG, "No data, Calling Create")
        createTasks()
        saveTasks()
        return
    }

    Log.v(LOG, "Loading Data")
    version = data.optInt(VERSION, RELEASE)

    var h = data.optJSONArray(HOME)
    if (h == null) h = JSONArray()
    var t = data.optJSONArray(TASKS)
    if (t == null) t = JSONArray()
    var a = data.optJSONArray(ARCHIVED)
    if (a == null) a = JSONArray()
    var d = data.optJSONArray(DELETED)
    if (d == null) d = JSONArray()

    home = ArrayList()
    tasks = ArrayList()
    archived = ArrayList()
    deleted = ArrayList()

    Log.v(LOG, "Insert Tasks")
    (0 until h.length())
            .map { h.optString(it, "") }
            .filter { it != "" && !home!!.contains(it) }
            .forEach { home!!.add(it) }

    (0 until t.length())
            .mapNotNull { t.optString(it) }
            .filterNot { it != "" && !tasks!!.contains(it) }
            .forEach { tasks!!.add(it) }

    (0 until a.length())
            .mapNotNull { a.optString(it) }
            .filterNot { it != "" && !archived!!.contains(it) }
            .forEach { archived!!.add(it) }

    (0 until d.length())
            .mapNotNull { d.optString(it) }
            .filterNot { it != "" && !deleted!!.contains(it) }
            .forEach { deleted!!.add(it) }

    if (deleted!!.size >= 50) deleted!!.removeAt(0)
    Log.i(HOME, home!!.size.toString())
    Log.i(TASKS, tasks!!.size.toString())
    Log.i(ARCHIVED, archived!!.size.toString())
    Log.i(DELETED, deleted!!.size.toString())

    Log.v(LOG, "loadTasks Done")
}

fun saveTasks() {
    if (home == null) return

    Log.d(LOG, "saveTasks Called")
    val data = JSONObject()

    val h = JSONArray()
    val t = JSONArray()
    val a = JSONArray()
    val d = JSONArray()

    for (string in home!!) {
        Log.i(LOG + " home", string)
        h.put(string)
    }
    for (string in tasks!!) t.put(string)
    for (string in archived!!) a.put(string)
    for (string in deleted!!) d.put(string)

    data.put(VERSION, version)
    data.put(HOME, h)
    data.put(TASKS, t)
    data.put(ARCHIVED, a)
    data.put(DELETED, d)

    Log.i(HOME, home!!.size.toString())
    Log.i(TASKS, tasks!!.size.toString())
    Log.i(ARCHIVED, archived!!.size.toString())
    Log.i(DELETED, deleted!!.size.toString())

    Log.v(LOG, "Saving")
    saveJSONObject(getApplicationFileDirectory(), FILENAME, data)

    Log.v(LOG, "saveTasks Done")
}

// Task Methods
fun createTask(): Task {
    if (home == null) loadTasks()

    Log.d(LOG, "createTask Called")

    // Create Task
    val task = Task("home", Task.TYPE_TASK)
    task.save()

    // Add Task
    tasks!!.add(task.filename)
    home!!.add(task.filename)
    saveTasks()

    // Return the New Task
    Log.v(LOG, task.toString())
    return task
}

fun createFolder(): Task {
    if (home == null) loadTasks()

    Log.d(LOG, "createFolder Called")
    // Create Folder
    val task = Task("home", Task.TYPE_FLDR)
    task.save()

    // Add Folder
    tasks!!.add(task.filename)
    home!!.add(task.filename)
    saveTasks()

    // Return the New Folder
    return task
}

fun createTask(parent: Task): Task {
    loadTasks()

    Log.d(LOG, "createTask() Called")

    // Create Task
    val task = Task(parent.filename, Task.TYPE_TASK)
    task.save()

    // Add Task
    tasks!!.add(task.filename)
    saveTasks()

    parent.addChild(task.filename)
    parent.save()

    // Return the New Task
    return task
}

fun createFolder(parent: Task): Task {
    if (home == null) loadTasks()

    Log.d(LOG, "createFolder() Called")

    // Create Folder
    val task = Task(parent.filename, Task.TYPE_FLDR)
    task.save()

    // Add Folder
    tasks!!.add(task.filename)
    saveTasks()

    parent.addChild(task.filename)
    parent.save()

    // Return the New Folder
    return task
}

fun archiveTask(task: Task) {
    if (home == null) loadTasks()

    task.markArchived()
    task.save()

    tasks!!.remove(task.filename)
    archived!!.add(task.filename)
    saveTasks()
}

/* TODO: Implement Method
fun unArchiveTask(task: Task) {

}
*/

fun deleteTask(task: Task) {
    if (home == null) loadTasks()

    if (archived!!.contains(task.filename)) {
        task.delete()
        deleted!!.add(task.filename)
        archived!!.remove(task.filename)
        saveTasks()
    } else {
        archiveTask(task)
    }
}
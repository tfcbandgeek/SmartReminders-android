package jgappsandgames.smartreminderssave.tasks

// JSON
import org.json.JSONArray
import org.json.JSONObject

// Save
import jgappsandgames.smartreminderssave.utility.*

/**
 * TaskManager
 * Created by joshuagarner on 11/2/17.
 */

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
    version = MANAGEMENT

    home = ArrayList()
    tasks = ArrayList()
    archived = ArrayList()
    deleted = ArrayList()
}

fun loadTasks() {
    val data = loadJSON(getApplicationFileDirectory(), FILENAME)

    if (data == JSONObject()) {
        createTasks()
        saveTasks()
        return
    }

    version = data.optInt(VERSION, RELEASE)

    val h = data.optJSONArray(HOME)
    val t = data.optJSONArray(TASKS)
    val a = data.optJSONArray(ARCHIVED)
    val d = data.optJSONArray(DELETED)

    home = ArrayList()
    tasks = ArrayList()
    archived = ArrayList()
    deleted = ArrayList()

    (0 .. h.length())
            .mapNotNull { h.optString(it) }
            .filterNot { home!!.contains(it) }
            .forEach { home!!.add(it) }

    (0 .. t.length())
            .mapNotNull { t.optString(it) }
            .filterNot { tasks!!.contains(it) }
            .forEach { tasks!!.add(it) }

    (0 .. a.length())
            .mapNotNull { a.optString(it) }
            .filterNot { archived!!.contains(it) }
            .forEach { archived!!.add(it) }

    (0 .. d.length())
            .mapNotNull { d.optString(it) }
            .filterNot { deleted!!.contains(it) }
            .forEach { deleted!!.add(it) }

    if (deleted!!.size >= 50) deleted!!.removeAt(0)
}

fun saveTasks() {
    val data = JSONObject()

    val h = JSONArray()
    val t = JSONArray()
    val a = JSONArray()
    val d = JSONArray()

    for (string in home!!) h.put(string)
    for (string in tasks!!) t.put(string)
    for (string in archived!!) a.put(string)
    for (string in deleted!!) d.put(string)

    data.put(HOME, h)
    data.put(TASKS, t)
    data.put(ARCHIVED, a)
    data.put(DELETED, d)

    saveJSONObject(getApplicationFileDirectory(), FILENAME, data)
}

// Task Methods
fun createTask(): Task {
    // Create Task
    val task = Task("home", Task.TYPE_TASK)
    task.save()

    // Add Task
    tasks!!.add(task.filename)
    home!!.add(task.filename)
    saveTasks()

    // Return the New Task
    return task
}

fun createFolder(): Task {
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
    if (archived!!.contains(task.filename)) {
        task.delete()
        deleted!!.add(task.filename)
        archived!!.remove(task.filename)
        saveTasks()
    } else {
        archiveTask(task)
    }
}
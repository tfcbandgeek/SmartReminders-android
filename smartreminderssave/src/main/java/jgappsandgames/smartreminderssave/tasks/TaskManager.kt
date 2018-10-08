package jgappsandgames.smartreminderssave.tasks

// Java
import jgappsandgames.smartreminderssave.settings.getUseVersion
import java.io.File
import java.io.IOException

// JSONObject
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

// Pool Utility
import me.jgappsandgames.poolutility.SimplePool

// Save Library
import jgappsandgames.smartreminderssave.utility.*

/**
 * TaskManager
 * Created by joshua on 12/12/2017.
 */
// Constants -------------------------------------------------------------------------------
private const val FILENAME = "taskmanager.srj"

private const val VERSION = "version"
private const val META = "meta"
private const val HOME = "home"
private const val TASKS = "tasks"
private const val ARCHIVED = "archived"
private const val DELETED = "deleted"

private const val VERSION_12 = "version"
private const val META_12 = "meta"
private const val HOME_12 = "home"
private const val TASKS_12 = "tasks"
private const val ARCHIVED_12 = "archived"
private const val DELETED_12 = "deleted"

// Pools -----------------------------------------------------------------------------------
private val taskPool = SimplePool { Task() } //Pool(maxSize = 100, minSize = 20, generator = TaskCreator())

// Data ------------------------------------------------------------------------------------
private var version: Int = MANAGEMENT

private var meta: JSONObject = JSONObject()
private var home: ArrayList<String> = ArrayList()
private var tasks: ArrayList<String> = ArrayList()
private var archived: ArrayList<String> = ArrayList()
private var deleted: ArrayList<String> = ArrayList()

// Management Methods ----------------------------------------------------------------------
fun createTasks() {
    if (File(getApplicationDataDirectory(), FILENAME).exists()) loadTasks()
    else forceCreateTasks()
}

fun forceCreateTasks() {
    version = getUseVersion()
    meta = JSONObject()

    home.clear()
    tasks.clear()
    archived.clear()
    deleted.clear()
}

fun loadTasks() {
    try {
        loadJSON(loadJSONObject(File(getApplicationDataDirectory(), FILENAME)))
    } catch (i: IOException) {
        i.printStackTrace()

        createTasks()
        saveTasks()
    }

    if (deleted.size >= 50) deleted.removeAt(0)
}

fun saveTasks() = saveJSONObject(File(getApplicationDataDirectory(), FILENAME), saveJSON())

fun clearTasks() {
    for (task in archived) deleteTask(getTaskFromPool().load(task))
    archived = ArrayList()
    saveTasks()
}

// JSONManagement Methods --------------------------------------------------------------------------
private fun loadJSON(data: JSONObject?) {
    if (data == null) {
        createTasks()
        return
    }

    version = data.optInt(VERSION, RELEASE)

    if (version <= MANAGEMENT) {
        home.clear()
        tasks.clear()
        archived.clear()
        deleted.clear()

        val h = data.optJSONArray(HOME)
        val t = data.optJSONArray(TASKS)
        val a = data.optJSONArray(ARCHIVED)
        val d = data.optJSONArray(DELETED)

        if (h != null && h.length() != 0)
            for (i in 0 until h.length())
                if (!home.contains(h.optString(i))) home.add(h.optString(i))
        if (t != null && t.length() != 0)
            for (i in 0 until t.length())
                if (!tasks.contains(t.optString(i))) tasks.add(t.optString(i))
        if (a != null && a.length() != 0)
            for (i in 0 until a.length())
                if (!archived.contains(a.optString(i))) archived.add(a.optString(i))
        if (d != null && d.length() != 0)
            for (i in 0 until d.length())
                if (!deleted.contains(d.optString(i))) deleted.add(d.optString(i))

        // API 11
        meta = if (version >= MANAGEMENT) data.optJSONObject(META)
        else JSONObject()
    } else {
        home.clear()
        tasks.clear()
        archived.clear()
        deleted.clear()

        val h = data.optJSONArray(HOME_12)
        val t = data.optJSONArray(TASKS_12)
        val a = data.optJSONArray(ARCHIVED_12)
        val d = data.optJSONArray(DELETED_12)

        if (h != null && h.length() != 0)
            for (i in 0 until h.length())
                if (!home.contains(h.optString(i))) home.add(h.optString(i))
        if (t != null && t.length() != 0)
            for (i in 0 until t.length())
                if (!tasks.contains(t.optString(i))) tasks.add(t.optString(i))
        if (a != null && a.length() != 0)
            for (i in 0 until a.length())
                if (!archived.contains(a.optString(i))) archived.add(a.optString(i))
        if (d != null && d.length() != 0)
            for (i in 0 until d.length())
                if (!deleted.contains(d.optString(i))) deleted.add(d.optString(i))

        // API 11
        meta = if (version >= MANAGEMENT) data.optJSONObject(META_12)
        else JSONObject()
    }
}

private fun saveJSON(): JSONObject {
    val data = JSONObject()

    try {
        data.put(VERSION, MANAGEMENT)

        val h = JSONArray()
        val t = JSONArray()
        val a = JSONArray()
        val d = JSONArray()

        for (task in home) h.put(task)
        for (task in tasks) t.put(task)
        for (task in archived) a.put(task)
        for (task in deleted) d.put(task)

        if (getUseVersion() <= MANAGEMENT) {
            data.put(META, meta)
            data.put(HOME, h)
            data.put(TASKS, t)
            data.put(ARCHIVED, a)
            data.put(DELETED, d)
        } else {
            data.put(VERSION_12, MANAGEMENT)
            data.put(META_12, meta)
            data.put(HOME_12, h)
            data.put(TASKS_12, t)
            data.put(ARCHIVED_12, a)
            data.put(DELETED_12, d)
        }
    } catch (j: JSONException) {
        j.printStackTrace()
    }

    return data
}

// Task Methods ------------------------------------------------------------------------------------
fun addTask(task: Task, homeTask: Boolean): Task {
    if (homeTask) {
        home.add(task.getFilename())
        tasks.add(task.getFilename())
        saveTasks()
    } else {
        val p = getTaskFromPool().load(task.getParent())
        p.addChild(task.getFilename())
        tasks.add(task.getFilename())
        p.save()
        saveTasks()
    }

    return task
}

fun archiveTask(task: Task) {
    task.markArchived()
    task.save()

    when {
        task.getParent() == "home" -> home.remove(task.getFilename())

        tasks.contains(task.getParent()) -> {
            val parent = taskPool.getPoolObject().load(task.getParent())
            parent.removeChild(task.getFilename())
            parent.save()
            taskPool.returnPoolObject(parent)
        }

        else -> {
            try {
                val parent = taskPool.getPoolObject().load(task.getParent())
                parent.removeChild(task.getFilename())
                parent.save()
                taskPool.returnPoolObject(parent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    if (task.getType() == Task.TYPE_FOLDER) {
        val children: ArrayList<String> = task.getChildren().clone() as ArrayList<String>
        val t = taskPool.getPoolObject()

        for (i in 0 until children.size) archiveTask(t.load(children[i]))
        taskPool.returnPoolObject(t)
    }

    tasks.remove(task.getFilename())
    archived.add(task.getFilename())
    saveTasks()
}

fun deleteTask(task: Task): Boolean {
    if (archived.contains(task.getFilename())) {
        task.delete()
        deleted.add(task.getFilename())
        archived.remove(task.getFilename())
        saveTasks()
        return true
    }

    return false
}

// Getters -----------------------------------------------------------------------------------------
fun getHome(): ArrayList<String> = home

fun getHomeTasks(): ArrayList<Task> {
    val t = ArrayList<Task>()
    for (i in 0 until home.size) t.add(taskPool.getPoolObject().load(home[i]))
    return t
}

fun getAll(): ArrayList<String> = tasks

fun getAllTasks(): ArrayList<Task> {
    val t = ArrayList<Task>()
    for (i in 0 until tasks.size) t.add(taskPool.getPoolObject().load(tasks[i]))
    return t
}

fun getArchived(): ArrayList<String> = archived

fun getArchivedTasks(): ArrayList<Task> {
    val t = ArrayList<Task>()
    for (i in 0 until archived.size) t.add(taskPool.getPoolObject().load(archived[i]))
    return t
}

fun getDeleted(): ArrayList<String> = deleted

fun getDeletedTasks(): ArrayList<Task> {
    val t = ArrayList<Task>()
    for (i in 0 until deleted.size) t.add(taskPool.getPoolObject().load(deleted[i]))
    return t
}

// Pool Methods ------------------------------------------------------------------------------------
fun getTaskFromPool(): Task = taskPool.getPoolObject()

fun returnTaskToPool(task: Task) {
    taskPool.returnPoolObject(task)
}
package jgappsandgames.smartreminderssave.planner

import jgappsandgames.smartreminderssave.utility.API
import jgappsandgames.smartreminderssave.utility.FileUtility
import jgappsandgames.smartreminderssave.utility.JSONUtility
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

/**
 * Day
 * Created by joshua on 2/24/2018.
 */
class Day(filename: String, date: Calendar, note: String, tasks: ArrayList<String>) {
    // Constants -----------------------------------------------------------------------------------
    companion object {
        private const val DATE = "a"
        private const val VERSION = "version"
        private const val META = "c"
        private const val NOTE = "d"
        private const val TASKS = "e"
    }

    // Data ----------------------------------------------------------------------------------------
    private var filename: String = ""
    private var date: Calendar

    private var version: Int = API.SHRINKING
    private var meta: JSONObject = JSONObject()

    private var note: String
    private var tasks: ArrayList<String>

    // Constructors --------------------------------------------------------------------------------
    init {
        this.filename = filename
        this.date = date

        this.note = note
        this.tasks = tasks
    }

    constructor(day: Calendar) : this(
            day.get(Calendar.YEAR).toString() + "_" +
            day.get(Calendar.MONTH).toString() + "_" +
            day.get(Calendar.DAY_OF_MONTH).toString() + ".planner.srj",
            day, "", ArrayList()) {
        if (File(FileUtility.getApplicationDataDirectory(), filename).exists()) {
            val data = JSONUtility.loadJSON(File(FileUtility.getApplicationDataDirectory(), filename))

            note = data.optString(NOTE, "")
            val t = data.optJSONArray(TASKS)
            for (i in 0 until t.length()) tasks.add(t.optString(i))
            meta = data.getJSONObject(META)
        }
    }

    // Management Methods --------------------------------------------------------------------------
    fun save() {
        val data = JSONObject()
        val array = JSONArray()

        if (tasks.size != 0) for (task in tasks) array.put(task)

        data.put(DATE, JSONUtility.saveCalendar(date))
        data.put(VERSION, API.SHRINKING)
        data.put(META, meta)
        data.put(NOTE, note)
        data.put(TASKS, array)

        JSONUtility.saveJSONObject(File(FileUtility.getApplicationDataDirectory(), filename), data)
    }

    // Getters -------------------------------------------------------------------------------------
    fun getFilename(): String {
        return filename
    }

    fun getDate(): Calendar {
        return date
    }

    fun getMeta():JSONObject {
        return meta
    }

    fun getNote(): String {
        return note
    }

    fun getTasks(): ArrayList<String> {
        return tasks
    }

    // Setters -------------------------------------------------------------------------------------
    fun setMeta(meta: JSONObject): Day {
        this.meta = meta
        return this
    }

    fun setNote(note: String): Day {
        this.note = note
        return this
    }

    fun setTasks(tasks: ArrayList<String>): Day {
        this.tasks = tasks
        return this
    }

    // Manipulators --------------------------------------------------------------------------------
    fun addMeta(key: String, boolean: Boolean): Day {
        if (meta.has(key)) meta.remove(key)
        meta.put(key, boolean)
        return this
    }

    fun addMeta(key: String, int: Int): Day {
        if (meta.has(key)) meta.remove(key)
        meta.put(key, int)
        return this
    }

    fun addMeta(key: String, double: Double): Day {
        if (meta.has(key)) meta.remove(key)
        meta.put(key, double)
        return this
    }

    fun addMeta(key: String, long: Long): Day {
        if (meta.has(key)) meta.remove(key)
        meta.put(key, long)
        return this
    }

    fun addMeta(key: String, string: String): Day {
        if (meta.has(key)) meta.remove(key)
        meta.put(key, string)
        return this
    }

    fun addMeta(key: String, jsonObject: JSONObject): Day {
        if (meta.has(key)) meta.remove(key)
        meta.put(key, jsonObject)
        return this
    }

    fun addMeta(key: String, jsonArray: JSONArray): Day {
        if (meta.has(key)) meta.remove(key)
        meta.put(key, jsonArray)
        return this
    }

    fun removeMeta(key: String): Day {
        if (meta.has(key)) meta.remove(key)
        return this
    }

    fun addTask(task: String): Day {
        if (!tasks.contains(task)) tasks.add(task)
        return this
    }

    fun removeTask(task: String): Day {
        tasks.remove(task)
        return this
    }

    // To Methods ----------------------------------------------------------------------------------
    // Todo: toJSON()
    // Todo: toString()
}
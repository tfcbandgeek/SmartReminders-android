package jgappsandgames.smartreminderssave.tasks

// Java
import java.io.File
import java.util.Calendar

// JSON
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

// Save
import jgappsandgames.smartreminderssave.utility.RELEASE
import jgappsandgames.smartreminderssave.utility.getApplicationFileDirectory
import jgappsandgames.smartreminderssave.utility.loadJSON
import jgappsandgames.smartreminderssave.utility.saveJSONObject
import jgappsandgames.smartreminderssave.date.removeTask
import jgappsandgames.smartreminderssave.status.addTask
import jgappsandgames.smartreminderssave.status.editTask

/**
 * Task
 * Created by joshu on 11/13/2017.
 *
 * API 11
 */

// Type Constants
val TYPE_NONE = 0
val TYPE_FLDR = 1
val TYPE_TASK = 2

// Checkpoint Constants
val CHECKPOINT_POSITION = "position"
val CHECKPOINT_STATUS = "status"
val CHECKPOINT_TEXT = "text"

// Status Constants
val STATUS_DONE = 10

class Task {
    // Save Constants
    private val PARENT = "parent"
    private val VERSION = "version"
    private val TYPE = "type"
    private val TASK_ID = "id"

    private val CAL_CREATE = "cal_a"
    private val CAL_DUE = "cal_b"
    private val CAL_UPDATE = "cal_c"
    private val CAL_ARCHIVED = "cal_d"
    private val CAL_DELETED = "cal_e"

    private val TITLE = "title"
    private val NOTE = "note"
    private val TAGS = "tags"
    private val CHILDREN = "children"
    private val CHECKPOINTS = "checkpoints"
    private val STATUS = "status"
    private val PRIORITY = "priority"

    private val COMPLETED_ON_TIME = "completed_on_time"
    private val COMPLETED_LATE = "completed_late"

    // Data
    private var filename: String = ""
    private var parent: String? = null
    private var version: Int = 0
    private var type: Int = 0
    private var task_id: Long = 0

    private var date_create: Calendar? = null
    private var date_due: Calendar? = null
    private var date_updated: Calendar? = null
    private var date_deleted: Calendar? = null
    private var date_archived: Calendar? = null

    private var title: String? = null
    private var note: String? = null
    private var tags: ArrayList<String>? = null
    private var children: ArrayList<String>? = null
    private var checkpoints: ArrayList<Checkpoint>? = null
    private var status: Int = 0
    private var priority: Int = 0

    private var complete_on_time: Boolean = false
    private var complete_late: Boolean = false

    // Calendar
    private val j_cal = JSONCalendar()

    // Initializers
    constructor(parent: String, type: Int) {
        val calendar = Calendar.getInstance()

        filename = calendar.timeInMillis.toString() + ".srj"

        this.parent = parent
        version = RELEASE
        this.type = type
        task_id = Calendar.getInstance().timeInMillis

        date_create = calendar.clone() as Calendar
        date_due = null
        date_updated = calendar.clone() as Calendar
        date_archived = null
        date_deleted = null

        title = ""
        note = ""
        tags = ArrayList()
        children = ArrayList()
        checkpoints = ArrayList()
        status = 0
        priority = 20

        complete_on_time = false
        complete_late = false
    }

    constructor(filename: String) {
        this.filename = filename
        loadJSON(loadJSON(getApplicationFileDirectory(), filename))
    }

    // Management Methods
    fun save() {
        saveJSONObject(getApplicationFileDirectory(), filename, toJSON())
    }

    fun delete() {
        val file = File(getApplicationFileDirectory(), filename)

        file.delete()
    }

    // JSON Management Methods
    fun loadJSON(data: JSONObject?) {
        if (data == null) {
            title = "Null Task Load Error"
            note = "Error occurs when there is no Task to load"
            return
        }

        version = data.optInt(VERSION, RELEASE)
        parent = data.optString(PARENT, "home")
        type = data.optInt(TYPE, TYPE_NONE)
        task_id = data.optLong(TASK_ID, Calendar.getInstance().timeInMillis)

        date_create = j_cal.loadCalendar(data.optJSONObject(CAL_CREATE))
        date_due = j_cal.loadCalendar(data.optJSONObject(CAL_DUE))
        date_updated = j_cal.loadCalendar(data.optJSONObject(CAL_UPDATE))
        date_archived = j_cal.loadCalendar(data.optJSONObject(CAL_ARCHIVED))
        date_deleted = j_cal.loadCalendar(data.optJSONObject(CAL_DELETED))

        title = data.optString(TITLE, "")
        note = data.optString(NOTE, "")
        status = data.optInt(STATUS, 0)
        priority = data.optInt(PRIORITY, 20)
        complete_on_time = data.optBoolean(COMPLETED_ON_TIME, false)
        complete_late = data.optBoolean(COMPLETED_LATE, false)

        val t = data.optJSONArray(TAGS)
        val c = data.optJSONArray(CHILDREN)
        val p = data.optJSONArray(CHECKPOINTS)

        tags = ArrayList()
        children = ArrayList()
        checkpoints = ArrayList()

        if (t != null && t.length() != 0) for (i in 0 until t.length()) tags!!.add(t.optString(i))
        if (c != null && c.length() != 0) for (i in 0 until c.length()) children!!.add(c.optString(i))
        if (p != null && p.length() != 0) for (i in 0 until p.length()) checkpoints!!.add(Checkpoint(p.optJSONObject(i)))
    }

    fun toJSON(): JSONObject {
        val data = JSONObject()

        try {
            data.put(PARENT, parent)
            data.put(VERSION, version)
            data.put(TYPE, type)
            data.put(TASK_ID, task_id)

            data.put(CAL_CREATE, j_cal.saveCalendar(date_create))
            data.put(CAL_DUE, j_cal.saveCalendar(date_due))
            data.put(CAL_UPDATE, j_cal.saveCalendar(date_updated))
            data.put(CAL_ARCHIVED, j_cal.saveCalendar(date_archived))
            data.put(CAL_DELETED, j_cal.saveCalendar(date_deleted))

            data.put(TITLE, title)
            data.put(NOTE, note)
            data.put(STATUS, status)
            data.put(PRIORITY, priority)
            data.put(COMPLETED_ON_TIME, complete_on_time)
            data.put(COMPLETED_LATE, complete_late)

            val t = JSONArray()
            val c = JSONArray()
            val p = JSONArray()

            if (tags != null && tags!!.size != 0) for (tag in tags!!) t.put(tag)
            if (children != null && children!!.size != 0) for (child in children!!) c.put(child)
            if (checkpoints != null && checkpoints!!.size != 0) for (checkpoint in checkpoints!!) p.put(checkpoint.toJSON())

            data.put(TAGS, t)
            data.put(CHILDREN, c)
            data.put(CHECKPOINTS, p)
        } catch (j: JSONException) {
            j.printStackTrace()
        } catch (n: NullPointerException) {
            n.printStackTrace()
        }

        return data
    }

    override fun toString(): String {
        try {
            return toJSON().toString()
        } catch (e: NullPointerException) {
            e.printStackTrace()
            return super.toString()
        }

    }

    fun updateTask(data: JSONObject) {
        if (j_cal.loadCalendar(data.optJSONObject(CAL_UPDATE))!!.after(date_updated)) loadJSON(data)
    }

    // Getters
    fun getFilename(): String {
        return filename
    }

    fun getParent(): String? {
        return parent
    }

    fun getType(): Int {
        return type
    }

    fun getID(): Long {
        return task_id
    }

    fun getDateCreated(): Calendar? {
        return date_create
    }

    @Deprecated("")
    fun getDate_due(): Calendar? {
        return date_due
    }

    fun getDateDue(): Calendar? {
        return date_due
    }

    fun getDateDueString(): String {
        return if (date_due == null) "No Date" else (date_due!!.get(Calendar.MONTH) + 1).toString() + "/" +
                date_due!!.get(Calendar.DAY_OF_MONTH).toString() + "/" +
                date_due!!.get(Calendar.YEAR).toString()
    }

    fun getDateUpdated(): Calendar? {
        return date_updated
    }

    fun getDateArchived(): Calendar? {
        return date_archived
    }

    fun getDate_deleted(): Calendar? {
        return date_deleted
    }

    fun getTitle(): String? {
        return title
    }

    fun getNote(): String? {
        return note
    }

    fun getTags(): ArrayList<String>? {
        return tags
    }

    fun getTagString(): String {
        if (tags == null || tags!!.size == 0) return "No Tags Selected"

        val builder = StringBuilder()
        for (tag in tags!!) builder.append(tag).append(", ")
        if (builder.length >= 2) builder.setLength(builder.length - 2)
        return builder.toString()
    }

    fun getChildren(): ArrayList<String>? {
        return children
    }

    fun getCheckpoints(): ArrayList<Checkpoint>? {
        return checkpoints
    }

    fun getStatus(): Int {
        return status
    }

    fun isCompleted(): Boolean {
        if (status == STATUS_DONE) return true

        for (c in checkpoints!!) if (!c.status) return false

        return true
    }

    fun isOverdue(): Boolean {
        val today = Calendar.getInstance()

        // If the Task is a folder Return False
        if (type == TYPE_FLDR) return false

        // If the Task is Completed Return False
        if (isCompleted()) return false

        // If the Task has no date due Return False
        if (date_due == null) return false

        // If the Task is Due today Return False
        if (date_due!!.get(Calendar.YEAR) == today.get(Calendar.YEAR) && date_due!!.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) return false

        // If the Task's Due Date is After todays date Return False
        if (date_due!!.get(Calendar.YEAR) > today.get(Calendar.YEAR)) return false
        return if (date_due!!.get(Calendar.YEAR) == today.get(Calendar.YEAR) && date_due!!.get(Calendar.DAY_OF_YEAR) > today.get(Calendar.DAY_OF_YEAR)) false else true

        // The task is Overdue
    }

    fun getStatusString(): String {
        return if (isCompleted()) "Completed" else "Incomplete"
    }

    fun getPriority(): Int {
        return priority
    }

    fun onTime(): Boolean {
        return complete_on_time
    }

    fun late(): Boolean {
        return complete_late
    }

    // Setters
    fun setDateDue(calendar: Calendar?): Task {
        val b = date_due == null

        // Task Changes
        if (calendar == null)
            date_due = null
        else
            date_due = calendar.clone() as Calendar
        date_updated = Calendar.getInstance()

        // DateManager Changes
        if (b && calendar != null)
            addTask(this)
        else if (calendar != null)
            editTask(this)
        else if (calendar == null) removeTask(this)

        // Return Task Object
        return this
    }

    fun setTitle(title: String): Task {
        this.title = title
        date_updated = Calendar.getInstance()
        return this
    }

    fun setNote(note: String): Task {
        this.note = note
        date_updated = Calendar.getInstance()
        return this
    }

    fun setTags(tags: ArrayList<String>): Task {
        this.tags = tags
        date_updated = Calendar.getInstance()
        return this
    }

    fun setChildren(children: ArrayList<String>): Task {
        this.children = children
        date_updated = Calendar.getInstance()
        return this
    }

    fun setCheckpoints(checkpoints: ArrayList<Checkpoint>): Task {
        this.checkpoints = checkpoints
        date_updated = Calendar.getInstance()
        return this
    }

    fun setPriority(priority: Int): Task {
        // Task Changes
        this.priority = priority
        date_updated = Calendar.getInstance()

        // Priority Changes
        editTask(this)

        // Return Task
        return this
    }

    fun markComplete(mark: Boolean): Task {
        // Task Changes
        if (mark) {
            status = STATUS_DONE

            if (date_due == null)
                complete_on_time = true
            else if (date_due!!.before(Calendar.getInstance()))
                complete_late = true
            else
                complete_on_time = true
        } else {
            status = 0

            complete_late = false
            complete_on_time = false
        }

        date_updated = Calendar.getInstance()

        // Status Changes
        editTask(this)
        return this
    }

    fun markArchived(): Task {
        date_archived = Calendar.getInstance()
        date_updated = Calendar.getInstance()
        return this
    }

    fun markDeleted(): Task {
        date_deleted = Calendar.getInstance()
        date_updated = Calendar.getInstance()
        return this
    }

    // Manipulaters
    fun addTag(tag: String): Task {
        if (!tags!!.contains(tag)) {
            tags!!.add(tag)
            date_updated = Calendar.getInstance()
            return this
        }

        return this
    }

    fun removeTag(tag: String): Task {
        if (tags!!.contains(tag)) {
            tags!!.remove(tag)
            date_updated = Calendar.getInstance()
            return this
        }

        return this
    }

    fun addChild(child: String): Task {
        if (!children!!.contains(child)) {
            children!!.add(child)
            date_updated = Calendar.getInstance()
            return this
        }

        return this
    }

    fun removeChild(child: String): Task {
        if (children!!.contains(child)) {
            children!!.remove(child)
            date_updated = Calendar.getInstance()
            return this
        }

        return this
    }

    fun addCheckpoint(checkpoint: Checkpoint): Task {
        for (c in checkpoints!!) if (c.id == checkpoint.id) return this

        checkpoints!!.add(checkpoint)
        date_updated = Calendar.getInstance()
        return this
    }

    fun editCheckpoint(checkpoint: Checkpoint): Task {
        for (c in checkpoints!!) {
            if (c.id == checkpoint.id) {
                c.status = checkpoint.status
                c.text = checkpoint.text
                date_updated = Calendar.getInstance()
                return this
            }
        }

        return this
    }

    fun removeCheckpoint(checkpoint: Checkpoint): Task {
        for (c in checkpoints!!) {
            if (c.id == checkpoint.id) {
                checkpoints!!.remove(c)
                date_updated = Calendar.getInstance()
                return this
            }
        }

        return this
    }

    // Class to Convert A Calendar to JSONObject
    private inner class JSONCalendar {
        private val ACTIVE = "active"
        private val DATE = "date"

        internal fun loadCalendar(data: JSONObject): Calendar? {
            if (data.optBoolean(ACTIVE, false)) {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = data.optLong(DATE, 0)
                return calendar
            }

            return null
        }

        internal fun saveCalendar(calendar: Calendar?): JSONObject {
            try {
                val data = JSONObject()

                if (calendar == null) {
                    data.put(ACTIVE, false)
                } else {
                    data.put(ACTIVE, true)
                    data.put(DATE, calendar.timeInMillis)
                }
                return data
            } catch (j: JSONException) {
                j.printStackTrace()
            }

            return JSONObject()
        }
    }
}
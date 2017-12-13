package jgappsandgames.smartreminderssave.tasks

import jgappsandgames.smartreminderssave.utility.API
import jgappsandgames.smartreminderssave.utility.FileUtility
import jgappsandgames.smartreminderssave.utility.JSONUtility
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.util.*

/**
 * Task
 * Created by joshua on 12/12/2017.
 */
class Task() {
    companion object {
        // Save Constants
        private val PARENT = "parent"
        private val VERSION = "version"
        private val META = "meta"
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

        // Type Constants
        @JvmField
        val TYPE_NONE = 0
        @JvmField
        val TYPE_FLDR = 1
        @JvmField
        val TYPE_TASK = 2

        // Checkpoint Constants
        @JvmField
        val CHECKPOINT_POSITION = "position"
        @JvmField
        val CHECKPOINT_STATUS = "status"
        @JvmField
        val CHECKPOINT_TEXT = "text"

        // Status Constants
        @JvmField
        val STATUS_DONE = 10
    }

    // Data
    private var filename: String? = null
    private var parent: String? = null
    private var version: Int = 0
    private var meta: JSONObject? = null
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

    // Initializers
    constructor(filename: String): this() {
        this.filename = filename
        loadJSON(JSONUtility.loadJSON(File(FileUtility.getApplicationDataDirectory(), filename)))
    }

    constructor(parent: String, type: Int): this() {
        val calendar = Calendar.getInstance()

        this.filename = calendar.timeInMillis.toString() + ".srj"

        this.parent = parent
        version = API.MANAGEMENT
        meta = JSONObject()
        this.type = type
        task_id = calendar.timeInMillis

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

    constructor(data: JSONObject): this() {
        filename = data.optString("filename", "error.srj")
        loadJSON(data)
    }

    // Management Methods
    fun save() {
        JSONUtility.saveJSONObject(File(FileUtility.getApplicationDataDirectory(), filename), toJSON())
    }

    fun delete() {
        val file = File(FileUtility.getApplicationDataDirectory(), filename)
        file.delete()
    }

    // JSON Management Methods
    fun loadJSON(data: JSONObject?) {
        if (data == null) {
            title = "Null Task Load Error"
            note = "Error occurs when there is no Task to load"
            return
        }

        version = data.optInt(VERSION, API.RELEASE)
        parent = data.optString(PARENT, "home")
        type = data.optInt(TYPE, TYPE_NONE)
        task_id = data.optLong(TASK_ID, Calendar.getInstance().timeInMillis)

        date_create = JSONUtility.loadCalendar(data.optJSONObject(CAL_CREATE))
        date_due = JSONUtility.loadCalendar(data.optJSONObject(CAL_DUE))
        date_updated = JSONUtility.loadCalendar(data.optJSONObject(CAL_UPDATE))
        date_archived = JSONUtility.loadCalendar(data.optJSONObject(CAL_ARCHIVED))
        date_deleted = JSONUtility.loadCalendar(data.optJSONObject(CAL_DELETED))

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

        // API 11
        if (version >= API.MANAGEMENT) {
            meta = data.optJSONObject(META)
        } else {
            meta = JSONObject()
        }
    }

    fun toJSON(): JSONObject {
        val data = JSONObject()

        try {
            data.put(PARENT, parent)
            data.put(VERSION, API.MANAGEMENT)
            data.put(TYPE, type)
            data.put(TASK_ID, task_id)

            data.put(CAL_CREATE, JSONUtility.saveCalendar(date_create))
            data.put(CAL_DUE, JSONUtility.saveCalendar(date_due))
            data.put(CAL_UPDATE, JSONUtility.saveCalendar(date_updated))
            data.put(CAL_ARCHIVED, JSONUtility.saveCalendar(date_archived))
            data.put(CAL_DELETED, JSONUtility.saveCalendar(date_deleted))

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

            // API 11
            data.put(META, meta)
        } catch (j: JSONException) {
            j.printStackTrace()
        } catch (n: NullPointerException) {
            n.printStackTrace()
            //throw new RuntimeException("Fix Me");
        }

        return data
    }

    fun updateTask(data: JSONObject) {
        if (JSONUtility.loadCalendar(data.optJSONObject(CAL_UPDATE))!!.after(date_updated)) loadJSON(data)
    }

    // Getters
    fun getFilename(): String {
        return filename!!
    }

    fun getParent(): String {
        return parent!!
    }

    fun getMeta(): JSONObject {
        return meta!!
    }

    fun getType(): Int {
        return type
    }

    fun getID(): Long {
        return task_id
    }

    fun getDateCreated(): Calendar {
        return date_create!!
    }

    fun getDateDue(): Calendar {
        return date_due!!
    }

    fun getDateDueString(): String {
        return if (date_due == null) "No Date" else (date_due!!.get(Calendar.MONTH) + 1).toString() + "/" +
                date_due!!.get(Calendar.DAY_OF_MONTH).toString() + "/" +
                date_due!!.get(Calendar.YEAR).toString()
    }

    fun getDateUpdated(): Calendar {
        return date_updated!!
    }

    fun getDateArchived(): Calendar {
        return date_archived!!
    }

    fun getDateDeleted(): Calendar {
        return date_deleted!!
    }

    fun getTitle(): String {
        return title!!
    }

    fun getNote(): String {
        return note!!
    }

    fun getTags(): ArrayList<String> {
        return tags!!
    }

    fun getTagString(): String {
        if (tags == null || tags!!.size == 0) return "No Tags Selected"

        val builder = StringBuilder()
        for (tag in tags!!) builder.append(tag).append(", ")
        if (builder.length >= 2) builder.setLength(builder.length - 2)
        return builder.toString()
    }

    fun getChildren(): ArrayList<String> {
        return children!!
    }

    fun getCheckpoints(): ArrayList<Checkpoint> {
        return checkpoints!!
    }

    fun getStatus(): Int {
        return status
    }

    fun isCompleted(): Boolean {
        return status == STATUS_DONE
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
        if (calendar == null) date_due = null
        else date_due = calendar.clone() as Calendar
        date_updated = Calendar.getInstance()
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
        this.priority = priority
        date_updated = Calendar.getInstance()
        return this
    }

    fun markComplete(mark: Boolean): Task {
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
    fun addMeta(key: String, boolean: Boolean) {
        if (meta!!.has(key)) meta!!.remove(key)
        meta!!.put(key, boolean)
    }

    fun addMeta(key: String, int: Int) {
        if (meta!!.has(key)) meta!!.remove(key)
        meta!!.put(key, int)
    }

    fun addMeta(key: String, double: Double) {
        if (meta!!.has(key)) meta!!.remove(key)
        meta!!.put(key, double)
    }

    fun addMeta(key: String, long: Long) {
        if (meta!!.has(key)) meta!!.remove(key)
        meta!!.put(key, long)
    }

    fun addMeta(key: String, string: String) {
        if (meta!!.has(key)) meta!!.remove(key)
        meta!!.put(key, string)
    }

    fun addMeta(key: String, jsonObject: JSONObject) {
        if (meta!!.has(key)) meta!!.remove(key)
        meta!!.put(key, jsonObject)
    }

    fun addMeta(key: String, jsonArray: JSONArray) {
        if (meta!!.has(key)) meta!!.remove(key)
        meta!!.put(key, jsonArray)
    }

    fun removeMeta(key: String) {
        if (meta!!.has(key)) meta!!.remove(key)
    }

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
}
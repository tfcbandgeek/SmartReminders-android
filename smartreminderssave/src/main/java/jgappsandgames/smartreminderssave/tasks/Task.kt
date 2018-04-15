package jgappsandgames.smartreminderssave.tasks

// Java
import java.io.File
import java.util.Calendar

// JSON
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

// Save
import jgappsandgames.smartreminderssave.utility.API
import jgappsandgames.smartreminderssave.utility.FileUtility
import jgappsandgames.smartreminderssave.utility.JSONUtility

/**
 * Task
 * Created by joshua on 12/12/2017.
 */

class Task() {
    companion object {
        // Constants -------------------------------------------------------------------------------
        private const val PARENT = "parent"
        private const val VERSION = "version"
        private const val META = "meta"
        private const val TYPE = "type"
        private const val TASK_ID = "id"

        private const val PARENT_12 = "a"
        // private const val VERSION_12 = "version"
        private const val META_12 = "b"
        private const val TYPE_12 = "c"
        private const val TASK_ID_12 = "d"

        private const val CAL_CREATE = "cal_a"
        private const val CAL_DUE = "cal_b"
        private const val CAL_UPDATE = "cal_c"
        private const val CAL_ARCHIVED = "cal_d"
        private const val CAL_DELETED = "cal_e"

        private const val CAL_CREATE_12 = "e"
        private const val CAL_DUE_12 = "f"
        private const val CAL_UPDATE_12 = "g"
        private const val CAL_ARCHIVED_12 = "h"
        private const val CAL_DELETED_12 = "i"

        private const val TITLE = "title"
        private const val NOTE = "note"
        private const val TAGS = "tags"
        private const val CHILDREN = "children"
        private const val CHECKPOINTS = "checkpoints"
        private const val STATUS = "status"
        private const val PRIORITY = "priority"

        private const val TITLE_12 = "j"
        private const val NOTE_12 = "k"
        private const val TAGS_12 = "l"
        private const val CHILDREN_12 = "m"
        private const val CHECKPOINTS_12 = "n"
        private const val LIST_12 = "o"
        private const val STATUS_12 = "p"
        private const val PRIORITY_12 = "q"

        private const val COMPLETED_ON_TIME = "completed_on_time"
        private const val COMPLETED_LATE = "completed_late"

        private const val COMPLETED_ON_TIME_12 = "r"
        private const val COMPLETED_LATE_12 = "s"

        // Type Constants
        const val TYPE_NONE = 0
        const val TYPE_FLDR = 1
        const val TYPE_TASK = 2
        const val TYPE_NOTE = 3
        const val TYPE_LIST = 4
        const val TYPE_SHORTCUT = 5

        // Checkpoint Constants
        const val CHECKPOINT_POSITION = "position"
        const val CHECKPOINT_STATUS = "status"
        const val CHECKPOINT_TEXT = "text"

        const val CHECKPOINT_POSITION_12 = "t"
        const val CHECKPOINT_STATUS_12 = "u"
        const val CHECKPOINT_TEXT_12 = "v"

        // Status Constants
        const val STATUS_DONE = 10
    }

    // Data ----------------------------------------------------------------------------------------
    private var filename: String = ""
    private var parent: String = ""
    private var version: Int = 0
    private var meta: JSONObject? = null
    private var type: Int = 0
    private var taskID: Long = 0

    private var dateCreate: Calendar? = null
    private var dateDue: Calendar? = null
    private var dateUpdated: Calendar? = null
    private var dateDeleted: Calendar? = null
    private var dateArchived: Calendar? = null

    private var title: String = ""
    private var note: String = ""
    private var tags: ArrayList<String>? = null
    private var children: ArrayList<String>? = null
    private var checkpoints: ArrayList<Checkpoint>? = null
    private var list: ArrayList<String>? = null
    private var status: Int = 0
    private var priority: Int = 0

    private var completeOnTime: Boolean = false
    private var completeLate: Boolean = false

    // Constructors --------------------------------------------------------------------------------
    constructor(_filename: String): this() {
        filename = _filename
        loadJSON(JSONUtility.loadJSON(File(FileUtility.getApplicationDataDirectory(), filename)))

        if (!TaskManager.tasks.contains(filename)) {
            if (!TaskManager.archived.contains(filename)) {
                if (!TaskManager.deleted.contains(filename)) {
                    TaskManager.tasks.add(filename)
                    addMeta("Error", "Task was not in the main list at some point.")
                    note = note + System.getProperty("line.separator") +
                            System.getProperty("line.separator") +
                            "Save Error Experienced." +
                            System.getProperty("line.separator") +
                            "This Message can be Ignored"

                    save()
                    TaskManager.save()
                }
            }
        }
    }

    constructor(_parent: String, _type: Int) : this() {
        val calendar = Calendar.getInstance()
        filename = calendar.timeInMillis.toString() + ".srj"
        parent = _parent
        version = API.MANAGEMENT
        meta = JSONObject()
        this.type = type
        taskID = calendar.timeInMillis
        dateCreate = calendar.clone() as Calendar
        dateDue = null
        dateUpdated = calendar.clone() as Calendar
        dateArchived = null
        dateDeleted = null
        title = ""
        note = ""
        tags = ArrayList(5)
        when (type) {
            TYPE_TASK -> checkpoints = ArrayList(5)
            TYPE_FLDR -> children = ArrayList(5)
            TYPE_LIST -> list = ArrayList(5)
        }
        status = 0
        priority = 20
        completeOnTime = false
        completeLate = false
    }

    constructor(_data: JSONObject): this() {
        filename = _data.optString("filename", "error.srj")
        loadJSON(_data)
    }

    // Management Methods --------------------------------------------------------------------------
    fun save(): Task {
        JSONUtility.saveJSONObject(File(FileUtility.getApplicationDataDirectory(), filename), toJSON())
        return this
    }

    fun delete() {
        val file = File(FileUtility.getApplicationDataDirectory(), filename)
        file.delete()
    }

    // JSON Management Methods ---------------------------------------------------------------------
    fun loadJSON(data: JSONObject?): Task {
        if (data == null) {
            title = "Null Task Load Error"
            note = "Error occurs when there is no Task to load"
            return this
        }

        version = data.optInt(VERSION, API.RELEASE)

        if (version <= API.MANAGEMENT) {
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
            tags = ArrayList()
            if (t != null && t.length() != 0) for (i in 0 until t.length()) tags?.add(t.optString(i))

            val c = data.optJSONArray(CHILDREN)
            children = ArrayList()
            if (c != null && c.length() != 0) for (i in 0 until c.length()) children?.add(c.optString(i))

            val p = data.optJSONArray(CHECKPOINTS)
            checkpoints = ArrayList()
            if (p != null && p.length() != 0) for (i in 0 until p.length()) checkpoints?.add(Checkpoint(p.optJSONObject(i)))

            // API 11
            meta = if (version >= API.MANAGEMENT) data.optJSONObject(META)
            else JSONObject()
        } else {
            parent = data.optString(PARENT_12, "home")
            type = data.optInt(TYPE_12, TYPE_TASK)
            task_id = data.optLong(TASK_ID_12, Calendar.getInstance().timeInMillis)
            date_create = JSONUtility.loadCalendar(data.optJSONObject(CAL_CREATE_12))
            date_due = JSONUtility.loadCalendar(data.optJSONObject(CAL_DUE_12))
            date_updated = JSONUtility.loadCalendar(data.optJSONObject(CAL_UPDATE_12))
            date_archived = JSONUtility.loadCalendar(data.optJSONObject(CAL_ARCHIVED_12))
            date_deleted = JSONUtility.loadCalendar(data.optJSONObject(CAL_DELETED_12))
            title = data.optString(TITLE_12, "")
            note = data.optString(NOTE_12, "")
            status = data.optInt(STATUS_12, 0)
            priority = data.optInt(PRIORITY_12, 20)
            complete_on_time = data.optBoolean(COMPLETED_ON_TIME_12)
            complete_late = data.optBoolean(COMPLETED_LATE_12)

            val t = data.optJSONArray(TAGS_12)
            tags = ArrayList()
            if (t != null && t.length() != 0) for (i in 0 until t.length()) tags?.add(t.optString(i))

            val c = data.optJSONArray(CHILDREN_12)
            children = ArrayList()
            if (c != null && c.length() != 0) for (i in 0 until c.length()) children?.add(c.optString(i))

            val p = data.optJSONArray(CHECKPOINTS_12)
            checkpoints = ArrayList()
            if (p != null && p.length() != 0) for (i in 0 until p.length()) checkpoints?.add(Checkpoint(p.optJSONObject(i)))

            val l = data.optJSONArray(LIST_12)
            list = ArrayList(l.length())
            if (l != null && l.length() != 0) for (i in 0 until l.length()) list?.add(l.optString(i))

            meta = data.optJSONObject(META)
        }

        return this
    }

    fun toJSON(): JSONObject {
        val data = JSONObject()
        try {
            if (version <= API.MANAGEMENT) {
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
                if (tags != null && tags?.size != 0) for (tag in tags!!) t.put(tag)
                data.put(TAGS, t)

                val c = JSONArray()
                if (children != null && children?.size != 0) for (child in children!!) c.put(child)
                data.put(CHILDREN, c)

                val p = JSONArray()
                if (checkpoints != null && checkpoints?.size != 0) for (checkpoint in checkpoints!!) p.put(checkpoint.toJSON())
                data.put(CHECKPOINTS, p)

                // API 11
                data.put(META, meta)
            } else {
                data.put(PARENT_12, parent)
                data.put(VERSION, API.MANAGEMENT)
                data.put(META_12, meta)
                data.put(TYPE_12, type)
                data.put(TASK_ID_12, task_id)
                data.put(CAL_CREATE_12, JSONUtility.saveCalendar(date_create))
                data.put(CAL_DUE_12, JSONUtility.saveCalendar(date_due))
                data.put(CAL_UPDATE_12, JSONUtility.saveCalendar(date_updated))
                data.put(CAL_ARCHIVED_12, JSONUtility.saveCalendar(date_archived))
                data.put(CAL_DELETED_12, JSONUtility.saveCalendar(date_deleted))
                data.put(TITLE_12, title)
                data.put(NOTE_12, note)
                data.put(STATUS_12, status)
                data.put(PRIORITY_12, priority)
                data.put(COMPLETED_ON_TIME_12, complete_on_time)
                data.put(COMPLETED_LATE_12, complete_late)

                val t = JSONArray()
                if (tags != null && tags?.size != 0) for (tag in tags!!) t.put(tag)
                data.put(TAGS_12, t)

                val c = JSONArray()
                if (children != null && children?.size != 0) for (child in children!!) c.put(child)
                data.put(CHILDREN_12, c)

                val p = JSONArray()
                if (checkpoints != null && checkpoints?.size != 0) for (checkpoint in checkpoints!!) p.put(checkpoint.toJSON())
                data.put(CHECKPOINTS_12, p)

                val l = JSONArray()
                if (list != null && list?.size != 0) for (item in list!!) l.put(item)
                data.put(LIST_12, l)
            }
        } catch (j: JSONException) {
            j.printStackTrace()
        } catch (n: NullPointerException) {
            n.printStackTrace()
        }

        return data
    }

    // Getters -------------------------------------------------------------------------------------
    fun getFilename(): String {
        return filename
    }

    fun getParent(): String {
        return parent
    }

    fun getMeta(): JSONObject {
        return meta!!
    }

    fun getType(): Int {
        return type
    }

    fun getID(): Long {
        return taskID
    }

    fun getDateCreated(): Calendar {
        return dateCreate!!
    }

    fun getDateCreatedString(): String {
        return if (date_create == null) "Error" else (date_create!!.get(Calendar.MONTH) + 1).toString() + "/" +
                date_create?.get(Calendar.DAY_OF_MONTH).toString() + "/" +
                date_create?.get(Calendar.YEAR).toString()
    }

    fun getDateDue(): Calendar? {
        return dateDue
    }

    fun getDateDueString(): String {
        return if (date_due == null) "No Date" else (date_due!!.get(Calendar.MONTH) + 1).toString() + "/" +
                date_due?.get(Calendar.DAY_OF_MONTH).toString() + "/" +
                date_due?.get(Calendar.YEAR).toString()
    }

    fun getDateUpdated(): Calendar {
        return dateUpdated!!
    }

    fun getDateUpdatedString(): String {
        return if (date_updated == null) "Error" else (date_updated!!.get(Calendar.MONTH) + 1).toString() + "/" +
                date_updated?.get(Calendar.DAY_OF_MONTH).toString() + "/" +
                date_updated?.get(Calendar.YEAR).toString()
    }

    fun getDateArchived(): Calendar? {
        return dateArchived
    }

    fun getDateArchivedString(): String {
        return if (date_archived == null) "No Date" else (date_archived!!.get(Calendar.MONTH) + 1).toString() + "/" +
                date_archived?.get(Calendar.DAY_OF_MONTH).toString() + "/" +
                date_archived?.get(Calendar.YEAR).toString()
    }

    fun getDateDeleted(): Calendar? {
        return dateDeleted
    }

    fun getDateDeletedString(): String {
        return if (date_deleted == null) "No Date" else (date_deleted!!.get(Calendar.MONTH) + 1).toString() + "/" +
                date_deleted?.get(Calendar.DAY_OF_MONTH).toString() + "/" +
                date_deleted?.get(Calendar.YEAR).toString()
    }

    fun getTitle(): String {
        return title
    }

    fun getNote(): String {
        return note
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

    fun getList(): ArrayList<String> {
        return list!!
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

    @Deprecated("To be Removed in 13")
    fun onTime(): Boolean {
        return completeOnTime
    }

    fun completedOnTime(): Boolean {
        return complete_on_time
    }

    @Deprecated("To be Removed in 13")
    fun late(): Boolean {
        return completeLate
    }

    fun completedLate(): Boolean {
        return complete_late
    }

    // Setters -------------------------------------------------------------------------------------
    fun setDateDue(_calendar: Calendar?): Task {
        if (_calendar == null) dateDue = null
        else dateDue = _calendar.clone() as Calendar
        dateUpdated = Calendar.getInstance()
        return this
    }

    fun setTitle(_title: String): Task {
        this.title = _title
        dateUpdated = Calendar.getInstance()
        return this
    }

    fun setNote(_note: String): Task {
        this.note = _note
        dateUpdated = Calendar.getInstance()
        return this
    }

    fun setTags(_tags: ArrayList<String>): Task {
        this.tags = _tags
        dateUpdated = Calendar.getInstance()
        return this
    }

    fun setChildren(_children: ArrayList<String>): Task {
        this.children = _children
        dateUpdated = Calendar.getInstance()
        return this
    }

    fun setCheckpoints(_checkpoints: ArrayList<Checkpoint>): Task {
        this.checkpoints = _checkpoints
        dateUpdated = Calendar.getInstance()
        return this
    }

    fun setPriority(_priority: Int): Task {
        priority = _priority
        date_updated = Calendar.getInstance()
        return this
    }

    fun markComplete(_mark: Boolean): Task {
        if (_mark) {
            status = STATUS_DONE
            when {
                dateDue == null -> completeOnTime = true
                dateDue!!.before(Calendar.getInstance()) -> completeLate = true
                else -> completeOnTime = true
            }
        } else {
            status = 0
            completeLate = false
            completeOnTime = false
        }

        dateUpdated = Calendar.getInstance()
        return this
    }

    fun markArchived(): Task {
        dateArchived = Calendar.getInstance()
        dateUpdated = Calendar.getInstance()
        return this
    }

    fun markDeleted(): Task {
        dateDeleted = Calendar.getInstance()
        dateUpdated = Calendar.getInstance()
        return this
    }

    // Manipulaters --------------------------------------------------------------------------------
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
            dateUpdated = Calendar.getInstance()
            return this
        }

        return this
    }

    fun removeTag(tag: String): Task {
        if (tags!!.contains(tag)) {
            tags!!.remove(tag)
            dateUpdated = Calendar.getInstance()
            return this
        }

        return this
    }

    fun addChild(child: String): Task {
        if (!children!!.contains(child)) {
            children!!.add(child)
            dateUpdated = Calendar.getInstance()
            return this
        }

        return this
    }

    fun removeChild(child: String): Task {
        if (children!!.contains(child)) {
            children!!.remove(child)
            dateUpdated = Calendar.getInstance()
            return this
        }

        return this
    }

    fun addCheckpoint(checkpoint: Checkpoint): Task {
        for (c in checkpoints!!) if (c.id == checkpoint.id) return this
        checkpoints!!.add(checkpoint)
        dateUpdated = Calendar.getInstance()
        return this
    }

    fun editCheckpoint(checkpoint: Checkpoint): Task {
        for (c in checkpoints!!) {
            if (c.id == checkpoint.id) {
                c.status = checkpoint.status
                c.text = checkpoint.text
                dateUpdated = Calendar.getInstance()
                return this
            }
        }

        return this
    }

    fun removeCheckpoint(checkpoint: Checkpoint): Task {
        for (c in checkpoints!!) {
            if (c.id == checkpoint.id) {
                checkpoints!!.remove(c)
                dateUpdated = Calendar.getInstance()
                return this
            }
        }

        return this
    }

    fun addListItem(_text: String): Task {
        list?.add(_text)
        date_updated = Calendar.getInstance()
        return this
    }

    fun removeListItemAt(_position: Int): Task {
        list?.removeAt(_position)
        date_updated = Calendar.getInstance()
        return this
    }

    // To Methods ----------------------------------------------------------------------------------
    override fun toString(): String {
        return toJSON().toString()
    }
}
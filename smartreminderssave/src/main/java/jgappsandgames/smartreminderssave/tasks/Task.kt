package jgappsandgames.smartreminderssave.tasks

// Java
import jgappsandgames.smartreminderssave.settings.SettingsManager
import java.io.File
import java.util.*

// JSON
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

// Save
import jgappsandgames.smartreminderssave.utility.API
import jgappsandgames.smartreminderssave.utility.FileUtility
import jgappsandgames.smartreminderssave.utility.JSONUtility
import kotlin.collections.ArrayList

/**
 * Task
 * Created by joshua on 12/12/2017.
 */
class Task() {
    companion object {
        // Constants -------------------------------------------------------------------------------
        // Old Constants
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

        // 12 Constants
        private val PARENT_A = "a"
        private val META_A = "b"
        private val TYPE_A = "c"
        private val TASK_ID_A = "d"

        private val CAL_CREATE_A = "e"
        private val CAL_DUE_A = "f"
        private val CAL_UPDATE_A = "g"
        private val CAL_ARCHIVED_A = "h"
        private val CAL_DELETED_A = "i"

        private val TITLE_A = "j"
        private val NOTE_A = "k"
        private val TAGS_A = "l"
        private val CHILDREN_A = "m"
        private val CHECKPOINTS_A = "n"
        private val LIST_A = "o"
        private val TASKS_A = "t"
        private val STATUS_A = "p"
        private val PRIORITY_A = "q"

        private val COMPLETED_ON_TIME_A = "r"
        private val COMPLETED_LATE_A = "s"

        // Type Constants
        @JvmField
        val TYPE_NONE = 0
        @JvmField
        val TYPE_FLDR = 1
        @JvmField
        val TYPE_TASK = 2
        @JvmField
        val TYPE_NOTE = 3
        @JvmField
        val TYPE_LIST = 4
        @JvmField
        val TYPE_BOOK = 5

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

    // Data ----------------------------------------------------------------------------------------
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
    private var list: ArrayList<String>? = null
    private var tasks: ArrayList<String>? = null
    private var status: Int = 0
    private var priority: Int = 0

    private var complete_on_time: Boolean = false
    private var complete_late: Boolean = false

    // Initializers --------------------------------------------------------------------------------
    constructor(filename: String): this() {
        this.filename = filename
        loadJSON(JSONUtility.loadJSON(File(FileUtility.getApplicationDataDirectory(), filename)))
    }

    constructor(parent: String, type: Int): this() {
        if (SettingsManager.api_level <= 11) {
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
        } else {
            val calendar = Calendar.getInstance()

            this.filename = calendar.timeInMillis.toString() + ".12.srj"
            this.parent = parent
            version = API.SHRINKING
            meta = JSONObject()
            task_id = calendar.timeInMillis

            when (type) {
                TYPE_FLDR -> this.type = TYPE_FLDR
                TYPE_TASK -> this.type = TYPE_TASK
                TYPE_NOTE -> this.type = TYPE_NOTE
                TYPE_LIST -> this.type = TYPE_LIST
                TYPE_BOOK -> this.type = TYPE_BOOK
                else -> this.type = TYPE_NONE
            }

            date_create = calendar.clone() as Calendar
            date_due = null
            date_updated = calendar.clone() as Calendar
            date_archived = null
            date_deleted = null
            complete_on_time = false
            complete_late = false

            title = ""
            note = ""
            tags = ArrayList()
            status = 0
            priority = 20

            children = ArrayList()
            checkpoints = ArrayList()
            list = ArrayList()
            tasks = ArrayList()
        }
    }

    constructor(data: JSONObject): this() {
        filename = data.optString("filename", "error.srj")
        loadJSON(data)
    }

    // Management Methods --------------------------------------------------------------------------
    fun save() {
        JSONUtility.saveJSONObject(File(FileUtility.getApplicationDataDirectory(), filename), toJSON())
    }

    fun delete() {
        val file = File(FileUtility.getApplicationDataDirectory(), filename)
        file.delete()
    }

    // JSON Management Methods ---------------------------------------------------------------------
    fun loadJSON(data: JSONObject?) {
        if (data == null) {
            title = "Null Task Load Error"
            note = "Error occurs when there is no Task to load"
            return
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

        // A Series Load.
        else {
            parent = data.optString(PARENT_A, "home")
            meta = if (data.has(META_A)) data.optJSONObject(META_A)
                   else JSONObject()
            task_id = data.optLong(TASK_ID_A, 0)

            when (data.optInt(TYPE_A, TYPE_TASK)) {
                TYPE_TASK -> type = TYPE_TASK
                TYPE_FLDR -> type = TYPE_FLDR
                TYPE_NOTE -> type = TYPE_NOTE
                TYPE_LIST -> type = TYPE_LIST
                TYPE_BOOK -> type = TYPE_BOOK
                else -> type = TYPE_NONE
            }

            date_create = JSONUtility.loadCalendar(data.optJSONObject(CAL_CREATE_A))
            date_due = JSONUtility.loadCalendar(data.optJSONObject(CAL_DUE_A))
            date_updated = JSONUtility.loadCalendar(data.optJSONObject(CAL_UPDATE_A))
            date_archived = JSONUtility.loadCalendar(data.optJSONObject(CAL_ARCHIVED_A))
            date_deleted = JSONUtility.loadCalendar(data.optJSONObject(CAL_DELETED_A))

            title = data.optString(TITLE_A, "")
            note = data.optString(NOTE_A, "")
            status = data.optInt(STATUS_A, 0)
            priority = data.optInt(PRIORITY_A, 20)
            complete_on_time = data.optBoolean(COMPLETED_ON_TIME_A, false)
            complete_late = data.optBoolean(COMPLETED_LATE_A, false)

            val t = data.optJSONArray(TAGS_A)
            val c = data.optJSONArray(CHILDREN_A)
            val p = data.optJSONArray(CHECKPOINTS_A)
            val l = data.optJSONArray(LIST_A)
            val a = data.optJSONArray(TASKS_A)

            tags = ArrayList()
            children = ArrayList()
            checkpoints = ArrayList()
            list = ArrayList()
            tasks = ArrayList()

            if (t != null && t.length() != 0) for (i in 0 until t.length()) tags!!.add(t.optString(i))
            if (c != null && c.length() != 0) for (i in 0 until c.length()) children!!.add(c.optString(i))
            if (p != null && p.length() != 0) for (i in 0 until p.length()) checkpoints!!.add(Checkpoint(p.optJSONObject(i)))
            if (l != null && l.length() != 0) for (i in 0 until l.length()) list!!.add(l.optString(i))
            if (a != null && a.length() != 0) for (i in 0 until a.length()) tasks!!.add(a.optString(i))
        }
    }

    fun toJSON(): JSONObject {
        val data = JSONObject()

        if (SettingsManager.api_level <= 11) {
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
            }
        } else {
            try {
                data.put(PARENT_A, parent)
                data.put(META_A, meta)
                data.put(TASK_ID_A, task_id)
                data.put(TYPE_A, type)

                data.put(CAL_CREATE_A, JSONUtility.saveCalendar(date_create))
                data.put(CAL_DUE_A, JSONUtility.saveCalendar(date_due))
                data.put(CAL_UPDATE_A, JSONUtility.saveCalendar(date_updated))
                data.put(CAL_ARCHIVED_A, JSONUtility.saveCalendar(date_archived))
                data.put(CAL_DELETED_A, JSONUtility.saveCalendar(date_deleted))

                data.put(TITLE_A, title)
                data.put(NOTE_A, note)
                data.put(STATUS_A, status)
                data.put(PRIORITY_A, priority)
                data.put(COMPLETED_ON_TIME_A, complete_on_time)
                data.put(COMPLETED_LATE_A, complete_late)

                val t = JSONArray()
                val c = JSONArray()
                val p = JSONArray()
                val l = JSONArray()
                val a = JSONArray()

                if (tags != null && tags!!.size != 0) for (tag in tags!!) t.put(tag)
                if (children != null && children!!.size != 0) for (child in children!!) c.put(child)
                if (checkpoints != null && checkpoints!!.size != 0) for (checkpoint in checkpoints!!) p.put(checkpoint.toJSON())
                if (list != null && list!!.size != 0) for (item in list!!) l.put(item)
                if (tasks != null && tasks!!.size != 0) for (task in tasks!!) a.put(task)

                data.put(TAGS_A, t)
                data.put(CHILDREN_A, c)
                data.put(CHECKPOINTS_A, p)
                data.put(LIST_A, l)
                data.put(TASKS_A, a)
            } catch (j: JSONException) {
                j.printStackTrace()
            } catch (n: NullPointerException) {
                n.printStackTrace()
            }
        }

        return data
    }

    // Getters -------------------------------------------------------------------------------------
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

    fun getDateCreatedString(): String {
        return if (date_create == null) "No Date"
        else (date_create!!.get(Calendar.MONTH) + 1).toString() + "/" +
                date_create!!.get(Calendar.DAY_OF_MONTH).toString() + "/" +
                date_create!!.get(Calendar.YEAR).toString()
    }

    fun getDateDue(): Calendar? {
        return date_due
    }

    fun getDateDueString(): String {
        return if (date_due == null) "No Date"
                else (date_due!!.get(Calendar.MONTH) + 1).toString() + "/" +
                date_due!!.get(Calendar.DAY_OF_MONTH).toString() + "/" +
                date_due!!.get(Calendar.YEAR).toString()
    }

    fun getDateUpdated(): Calendar {
        return date_updated!!
    }

    fun getDateUpdatedString(): String {
        return if (date_updated == null) "No Date"
        else (date_updated!!.get(Calendar.MONTH) + 1).toString() + "/" +
                date_updated!!.get(Calendar.DAY_OF_MONTH).toString() + "/" +
                date_updated!!.get(Calendar.YEAR).toString()
    }

    fun getDateArchived(): Calendar? {
        return date_archived
    }

    fun getDateArchivedString(): String {
        return if (date_archived == null) "No Date"
        else (date_archived!!.get(Calendar.MONTH) + 1).toString() + "/" +
                date_archived!!.get(Calendar.DAY_OF_MONTH).toString() + "/" +
                date_archived!!.get(Calendar.YEAR).toString()
    }

    fun getDateDeleted(): Calendar? {
        return date_deleted
    }

    fun getDateDeletedString(): String {
        return if (date_deleted == null) "No Date"
        else (date_deleted!!.get(Calendar.MONTH) + 1).toString() + "/" +
                date_deleted!!.get(Calendar.DAY_OF_MONTH).toString() + "/" +
                date_deleted!!.get(Calendar.YEAR).toString()
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

    fun getChildrenLoaded(): ArrayList<Task> {
        // Todo: Coming Soon
        return ArrayList(0)
    }

    fun getChildrenString(): String {
        // Todo: Coming Soon
        return "Coming Soon"
    }

    fun getCheckpoints(): ArrayList<Checkpoint> {
        return checkpoints!!
    }

    fun getCheckpointString(): String {
        // Todo: Coming Soon
        return "Coming Soon"
    }

    fun getList(): ArrayList<String> {
        return list!!
    }

    fun getListString(): String {
        // Todo: Coming Soon
        return "Coming Soon"
    }

    fun getTasks(): ArrayList<String> {
        return tasks!!
    }

    fun getTasksLoaded(): ArrayList<Task> {
        // Todo: Coming Soon
        return ArrayList(0)
    }

    fun getTasksString(): String {
        // Todo: Coming Soon
        return "Coming Soon"
    }

    fun getStatus(): Int {
        // Todo: Check Tags
        return status
    }

    fun isCompleted(): Boolean {
        // Todo: Check Tags
        return status == STATUS_DONE
    }

    fun getStatusString(): String {
        return if (isCompleted()) "Completed" else "Incomplete"
    }

    fun getPriority(): Int {
        return priority
    }

    fun completedOnTime(): Boolean {
        return complete_on_time
    }

    fun completedLate(): Boolean {
        return complete_late
    }

    // Setters -------------------------------------------------------------------------------------
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

    fun setDateDue(calendar: Calendar?): Task {
        date_due = if (calendar == null) null
                   else calendar.clone() as Calendar
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

    fun setList(list: ArrayList<String>): Task {
        this.list = list
        date_updated = Calendar.getInstance()
        return this
    }

    fun setBookmarked(bookmarked: ArrayList<String>): Task {
        this.tasks = bookmarked
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

            when {
                date_due == null -> complete_on_time = true
                date_due!!.before(Calendar.getInstance()) -> complete_late = true
                else -> complete_on_time = true
            }
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

    // Manipulators --------------------------------------------------------------------------------
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

    // Todo: Group Add Tags

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

    // Todo: Group Remove Tags

    fun addChild(child: String): Task {
        if (!children!!.contains(child)) {
            children!!.add(child)
            date_updated = Calendar.getInstance()
            return this
        }

        return this
    }

    // Todo: Group Add Child

    fun removeChild(child: String): Task {
        if (children!!.contains(child)) {
            children!!.remove(child)
            date_updated = Calendar.getInstance()
            return this
        }

        return this
    }

    // Todo: Group Remove Children

    fun addCheckpoint(checkpoint: Checkpoint): Task {
        for (c in checkpoints!!) if (c.id == checkpoint.id) return this

        checkpoints!!.add(checkpoint)
        date_updated = Calendar.getInstance()
        return this
    }

    // Todo: Group Add Checkpoint

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

    // Todo: Group Remove Checkpoint

    fun addToList(item: String): Task {
        if (list == null) list = ArrayList()

        list!!.add(item)
        date_updated = Calendar.getInstance()
        return this
    }

    // Todo: Group Add To List

    fun editList(key: Int, item: String): Task {
        if (list == null) list = ArrayList()

        list!![key] = item
        date_updated = Calendar.getInstance()
        return this
    }

    fun removeFromList(key: Int): Task {
        if (list == null) return this

        list!!.removeAt(key)
        date_updated = Calendar.getInstance()
        return this
    }

    // Todo: Group Remove From List

    fun addBookmark(task: String): Task {
        if (tasks == null) tasks = ArrayList()

        if (!tasks!!.contains(task)) {
            tasks!!.add(task)
            date_updated = Calendar.getInstance()
        }

        return this
    }

    // Todo: Group Add Bookmarks

    fun removeBookmark(task: String): Task {
        if (tasks == null) return this

        tasks!!.remove(task)
        date_updated = Calendar.getInstance()
        return this
    }

    // Todo: Group remove Bookmarks

    override fun toString(): String {
        return toJSON().toString()
    }
}
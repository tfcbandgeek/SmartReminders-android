package jgappsandgames.smartreminderssave.tasks

// Java
import java.io.File
import java.util.Calendar

// JSON
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

// PoolUtility
import jgappsandgames.me.poolutilitykotlin.PoolObjectCreator
import jgappsandgames.me.poolutilitykotlin.PoolObjectInterface

// Save
import jgappsandgames.smartreminderssave.utility.API
import jgappsandgames.smartreminderssave.utility.FileUtility
import jgappsandgames.smartreminderssave.utility.JSONUtility

/**
 * Task
 * Created by joshua on 12/12/2017.
 */
class Task(): PoolObjectInterface {
    companion object {
        // Constants -------------------------------------------------------------------------------
        private const val PARENT = "parent"
        private const val VERSION = "version"
        private const val META = "meta"
        private const val TYPE = "type"
        private const val TASK_ID = "id"

        private const val CAL_CREATE = "cal_a"
        private const val CAL_DUE = "cal_b"
        private const val CAL_UPDATE = "cal_c"
        private const val CAL_ARCHIVED = "cal_d"
        private const val CAL_DELETED = "cal_e"

        private const val TITLE = "title"
        private const val NOTE = "note"
        private const val TAGS = "tags"
        private const val CHILDREN = "children"
        private const val CHECKPOINTS = "checkpoints"
        private const val STATUS = "status"
        private const val PRIORITY = "priority"

        private const val COMPLETED_ON_TIME = "completed_on_time"
        private const val COMPLETED_LATE = "completed_late"

        // Type Constants
        const val TYPE_NONE = 0
        @Deprecated("To be removed in API 12, Replace with TYPE_FOLDER constant")
        const val TYPE_FLDR = 1
        const val TYPE_FOLDER = 1
        const val TYPE_TASK = 2
        const val TYPE_NOTE = 3
        const val TYPE_SHOPPING_LIST = 4

        // Priority Constants ----------------------------------------------------------------------
        const val default = 20
        const val DEFAULT_PRIORITY = 33

        // Checkpoint Constants
        const val CHECKPOINT_POSITION = "position"
        const val CHECKPOINT_STATUS = "status"
        const val CHECKPOINT_TEXT = "text"

        // Status Constants
        const val STATUS_DONE = 10
    }

    // Data ----------------------------------------------------------------------------------------
    private var filename = ""
    private var parent = ""
    private var version = 0
    private var meta: JSONObject? = null
    private var type = 0
    private var taskID: Long = 0

    private var dateCreate: Calendar? = null
    private var dateDue: Calendar? = null
    private var dateUpdated: Calendar? = null
    private var dateDeleted: Calendar? = null
    private var dateArchived: Calendar? = null

    private var title: String = ""
    private var note: String = ""
    private var tags = ArrayList<String>()
    private var children = ArrayList<String>()
    private var checkpoints = ArrayList<Checkpoint>()
    private var status = 0
    private var priority = DEFAULT_PRIORITY

    private var completeOnTime: Boolean = false
    private var completeLate: Boolean = false

    // Constructors --------------------------------------------------------------------------------
    constructor(filename: String, sort: Boolean = false): this() {
        this.filename = filename
        loadJSON(JSONUtility.loadJSONObject(File(FileUtility.getApplicationDataDirectory(), filename)))
        tags.sort()

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

        if (sort) {
            if (type == TYPE_FOLDER) sortTasks()
        }
    }

    constructor(parent: String, type: Int): this() {
        val calendar = Calendar.getInstance()
        this.filename = calendar.timeInMillis.toString() + ".srj"
        this.parent = parent
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
        tags = ArrayList()
        children = ArrayList()
        checkpoints = ArrayList()
        status = 0
        priority = 20
        completeOnTime = false
        completeLate = false
    }

    constructor(data: JSONObject, sort: Boolean = false): this() {
        filename = data.optString("filename", "error.srj")
        loadJSON(data)
        tags.sort()

        if (sort) {
            if (type == TYPE_FOLDER) sortTasks()
        }
    }

    // Management Methods --------------------------------------------------------------------------
    fun load(filename: String, sort: Boolean = false): Task {
        this.filename = filename
        loadJSON(JSONUtility.loadJSONObject(File(FileUtility.getApplicationDataDirectory(), filename)))
        tags.sort()

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

        if (sort) {
            if (type == TYPE_FOLDER) sortTasks()
        }

        return this
    }

    fun load(parent: String, type: Int): Task {
        val calendar = Calendar.getInstance()
        this.filename = calendar.timeInMillis.toString() + ".srj"
        this.parent = parent
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
        tags = ArrayList()
        children = ArrayList()
        checkpoints = ArrayList()
        status = 0
        priority = 20
        completeOnTime = false
        completeLate = false

        return this
    }

    fun load(data: JSONObject, sort: Boolean = false): Task {
        filename = data.optString("filename", "error.srj")
        loadJSON(data)
        tags.sort()

        if (sort) {
            if (type == TYPE_FOLDER) sortTasks()
        }

        return this
    }

    fun save(): Task {
        JSONUtility.saveJSONObject(File(FileUtility.getApplicationDataDirectory(), filename), toJSON())
        tags.sort()
        return this
    }

    fun delete() {
        val file = File(FileUtility.getApplicationDataDirectory(), filename)
        file.delete()
    }

    fun search(search: String): Boolean {
        return when {
            getTitle().toLowerCase().contains(search.toLowerCase()) -> true
            getTags().isEmpty() -> false
            getTagString().toLowerCase().contains(search.toLowerCase()) -> true
            else -> false
        }
    }

    override fun deconstruct() {

    }

    // JSON Management Methods ---------------------------------------------------------------------
    fun loadJSON(data: JSONObject?) {
        if (data == null) {
            title = "Null Task Load Error"
            note = "Error occurs when there is no Task to load"
            return
        }

        version = data.optInt(VERSION, API.RELEASE)
        parent = data.optString(PARENT, "home")
        type = data.optInt(TYPE, TYPE_NONE)
        taskID = data.optLong(TASK_ID, Calendar.getInstance().timeInMillis)
        dateCreate = JSONUtility.loadCalendar(data.optJSONObject(CAL_CREATE))
        dateDue = JSONUtility.loadCalendar(data.optJSONObject(CAL_DUE))
        dateUpdated = JSONUtility.loadCalendar(data.optJSONObject(CAL_UPDATE))
        dateArchived = JSONUtility.loadCalendar(data.optJSONObject(CAL_ARCHIVED))
        dateDeleted = JSONUtility.loadCalendar(data.optJSONObject(CAL_DELETED))
        title = data.optString(TITLE, "")
        note = data.optString(NOTE, "")
        status = data.optInt(STATUS, 0)
        priority = data.optInt(PRIORITY, 20)
        completeOnTime = data.optBoolean(COMPLETED_ON_TIME, false)
        completeLate = data.optBoolean(COMPLETED_LATE, false)

        val t = data.optJSONArray(TAGS)
        tags = ArrayList()
        if (t != null && t.length() != 0) for (i in 0 until t.length()) tags.add(t.optString(i))

        val c = data.optJSONArray(CHILDREN)
        children = ArrayList()
        if (c != null && c.length() != 0) for (i in 0 until c.length()) children.add(c.optString(i))

        val p = data.optJSONArray(CHECKPOINTS)
        checkpoints = ArrayList()
        if (p != null && p.length() != 0) for (i in 0 until p.length()) checkpoints.add(Checkpoint(p.optJSONObject(i)))

        // API 11
        meta = if (version >= API.MANAGEMENT) data.optJSONObject(META)
        else JSONObject()
    }

    fun toJSON(): JSONObject {
        val data = JSONObject()
        try {
            data.put(PARENT, parent)
            data.put(VERSION, API.MANAGEMENT)
            data.put(TYPE, type)
            data.put(TASK_ID, taskID)
            data.put(CAL_CREATE, JSONUtility.saveCalendar(dateCreate))
            data.put(CAL_DUE, JSONUtility.saveCalendar(dateDue))
            data.put(CAL_UPDATE, JSONUtility.saveCalendar(dateUpdated))
            data.put(CAL_ARCHIVED, JSONUtility.saveCalendar(dateArchived))
            data.put(CAL_DELETED, JSONUtility.saveCalendar(dateDeleted))
            data.put(TITLE, title)
            data.put(NOTE, note)
            data.put(STATUS, status)
            data.put(PRIORITY, priority)
            data.put(COMPLETED_ON_TIME, completeOnTime)
            data.put(COMPLETED_LATE, completeLate)

            val t = JSONArray()
            if (tags.size != 0) for (tag in tags) t.put(tag)
            data.put(TAGS, t)

            val c = JSONArray()
            if (children.size != 0) for (child in children) c.put(child)
            data.put(CHILDREN, c)

            val p = JSONArray()
            if (checkpoints.size != 0) for (checkpoint in checkpoints) p.put(checkpoint.toJSON())
            data.put(CHECKPOINTS, p)

            // API 11
            data.put(META, meta)
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

    override fun getID(): Int {
        return taskID.toInt()
    }

    fun getDateCreated(): Calendar {
        return dateCreate!!
    }

    fun getDateDue(): Calendar? {
        return dateDue
    }

    fun getDateDueString(): String {
        return if (dateDue == null) "No Date" else (dateDue!!.get(Calendar.MONTH) + 1).toString() + "/" +
                dateDue!!.get(Calendar.DAY_OF_MONTH).toString() + "/" +
                dateDue!!.get(Calendar.YEAR).toString()
    }

    fun getDateUpdated(): Calendar {
        return dateUpdated!!
    }

    fun getDateArchived(): Calendar? {
        return dateArchived
    }

    fun getDateDeleted(): Calendar? {
        return dateDeleted
    }

    fun getTitle(): String {
        return title
    }

    fun getNote(): String {
        return note
    }

    fun getTags(): ArrayList<String> {
        return tags
    }

    fun getTagString(): String {
        if (tags.isEmpty()) return "No Tags Selected"
        val builder = StringBuilder()
        for (tag in tags) builder.append(tag).append(", ")
        if (builder.length >= 2) builder.setLength(builder.length - 2)
        return builder.toString()
    }

    fun getChildren(): ArrayList<String> {
        return children
    }

    fun getCheckpoints(): ArrayList<Checkpoint> {
        return checkpoints
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
        return completeOnTime
    }

    fun late(): Boolean {
        return completeLate
    }

    // Setters -------------------------------------------------------------------------------------
    fun setDateDue(calendar: Calendar?): Task {
        dateDue = if (calendar == null) null
            else calendar.clone() as Calendar
        dateUpdated = Calendar.getInstance()
        return this
    }

    fun setTitle(title: String): Task {
        this.title = title
        dateUpdated = Calendar.getInstance()
        return this
    }

    fun setNote(note: String): Task {
        this.note = note
        dateUpdated = Calendar.getInstance()
        return this
    }

    fun setTags(tags: ArrayList<String>): Task {
        this.tags = tags
        dateUpdated = Calendar.getInstance()
        return this
    }

    fun setChildren(children: ArrayList<String>): Task {
        this.children = children
        dateUpdated = Calendar.getInstance()
        return this
    }

    fun setCheckpoints(checkpoints: ArrayList<Checkpoint>): Task {
        this.checkpoints = checkpoints
        dateUpdated = Calendar.getInstance()
        return this
    }

    fun setPriority(priority: Int): Task {
        this.priority = priority
        dateUpdated = Calendar.getInstance()
        return this
    }

    fun markComplete(mark: Boolean): Task {
        if (mark) {
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
        if (!tags.contains(tag)) {
            tags.add(tag)
            tags.sort()
            dateUpdated = Calendar.getInstance()
            return this
        }

        return this
    }

    fun removeTag(tag: String): Task {
        if (tags.contains(tag)) {
            tags.remove(tag)
            dateUpdated = Calendar.getInstance()
            return this
        }

        return this
    }

    fun addChild(child: String): Task {
        if (!children.contains(child)) {
            children.add(child)
            dateUpdated = Calendar.getInstance()
            return this
        }

        return this
    }

    fun removeChild(child: String): Task {
        if (children.contains(child)) {
            children.remove(child)
            dateUpdated = Calendar.getInstance()
            return this
        }

        return this
    }

    fun addCheckpoint(checkpoint: Checkpoint): Task {
        for (c in checkpoints) if (c.id == checkpoint.id) return this
        checkpoints.add(checkpoint)
        dateUpdated = Calendar.getInstance()
        return this
    }

    fun editCheckpoint(checkpoint: Checkpoint): Task {
        for (c in checkpoints) {
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
        for (c in checkpoints) {
            if (c.id == checkpoint.id) {
                checkpoints.remove(c)
                dateUpdated = Calendar.getInstance()
                return this
            }
        }

        return this
    }

    // To Methods ----------------------------------------------------------------------------------
    override fun toString(): String {
        return toJSON().toString()
    }

    // Private Class Methods -----------------------------------------------------------------------
    private fun sortTasks() {
        val folder = ArrayList<Task>()
        val main = ArrayList<Task>()
        val dated = ArrayList<Task>()
        val completed = ArrayList<Task>()

        for (t in children) {
            val task = Task(t)

            when (task.getType()) {
                Task.TYPE_FOLDER -> folder.add(task)

                Task.TYPE_TASK -> {
                    if (task.isCompleted()) completed.add(task)
                    else if (task.getDateDue() == null) main.add(task)
                    else {
                        if (dated.size == 0) dated.add(task)

                        else if (dated.size == 1) {
                            if (dated[0].getDateDue()!!.after(task.getDateDue()!!)) {
                                dated.add(0, task)
                            } else {
                                dated.add(task)
                            }
                        }

                        else {
                            var added = true

                            for (i in 0 until dated.size) {
                                if (dated[i].getDateDue()!!.after(task.getDateDue()!!)) {
                                    dated.add(i, task)
                                    added = false
                                    break
                                }
                            }

                            if (added) dated.add(task)
                        }
                    }
                }
            }
        }

        children = ArrayList()
        for (f in folder) children.add(f.getFilename())
        for (m in main) children.add(m.getFilename())
        for (d in dated) children.add(d.getFilename())
        for (c in completed) children.add(c.getFilename())
    }
}

class TaskCreator: PoolObjectCreator<Task> {
    override fun generatePoolObject(): Task {
        return Task()
    }
}
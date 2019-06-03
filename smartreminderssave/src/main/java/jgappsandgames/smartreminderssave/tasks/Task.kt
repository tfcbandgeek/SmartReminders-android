package jgappsandgames.smartreminderssave.tasks

// Java
import android.util.Log
import java.io.File
import java.util.*

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
import jgappsandgames.smartreminderssave.settings.SettingsManager
import jgappsandgames.smartreminderssave.shopping.ShoppingItem
import jgappsandgames.smartreminderssave.utility.getLineSeperator

/**
 * Task
 * Created by joshua on 12/12/2017.
 */
class Task(): PoolObjectInterface {
    // TODO: Add Copy Method./'
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

        private const val PARENT_12 = "b"
        private const val VERSION_12 = "a"
        private const val META_12 = "c"
        private const val TYPE_12 = "d"
        private const val TASK_ID_12 = "e"

        private const val CAL_CREATE_12 = "f"
        private const val CAL_DUE_12 = "g"
        private const val CAL_UPDATE_12 = "h"
        private const val CAL_ARCHIVED_12 = "i"
        private const val CAL_DELETED_12 = "j"

        private const val TITLE_12 = "k"
        private const val NOTE_12 = "l"
        private const val TAGS_12 = "m"
        private const val CHILDREN_12 = "n"
        private const val CHECKPOINTS_12 = "o"
        private const val STATUS_12 = "p"
        private const val PRIORITY_12 = "q"

        private const val COMPLETED_ON_TIME_12 = "r"
        private const val COMPLETED_LATE_12 = "s"

        private const val BACKGROUND_COLOR_12 = "t"
        private const val FOREGROUND_COLOR_12 = "u"
        private const val FILENAME_12 = "v"
        const val LIST_META = "w"

        private const val SHOPPING_LIST = "x"
        private const val REPEAT_DATA = "y"
        private const val OVERDUE = "z"

        // Type Constants
        const val TYPE_NONE = 0
        const val TYPE_FOLDER = 1
        const val TYPE_TASK = 2
        const val TYPE_NOTE = 3
        const val TYPE_SHOPPING_LIST = 4

        // Priority Constants ----------------------------------------------------------------------
        const val DEFAULT_PRIORITY = 40
        const val STARRED_PRIORITY = 100
        const val HIGH_PRIORITY = 70
        const val NORMAL_PRIORITY = 39
        const val LOW_PRIORITY = 1
        const val IGNORE_PRIORITY = 0

        // Status Constants ------------------------------------------------------------------------
        const val STATUS_DONE = 10

        // List Constants --------------------------------------------------------------------------
        const val LIST_DEFAULT_NONE = 0
        const val LIST_DEFAULT_FOLDER = 1
        const val LIST_DEFAULT_TASK = 2
        const val LIST_DEFAULT_NOTE = 3
        const val LIST_DEFAULT_SHOPPING_LIST = 4

        const val LIST_PATH_NONE = 10
        const val LIST_PATH_FOLDER = 11
        const val LIST_PATH_TASK = 12
        const val LIST_PATH_NOTE = 13
        const val LIST_PATH_SHOPPING_LIST = 14

        const val LIST_TAG_NONE = 20
        const val LIST_TAG_FOLDER = 21
        const val LIST_TAG_TASK = 22
        const val LIST_TAG_NOTE = 23
        const val LIST_TAG_SHOPPING_LIST = 24

        const val LIST_CHILDREN_NONE = 30
        const val LIST_CHILDREN_FOLDER = 31
        const val LIST_CHILDREN_TASK = 32
        const val LIST_CHILDREN_NOTE = 33
        const val LIST_CHILDREN_SHOPPING_LIST = 34

        const val LIST_ALL_NONE = 40
        const val LIST_ALL_FOLDER = 41
        const val LIST_ALL_TASK = 42
        const val LIST_ALL_NOTE = 43
        const val LIST_ALL_SHOPPING_LIST = 44

        private fun defaultListMode(type: Int): Int {
            return type
        }
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

    private var completeOnTime = false
    private var completeLate = false

    private var backgroundColor = 0
    private var foregroundColor = 0

    private var metaList = 0

    private var shoppingItems = ArrayList<String>()
    private var repeatData = ""
    private var overdue = false

    // Constructors --------------------------------------------------------------------------------
    // Called when loading the TaskObject
    // The sort option is not currently in use but will be brought back in the future
    constructor(filename: String, sort: Boolean = false): this() {
        this.filename = filename
        loadJSON(JSONUtility.loadJSONObject(File(FileUtility.getApplicationDataDirectory(), filename)))
        tags.sort()

        if (!TaskManager.getAll().contains(filename)) {
            if (!TaskManager.getArchived().contains(filename)) {
                if (!TaskManager.getDeleted().contains(filename)) {
                    TaskManager.getAll().add(filename)
                    addMeta("Error", "Task was not in the main list at some point.")
                    save()
                    TaskManager.save()
                }
            }
        }
    }

    // Called when creating the TaskObject
    constructor(parent: String, type: Int): this() {
        create(parent, type)
    }

    // Called When Loading the TaskObject from file
    // The sort option is not currently in use but will be brought back in the future
    constructor(data: JSONObject, sort: Boolean = false): this() {
        loadJSON(data)
        filename = data.optString("filename", "$taskID-error.srj")
        tags.sort()
    }

    // Management Methods --------------------------------------------------------------------------
    // Use instead of the Load(parent, type) method. Eventually we will get them swapped around
    fun create(parent: String, type: Int): Task = load(parent, type)

    // The sort option is not currently in use but will be brought back in the future
    fun load(filename: String, sort: Boolean = false): Task {
        this.filename = filename
        loadJSON(JSONUtility.loadJSONObject(File(FileUtility.getApplicationDataDirectory(), filename)))
        tags.sort()

        if (!TaskManager.getAll().contains(filename)) {
            if (!TaskManager.getArchived().contains(filename)) {
                if (!TaskManager.getDeleted().contains(filename)) {
                    TaskManager.getAll().add(filename)
                    addMeta("Error", "Task was not in the main list at some point.")
                    save()
                    TaskManager.save()
                }
            }
        }

        return this
    }

    // todo: Remove in API 14
    @Deprecated("use Create instead") fun load(parent: String, type: Int): Task {
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
        tags.clear()
        children.clear()
        checkpoints.clear()
        status = 0
        priority = 20
        completeOnTime = false
        completeLate = false
        backgroundColor = 0
        foregroundColor = 0
        metaList = defaultListMode(getType())

        shoppingItems.clear()
        repeatData = ""
        overdue = false

        return this
    }

    fun load(data: JSONObject, sort: Boolean = false): Task {
        when (data.optInt(VERSION, -1)){
            -1 -> {
                title = "Null Task Load Error"
                note = "Error occurs when there is no Task to load"
                return this
            }

            API.SHRINKING -> {
                filename = data.optString(FILENAME_12)
                loadJSON(data)
                tags.sort()

                return this
            }

            else -> {
                filename = data.optString("filename")
                loadJSON(data)
                tags.sort()

                return this
            }
        }
    }

    fun save(): Task {
        JSONUtility.saveJSONObject(File(FileUtility.getApplicationDataDirectory(), filename), toJSON())
        return this
    }

    fun copy(n_parent: String? = null): Task {
        val temp = TaskManager.taskPool.getPoolObject().load(filename)
        temp.filename = Calendar.getInstance().timeInMillis.toString() + "c" + ".srj"
        if (n_parent == null) temp.parent = parent
        else temp.parent = n_parent
        temp.save()

        TaskManager.addTask(temp, (parent == "home"))
        TaskManager.save()

        return temp
    }

    fun delete() = File(FileUtility.getApplicationDataDirectory(), filename).delete()

    fun forceDelete() {
        deconstruct()
        delete()
    }

    fun search(search: String): Boolean {
        return when {
            getTitle().toLowerCase().contains(search.toLowerCase()) -> true
            getNote().toLowerCase().contains(search.toLowerCase()) -> true
            getTags().isEmpty() -> false
            getTagString().toLowerCase().contains(search.toLowerCase()) -> true
            else -> false
        }
    }

    override fun deconstruct() {
        this.filename = ""
        this.parent = ""
        version = -404
        meta = JSONObject()
        this.type = -404
        taskID = -404
        dateCreate = null
        dateDue = null
        dateUpdated = null
        dateArchived = null
        dateDeleted = null
        title = ""
        note = ""
        status = -404
        priority = -404
        completeOnTime = false
        completeLate = false
        backgroundColor = -404
        foregroundColor = -404
        metaList = -404
        repeatData = ""
        overdue = false

        tags.clear()
        tags.trimToSize()
        children.clear()
        children.trimToSize()
        checkpoints.clear()
        checkpoints.trimToSize()
        shoppingItems.clear()
        shoppingItems.trimToSize()
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
            tags.clear()
            if (t != null && t.length() != 0) for (i in 0 until t.length()) tags.add(t.optString(i))

            val c = data.optJSONArray(CHILDREN)
            children.clear()
            if (c != null && c.length() != 0) for (i in 0 until c.length()) children.add(c.optString(i))

            val p = data.optJSONArray(CHECKPOINTS)
            checkpoints.clear()
            if (p != null && p.length() != 0) for (i in 0 until p.length()) checkpoints.add(Checkpoint(p.optJSONObject(i)))

            // API 11
            meta = if (version >= API.MANAGEMENT) data.optJSONObject(META)
            else JSONObject()
        } else if (version == API.SHRINKING){
            parent = data.optString(PARENT_12, "home")
            type = data.optInt(TYPE_12, TYPE_NONE)
            taskID = data.optLong(TASK_ID_12, Calendar.getInstance().timeInMillis)
            dateCreate = JSONUtility.loadCalendar(data.optJSONObject(CAL_CREATE_12))
            dateDue = JSONUtility.loadCalendar(data.optJSONObject(CAL_DUE_12))
            dateUpdated = JSONUtility.loadCalendar(data.optJSONObject(CAL_UPDATE_12))
            dateArchived = JSONUtility.loadCalendar(data.optJSONObject(CAL_ARCHIVED_12))
            dateDeleted = JSONUtility.loadCalendar(data.optJSONObject(CAL_DELETED_12))
            title = data.optString(TITLE_12, "")
            note = data.optString(NOTE_12, "")
            status = data.optInt(STATUS_12, 0)
            priority = data.optInt(PRIORITY_12, 20)
            completeOnTime = data.optBoolean(COMPLETED_ON_TIME_12, false)
            completeLate = data.optBoolean(COMPLETED_LATE_12, false)

            val t = data.optJSONArray(TAGS_12)
            tags.clear()
            if (t != null && t.length() != 0) for (i in 0 until t.length()) tags.add(t.optString(i))

            val c = data.optJSONArray(CHILDREN_12)
            children.clear()
            if (c != null && c.length() != 0) for (i in 0 until c.length()) children.add(c.optString(i))

            val p = data.optJSONArray(CHECKPOINTS_12)
            checkpoints.clear()
            if (p != null && p.length() != 0) for (i in 0 until p.length()) checkpoints.add(Checkpoint(p.optJSONObject(i)))

            // API 11
            meta = data.optJSONObject(META_12)

            // API 12
            backgroundColor = data.optInt(BACKGROUND_COLOR_12, 0)
            foregroundColor = data.optInt(FOREGROUND_COLOR_12, 0)

            // Meta
            metaList = meta!!.optInt(LIST_META, defaultListMode(getType()))
        } else /*if (version == API.TASK_OVERHAUL)*/ {
            // Independent Loads
            completeLate = data.optBoolean(COMPLETED_LATE_12, false)
            completeOnTime = data.optBoolean(COMPLETED_ON_TIME_12, false)
            overdue = data.optBoolean(OVERDUE, false)

            priority = data.optInt(PRIORITY_12, 20)
            status = data.optInt(STATUS_12, 0)
            type = data.optInt(TYPE_12, TYPE_NONE)

            taskID = data.optLong(TASK_ID_12, Calendar.getInstance().timeInMillis)

            note = data.optString(NOTE_12, "")
            parent = data.optString(PARENT_12, "home")
            title = data.optString(TITLE_12, "")

            dateCreate = JSONUtility.loadCalendar(data.optJSONObject(CAL_CREATE_12))
            dateDue = JSONUtility.loadCalendar(data.optJSONObject(CAL_DUE_12))
            dateUpdated = JSONUtility.loadCalendar(data.optJSONObject(CAL_UPDATE_12))
            dateArchived = JSONUtility.loadCalendar(data.optJSONObject(CAL_ARCHIVED_12))
            dateDeleted = JSONUtility.loadCalendar(data.optJSONObject(CAL_DELETED_12))

            val t = data.optJSONArray(TAGS_12)
            tags.clear()
            if (t != null && t.length() != 0) for (i in 0 until t.length()) tags.add(t.optString(i))

            val c = data.optJSONArray(CHILDREN_12)
            children.clear()
            if (c != null && c.length() != 0) for (i in 0 until c.length()) children.add(c.optString(i))

            val p = data.optJSONArray(CHECKPOINTS_12)
            checkpoints.clear()
            if (p != null && p.length() != 0) for (i in 0 until p.length()) checkpoints.add(Checkpoint(p.optJSONObject(i)))

            val s = data.optJSONArray(SHOPPING_LIST)
            shoppingItems.clear()
            if (s != null && s.length() != 0) for (i in 0 until s.length()) shoppingItems.add(s.optString(i))

            // Dependent Loads
            metaList = meta!!.optInt(LIST_META, defaultListMode(type))
        }
    }

    fun toJSON(): JSONObject {
        val data = JSONObject()
        try {
            if (SettingsManager.getUseVersion() <= API.MANAGEMENT) {
                Log.e("API Settings", "Please update as soon as posible to not lose compatability")
                data.put("filename", filename)
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
            } else {
                data.put(FILENAME_12, filename)
                data.put(PARENT_12, parent)
                data.put(VERSION_12, API.SHRINKING)
                data.put(VERSION, API.SHRINKING)
                data.put(META_12, meta)
                data.put(TYPE_12, type)
                data.put(TASK_ID_12, taskID)
                data.put(CAL_CREATE_12, JSONUtility.saveCalendar(dateCreate))
                data.put(CAL_DUE_12, JSONUtility.saveCalendar(dateDue))
                data.put(CAL_UPDATE_12, JSONUtility.saveCalendar(dateUpdated))
                data.put(CAL_ARCHIVED_12, JSONUtility.saveCalendar(dateArchived))
                data.put(CAL_DELETED_12, JSONUtility.saveCalendar(dateDeleted))
                data.put(TITLE_12, title)
                data.put(NOTE_12, note)
                data.put(STATUS_12, status)
                data.put(PRIORITY_12, priority)
                data.put(COMPLETED_ON_TIME_12, completeOnTime)
                data.put(COMPLETED_LATE_12, completeLate)
                data.put(LIST_META, metaList)
                data.put(OVERDUE, overdue)

                val t = JSONArray()
                if (tags.size != 0) for (tag in tags) t.put(tag)
                data.put(TAGS_12, t)

                val c = JSONArray()
                if (children.size != 0) for (child in children) c.put(child)
                data.put(CHILDREN_12, c)

                val p = JSONArray()
                if (checkpoints.size != 0) for (checkpoint in checkpoints) p.put(checkpoint.toJSON())
                data.put(CHECKPOINTS_12, p)

                val s = JSONArray()
                if (shoppingItems.size != 0) for (shopping in shoppingItems) s.put(shopping)

                data.put(FOREGROUND_COLOR_12, foregroundColor)
                data.put(BACKGROUND_COLOR_12, backgroundColor)
            }
        } catch (j: JSONException) {
            j.printStackTrace()
        } catch (n: NullPointerException) {
            n.printStackTrace()
        }

        return data
    }

    // Getters -------------------------------------------------------------------------------------
    fun getVersion(): Int {
        when {
            version > API.TASK_OVERHAUL -> version = API.TASK_OVERHAUL
            version < API.RELEASE -> version = API.RELEASE
            version == API.SHRINKING -> version = API.TASK_OVERHAUL
        }

        return version
    }

    fun getFilename(): String = filename

    fun getPath(): String {
        if (getParent() == "home") return ""

        val p = TaskManager.taskPool.getPoolObject().load(getParent())
        val path = p.getPath() + p.getTitle() + "/"
        TaskManager.taskPool.returnPoolObject(p)
        return path
    }

    fun getParent(): String = parent

    fun getMeta(): JSONObject {
        if (meta == null) meta = JSONObject()
        return meta!!
    }

    fun getListViewType(): Int {
        if ((metaList - getType()) == LIST_DEFAULT_NONE) {
            if (getNote().trim() == "") return (LIST_ALL_NONE + getType())
        }

        return metaList
    }

    fun getType(): Int {
        return when (type) {
            TYPE_NONE -> TYPE_NONE
            TYPE_FOLDER -> TYPE_FOLDER
            TYPE_NOTE -> TYPE_NOTE
            TYPE_TASK -> TYPE_TASK
            TYPE_SHOPPING_LIST -> TYPE_SHOPPING_LIST
            else -> TYPE_NONE
        }
    }

    override fun getID(): Int = taskID.toInt()

    fun getDateCreated(): Calendar {
        if (dateCreate == null) {
            dateCreate = Calendar.getInstance()
            dateCreate!!.timeInMillis = taskID
        }

        return dateCreate!!
    }

    fun getDateDue(): Calendar? {
        if (type != TYPE_TASK) throw InvalidTaskTypeException(type, task = true)
        return dateDue
    }

    fun getDateDueString(): String {
        if (type != TYPE_TASK) throw InvalidTaskTypeException(type, task = true)

        return if (dateDue == null) "No Date"
        else (dateDue!!.get(Calendar.MONTH) + 1).toString() + "/" +
                dateDue!!.get(Calendar.DAY_OF_MONTH).toString() + "/" +
                dateDue!!.get(Calendar.YEAR).toString()
    }

    fun getDateUpdated(): Calendar {
        if (dateUpdated == null) {
            dateUpdated = Calendar.getInstance()
            dateUpdated!!.timeInMillis = taskID
        }

        return dateUpdated!!
    }

    fun getDateUpdatedString(): String = if (dateUpdated == null) "No Date"
        else (dateUpdated!!.get(Calendar.MONTH) + 1).toString() + "/" +
            dateUpdated!!.get(Calendar.DAY_OF_MONTH).toString() + "/" +
            dateUpdated!!.get(Calendar.YEAR).toString()

    fun getDateArchived(): Calendar? = dateArchived

    fun getDateArchivedString(): String = if (dateArchived == null) "No Date"
        else (dateArchived!!.get(Calendar.MONTH) + 1).toString() + "/" +
            dateArchived!!.get(Calendar.DAY_OF_MONTH).toString() + "/" +
            dateArchived!!.get(Calendar.YEAR).toString()

    fun getDateDeleted(): Calendar? = dateDeleted

    fun getDateDeletedString(): String = if (dateDeleted == null) "No Date"
        else (dateDeleted!!.get(Calendar.MONTH) + 1).toString() + "/" +
            dateDeleted!!.get(Calendar.DAY_OF_MONTH).toString() + "/" +
            dateDeleted!!.get(Calendar.YEAR).toString()

    fun getTitle(): String = title

    fun getNote(): String = note

    fun getTags(): ArrayList<String> = tags

    fun getTagString(): String {
        if (tags.isEmpty()) return "No Tags Selected"
        val builder = StringBuilder()
        for (tag in tags) builder.append(tag).append(", ")
        if (builder.length >= 2) builder.setLength(builder.length - 2)
        return builder.toString()
    }

    fun getChildren(): ArrayList<String> {
        if (type != TYPE_FOLDER) throw InvalidTaskTypeException(type, folder = true)

        return children
    }

    fun getChildrenTasks(): ArrayList<Task> {
        if (type != TYPE_FOLDER) throw InvalidTaskTypeException(type, folder = true)

        val temp = ArrayList<Task>(children.size)
        for (c in children) temp.add(TaskManager.taskPool.getPoolObject().load(c))
        return temp
    }

    fun getChildrenString(): String {
        if (type != TYPE_FOLDER) throw InvalidTaskTypeException(type, folder = true)

        val builder = StringBuilder()
        val tasks = getChildrenTasks()

        for (task in tasks) builder.append(task.getTitle()).append(getLineSeperator())
        return builder.toString()
    }

    fun getCheckpoints(): ArrayList<Checkpoint> {
        Log.e("1", "1")
        if (type != TYPE_TASK) throw InvalidTaskTypeException(type, task = true)
        return checkpoints
    }

    fun getCheckpointString(): String {
        Log.e("2", "1")
        if (type != TYPE_TASK) throw InvalidTaskTypeException(type, task = true)

        val builder = StringBuilder()

        for (checkpoint in checkpoints) {
            builder.append(checkpoint.toUserString()).append(getLineSeperator())
        }

        return builder.toString()
    }

    fun getShoppingList(): ArrayList<String> {
        Log.e("1", "2")
        if (type != TYPE_SHOPPING_LIST) throw InvalidTaskTypeException(type, shopping_list = true)

        return shoppingItems
    }

    fun getStatus(): Int {
        if (type != TYPE_TASK && type != TYPE_SHOPPING_LIST) throw InvalidTaskTypeException(type, task = true, shopping_list = true)

        return status
    }

    fun isCompleted(): Boolean {
        if (type != TYPE_TASK && type != TYPE_SHOPPING_LIST) throw InvalidTaskTypeException(type, task = true, shopping_list = true)
        return status == STATUS_DONE
    }

    // todo: Remove API 14
    @Deprecated("No longer in use do to internationalization issues")
    fun getStatusString(): String = if (isCompleted()) "Completed" else "Incomplete"

    fun getStatusString(complete: String, inComplete: String): String {
        if (type != TYPE_TASK && type != TYPE_SHOPPING_LIST) throw InvalidTaskTypeException(type, task = true, shopping_list = true)

        return if (isCompleted()) complete else inComplete
    }

    fun getPriority(): Int {
        Log.e("3", "1")
        if (type != TYPE_TASK) throw InvalidTaskTypeException(type, task = true)

        return priority
    }

    // todo: Remove API 14
    @Deprecated("Confusing")
    fun onTime(): Boolean = completeOnTime

    // todo: Remove API 14
    @Deprecated("Confusing")
    fun late(): Boolean = completeLate

    fun wasCompletedOnTime(): Boolean {
        if (type != TYPE_TASK) throw InvalidTaskTypeException(type, task = true)

        return completeOnTime
    }

    fun wasCompletedOnTimeReason(): Int {
        // TODO: Implement Method
        return -1
    }

    fun isOverdue(): Boolean {
        // TODO: Implement Method
        return false
    }

    fun wasCompletedLate(): Boolean {
        if (type != TYPE_TASK) throw InvalidTaskTypeException(type, task = true)

        return completeLate
    }

    fun wasCompletedLateReason(): Int {
        // TODO: Implement Method
        return -1
    }

    // Setters -------------------------------------------------------------------------------------
    fun setListViewType(type: Int): Task {
        // TODO: Safety Checks
        metaList = type
        removeMeta(LIST_META)
        addMeta(LIST_META, type)
        return this
    }

    fun setDateDue(calendar: Calendar?): Task {
        Log.e("1", "3")
        if (type != TYPE_TASK) throw InvalidTaskTypeException(type, task = true)

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
        if (type != TYPE_FOLDER) throw InvalidTaskTypeException(type, folder = true)

        this.children = children
        dateUpdated = Calendar.getInstance()
        return this
    }

    fun setCheckpoints(checkpoints: ArrayList<Checkpoint>): Task {
        if (type != TYPE_TASK) throw InvalidTaskTypeException(type, task = true)

        this.checkpoints = checkpoints
        dateUpdated = Calendar.getInstance()
        return this
    }

    fun setPriority(priority: Int): Task {
        if (type != TYPE_TASK) throw InvalidTaskTypeException(type, task = true)

        this.priority = priority
        dateUpdated = Calendar.getInstance()
        return this
    }

    fun markComplete(mark: Boolean): Task {
        if (type != TYPE_TASK && type != TYPE_SHOPPING_LIST) throw InvalidTaskTypeException(type, task = true, shopping_list = true)

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
        if (type != TYPE_FOLDER) throw InvalidTaskTypeException(type, folder = true)

        if (!children.contains(child)) {
            children.add(child)
            dateUpdated = Calendar.getInstance()
            return this
        }

        return this
    }

    fun removeChild(child: String): Task {
        if (type != TYPE_FOLDER) throw InvalidTaskTypeException(type, folder = true)

        if (children.contains(child)) {
            children.remove(child)
            dateUpdated = Calendar.getInstance()
            return this
        }

        return this
    }

    fun addCheckpoint(checkpoint: Checkpoint): Task {
        if (type != TYPE_TASK) throw InvalidTaskTypeException(type, task = true)

        for (c in checkpoints) if (c.id == checkpoint.id) return this
        checkpoints.add(checkpoint)
        dateUpdated = Calendar.getInstance()
        return this
    }

    fun editCheckpoint(checkpoint: Checkpoint): Task {
        if (type != TYPE_TASK) throw InvalidTaskTypeException(type, task = true)

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
        if (type != TYPE_TASK) throw InvalidTaskTypeException(type, task = true)

        for (c in checkpoints) {
            if (c.id == checkpoint.id) {
                checkpoints.remove(c)
                dateUpdated = Calendar.getInstance()
                return this
            }
        }

        return this
    }

    fun addShoppingItem(item: String): Task {
        if (type != TYPE_SHOPPING_LIST) throw InvalidTaskTypeException(type, shopping_list = true)

        if (!shoppingItems.contains(item)) {
            shoppingItems.add(item)
            dateUpdated = Calendar.getInstance()
            return this
        }

        return this
    }

    fun removeShoppingItem(item: String): Task {
        if (type != TYPE_SHOPPING_LIST) throw InvalidTaskTypeException(type, shopping_list = true)

        if (shoppingItems.contains(item)) {
            shoppingItems.remove(item)
            dateUpdated = Calendar.getInstance()
            return this
        }

        return this
    }

    // To Methods ----------------------------------------------------------------------------------
    override fun toString(): String {
        Log.e("Task.toString", "Soon will not return the jsonobject in string form")
        return toJSON().toString()
    }

    // Private Class Methods -----------------------------------------------------------------------
    @Deprecated("To be replaced by a none save changing method") private fun sortTasks() {
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

    @Deprecated("Does nothing, was too unstable") fun sortTags() {
        /*val c = ArrayList<Checkpoint>()
        val i = ArrayList<Checkpoint>()

        for (j in 0 until checkpoints.size) {
            if (checkpoints[j].status) c.add(checkpoints[j])
            else i.add(checkpoints[j])
        }

        checkpoints.clear()
        checkpoints.addAll(i)
        checkpoints.addAll(c)*/
    }
}

class TaskCreator: PoolObjectCreator<Task> {
    override fun generatePoolObject(): Task = Task()
}

/**
 * Checkpoint class
 * Created by joshua on 12/12/2017.
 *
 * TODO: Convert to Pool Type Object
 */
class Checkpoint(var id: Int, var text: String, var status: Boolean) {
    companion object {
        // Constants -------------------------------------------------------------------------------
        const val ID = "position"
        const val STATUS = "status"
        const val TEXT = "text"
    }

    // Constructors --------------------------------------------------------------------------------
    constructor(c_id: Int, c_text: String): this(c_id, c_text, false)
    constructor(data: JSONObject): this(data.optInt(ID, 0), data.optString(TEXT, ""), data.optBoolean(STATUS, false))

    // Json Methods --------------------------------------------------------------------------------
    fun toJSON(): JSONObject? {
        return try {
            val data = JSONObject()
            data.put(ID, id)
            data.put(STATUS, status)
            data.put(TEXT, text)

            data
        } catch (j: JSONException) {
            j.printStackTrace()
            null
        }

    }

    // String Method -------------------------------------------------------------------------------
    override fun toString(): String = toJSON()!!.toString()

    fun toUserString(): String {
        val b = StringBuilder()
        b.append("$id: ")
        if (status) b.append("[*] ")
        else b.append("[ ] ")
        b.append(text)

        return b.toString()
    }
}
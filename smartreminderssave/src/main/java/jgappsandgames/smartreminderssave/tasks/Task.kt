package jgappsandgames.smartreminderssave.tasks

// Java
import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import java.io.File
import java.util.Calendar

// JSON
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

// PoolUtility
import jgappsandgames.smartreminderssave.settings.SettingsManager
import jgappsandgames.smartreminderssave.utility.*

// Save
import jgappsandgames.smartreminderssave.utility.pool.ComplexPool

/**
 * Task
 * Created by joshua on 12/12/2017.
 */
class Task(): ComplexPool.PoolObject {
    companion object {
        // Constants -------------------------------------------------------------------------------
        // API Level 11
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

        // API 12+
        private const val FILENAME_12 = "v"
        private const val FILENAME_UPDATE_12 = "V"
        private const val PARENT_12 = "b"
        private const val PARENT_UPDATE_12 = "B"
        private const val VERSION_12 = "a"
        private const val VERSION_UPDATE_12 = "A"
        private const val META_12 = "c"
        private const val META_UPDATE_12 = "C"
        private const val TYPE_12 = "d"
        private const val TYPE_UPDATE_12 = "D"
        private const val TASK_ID_12 = "e"
        private const val TASK_ID_UPDATE_12 = "E"

        private const val CAL_CREATE_12 = "f"
        private const val CAL_CREATE_UPDATE_12 = "F"
        private const val CAL_DUE_12 = "g"
        private const val CAL_DUE_UPDATE_12 = "G"
        private const val CAL_UPDATE_12 = "h"
        private const val CAL_UPDATE_UPDATE_12 = "H"
        private const val CAL_ARCHIVED_12 = "i"
        private const val CAL_ARCHIVED_UPDATE_12 = "I"
        private const val CAL_DELETED_12 = "j"
        private const val CAL_DELETED_UPDATE_12 = "J"

        private const val TITLE_12 = "k"
        private const val TITLE_UPDATE_12 = "K"
        private const val NOTE_12 = "l"
        private const val NOTE_UPDATE_12 = "L"
        private const val TAGS_12 = "m"
        private const val TAGS_UPFATE_12 = "M"
        private const val CHILDREN_12 = "n"
        private const val CHILDREN_UPDATE_12 = "N"
        private const val CHECKPOINTS_12 = "o"
        private const val CHECKPOINTS_UPDATE_12 = "O"
        private const val SHOPPING_LIST_12 = "w"
        private const val SHOPPING_LIST_UPDATE_12 = "W"
        private const val STATUS_12 = "p"
        private const val STATUS_UPDATE_12 = "P"
        private const val PRIORITY_12 = "q"
        private const val PRIORITY_UPDATE_12 = "Q"

        private const val COMPLETED_ON_TIME_12 = "r"
        private const val COMPLETED_ON_TIME_UPDATE_12 = "R"
        private const val COMPLETED_LATE_12 = "s"
        private const val COMPLETED_LATE_UPDATE_12 = "S"

        private const val BACKGROUND_COLOR_12 = "t"
        private const val BACKGROUND_COLOR_UPDATE_12 = "T"
        private const val FOREGROUND_COLOR_12 = "u"
        private const val FOREGROUND_COLOR_UPDATE_12 = "U"
        private const val IMAGE_12 = "x"
        private const val IMAGE_UPDATE_12 = "X"
        private const val LIST_TYPE_12 = "y"
        private const val LIST_TYPE_UPDATE_12 = "Y"

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
        const val NORMAL_PRIORITY = 30
        const val LOW_PRIORITY = 1
        const val IGNORE_PRIORITY = 0

        // Status Constants ------------------------------------------------------------------------
        const val STATUS_DONE = 10

        // List View Types -------------------------------------------------------------------------
        const val LIST_DEFAULT = 0
        const val LIST_NOTE = 10
        const val LIST_TASK = 20
        const val LIST_FOLDER = 30
        const val LIST_SHOPPING = 40
    }

    // Data ----------------------------------------------------------------------------------------
    private var filename = Field(0L, None, "error.srj")
    private var parent = Field(0L, None, "home")
    private var version = Field(0L, None, API.MANAGEMENT)
    private var meta = Field(0L, None, JSONObject())
    private var type =  Field(0L, None, TYPE_NONE)
    private var listType = Field(0L, None, LIST_DEFAULT)
    private var taskID =  Field(0L, None, 0L)

    private var dateCreate = Field<Calendar?>(0L, None, null)
    private var dateDue = Field<Calendar?>(0L, None, null)
    private var dateUpdated = Field<Calendar?>(0L, None, null)
    private var dateDeleted = Field<Calendar?>(0L, None, null)
    private var dateArchived = Field<Calendar?>(0L, None, null)

    private var title = Field(0L, None, "")
    private var note = Field(0L, None, "")
    private var image = Field(0L, None, "")
    private var tags = Field(0L, None, ArrayList<String>())
    private var children = Field(0L, None, ArrayList<String>())
    private var checkpoints = Field(0L, None, ArrayList<Checkpoint>())
    private var shoppingList = Field(0L, None, ArrayList<String>())
    private var status = Field(0L, Some(0), 0)
    private var priority = Field(0L, Some(DEFAULT_PRIORITY), DEFAULT_PRIORITY)

    private var completeOnTime =  Field(0L, None, false)
    private var completeLate = Field(0L, None, false)

    private var backgroundColor = Field(0L, None, 0)
    private var foregroundColor = Field(0L, None, 0)

    // Constructors --------------------------------------------------------------------------------
    @Deprecated(" To Be Removed in API 13")
    constructor(filename: String, sort: Boolean = false): this() {
        load(filename, sort)
    }

    @Deprecated(" To Be Removed in API 13")
    constructor(parent: String, type: Int): this() {
        create(parent, type)
    }

    @Deprecated(" To Be Removed in API 13")
    constructor(data: JSONObject, sort: Boolean = false): this() {
        load(data, sort)
    }

    // Management Methods --------------------------------------------------------------------------
    fun create(parent: String, type: Int): Task {
        val calendar = Calendar.getInstance()

        this.filename.setValue(calendar.timeInMillis.toString() + ".srj")
        this.parent.setValue(parent)
        this.version.setValue(SettingsManager.getUseVersion())
        this.meta.data = None
        this.type.setValue(type)
        this.listType.data = None
        this.taskID.setValue(calendar.timeInMillis)

        this.dateCreate.setValue(calendar.clone() as Calendar)
        this.dateDue.data = None
        this.dateUpdated.setValue(calendar.clone() as Calendar)
        this.dateArchived.data = None
        this.dateDeleted.data = None

        this.title.data = None
        this.note.data = None
        this.image.data = None
        this.tags.get().clear()
        this.status.setValue(0)
        this.priority.setValue(DEFAULT_PRIORITY)
        this.completeOnTime.data = None
        this.completeLate.data = None

        this.backgroundColor.data = None
        this.foregroundColor.data = None

        when (type) {
            TYPE_FOLDER -> {
                this.children.get().clear()
                this.checkpoints.data = None
                this.shoppingList.data = None
            }

            TYPE_TASK -> {
                this.children.data = None
                this.checkpoints.get().clear()
                this.shoppingList.data = None
            }

            TYPE_SHOPPING_LIST -> {
                this.children.data = None
                this.checkpoints.data = None
                this.shoppingList.get().clear()
            }

            else -> {
                this.children.data = None
                this.checkpoints.data = None
                this.shoppingList.data = None
            }
        }

        return this
    }

    @Deprecated(" To Be Removed in API 13")
    fun load(parent: String, type: Int): Task {
        return create(parent, type)
    }

    fun load(filename: String, sort: Boolean = false): Task {
        this.filename.setValue(filename)
        load(JSONUtility.loadJSONObject(File(FileUtility.getApplicationDataDirectory(), filename)), sort)

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

    fun load(data: JSONObject, sort: Boolean = false): Task {
        val ver = data.optInt(VERSION, API.MANAGEMENT)

        if (ver <= API.MANAGEMENT) load11(data, sort)
        else if (ver == API.SHRINKING) load12(data, sort)

        return this
    }

    private fun load11(data: JSONObject, sort: Boolean) {
        parent.setValue(data.optString(PARENT, "home"))
        version.setValue(API.MANAGEMENT)
        meta.setValue(data.optJSONObject(META))
        type.setValue(data.optInt(TYPE, TYPE_NONE))
        listType.data = None
        taskID.setValue(data.optLong(TASK_ID, Calendar.getInstance().timeInMillis))

        dateCreate.setValue(JSONUtility.loadCalendar(data.optJSONObject(CAL_CREATE)))
        dateDue.setValue(JSONUtility.loadCalendar(data.optJSONObject(CAL_DUE)))
        dateUpdated.setValue(JSONUtility.loadCalendar(data.optJSONObject(CAL_UPDATE)))
        dateArchived.setValue(JSONUtility.loadCalendar(data.optJSONObject(CAL_ARCHIVED)))
        dateDeleted.setValue(JSONUtility.loadCalendar(data.optJSONObject(CAL_DELETED)))

        title.setValue(data.optString(TITLE, ""))
        note.setValue(data.optString(NOTE, ""))
        image.data = None
        status.setValue(data.optInt(STATUS, 0))
        priority.setValue(data.optInt(PRIORITY, 20))

        val t = data.optJSONArray(TAGS)
        val tt = ArrayList<String>()
        if (t != null && t.length() != 0) for (i in 0 until t.length()) tt.add(t.optString(i))
        tags.setValue(tt)

        val c = data.optJSONArray(CHILDREN)
        val cc  = ArrayList<String>()
        if (c != null && c.length() != 0) for (i in 0 until c.length()) cc.add(c.optString(i))
        children.setValue(cc)

        val p = data.optJSONArray(CHECKPOINTS)
        val pp = ArrayList<Checkpoint>()
        if (p != null && p.length() != 0) for (i in 0 until p.length()) pp.add(Checkpoint(p.optJSONObject(i)))
        checkpoints.setValue(pp)

        shoppingList.data = None

        completeOnTime.setValue(data.optBoolean(COMPLETED_ON_TIME, false))
        completeLate.setValue(data.optBoolean(COMPLETED_LATE, false))

        backgroundColor.data = None
        foregroundColor.data = None
    }

    private fun load12(data: JSONObject, sort: Boolean) {
        filename.setValue(data.optString(FILENAME_12, "error.srj"), data.optLong(FILENAME_UPDATE_12, 0L))
        parent.setValue(data.optString(PARENT_12, "home"), data.optLong(PARENT_UPDATE_12, 0L))
        version.setValue(data.optInt(VERSION_12, API.SHRINKING), data.optLong(VERSION_UPDATE_12, 0L))
        meta.setValue(data.optJSONObject(META_12), data.optLong(META_UPDATE_12, 0L))
        type.setValue(data.optInt(TYPE_12, TYPE_NONE), data.optLong(TYPE_UPDATE_12, 0L))
        listType.setValue(data.optInt(LIST_TYPE_12, LIST_DEFAULT), data.optLong(LIST_TYPE_UPDATE_12, 0L))
        taskID.setValue(data.optLong(TASK_ID_12, Calendar.getInstance().timeInMillis), data.optLong(TASK_ID_UPDATE_12, 0L))

        dateCreate.setValue(JSONUtility.loadCalendar(data.optJSONObject(CAL_CREATE_12)), data.optLong(CAL_CREATE_UPDATE_12, 0L))
        dateDue.setValue(JSONUtility.loadCalendar(data.optJSONObject(CAL_DUE_12)), data.optLong(CAL_DUE_UPDATE_12, 0L))
        dateUpdated.setValue(JSONUtility.loadCalendar(data.optJSONObject(CAL_UPDATE_12)), data.optLong(CAL_UPDATE_UPDATE_12, 0L))
        dateArchived.setValue(JSONUtility.loadCalendar(data.optJSONObject(CAL_ARCHIVED_12)), data.optLong(CAL_ARCHIVED_UPDATE_12, 0L))
        dateDeleted.setValue(JSONUtility.loadCalendar(data.optJSONObject(CAL_DELETED_12)), data.optLong(CAL_DELETED_UPDATE_12, 0L))

        title.setValue(data.optString(TITLE_12, ""), data.optLong(TITLE_UPDATE_12, 0L))
        note.setValue(data.optString(NOTE_12, ""), data.optLong(NOTE_UPDATE_12, 0L))
        if (data.has(IMAGE_12)) image.setValue(data.getString(IMAGE_12), data.optLong(IMAGE_UPDATE_12, 0L))
        else {
            image.data = None
            image.timeStamp = data.optLong(IMAGE_UPDATE_12, 0L)
        }
        status.setValue(data.optInt(STATUS_12, 0), data.optLong(STATUS_UPDATE_12, 0L))
        priority.setValue(data.optInt(PRIORITY_12, DEFAULT_PRIORITY), data.optLong(PRIORITY_UPDATE_12, 0L))

        completeOnTime.setValue(data.optBoolean(COMPLETED_ON_TIME_12, false), data.optLong(COMPLETED_ON_TIME_UPDATE_12, 0L))
        completeLate.setValue(data.optBoolean(COMPLETED_LATE_12, false), data.optLong(COMPLETED_LATE_UPDATE_12, 0L))

        val t = data.optJSONArray(TAGS_12)
        val tt = ArrayList<String>()
        if (t != null && t.length() != 0) for (i in 0 until t.length()) tt.add(t.optString(i))
        tags.setValue(tt, data.optLong(TAGS_UPFATE_12, 0L))

        val c = data.optJSONArray(CHILDREN_12)
        val cc  = ArrayList<String>()
        if (c != null && c.length() != 0) for (i in 0 until c.length()) cc.add(c.optString(i))
        children.setValue(cc, data.optLong(CHILDREN_UPDATE_12, 0L))

        val p = data.optJSONArray(CHECKPOINTS_12)
        val pp = ArrayList<Checkpoint>()
        if (p != null && p.length() != 0) for (i in 0 until p.length()) pp.add(Checkpoint(p.optJSONObject(i)))
        checkpoints.setValue(pp, data.optLong(CHECKPOINTS_UPDATE_12, 0L))

        val s = data.optJSONArray(SHOPPING_LIST_12)
        val ss = ArrayList<String>()
        if (s != null && s.length() != 0) for (i in 0 until s.length()) ss.add(s.optString(i))
        shoppingList.setValue(ss, data.optLong(SHOPPING_LIST_UPDATE_12, 0L))
    }

    fun sync(data: JSONObject, sort: Boolean = true): Task {
        throw NotImplementedError()
    }

    fun save(): Task {
        JSONUtility.saveJSONObject(File(FileUtility.getApplicationDataDirectory(), filename.get()), toJSON())
        return this
    }

    fun upgrade(version: Int): Boolean {
        return false
    }

    private fun upgrade12(): Boolean {
        return false
    }

    fun delete() {
        File(FileUtility.getApplicationDataDirectory(), filename.get()).delete()
    }

    fun search(search: String): Boolean {
        return when {
            getTitle().toLowerCase().contains(search.toLowerCase()) -> true
            getTags().isEmpty() -> false
            getTagString().toLowerCase().contains(search.toLowerCase()) -> true
            else -> false
        }
    }

    override fun destroy() {
        tags.get().clear()
        tags.get().trimToSize()
        children.get().clear()
        children.get().trimToSize()
        checkpoints.get().clear()
        checkpoints.get().trimToSize()
        shoppingList.get().clear()
        shoppingList.get().trimToSize()
    }

    // JSON Management Methods ---------------------------------------------------------------------
    fun toJSON(): JSONObject {
        val data = JSONObject()
        try {
            if (SettingsManager.getUseVersion() <= API.MANAGEMENT) {
                data.put("filename", filename.get())
                data.put(PARENT, parent.get())
                data.put(VERSION, API.MANAGEMENT)
                data.put(TYPE, type.get())
                data.put(TASK_ID, taskID.get())
                data.put(CAL_CREATE, JSONUtility.saveCalendar(dateCreate.get()))
                data.put(CAL_DUE, JSONUtility.saveCalendar(dateDue.get()))
                data.put(CAL_UPDATE, JSONUtility.saveCalendar(dateUpdated.get()))
                data.put(CAL_ARCHIVED, JSONUtility.saveCalendar(dateArchived.get()))
                data.put(CAL_DELETED, JSONUtility.saveCalendar(dateDeleted.get()))
                data.put(TITLE, title.get())
                data.put(NOTE, note.get())
                data.put(STATUS, status.get())
                data.put(PRIORITY, priority.get())
                data.put(COMPLETED_ON_TIME, completeOnTime.get())
                data.put(COMPLETED_LATE, completeLate.get())

                val t = JSONArray()
                if (tags.get().size != 0) for (tag in tags.get()) t.put(tag)
                data.put(TAGS, t)

                val c = JSONArray()
                if (children.get().size != 0) for (child in children.get()) c.put(child)
                data.put(CHILDREN, c)

                val p = JSONArray()
                if (checkpoints.get().size != 0) for (checkpoint in checkpoints.get()) p.put(checkpoint.toJSON())
                data.put(CHECKPOINTS, p)

                // API 11
                data.put(META, meta)
            } else {
                data.put(FILENAME_12, filename.get())
                data.put(PARENT_12, parent.get())
                data.put(VERSION_12, API.SHRINKING)
                data.put(VERSION, API.SHRINKING)
                data.put(META_12, meta.get())
                data.put(TYPE_12, type.get())
                data.put(TASK_ID_12, taskID.get())
                data.put(CAL_CREATE_12, JSONUtility.saveCalendar(dateCreate.get()))
                data.put(CAL_DUE_12, JSONUtility.saveCalendar(dateDue.get()))
                data.put(CAL_UPDATE_12, JSONUtility.saveCalendar(dateUpdated.get()))
                data.put(CAL_ARCHIVED_12, JSONUtility.saveCalendar(dateArchived.get()))
                data.put(CAL_DELETED_12, JSONUtility.saveCalendar(dateDeleted.get()))
                data.put(TITLE_12, title.get())
                data.put(NOTE_12, note.get())
                data.put(STATUS_12, status.get())
                data.put(PRIORITY_12, priority.get())
                data.put(COMPLETED_ON_TIME_12, completeOnTime.get())
                data.put(COMPLETED_LATE_12, completeLate.get())

                val t = JSONArray()
                if (tags.get().size != 0) for (tag in tags.get()) t.put(tag)
                data.put(TAGS_12, t)

                val c = JSONArray()
                if (children.get().size != 0) for (child in children.get()) c.put(child)
                data.put(CHILDREN_12, c)

                val p = JSONArray()
                if (checkpoints.get().size != 0) for (checkpoint in checkpoints.get()) p.put(checkpoint.toJSON())
                data.put(CHECKPOINTS_12, p)

                data.put(FOREGROUND_COLOR_12, foregroundColor.get())
                data.put(BACKGROUND_COLOR_12, backgroundColor.get())
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
        return version.get()
    }

    fun getFilename(): String {
        return filename.get()
    }

    fun getParent(): String {
        return parent.get()
    }

    fun getMeta(): JSONObject {
        return meta.get()
    }

    fun getType(): Int {
        return type.get()
    }

    override fun getID(): Long {
        return taskID.get()
    }

    fun getDateCreated(): Calendar {
        return dateCreate.get()!!
    }

    fun getDateDue(): Calendar? {
        return dateDue.get()
    }

    fun getDateDueString(): String {
        return if (dateDue.get() == null) "No Date"
               else (dateDue.get()!!.get(Calendar.MONTH) + 1).toString() + "/" +
                dateDue.get()!!.get(Calendar.DAY_OF_MONTH).toString() + "/" +
                dateDue.get()!!.get(Calendar.YEAR).toString()
    }

    fun getDateUpdated(): Calendar {
        return dateUpdated.get()!!
    }

    fun getDateUpdatedString(): String {
        return if (dateUpdated.get() == null) "No Date"
               else (dateUpdated.get()!!.get(Calendar.MONTH) + 1).toString() + "/" +
                dateUpdated.get()!!.get(Calendar.DAY_OF_MONTH).toString() + "/" +
                dateUpdated.get()!!.get(Calendar.YEAR).toString()
    }

    fun getDateArchived(): Calendar? {
        return dateArchived.get()
    }

    fun getDateArchivedString(): String {
        return if (dateArchived.get() == null) "No Date"
        else (dateArchived.get()!!.get(Calendar.MONTH) + 1).toString() + "/" +
                dateArchived.get()!!.get(Calendar.DAY_OF_MONTH).toString() + "/" +
                dateArchived.get()!!.get(Calendar.YEAR).toString()
    }

    fun getDateDeleted(): Calendar? {
        return dateDeleted.get()
    }

    fun getDateDeletedString(): String {
        return if (dateDeleted.get() == null) "No Date"
               else (dateDeleted.get()!!.get(Calendar.MONTH) + 1).toString() + "/" +
                dateDeleted.get()!!.get(Calendar.DAY_OF_MONTH).toString() + "/" +
                dateDeleted.get()!!.get(Calendar.YEAR).toString()
    }

    fun getTitle(): String {
        return title.get()
    }

    fun getNote(): String {
        return note.get()
    }

    fun getTags(): ArrayList<String> {
        return tags.get()
    }

    fun getTagString(): String {
        if (tags.get().isEmpty()) return "No Tags Selected"
        val builder = StringBuilder()
        for (tag in tags.get()) builder.append(tag).append(", ")
        if (builder.length >= 2) builder.setLength(builder.length - 2)
        return builder.toString()
    }

    fun getChildren(): ArrayList<String> {
        return children.get()
    }

    fun getChildrenTasks(): ArrayList<Task> {
        val temp = ArrayList<Task>(children.get().size)
        for (c in children.get()) temp.add(TaskManager.taskPool.poolObject.load(c))
        return temp
    }

    fun getCheckpoints(): ArrayList<Checkpoint> {
        return checkpoints.get()
    }

    fun getStatus(): Int {
        return status.get()
    }

    fun isCompleted(): Boolean {
        return status.get() == STATUS_DONE
    }

    fun getStatusString(): String {
        return if (isCompleted()) "Completed" else "Incomplete"
    }

    fun getPriority(): Int {
        return priority.get()
    }

    fun onTime(): Boolean {
        return completeOnTime.get()
    }

    fun late(): Boolean {
        return completeLate.get()
    }

    // Setters -------------------------------------------------------------------------------------
    fun setDateDue(calendar: Calendar?): Task {
        if (calendar == null) dateDue.data = None
        else calendar.clone() as Calendar
        setDateUpdate(Calendar.getInstance())
        dateDue.timeStamp = Calendar.getInstance().timeInMillis
        return this
    }

    private fun setDateUpdate(calendar: Calendar) {
        dateUpdated.setValue(calendar)
        dateUpdated.timeStamp = calendar.timeInMillis
    }

    fun setTitle(title: String): Task {
        this.title.setValue(title, Calendar.getInstance().timeInMillis)
        setDateUpdate(Calendar.getInstance())
        return this
    }

    fun setNote(note: String): Task {
        this.note.setValue(note, Calendar.getInstance().timeInMillis)
        setDateUpdate(Calendar.getInstance())
        return this
    }

    fun setTags(tags: ArrayList<String>): Task {
        this.tags.setValue(tags, Calendar.getInstance().timeInMillis)
        setDateUpdate(Calendar.getInstance())
        return this
    }

    fun setChildren(children: ArrayList<String>): Task {
        this.children.setValue(children, Calendar.getInstance().timeInMillis)
        setDateUpdate(Calendar.getInstance())
        return this
    }

    fun setCheckpoints(checkpoints: ArrayList<Checkpoint>): Task {
        this.checkpoints.setValue(checkpoints, Calendar.getInstance().timeInMillis)
        setDateUpdate(Calendar.getInstance())
        return this
    }

    fun setPriority(priority: Int): Task {
        this.priority.setValue(priority)
        setDateUpdate(Calendar.getInstance())
        return this
    }

    fun markComplete(mark: Boolean): Task {
        val calendar = Calendar.getInstance()
        val time = calendar.timeInMillis

        if (mark) {
            status.setValue(STATUS_DONE, time)

            when {
                dateDue.get() == null -> completeOnTime.setValue(true, time)
                dateDue.get()!!.before(Calendar.getInstance()) -> completeLate.setValue(true, time)
                else -> completeOnTime.setValue(true, time)
            }
        } else {
            status.setValue(0, time)
            completeLate.setValue(false, time)
            completeOnTime.setValue(false, time)
        }

        setDateUpdate(calendar)
        return this
    }

    fun markArchived(): Task {
        val calendar = Calendar.getInstance()
        val time = calendar.timeInMillis

        dateArchived.setValue(calendar, time)
        setDateUpdate(calendar)
        return this
    }

    fun markDeleted(): Task {
        val calendar = Calendar.getInstance()
        val time = calendar.timeInMillis

        dateDeleted.setValue(calendar, time)
        setDateUpdate(calendar)
        return this
    }

    // Manipulaters --------------------------------------------------------------------------------
    fun addMeta(key: String, boolean: Boolean) {
        val calendar = Calendar.getInstance()
        val time = calendar.timeInMillis
        meta.timeStamp = time
        setDateUpdate(calendar)

        if (meta.get().has(key)) meta.get().remove(key)
        meta.get().put(key, boolean)
    }

    fun addMeta(key: String, int: Int) {
        val calendar = Calendar.getInstance()
        val time = calendar.timeInMillis
        meta.timeStamp = time
        setDateUpdate(calendar)

        if (meta.get().has(key)) meta.get().remove(key)
        meta.get().put(key, int)
    }

    fun addMeta(key: String, double: Double) {
        val calendar = Calendar.getInstance()
        val time = calendar.timeInMillis
        meta.timeStamp = time
        setDateUpdate(calendar)

        if (meta.get().has(key)) meta.get().remove(key)
        meta.get().put(key, double)
    }

    fun addMeta(key: String, long: Long) {
        val calendar = Calendar.getInstance()
        val time = calendar.timeInMillis
        meta.timeStamp = time
        setDateUpdate(calendar)

        if (meta.get().has(key)) meta.get().remove(key)
        meta.get().put(key, long)
    }

    fun addMeta(key: String, string: String) {
        val calendar = Calendar.getInstance()
        val time = calendar.timeInMillis
        meta.timeStamp = time
        setDateUpdate(calendar)

        if (meta.get().has(key)) meta.get().remove(key)
        meta.get().put(key, string)
    }

    fun addMeta(key: String, jsonObject: JSONObject) {
        val calendar = Calendar.getInstance()
        val time = calendar.timeInMillis
        meta.timeStamp = time
        setDateUpdate(calendar)

        if (meta.get().has(key)) meta.get().remove(key)
        meta.get().put(key, jsonObject)
    }

    fun addMeta(key: String, jsonArray: JSONArray) {
        val calendar = Calendar.getInstance()
        val time = calendar.timeInMillis
        meta.timeStamp = time
        setDateUpdate(calendar)

        if (meta.get().has(key)) meta.get().remove(key)
        meta.get().put(key, jsonArray)
    }

    fun removeMeta(key: String) {
        val calendar = Calendar.getInstance()
        val time = calendar.timeInMillis
        meta.timeStamp = time
        setDateUpdate(calendar)

        if (meta.get().has(key)) meta.get().remove(key)
    }

    fun addTag(tag: String): Task {
        val calendar = Calendar.getInstance()
        val time = calendar.timeInMillis

        if (!tags.get().contains(tag)) {
            tags.get().add(tag)
            tags.get().sort()
            tags.timeStamp = time
            setDateUpdate(calendar)
            return this
        }

        return this
    }

    fun removeTag(tag: String): Task {
        val calendar = Calendar.getInstance()
        val time = calendar.timeInMillis

        if (tags.get().contains(tag)) {
            tags.get().remove(tag)
            tags.timeStamp = time
            setDateUpdate(calendar)
            return this
        }

        return this
    }

    fun addChild(child: String): Task {
        val calendar = Calendar.getInstance()
        val time = calendar.timeInMillis

        if (!children.get().contains(child)) {
            children.get().add(child)
            children.timeStamp = time
            setDateUpdate(calendar)
            return this
        }

        return this
    }

    fun removeChild(child: String): Task {
        val calendar = Calendar.getInstance()
        val time = calendar.timeInMillis

        if (children.get().contains(child)) {
            children.get().remove(child)
            children.timeStamp = time
            setDateUpdate(calendar)
            return this
        }

        return this
    }

    fun addCheckpoint(checkpoint: Checkpoint): Task {
        val calendar = Calendar.getInstance()
        val time = calendar.timeInMillis

        for (c in checkpoints.get()) if (c.id == checkpoint.id) return this

        checkpoints.get().add(checkpoint)
        checkpoints.timeStamp = time
        setDateUpdate(calendar)
        return this
    }

    fun editCheckpoint(checkpoint: Checkpoint): Task {
        val calendar = Calendar.getInstance()
        val time = calendar.timeInMillis

        for (c in checkpoints.get()) {
            if (c.id == checkpoint.id) {
                c.status = checkpoint.status
                c.text = checkpoint.text
                checkpoints.timeStamp = time
                setDateUpdate(calendar)
                return this
            }
        }

        return this
    }

    fun removeCheckpoint(checkpoint: Checkpoint): Task {
        val calendar = Calendar.getInstance()
        val time = calendar.timeInMillis

        for (c in checkpoints.get()) {
            if (c.id == checkpoint.id) {
                checkpoints.get().remove(c)
                checkpoints.timeStamp = time
                setDateUpdate(calendar)
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
        val calendar = Calendar.getInstance()
        val time = calendar.timeInMillis

        val folder = ArrayList<Task>()
        val main = ArrayList<Task>()
        val dated = ArrayList<Task>()
        val completed = ArrayList<Task>()

        for (t in children.get()) {
            val task = TaskManager.taskPool.poolObject.load(t, false)

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

        val a = ArrayList<String>()

        for (f in folder) {
            a.add(f.getFilename())
            TaskManager.taskPool.returnPoolObject(f)
        }

        for (m in main) {
            a.add(m.getFilename())
            TaskManager.taskPool.returnPoolObject(m)
        }

        for (d in dated) {
            a.add(d.getFilename())
            TaskManager.taskPool.returnPoolObject(d)
        }

        for (c in completed) {
            a.add(c.getFilename())
            TaskManager.taskPool.returnPoolObject(c)
        }

        children.setValue(a, time)
        setDateUpdate(calendar)
    }

    fun sortCheckpoints() {
        val calendar = Calendar.getInstance()
        val time = calendar.timeInMillis

        val c = ArrayList<Checkpoint>()
        val i = ArrayList<Checkpoint>()

        for (j in 0 until checkpoints.get().size) {
            if (checkpoints.get()[j].status) c.add(checkpoints.get()[j])
            else i.add(checkpoints.get()[j])
        }

        checkpoints.get().clear()
        checkpoints.get().addAll(i)
        checkpoints.get().addAll(c)

        checkpoints.timeStamp = time
        setDateUpdate(calendar)
    }

    private fun sortTags() {
        val calendar = Calendar.getInstance()
        val time = calendar.timeInMillis

        tags.get().sort()

        tags.timeStamp = time
        setDateUpdate(calendar)
    }
}

class TaskCreator: ComplexPool.PoolFactory<Task> {
    override fun createObject(id: Long): Task {
        return Task()
    }
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
    override fun toString(): String {
        return toJSON()!!.toString()
    }
}

class ShoppingItem(): ComplexPool.PoolObject {
    companion object {
        // Constants -------------------------------------------------------------------------------
        private const val FILENAME = "a"
        private const val PARENTS = "b"
        private const val VERSION = "c"
        private const val META = "d"

        private const val TITLE = "e"
        private const val NOTE = "f"
        private const val TAGS = "g"
        private const val PRICES = "h"
    }

    // Data ----------------------------------------------------------------------------------------
    private var filename = Field(0L, None, "")
    private var parents = Field(0L, None, ArrayList<String>())
    private var version = Field(0L, None, API.SHRINKING)
    private var meta = Field(0L, None, JSONObject())

    private var title = Field(0L, None, "")
    private var note = Field(0L, None, "")
    private var tags = Field(0L, None, ArrayList<String>())
    private var prices = Field(0L, None, ArrayList<Money>())

    // Loaders -------------------------------------------------------------------------------------
    fun load(): ShoppingItem {
        return this
    }

    fun load(filename: String): ShoppingItem {
        return this
    }

    fun load(data: JSONObject): ShoppingItem {
        return this
    }

    private fun load12(data: JSONObject) {

    }

    // Management Methods --------------------------------------------------------------------------
    fun save(): ShoppingItem {
        return this
    }

    private fun save12() {

    }

    fun upgrade(version: Int): ShoppingItem {
        return this
    }

    fun usedIn(): Int {
        return -1
    }

    fun delete(): Boolean {
        return false
    }

    fun forceDelete(): Boolean {
        return false
    }

    // To Methods ----------------------------------------------------------------------------------
    override fun toString(): String {
        return super.toString()
    }

    // Class Methods -------------------------------------------------------------------------------
    // Pool Methods --------------------------------------------------------------------------------
    override fun getID(): Long {
        throw NotImplementedError()
    }

    override fun destroy() {
        throw NotImplementedError()
    }

    // Getters -------------------------------------------------------------------------------------
    fun getFilename(): String {
        throw NotImplementedError()
    }

    fun getParents(): ArrayList<String> {
        throw NotImplementedError()
    }

    fun getVersion(): Int {
        throw NotImplementedError()
    }

    fun getMeta(): JSONObject {
        throw NotImplementedError()
    }

    fun getTitle(): String {
        throw NotImplementedError()
    }

    fun getNote(): String {
        throw NotImplementedError()
    }

    fun getTags(): ArrayList<String> {
        throw NotImplementedError()
    }

    fun getPrices(): ArrayList<Money> {
        throw NotImplementedError()
    }

    // Setters -------------------------------------------------------------------------------------
    fun setTitle(nTitle: String): ShoppingItem {
        throw NotImplementedError()
    }

    fun setNote(nNote: String): ShoppingItem {
        throw NotImplementedError()
    }

    fun setTags(nTags: ArrayList<String>): ShoppingItem {
        throw NotImplementedError()
    }

    fun setPrices(nPrices: ArrayList<Money>): ShoppingItem {
        throw NotImplementedError()
    }

    // Manipulators --------------------------------------------------------------------------------
    fun addParent(parent: String): ShoppingItem {
        throw NotImplementedError()
    }

    fun removeParent(parent: String): ShoppingItem {
        throw NotImplementedError()
    }

    fun addTag(tag: String): ShoppingItem {
        throw NotImplementedError()
    }

    fun removeTag(tag: String): ShoppingItem {
        throw NotImplementedError()
    }

    fun addPrice(price: Money): ShoppingItem {
        throw NotImplementedError()
    }

    fun removePrice(price: Money): ShoppingItem {
        throw NotImplementedError()
    }
}
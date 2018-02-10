package jgappsandgames.smartreminderslite.notifications

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.tasks.TaskActivity
import jgappsandgames.smartreminderslite.utility.ActivityUtility
import jgappsandgames.smartreminderssave.tasks.Task
import kotlinx.android.synthetic.main.list_task.view.*
import org.json.JSONObject
import java.util.*

/**
 * NotificationObject
 * Created by joshua on 2/1/2018.
 */
class NotificationObject(i_id: Int, i_filename: String, i_title: String, i_detail: String, i_time: Long) {
    companion object {
        // Constants -------------------------------------------------------------------------------
        private val ID = "id"
        private val FILENAME = "filename"
        private val TITLE = "title"
        private val DETAIL = "detail"
        private val TIME = "time"
    }

    // Data ----------------------------------------------------------------------------------------
    private var id: Int
    private var filename: String
    private var title: String
    private var detail: String
    private var time: Long

    private var task: Task? = null
    private var calendar: Calendar? = null

    // Constructors --------------------------------------------------------------------------------
    init {
        id = i_id
        filename = i_filename
        title = i_title
        detail = i_detail
        time = i_time
    }

    constructor(task: Task):
            this(Calendar.getInstance().timeInMillis.toInt(), task.getFilename(), task.getTitle(), task.getNote(), 0L)

    constructor(task: Task, time: Calendar):
            this(Calendar.getInstance().timeInMillis.toInt(), task.getFilename(), task.getTitle(), task.getNote(), time.timeInMillis)

    constructor(task: Task, detail: String):
            this(Calendar.getInstance().timeInMillis.toInt(), task.getFilename(), task.getTitle(), detail, 0L)

    constructor(task: Task, detail: String, time: Calendar):
            this(Calendar.getInstance().timeInMillis.toInt(), task.getFilename(), task.getTitle(), detail, time.timeInMillis)

    constructor(data: JSONObject):
            this(data.optInt(ID, 0), data.optString(FILENAME, ""), data.optString(TITLE, ""), data.optString(DETAIL, ""), data.optLong(TIME, 0L))

    // Management Methods --------------------------------------------------------------------------
    fun setNotification(context: Context) {

    }

    fun updateNotification(context: Context) {

    }

    fun removeNotification(context: Context) {

    }

    fun notificationActive(context: Context) {

    }

    fun deleteNotification(context: Context) {
    }

    // Getters -------------------------------------------------------------------------------------
    fun getId(): Int {
        return id
    }

    fun getTaskFilename(): String {
        return filename
    }

    fun getTask(): Task {
        if (task == null) task = Task(filename)

        return task!!
    }

    fun getTitle(): String {
        return title
    }

    fun getDetail(): String {
        return detail
    }

    fun getTime(): Long {
        return time
    }

    fun getCalendar(): Calendar {
        if (calendar == null) {
            calendar = Calendar.getInstance()
            calendar!!.timeInMillis = time
        }

        return calendar!!
    }

    // Setters -------------------------------------------------------------------------------------
    fun setTitle(new_title: String) {
        title = new_title
    }

    fun setDetail(new_detail: String) {
        detail = new_detail
    }

    fun setTime(new_time: Long) {
        time = new_time
        calendar = null
    }

    fun setCalendar(new_calendar: Calendar) {
        calendar = new_calendar
        time = new_calendar.timeInMillis
    }

    // To Methods ----------------------------------------------------------------------------------
    fun toJSON(): JSONObject {
        val data = JSONObject()

        data.put(ID, id)
        data.put(FILENAME, filename)
        data.put(TITLE, title)
        data.put(DETAIL, detail)
        data.put(TIME, time)

        return data
    }

    override fun toString(): String {
        return toJSON().toString()
    }
}
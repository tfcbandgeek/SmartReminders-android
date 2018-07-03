package jgappsandgames.smartreminderslite.adapter

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.tasks.TaskActivity
import jgappsandgames.smartreminderslite.utility.ActivityUtility
import jgappsandgames.smartreminderssave.tasks.Task
import jgappsandgames.smartreminderssave.tasks.TaskManager
import org.jetbrains.anko.vibrator

class TaskAdapter(private val activity: Activity, private val listener: OnTaskChangedListener,
                  var tasks: ArrayList<String>, search: String): BaseAdapter() {
    companion object {
        // Constants -------------------------------------------------------------------------------
        private const val TASK_TYPE_COUNT = 3

        // Static Class Methods --------------------------------------------------------------------
        fun swapTasks(tasks: ArrayList<Task>): ArrayList<String> {
            val t = ArrayList<String>(tasks.size)
            for (i in 0 until tasks.size) t.add(tasks[i].getFilename())
            return t
        }
    }

    // Initializer ---------------------------------------------------------------------------------
    init {
        if (search != "" || search != " ") {
            val r = ArrayList<String>()
            for (i in 0 until tasks.size) {
                val t = TaskManager.taskPool.getPoolObject().load(tasks[i])
                if (t.getTitle().toLowerCase().contains(search.toLowerCase())) r.add(t.getFilename())
                else if (t.getTags().size != 0) {
                    if (t.getTagString().toLowerCase().contains(search.toLowerCase())) r.add(t.getFilename())
                }
                TaskManager.taskPool.returnPoolObject(t, false)
            }

            tasks = r
        }
    }

    // List Methods --------------------------------------------------------------------------------
    override fun getCount(): Int {
        return tasks.size
    }

    override fun getViewTypeCount(): Int {
        return TASK_TYPE_COUNT
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    // Item Methods --------------------------------------------------------------------------------
    override fun getItem(position: Int): Task {
        return TaskManager.taskPool.getPoolObject().load(tasks[position])
    }

    override fun getItemId(position: Int): Long {
        val t = getItem(position)
        val id = t.getID().toLong()
        TaskManager.taskPool.returnPoolObject(t, false)
        return id
    }

    override fun getItemViewType(position: Int): Int {
        val t = getItem(position)
        val type = t.getType()
        TaskManager.taskPool.returnPoolObject(t, false)
        return type
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val t = getItem(position)

        if (convertView == null) {
            if (t.getType() == Task.TYPE_FOLDER) {
                val view = LayoutInflater.from(activity).inflate(R.layout.list_folder, parent, false)
                val holder = FolderHolder(activity, listener, view, t)
                view.tag = holder
                return view
            } else {
                val view = LayoutInflater.from(activity).inflate(R.layout.list_task, parent, false)
                val holder = TaskHolder(activity, listener, view, t)
                view.tag = holder
                return view
            }
        }

        if (t.getType() == Task.TYPE_FOLDER) {
            val holder: FolderHolder = convertView.tag as FolderHolder
            holder.updateViews(t)
            return convertView
        } else {
            val holder: TaskHolder = convertView.tag as TaskHolder
            holder.updateViews(t)
            return convertView
        }
    }

    // Interfaces ----------------------------------------------------------------------------------
    interface OnTaskChangedListener {
        fun onTaskChanged()
    }

    // Internal Classes ----------------------------------------------------------------------------
    class FolderHolder(private val activity: Activity, private val listener: OnTaskChangedListener, view: View, private var folder: Task):
            View.OnClickListener, View.OnLongClickListener {
        // Views -----------------------------------------------------------------------------------
        private val title: TextView = view.findViewById(R.id.title)
        private val note: TextView = view.findViewById(R.id.note)

        // Initializer -----------------------------------------------------------------------------
        init {
            title.setOnClickListener(this)
            note.setOnClickListener(this)

            title.setOnLongClickListener(this)
            note.setOnLongClickListener(this)

            setViews()
        }

        // View Handlers ---------------------------------------------------------------------------
        private fun setViews() {
            title.text = folder.getTitle()
            note.text = folder.getNote()
        }

        fun updateViews(folder: Task) {
            this.folder = folder
            setViews()
        }

        // Click Listeners -------------------------------------------------------------------------
        override fun onClick(p0: View?) {
            activity.startActivity(Intent(activity, TaskActivity::class.java)
                    .putExtra(ActivityUtility.TASK_NAME, folder.getFilename())
                    .putExtra(ActivityUtility.TASK_TYPE, folder.getType()))
        }

        override fun onLongClick(p0: View?): Boolean {
            TaskManager.archiveTask(folder)

            if (activity.vibrator.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    activity.vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    activity.vibrator.vibrate(100)
                }
            }

            listener.onTaskChanged()
            return true
        }
    }

    class TaskHolder(private val activity: Activity, private val listener: OnTaskChangedListener, view: View, private var task: Task):
            View.OnClickListener, View.OnLongClickListener, CompoundButton.OnCheckedChangeListener {
        // Views -----------------------------------------------------------------------------------
        private val title: TextView = view.findViewById(R.id.title)
        private val note: TextView = view.findViewById(R.id.note)
        private val status: CheckBox = view.findViewById(R.id.status)

        // Initializer -----------------------------------------------------------------------------
        init {
            title.setOnClickListener(this)
            note.setOnClickListener(this)

            title.setOnLongClickListener(this)
            note.setOnLongClickListener(this)

            status.setOnClickListener(this)

            setViews()
        }

        // View Handlers ---------------------------------------------------------------------------
        private fun setViews() {
            title.text = task.getTitle()
            note.text = task.getNote()
            status.isChecked = task.isCompleted()
        }

        fun updateViews(task: Task) {
            this.task = task
            setViews()
        }

        // Click Listeners -------------------------------------------------------------------------
        override fun onClick(p0: View?) {
            activity.startActivity(Intent(activity, TaskActivity::class.java)
                    .putExtra(ActivityUtility.TASK_NAME, task.getFilename())
                    .putExtra(ActivityUtility.TASK_TYPE, task.getType()))
        }

        override fun onLongClick(p0: View?): Boolean {
            TaskManager.archiveTask(task)

            if (activity.vibrator.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    activity.vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    activity.vibrator.vibrate(100)
                }
            }

            listener.onTaskChanged()
            return true
        }

        override fun onCheckedChanged(p0: CompoundButton?, b: Boolean) {
            task.markComplete(b)
            task.save()
        }
    }
}
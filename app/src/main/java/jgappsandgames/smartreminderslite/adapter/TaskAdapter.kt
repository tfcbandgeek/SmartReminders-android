package jgappsandgames.smartreminderslite.adapter

// Android OS
import android.app.Activity
import android.content.Intent

// Views
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.tasks.TaskActivity
import jgappsandgames.smartreminderslite.utility.TASK_NAME
import jgappsandgames.smartreminderslite.utility.TASK_TYPE
import jgappsandgames.smartreminderslite.utility.vibrate

// Save
import jgappsandgames.smartreminderssave.tasks.Task
import jgappsandgames.smartreminderssave.tasks.TaskManager

/**
 * TaskAdapter
 */
class TaskAdapter(private val activity: Activity, private val listener: OnTaskChangedListener, var tasks: ArrayList<String>, search: String): BaseAdapter() {
    companion object {
        // Constants -------------------------------------------------------------------------------
        const val TASK_TYPE_COUNT = 50

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
    override fun getCount(): Int = tasks.size

    override fun getViewTypeCount(): Int = TASK_TYPE_COUNT

    override fun hasStableIds(): Boolean = true

    // Item Methods --------------------------------------------------------------------------------
    override fun getItem(position: Int): Task = TaskManager.taskPool.getPoolObject().load(tasks[position])

    override fun getItemId(position: Int): Long {
        val t = getItem(position)
        val id = t.getID().toLong()
        TaskManager.taskPool.returnPoolObject(t, false)
        return id
    }

    override fun getItemViewType(position: Int): Int {
        val t = getItem(position)
        val type = t.getListViewType()
        TaskManager.taskPool.returnPoolObject(t, false)
        return type
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val t = getItem(position)

        if (convertView == null) {
            return when (t.getListViewType()) {
                Task.LIST_DEFAULT_FOLDER -> {
                    val view = LayoutInflater.from(activity).inflate(R.layout.list_folder_default, parent, false)
                    val holder = FolderHolder(activity, listener, view, t)
                    view.tag = holder
                    view
                }

                Task.LIST_PATH_FOLDER -> {
                    val view = LayoutInflater.from(activity).inflate(R.layout.list_folder_path, parent, false)
                    val holder = FolderHolder(activity, listener, view, t)
                    view.tag = holder
                    view
                }

                Task.LIST_TAG_FOLDER -> {
                    val view = LayoutInflater.from(activity).inflate(R.layout.list_folder_tag, parent, false)
                    val holder = FolderHolder(activity, listener, view, t)
                    view.tag = holder
                    view
                }

                Task.LIST_CHILDREN_FOLDER -> {
                    val view = LayoutInflater.from(activity).inflate(R.layout.list_folder_children, parent, false)
                    val holder = FolderHolder(activity, listener, view, t)
                    view.tag = holder
                    view
                }

                Task.LIST_ALL_FOLDER -> {
                    val view = LayoutInflater.from(activity).inflate(R.layout.list_folder_all, parent, false)
                    val holder = FolderHolder(activity, listener, view, t)
                    view.tag = holder
                    view
                }

                Task.LIST_DEFAULT_TASK -> {
                    val view = LayoutInflater.from(activity).inflate(R.layout.list_task_default, parent, false)
                    val holder = TaskHolder(activity, listener, view, t)
                    view.tag = holder
                    view
                }

                Task.LIST_PATH_TASK -> {
                    val view = LayoutInflater.from(activity).inflate(R.layout.list_task_path, parent, false)
                    val holder = TaskHolder(activity, listener, view, t)
                    view.tag = holder
                    view
                }

                Task.LIST_TAG_TASK -> {
                    val view = LayoutInflater.from(activity).inflate(R.layout.list_task_tag, parent, false)
                    val holder = TaskHolder(activity, listener, view, t)
                    view.tag = holder
                    view
                }

                Task.LIST_CHILDREN_TASK -> {
                    val view = LayoutInflater.from(activity).inflate(R.layout.list_task_children, parent, false)
                    val holder = TaskHolder(activity, listener, view, t)
                    view.tag = holder
                    view
                }

                Task.LIST_ALL_TASK -> {
                    val view = LayoutInflater.from(activity).inflate(R.layout.list_task_all, parent, false)
                    val holder = TaskHolder(activity, listener, view, t)
                    view.tag = holder
                    view
                }

                else -> throw Exception("No task list type or invalid task list type")
            }
        }

        return when (t.getType()) {
            Task.TYPE_FOLDER -> {
                val holder: FolderHolder = convertView.tag as FolderHolder
                holder.updateViews(t)
                TaskManager.taskPool.returnPoolObject(t, false)
                convertView
            }

            Task.TYPE_TASK -> {
                val view = LayoutInflater.from(activity).inflate(R.layout.list_task, parent, false)
                val holder = TaskHolder(activity, listener, view, t)
                view.tag = holder
                view
            }

            else -> throw Exception("No task list type or invalid task list type")
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

        private var path: TextView? = null
        private var children: TextView? = null
        private var tags: TextView? = null

        // Initializer -----------------------------------------------------------------------------
        init {
            title.setOnClickListener(this)
            note.setOnClickListener(this)

            title.setOnLongClickListener(this)
            note.setOnLongClickListener(this)

            // Setup secondary views
            if (folder.getListViewType() == Task.LIST_PATH_FOLDER) {
                path = view.findViewById(R.id.path)
                path?.setOnClickListener(this)
                path?.setOnLongClickListener(this)
            }

            if (folder.getListViewType() == Task.LIST_TAG_FOLDER) {
                tags = view.findViewById(R.id.tag)
                tags?.setOnClickListener(this)
                tags?.setOnLongClickListener(this)
            }

            if (folder.getListViewType() == Task.LIST_CHILDREN_FOLDER) {
                children = view.findViewById(R.id.children)
                children?.setOnClickListener(this)
                children?.setOnLongClickListener(this)
            }

            if (folder.getListViewType() == Task.LIST_ALL_FOLDER) {
                path = view.findViewById(R.id.path)
                path?.setOnClickListener(this)
                path?.setOnLongClickListener(this)

                tags = view.findViewById(R.id.tag)
                tags?.setOnClickListener(this)
                tags?.setOnLongClickListener(this)

                children = view.findViewById(R.id.children)
                children?.setOnClickListener(this)
                children?.setOnLongClickListener(this)
            }

            setViews()
        }

        // View Handlers ---------------------------------------------------------------------------
        private fun setViews() {
            title.text = folder.getTitle()
            note.text = folder.getNote()

            // Set secondary views
            if (folder.getListViewType() == Task.LIST_PATH_FOLDER) {
                path?.text = folder.getPath()
            }

            if (folder.getListViewType() == Task.LIST_TAG_FOLDER) {
                tags?.text = folder.getTagString()
            }

            if (folder.getListViewType() == Task.LIST_CHILDREN_FOLDER) {
                children?.text = folder.getChildrenString()
            }

            if (folder.getListViewType() == Task.LIST_ALL_FOLDER) {
                path?.text = folder.getPath()
                tags?.text = folder.getTagString()
                children?.text = folder.getChildrenString()
            }
        }

        fun updateViews(folder: Task) {
            this.folder = folder
            setViews()
        }

        // Click Listeners -------------------------------------------------------------------------
        override fun onClick(p0: View?) {
            activity.startActivity(Intent(activity, TaskActivity::class.java)
                    .putExtra(TASK_NAME, folder.getFilename())
                    .putExtra(TASK_TYPE, folder.getType()))
        }

        override fun onLongClick(p0: View?): Boolean {
            TaskManager.archiveTask(folder)
            listener.onTaskChanged()
            vibrate(activity)
            return true
        }
    }

    class TaskHolder(private val activity: Activity, private val listener: OnTaskChangedListener, view: View, private var task: Task):
            View.OnClickListener, View.OnLongClickListener, CompoundButton.OnCheckedChangeListener {
        // Views -----------------------------------------------------------------------------------
        private val title: TextView = view.findViewById(R.id.title)
        private val note: TextView = view.findViewById(R.id.note)
        private val status: CheckBox = view.findViewById(R.id.status)

        private var path: TextView? = null
        private var children: TextView? = null
        private var tags: TextView? = null

        // Initializer -----------------------------------------------------------------------------
        init {
            // Set Click Listeners
            title.setOnClickListener(this)
            note.setOnClickListener(this)

            title.setOnLongClickListener(this)
            note.setOnLongClickListener(this)

            status.setOnCheckedChangeListener(this)

            // Setup secondary views
            if (task.getListViewType() == Task.LIST_PATH_TASK) {
                path = view.findViewById(R.id.path)
                path?.setOnClickListener(this)
                path?.setOnLongClickListener(this)
            }

            if (task.getListViewType() == Task.LIST_TAG_TASK) {
                tags = view.findViewById(R.id.tag)
                tags?.setOnClickListener(this)
                tags?.setOnLongClickListener(this)
            }

            if (task.getListViewType() == Task.LIST_CHILDREN_TASK) {
                children = view.findViewById(R.id.children)
                children?.setOnClickListener(this)
                children?.setOnLongClickListener(this)
            }

            if (task.getListViewType() == Task.LIST_ALL_TASK) {
                path = view.findViewById(R.id.path)
                path?.setOnClickListener(this)
                path?.setOnLongClickListener(this)

                tags = view.findViewById(R.id.tag)
                tags?.setOnClickListener(this)
                tags?.setOnLongClickListener(this)

                children = view.findViewById(R.id.children)
                children?.setOnClickListener(this)
                children?.setOnLongClickListener(this)
            }

            setViews()
        }

        // View Handlers ---------------------------------------------------------------------------
        private fun setViews() {
            title.text = task.getTitle()
            note.text = task.getNote()
            status.isChecked = task.isCompleted()

            // Set secondary views
            if (task.getListViewType() == Task.LIST_PATH_TASK) {
                path?.setText(task.getPath())
            }

            if (task.getListViewType() == Task.LIST_TAG_TASK) {
                tags?.setText(task.getTagString())
            }

            if (task.getListViewType() == Task.LIST_CHILDREN_TASK) {
                children?.text = task.getCheckpointString()
            }

            if (task.getListViewType() == Task.LIST_ALL_TASK) {
                path?.setText(task.getPath())
                tags?.setText(task.getTagString())
                children?.text = task.getCheckpointString()
            }
        }

        fun updateViews(task: Task) {
            this.task = task
            setViews()
        }

        // Click Listeners -------------------------------------------------------------------------
        override fun onClick(p0: View?) {
            activity.startActivity(Intent(activity, TaskActivity::class.java)
                    .putExtra(TASK_NAME, task.getFilename())
                    .putExtra(TASK_TYPE, task.getType()))
        }

        override fun onLongClick(p0: View?): Boolean {
            TaskManager.archiveTask(task)
            listener.onTaskChanged()
            vibrate(activity)
            return true
        }

        override fun onCheckedChanged(p0: CompoundButton?, b: Boolean) {
            task.markComplete(b)
            task.save()
        }
    }
}
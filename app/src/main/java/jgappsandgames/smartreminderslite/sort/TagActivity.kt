package jgappsandgames.smartreminderslite.sort

// Android
import android.os.Bundle

// View
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.BaseAdapter
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.adapter.*
import jgappsandgames.smartreminderslite.utility.*

// KotlinX
import kotlinx.android.synthetic.main.activity_tag.*

// Save
import jgappsandgames.smartreminderssave.MasterManager
import jgappsandgames.smartreminderssave.tasks.*

/**
 * TagActivity
 * Created by joshua on 1/19/2018.
 */
class TagActivity: AppCompatActivity(), TagAdapter.TagSwitcher, TaskAdapter.OnTaskChangedListener {
    // Data ----------------------------------------------------------------------------------------
    private var selectedTags: ArrayList<String> = ArrayList()
    private var tasks: ArrayList<Task> = ArrayList()

    // LifeCycle Methods ---------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tag)

        loadClass(this)
    }

    override fun onResume() {
        super.onResume()

        tasks.clear()
        for (i in TaskManager.getAll().indices) tasks.add(Task(TaskManager.getAll()[i]))

        tag_tasks.adapter = TaskAdapter(this, selectedTags, tasks)
        tag_selected.adapter = TagAdapter(this, this, selectedTags, true)
        tag_unselected.adapter = TagAdapter(this, this, selectedTags, false)
    }

    // Menu ----------------------------------------------------------------------------------------
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_auxilary, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = onOptionsItemSelected(this, item, object: Save { override fun save() = this@TagActivity.save() })

    // Tag Switcher --------------------------------------------------------------------------------
    override fun moveTag(tag: String, selected: Boolean) {
        if (selected && !selectedTags.contains(tag)) selectedTags.add(tag)
        else if (!selected && selectedTags.contains(tag)) selectedTags.remove(tag)

        tag_tasks.adapter = TaskAdapter(this, selectedTags, tasks)
        tag_selected.adapter = TagAdapter(this, this, selectedTags, true)
        tag_unselected.adapter = TagAdapter(this, this, selectedTags, false)
    }

    override fun onTaskChanged() = onResume()

    // Save ----------------------------------------------------------------------------------------
    fun save() = MasterManager.save()

    // Internal Classes ----------------------------------------------------------------------------
    class TaskAdapter(private val activity: TagActivity, selected: java.util.ArrayList<String>, n_tasks: java.util.ArrayList<Task>): BaseAdapter() {
        // Data ----------------------------------------------------------------------------------------
        private val tasks: ArrayList<Task> = ArrayList()

        // Constructor ---------------------------------------------------------------------------------
        init {
            for (task in n_tasks) {
                var taskClear = true

                for (t in selected) {
                    var tagClear = false

                    for (i in 0 until task.getTags().size) {
                        if (task.getTags()[i] == t) {
                            tagClear = true
                            break
                        }
                    }

                    if (!tagClear) {
                        taskClear = false
                        break
                    }
                }

                if (taskClear) tasks.add(task)
            }
        }

        // List Methods --------------------------------------------------------------------------------
        override fun getCount(): Int = tasks.size

        override fun getViewTypeCount(): Int = 3

        override fun getItem(position: Int): Task = tasks[position]

        override fun getItemId(position: Int): Long = position.toLong()

        override fun getItemViewType(position: Int): Int = getItem(position).getType()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val t = getItem(position)

            if (convertView == null) {
                return when (t.getListViewType()) {
                    Task.LIST_DEFAULT_FOLDER -> {
                        val view = LayoutInflater.from(activity).inflate(R.layout.list_folder_default, parent, false)
                        val holder = jgappsandgames.smartreminderslite.adapter.TaskAdapter.FolderHolder(activity, activity, view, t)
                        view.tag = holder
                        view
                    }

                    Task.LIST_PATH_FOLDER -> {
                        val view = LayoutInflater.from(activity).inflate(R.layout.list_folder_path, parent, false)
                        val holder = jgappsandgames.smartreminderslite.adapter.TaskAdapter.FolderHolder(activity, activity, view, t)
                        view.tag = holder
                        view
                    }

                    Task.LIST_TAG_FOLDER -> {
                        val view = LayoutInflater.from(activity).inflate(R.layout.list_folder_tag, parent, false)
                        val holder = jgappsandgames.smartreminderslite.adapter.TaskAdapter.FolderHolder(activity, activity, view, t)
                        view.tag = holder
                        view
                    }

                    Task.LIST_CHILDREN_FOLDER -> {
                        val view = LayoutInflater.from(activity).inflate(R.layout.list_folder_children, parent, false)
                        val holder = jgappsandgames.smartreminderslite.adapter.TaskAdapter.FolderHolder(activity, activity, view, t)
                        view.tag = holder
                        view
                    }

                    Task.LIST_ALL_FOLDER -> {
                        val view = LayoutInflater.from(activity).inflate(R.layout.list_folder_all, parent, false)
                        val holder = jgappsandgames.smartreminderslite.adapter.TaskAdapter.FolderHolder(activity, activity, view, t)
                        view.tag = holder
                        view
                    }

                    Task.LIST_DEFAULT_NOTE -> {
                        val view = LayoutInflater.from(activity).inflate(R.layout.list_note_default, parent, false)
                        val holder = jgappsandgames.smartreminderslite.adapter.TaskAdapter.NoteHolder(activity, activity, view, t)
                        view.tag = holder
                        view
                    }

                    Task.LIST_PATH_NOTE -> {
                        val view = LayoutInflater.from(activity).inflate(R.layout.list_note_path, parent, false)
                        val holder = jgappsandgames.smartreminderslite.adapter.TaskAdapter.NoteHolder(activity, activity, view, t)
                        view.tag = holder
                        view
                    }

                    Task.LIST_TAG_NOTE -> {
                        val view = LayoutInflater.from(activity).inflate(R.layout.list_note_tag, parent, false)
                        val holder = jgappsandgames.smartreminderslite.adapter.TaskAdapter.NoteHolder(activity, activity, view, t)
                        view.tag = holder
                        view
                    }

                    Task.LIST_CHILDREN_NOTE -> {
                        val view = LayoutInflater.from(activity).inflate(R.layout.list_note_default, parent, false)
                        val holder = jgappsandgames.smartreminderslite.adapter.TaskAdapter.NoteHolder(activity, activity, view, t)
                        view.tag = holder
                        view
                    }

                    Task.LIST_ALL_NOTE -> {
                        val view = LayoutInflater.from(activity).inflate(R.layout.list_folder_all, parent, false)
                        val holder = jgappsandgames.smartreminderslite.adapter.TaskAdapter.NoteHolder(activity, activity, view, t)
                        view.tag = holder
                        view
                    }

                    Task.LIST_DEFAULT_TASK -> {
                        val view = LayoutInflater.from(activity).inflate(R.layout.list_task_default, parent, false)
                        val holder = jgappsandgames.smartreminderslite.adapter.TaskAdapter.TaskHolder(activity, activity, view, t)
                        view.tag = holder
                        view
                    }

                    Task.LIST_PATH_TASK -> {
                        val view = LayoutInflater.from(activity).inflate(R.layout.list_task_path, parent, false)
                        val holder = jgappsandgames.smartreminderslite.adapter.TaskAdapter.TaskHolder(activity, activity, view, t)
                        view.tag = holder
                        view
                    }

                    Task.LIST_TAG_TASK -> {
                        val view = LayoutInflater.from(activity).inflate(R.layout.list_task_tag, parent, false)
                        val holder = jgappsandgames.smartreminderslite.adapter.TaskAdapter.TaskHolder(activity, activity, view, t)
                        view.tag = holder
                        view
                    }

                    Task.LIST_CHILDREN_TASK -> {
                        val view = LayoutInflater.from(activity).inflate(R.layout.list_task_children, parent, false)
                        val holder = jgappsandgames.smartreminderslite.adapter.TaskAdapter.TaskHolder(activity, activity, view, t)
                        view.tag = holder
                        view
                    }

                    Task.LIST_ALL_TASK -> {
                        val view = LayoutInflater.from(activity).inflate(R.layout.list_task_all, parent, false)
                        val holder = jgappsandgames.smartreminderslite.adapter.TaskAdapter.TaskHolder(activity, activity, view, t)
                        view.tag = holder
                        view
                    }

                    else -> throw Exception("No task list type or invalid task list type")
                }
            }

            return when (t.getType()) {
                Task.TYPE_FOLDER -> {
                    val holder = convertView.tag as jgappsandgames.smartreminderslite.adapter.TaskAdapter.FolderHolder
                    holder.updateViews(t)
                    TaskManager.taskPool.returnPoolObject(t, false)
                    convertView
                }

                Task.TYPE_NOTE -> {
                    val holder = convertView.tag as jgappsandgames.smartreminderslite.adapter.TaskAdapter.NoteHolder
                    holder.updateViews(t)
                    TaskManager.taskPool.returnPoolObject(t, false)
                    convertView
                }

                Task.TYPE_TASK -> {
                    val holder = convertView.tag as jgappsandgames.smartreminderslite.adapter.TaskAdapter.TaskHolder
                    holder.updateViews(t)
                    TaskManager.taskPool.returnPoolObject(t, false)
                    convertView
                }

                else -> throw Exception("No task list type or invalid task list type")
            }
        }
    }
}
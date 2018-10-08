package jgappsandgames.smartreminderslite.sort

// Android
import android.app.Activity
import android.os.Bundle

// View
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.BaseAdapter
import android.view.View
import android.view.ViewGroup

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.adapter.*
import jgappsandgames.smartreminderslite.utility.*
import jgappsandgames.smartreminderssave.saveMaster

// KotlinX
import kotlinx.android.synthetic.main.activity_tag.*

// Save
import jgappsandgames.smartreminderssave.tasks.*

/**
 * TagActivity
 * Created by joshua on 1/19/2018.
 */
class TagActivity: Activity(), TagAdapter.TagSwitcher, TaskAdapter.OnTaskChangedListener {
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
        for (i in getAll().indices) tasks.add(getTaskFromPool().load(getAll()[i]))

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
    fun save() = saveMaster()

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

        override fun getView(position: Int, view: View?, parent: ViewGroup): View {
            val item = getItem(position)
            var convertView: View? = view
            val task: jgappsandgames.smartreminderslite.adapter.TaskAdapter.TaskHolder
            val folder: jgappsandgames.smartreminderslite.adapter.TaskAdapter.FolderHolder

            return if (item.getType() == Task.TYPE_FOLDER) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(activity).inflate(R.layout.list_folder, parent, false)
                    folder = jgappsandgames.smartreminderslite.adapter.TaskAdapter.FolderHolder(activity, activity, convertView!!, item)
                    convertView.tag = folder
                } else {
                    folder = convertView.tag as jgappsandgames.smartreminderslite.adapter.TaskAdapter.FolderHolder
                    folder.updateViews(item)
                }

                convertView
            } else {
                if (convertView == null) {
                    convertView = LayoutInflater.from(activity).inflate(R.layout.list_task, parent, false)
                    task = jgappsandgames.smartreminderslite.adapter.TaskAdapter.TaskHolder(activity, activity, convertView!!, item)
                    convertView.tag = task
                } else {
                    task = convertView.tag as jgappsandgames.smartreminderslite.adapter.TaskAdapter.TaskHolder
                    task.updateViews(item)
                }

                convertView
            }
        }
    }
}
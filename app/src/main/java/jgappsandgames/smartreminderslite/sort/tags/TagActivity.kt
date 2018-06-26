package jgappsandgames.smartreminderslite.sort.tags

// Android
import android.app.Activity
import android.os.Bundle
import android.view.*
import android.widget.BaseAdapter
import jgappsandgames.smartreminderslite.R

// Save
import jgappsandgames.smartreminderslite.adapter.TagAdapterInterface
import jgappsandgames.smartreminderslite.holder.TagHolder
import jgappsandgames.smartreminderslite.holder.TaskFolderHolder
import jgappsandgames.smartreminderslite.utility.OptionsUtility
import jgappsandgames.smartreminderssave.MasterManager
import jgappsandgames.smartreminderssave.tasks.Task
import jgappsandgames.smartreminderssave.tasks.TaskManager
import kotlinx.android.synthetic.main.activity_tag.*

/**
 * TagActivity
 * Created by joshua on 1/19/2018.
 */
class TagActivity: Activity(), TagHolder.TagSwitcher, TaskFolderHolder.OnTaskChangedListener {
    // Data ----------------------------------------------------------------------------------------
    private var selectedTags: ArrayList<String> = ArrayList()
    private var tasks: ArrayList<Task> = ArrayList()

    // LifeCycle Methods ---------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tag)
    }

    override fun onResume() {
        super.onResume()

        tasks.clear()
        for (i in TaskManager.tasks.indices) tasks.add(Task(TaskManager.tasks[i]))

        tag_tasks.adapter = TaskAdapter(this, selectedTags, tasks)
        tag_selected.adapter = TagAdapterInterface(this, this, selectedTags, true)
        tag_unselected.adapter = TagAdapterInterface(this, this, selectedTags, false)
    }

    // Menu ----------------------------------------------------------------------------------------
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_auxilary, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return OptionsUtility.onOptionsItemSelected(this, item, object: OptionsUtility.Save {
            override fun save() {
                this@TagActivity.save()
            }
        })
    }

    // Tag Switcher --------------------------------------------------------------------------------
    override fun moveTag(tag: String?, selected: Boolean) {
        if (selected && !selectedTags.contains(tag)) selectedTags.add(tag!!)
        else if (!selected && selectedTags.contains(tag)) selectedTags.remove(tag)

        tag_tasks.adapter = TaskAdapter(this, selectedTags, tasks)
        tag_selected.adapter = TagAdapterInterface(this, this, selectedTags, true)
        tag_unselected.adapter = TagAdapterInterface(this, this, selectedTags, false)
    }

    override fun onTaskChanged() {
        onResume()
    }

    // Save ----------------------------------------------------------------------------------------
    fun save() {
        MasterManager.save()
    }

    // Internal Classes ----------------------------------------------------------------------------
    class TaskAdapter(private val activity: TagActivity, selected: java.util.ArrayList<String>, n_tasks: java.util.ArrayList<Task>):
            BaseAdapter() {
        // Data ----------------------------------------------------------------------------------------
        private val tasks: java.util.ArrayList<Task> = java.util.ArrayList()

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
        override fun getCount(): Int {
            return tasks.size
        }

        override fun getViewTypeCount(): Int {
            return 3
        }

        override fun getItem(position: Int): Task {
            return tasks[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getItemViewType(position: Int): Int {
            return getItem(position).getType()
        }

        override fun getView(position: Int, view: View?, parent: ViewGroup): View {
            var convertView = view
            val holder: TaskFolderHolder

            if (convertView == null) {
                convertView = if (getItem(position).getType() == Task.TYPE_FLDR) LayoutInflater.from(activity).inflate(R.layout.list_folder, parent, false)
                else LayoutInflater.from(activity).inflate(R.layout.list_task, parent, false)

                holder = TaskFolderHolder(getItem(position), convertView!!, activity, activity)
                convertView.tag = holder
            } else {
                holder = convertView.tag as TaskFolderHolder
                holder.task = getItem(position)
            }

            holder.setViews()

            return convertView
        }
    }
}
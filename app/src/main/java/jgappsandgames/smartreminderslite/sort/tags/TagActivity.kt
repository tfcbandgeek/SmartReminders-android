package jgappsandgames.smartreminderslite.sort.tags

// Java
import java.util.ArrayList

// Android
import android.annotation.SuppressLint
import android.os.Bundle

// Save
import jgappsandgames.smartreminderssave.MasterManager
import jgappsandgames.smartreminderssave.tasks.Task
import jgappsandgames.smartreminderssave.tasks.TaskManager

/**
 * TagActivity
 * Created by joshua on 1/19/2018.
 */
class TagActivity: TagActivityInterface() {
    // Data
    private var selected_tags: ArrayList<String>? = null
    private var tasks: ArrayList<Task>? = null

    // LifeCycle Methods
    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load Data
        selected_tags = ArrayList()
    }

    override fun onResume() {
        super.onResume()

        tasks = ArrayList()
        for (i in TaskManager.tasks.indices) tasks!!.add(Task(TaskManager.tasks[i]))

        // Set Adapters
        task_adapter = TaskAdapter(this, selected_tags!!, tasks!!)
        selected_adapter = SelectedAdapter(this, selected_tags!!)
        unselected_adapter = UnselectedAdapter(this, selected_tags!!)

        tasks_list!!.adapter = task_adapter
        selected_list!!.adapter = selected_adapter
        unselected_list!!.adapter = unselected_adapter
    }

    // Tag Switcher
    override fun moveTag(tag: String?, selected: Boolean) {
        if (selected && !selected_tags!!.contains(tag)) selected_tags!!.add(tag!!)
        else if (!selected && selected_tags!!.contains(tag)) selected_tags!!.remove(tag)

        task_adapter = TaskAdapter(this, selected_tags!!, tasks!!)
        selected_adapter = SelectedAdapter(this, selected_tags!!)
        unselected_adapter = UnselectedAdapter(this, selected_tags!!)

        tasks_list!!.adapter = task_adapter
        selected_list!!.adapter = selected_adapter
        unselected_list!!.adapter = unselected_adapter
    }

    override fun onTaskChanged() {
        onResume()
    }

    override fun save() {
        MasterManager.save()
    }
}
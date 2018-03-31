package jgappsandgames.smartreminderslite.sort.tags

// Android
import android.annotation.SuppressLint
import android.os.Bundle

// Save
import jgappsandgames.smartreminderslite.adapter.TagAdapterInterface
import jgappsandgames.smartreminderssave.MasterManager
import jgappsandgames.smartreminderssave.tasks.Task
import jgappsandgames.smartreminderssave.tasks.TaskManager

/**
 * TagActivity
 * Created by joshua on 1/19/2018.
 */
class TagActivity: TagActivityInterface() {
    // Data ----------------------------------------------------------------------------------------
    private var selectedTags: ArrayList<String>? = null
    private var tasks: ArrayList<Task>? = null

    // LifeCycle Methods ---------------------------------------------------------------------------
    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load Data
        selectedTags = ArrayList()
    }

    override fun onResume() {
        super.onResume()

        tasks = ArrayList()
        for (i in TaskManager.tasks.indices) tasks!!.add(Task(TaskManager.tasks[i]))

        // Set Adapters
        taskAdapter = TaskAdapter(this, selectedTags!!, tasks!!)
        selectedAdapter = TagAdapterInterface(this, this, selectedTags!!, true)
        unselectedAdapter = TagAdapterInterface(this, this, selectedTags!!, false)

        tasksList!!.adapter = taskAdapter
        selectedList!!.adapter = selectedAdapter
        unselectedList!!.adapter = unselectedAdapter
    }

    // Tag Switcher --------------------------------------------------------------------------------
    override fun moveTag(tag: String?, selected: Boolean) {
        if (selected && !selectedTags!!.contains(tag)) selectedTags!!.add(tag!!)
        else if (!selected && selectedTags!!.contains(tag)) selectedTags!!.remove(tag)

        taskAdapter = TaskAdapter(this, selectedTags!!, tasks!!)
        selectedAdapter = TagAdapterInterface(this, this, selectedTags!!, true)
        unselectedAdapter = TagAdapterInterface(this, this, selectedTags!!, false)

        tasksList!!.adapter = taskAdapter
        selectedList!!.adapter = selectedAdapter
        unselectedList!!.adapter = unselectedAdapter
    }

    override fun onTaskChanged() {
        onResume()
    }

    // Save ----------------------------------------------------------------------------------------
    override fun save() {
        MasterManager.save()
    }
}
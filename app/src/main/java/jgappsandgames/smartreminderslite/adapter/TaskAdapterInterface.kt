package jgappsandgames.smartreminderslite.adapter

// Android OS
import android.app.Activity

// Views
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

// Jetbrain
import org.jetbrains.annotations.Nullable

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.holder.TaskFolderHolder

// Save
import jgappsandgames.smartreminderssave.tasks.Task

/**
 * TaskAdapterInterface
 * Created by joshua on 12/13/2017.
 *
 * Class that Handles The List View Items For Tasks and Folders Throughout the Entire App
 */
open class TaskAdapterInterface(val activity: Activity, private val listener: TaskFolderHolder.OnTaskChangedListener, val tasks: ArrayList<Task>): BaseAdapter() {
    // Secondary Initializer -----------------------------------------------------------------------
    constructor(activity: Activity, listener: TaskFolderHolder.OnTaskChangedListener, tasks: ArrayList<String>, @Nullable unused: String?):
            this(activity, listener, ArrayList()) {
        for (t in tasks) this.tasks.add(Task(t))
    }

    // List Methods --------------------------------------------------------------------------------
    override fun getCount(): Int {
        return tasks.size
    }

    override fun getViewTypeCount(): Int {
        return 3
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    // Item Methods --------------------------------------------------------------------------------
    override fun getItem(position: Int): Task {
        return tasks[position]
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).getID()
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).getType()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        if (convertView == null) {
            val temp: View = if (getItemViewType(position) == Task.TYPE_TASK) LayoutInflater.from(activity).inflate(R.layout.list_task, parent, false)
                else LayoutInflater.from(activity).inflate(R.layout.list_folder, parent, false)

            val holder = TaskFolderHolder(getItem(position), temp, activity, listener)
            temp.tag = holder
            holder.setViews()
            return temp
        }

        val holder: TaskFolderHolder = convertView.tag as TaskFolderHolder
        holder.task = getItem(position)
        holder.setViews()
        return convertView
    }
}
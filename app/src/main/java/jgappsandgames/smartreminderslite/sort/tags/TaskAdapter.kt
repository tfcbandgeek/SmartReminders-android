package jgappsandgames.smartreminderslite.sort.tags

// Java
import java.util.ArrayList

// Views
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.holder.TaskFolderHolder

// Save
import jgappsandgames.smartreminderssave.tasks.Task

/**
 * TaskAdapter
 * Created by joshua on 1/19/2018.
 */
class TaskAdapter(private val activity: TagActivity, selected: ArrayList<String>, n_tasks: ArrayList<Task>):
        BaseAdapter() {
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
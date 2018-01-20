package jgappsandgames.smartreminderslite.sort.tags

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.holder.TaskFolderHolder
import jgappsandgames.smartreminderssave.tasks.Task
import java.util.ArrayList

/**
 * Created by joshu on 1/19/2018.
 */
class TaskAdapter(private val activity: TagActivity, selected: ArrayList<String>, n_tasks: ArrayList<Task>): BaseAdapter() {
    private val tasks: ArrayList<Task> = ArrayList()

    init {
        for (task in n_tasks) {
            var task_clear = true

            for (t in selected) {
                var tag_clear = false

                for (i in 0 until task.getTags().size) {
                    if (task.getTags()[i] == t) {
                        tag_clear = true
                        break
                    }
                }

                if (!tag_clear) {
                    task_clear = false
                    break
                }
            }

            if (task_clear) tasks.add(task)
        }
    }

    // List Methods
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
        var convert_view = view
        val holder: TaskFolderHolder
        if (convert_view == null) {
            if (getItem(position).getType() == Task.TYPE_FLDR)
                convert_view = LayoutInflater.from(activity).inflate(R.layout.list_folder, parent, false)
            else
                convert_view = LayoutInflater.from(activity).inflate(R.layout.list_task, parent, false)

            holder = TaskFolderHolder(getItem(position), convert_view!!, activity, activity)
            convert_view.tag = holder
        } else {
            holder = convert_view.tag as TaskFolderHolder

            holder.task = getItem(position)
        }

        holder.setViews()

        return convert_view
    }
}
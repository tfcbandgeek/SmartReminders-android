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
        var convert_view = view
        val holder: TaskFolderHolder
        if (convert_view == null) {
            convert_view = if (getItem(position).getType() == Task.TYPE_FLDR) LayoutInflater.from(activity).inflate(R.layout.list_folder, parent, false)
                else LayoutInflater.from(activity).inflate(R.layout.list_task, parent, false)

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
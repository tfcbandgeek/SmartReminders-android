package jgappsandgames.smartreminderslite.adapter

// Android OS
import android.app.Activity

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
 * TaskAdapterInterface
 * Created by joshua on 12/13/2017.
 *
 * Class that Handles The List View Items For Tasks and Folders Throughout the Entire App
 */
open class TaskAdapterInterface(private val activity: Activity, private val listener: TaskFolderHolder.OnTaskChangedListener,
                                private val tasks: ArrayList<Task>, private val delete: Boolean = false): BaseAdapter() {
    companion object {
        // Constants -------------------------------------------------------------------------------
        const val TASK_TYPE_COUNT: Int = 3
    }

    // List Methods --------------------------------------------------------------------------------
    /**
     * GetCount
     *
     * @return The Size of the Tasks Array
     */
    override fun getCount(): Int {
        return tasks.size
    }

    /**
     * GetViewTypeCount
     *
     * @return The Number of Different View Types
     */
    override fun getViewTypeCount(): Int {
        return TASK_TYPE_COUNT
    }

    /**
     * HasStableIds
     *
     * @return True, Tasks Have Stable Ids
     */
    override fun hasStableIds(): Boolean {
        return true
    }

    // Item Methods --------------------------------------------------------------------------------
    /**
     * GetItem
     *
     * @param position Position of the Task
     * @return The Task/Folder At That Location
     */
    override fun getItem(position: Int): Task {
        return tasks[position]
    }

    /**
     * GetItemId
     *
     * @param position Position of The Item
     * @return The Id of The Item
     */
    override fun getItemId(position: Int): Long {
        return getItem(position).getID()
    }

    /**
     * GetItemViewType
     *
     * @param position Position of The Item
     * @return The View Type of the Item
     */
    override fun getItemViewType(position: Int): Int {
        return getItem(position).getType()
    }

    /**
     * GetView
     *
     * @param position Position of The Item
     * @param convertView Convert View
     * @param parent Parent View
     * @return The View Based on The Task at That Position
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        if (convertView == null) {
            val temp: View = if (getItemViewType(position) == Task.TYPE_TASK) LayoutInflater.from(activity).inflate(R.layout.list_task, parent, false)
                else LayoutInflater.from(activity).inflate(R.layout.list_folder, parent, false)

            val holder = TaskFolderHolder(activity, temp, getItem(position), listener, delete)
            temp.tag = holder
            holder.setViews()
            return temp
        }

        val holder: TaskFolderHolder = convertView.tag as TaskFolderHolder
        holder.update(getItem(position))
        return convertView
    }
}
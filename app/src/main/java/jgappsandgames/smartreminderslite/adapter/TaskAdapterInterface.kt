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

// Log
import me.jgappsandgames.openlog.Log
import org.jetbrains.annotations.Nullable

/**
 * TaskAdapterInterface
 * Created by joshua on 12/13/2017.
 *
 * Class that Handles The List View Items For Tasks and Folders Throughout the Entire App
 */
open class TaskAdapterInterface(val activity: Activity, val listener: TaskFolderHolder.OnTaskChangedListener, val tasks: ArrayList<Task>): BaseAdapter() {
    // Secondary Initializer -----------------------------------------------------------------------
    /**
     * Secondary Initializer
     *
     * Initializer Used to Handle Situations Where the Tasks are Handed Down in String Form
     */
    constructor(activity: Activity, listener: TaskFolderHolder.OnTaskChangedListener, tasks: ArrayList<String>, @Nullable unused: String?):
            this(activity, listener, ArrayList()) {
        for (t in tasks) this.tasks.add(Task(t))
    }

    // List Methods --------------------------------------------------------------------------------
    /**
     * GetCount
     *
     * @return The Size of the Tasks Array
     */
    override fun getCount(): Int {
        Log.d("TaskAdapterInterface", "GetCount")
        return tasks.size
    }

    /**
     * GetViewTypeCount
     *
     * @return The Number of Different View Types
     */
    override fun getViewTypeCount(): Int {
        Log.d("TaskAdapterInterface", "GetViewTypeCount Called")
        return 3
    }

    /**
     * HasStableIds
     *
     * @return True, Tasks Have Stable Ids
     */
    override fun hasStableIds(): Boolean {
        Log.d("TaskAdapterInterface", "HasStableIds Called")
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
        Log.d("TaskAdapterInterface", "GetItem Called")
        return tasks[position]
    }

    /**
     * GetItemId
     *
     * @param position Position of The Item
     * @return The Id of The Item
     */
    override fun getItemId(position: Int): Long {
        Log.d("TaskAdapterInterface", "GetItemId Called")
        return getItem(position).getID()
    }

    /**
     * GetItemViewType
     *
     * @param position Position of The Item
     * @return The View Type of the Item
     */
    override fun getItemViewType(position: Int): Int {
        Log.d("TaskAdapterInterface", "GetItemViewType Called")
        return getItem(position).getType()
    }

    /**
     * GetView
     *
     * @param position Position of The Item
     * @param convert_view Convert View
     * @param parent Parent View
     * @return The View Based on The Task at That Position
     */
    override fun getView(position: Int, convert_view: View?, parent: ViewGroup): View {
        Log.d("TaskAdapterInterface", "GetView Called")
        if (convert_view == null) {
            val temp: View
            if (getItemViewType(position) == Task.TYPE_TASK) temp = LayoutInflater.from(activity).inflate(R.layout.list_task, parent, false)
            else temp = LayoutInflater.from(activity).inflate(R.layout.list_folder, parent, false)

            val holder = TaskFolderHolder(getItem(position), temp, activity, listener)
            temp.setTag(holder)
            holder.setViews()
            return temp
        }

        val holder: TaskFolderHolder = convert_view.tag as TaskFolderHolder
        holder.task = getItem(position)
        holder.setViews()
        return convert_view
    }
}
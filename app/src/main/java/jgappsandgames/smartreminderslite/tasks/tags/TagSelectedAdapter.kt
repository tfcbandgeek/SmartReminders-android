package jgappsandgames.smartreminderslite.tasks.tags

// Java
import java.util.ArrayList

// Views
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.holder.TagHolder

// Save
import jgappsandgames.smartreminderssave.tasks.Task

/**
 * TagSelectedAdapter
 * Created by joshua on 1/19/2018.
 */
class TagSelectedAdapter: BaseAdapter {
    // Data ----------------------------------------------------------------------------------------
    private val activity: TagEditorActivity
    private val tags: ArrayList<String>

    // Constructors --------------------------------------------------------------------------------
    constructor(activity: TagEditorActivity, task: Task): super() {
        this.activity = activity
        tags = task.getTags()
    }

    constructor(activity: TagEditorActivity, task: Task, search: String) {
        // Set Activity
        this.activity = activity

        // Set Tags
        tags = ArrayList(task.getTags().size)
        for (tag in task.getTags())
            if (tag.toLowerCase().contains(search.toLowerCase())) tags.add(tag)
    }

    // List Methods --------------------------------------------------------------------------------
    override fun getCount(): Int {
        return tags.size
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    // Item Methods --------------------------------------------------------------------------------
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): String {
        return tags[position]
    }

    override fun getView(position: Int, convert_view: View, parent: ViewGroup): View {
        var view = convert_view
        view = LayoutInflater.from(activity).inflate(R.layout.list_tag, parent, false)

        val holder = TagHolder(getItem(position), true, activity, view)
        view.tag = holder

        return view
    }
}
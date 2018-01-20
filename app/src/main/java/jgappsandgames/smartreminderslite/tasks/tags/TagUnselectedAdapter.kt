package jgappsandgames.smartreminderslite.tasks.tags

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.holder.TagHolder
import jgappsandgames.smartreminderssave.tags.TagManager
import jgappsandgames.smartreminderssave.tasks.Task
import java.util.ArrayList

/**
 * Created by joshu on 1/19/2018.
 */
internal class TagUnselectedAdapter : BaseAdapter {
    // Data
    private val activity: TagEditorActivity
    private val tags: ArrayList<String>

    // Initializers
    constructor(activity: TagEditorActivity, task: Task) : super() {
        // Set Activity
        this.activity = activity

        // Set Tags
        tags = ArrayList()
        for (tag in TagManager.tags) {
            if (!task.getTags().contains(tag)) tags.add(tag)
        }
    }

    constructor(activity: TagEditorActivity, task: Task, search: String) {
        // Set Activity
        this.activity = activity


        // Set Tags
        tags = ArrayList()
        for (tag in TagManager.tags) {
            if (tag.toLowerCase().contains(search.toLowerCase())) {
                if (!task.getTags().contains(tag)) tags.add(tag)
            }
        }
    }

    // List Methods
    override fun getCount(): Int {
        return tags.size
    }

    override fun getItemViewType(position: Int): Int {
        return 1
    }

    // Item Methods

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): String {
        return tags[position]
    }

    override fun getView(position: Int, convert_view: View, parent: ViewGroup): View {
        var view = convert_view
        view = LayoutInflater.from(activity).inflate(R.layout.list_tag, parent, false)

        val holder = TagHolder(getItem(position), false, activity, view)
        view.tag = holder

        return view
    }
}
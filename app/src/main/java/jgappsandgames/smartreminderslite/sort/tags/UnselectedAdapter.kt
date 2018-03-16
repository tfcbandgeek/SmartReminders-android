package jgappsandgames.smartreminderslite.sort.tags

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.holder.TagHolder
import jgappsandgames.smartreminderssave.tags.TagManager
import java.util.ArrayList

/**
 * Created by joshu on 1/19/2018.
 */
class UnselectedAdapter(private val activity: TagActivity, selected: ArrayList<String>) : BaseAdapter() {
    private val tags: ArrayList<String> = ArrayList()

    init {
        for (i in TagManager.tags.indices)
            if (!selected.contains(TagManager.tags[i])) tags.add(TagManager.tags[i])
    }

    // List Methods
    override fun getCount(): Int {
        return tags.size
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    // Item Methods
    override fun getItem(position: Int): String {
        return tags[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convert_view: View, parent: ViewGroup): View {
        var view = convert_view
        view = LayoutInflater.from(activity).inflate(R.layout.list_tag, parent, false)

        val holder = TagHolder(getItem(position), false, activity, view)
        view.tag = holder

        return view
    }
}
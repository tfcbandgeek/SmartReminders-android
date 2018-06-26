package jgappsandgames.smartreminderslite.adapter

// Android
import android.app.Activity

// Views
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.holder.TagHolder

// Save
import jgappsandgames.smartreminderssave.tags.TagManager

open class TagAdapterInterface(): BaseAdapter() {
    // Data ----------------------------------------------------------------------------------------
    private lateinit var activity: Activity
    private lateinit var switcher: TagHolder.TagSwitcher
    private lateinit var tags: ArrayList<String>
    private var selected: Boolean = false

    // Constructor ---------------------------------------------------------------------------------
    // Main
    constructor(_activity: Activity, _switcher: TagHolder.TagSwitcher, _tags: ArrayList<String>, _selected: Boolean): this() {
        selected = _selected
        switcher = _switcher
        activity = _activity

        if (selected) tags = _tags
        else {
            tags = ArrayList()
            for (tag in TagManager.tags) if (!_tags.contains(tag)) tags.add(tag)
        }
    }

    // Search
    constructor(_activity: Activity, _switcher: TagHolder.TagSwitcher, _tags: ArrayList<String>, _selected: Boolean, _search: String): this() {
        selected = _selected
        switcher = _switcher
        activity = _activity

        if (selected) {
            tags = ArrayList()
            for (tag in _tags) if (tag.toLowerCase().contains(_search.toLowerCase())) tags.add(tag)
        } else {
            tags = ArrayList()
            for (tag in _tags) {
                if (tag.toLowerCase().contains(_search.toLowerCase())) {
                    if (!TagManager.tags.contains(tag)) tags.add(tag)
                }
            }
        }
    }

    // List Methods --------------------------------------------------------------------------------
    override fun getCount(): Int {
        return tags.size
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    // Item Methods --------------------------------------------------------------------------------
    override fun getItem(position: Int): String {
        return tags[position]
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return if (convertView == null) {
            val view = LayoutInflater.from(activity).inflate(R.layout.list_tag, parent, false)
            val holder = TagHolder(getItem(position), selected, switcher, view)
            view.tag = holder
            view
        } else {
            val holder = convertView.tag as TagHolder
            holder.updateView(getItem(position), selected)
            convertView
        }
    }
}
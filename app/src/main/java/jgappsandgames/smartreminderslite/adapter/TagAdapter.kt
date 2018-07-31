package jgappsandgames.smartreminderslite.adapter

// Android OS
import android.app.Activity
import android.util.Log

// Views
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

// Anko
import org.jetbrains.anko.sdk25.coroutines.onClick

// App
import jgappsandgames.smartreminderslite.R

// Save
import jgappsandgames.smartreminderssave.tags.TagManager

class TagAdapter(private var activity: Activity, private var switcher: TagSwitcher, _tags: ArrayList<String>,
                 private var selected: Boolean = false, search: String = ""): BaseAdapter() {
    // Data ----------------------------------------------------------------------------------------
    private var tags: ArrayList<String> = ArrayList()

    // Initializer ---------------------------------------------------------------------------------
    init {
        if (selected) {
            for (tag in _tags) {
                if (tag.toLowerCase().contains(search.toLowerCase())) {
                    if (TagManager.tags.contains(tag)) tags.add(tag)
                }
            }
        } else {
            for (tag in TagManager.tags) {
                if (tag.toLowerCase().contains(search.toLowerCase())) {
                    if (!_tags.contains(tag)) {
                        tags.add(tag)
                    }
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

    // Interfaces ----------------------------------------------------------------------------------
    interface TagSwitcher {
        fun moveTag(tag: String, selected: Boolean)
    }

    // Internal Classes ----------------------------------------------------------------------------
    class TagHolder(private var tagText: String, private var tagSelected: Boolean,
                    private val switcher: TagSwitcher, view: View) {
        private val textView: TextView = view.findViewById(R.id.tag)

        // Initializer -----------------------------------------------------------------------------
        init {
            textView.text = tagText
            textView.onClick {
                switcher.moveTag(tagText, !tagSelected)
            }
        }

        // Management Methods ----------------------------------------------------------------------
        fun updateView(tag: String, selected: Boolean) {
            this.tagSelected = selected
            this.tagText = tag

            textView.text = tagText
        }
    }
}
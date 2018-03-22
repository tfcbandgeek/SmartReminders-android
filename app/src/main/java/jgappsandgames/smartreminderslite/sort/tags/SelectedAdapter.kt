package jgappsandgames.smartreminderslite.sort.tags

// Java
import java.util.ArrayList

// Android
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.holder.TagHolder

/**
 * SelectedAdapter
 * Created by joshua on 1/19/2018.
 */
class SelectedAdapter(private val activity: TagActivity, private val tags: ArrayList<String>): BaseAdapter() {
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

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        if (convertView == null) {
            val view = LayoutInflater.from(activity).inflate(R.layout.list_tag, parent, false)

            val holder = TagHolder(getItem(position), true, activity, view)
            view.tag = holder

            return view
        }

        val holder = TagHolder(getItem(position), true, activity, convertView)
        convertView.tag = holder

        return convertView
    }
}
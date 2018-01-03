package jgappsandgames.smartreminderslite.adapter

// Views
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

/**
 * Created by joshua on 12/26/2017.
 * TODO Implement
 */
abstract class TagAdapterInterface : BaseAdapter() {
    override fun getCount(): Int {
        return 0
    }

    override fun getItem(i: Int): Any? {
        return null
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    override fun getView(i: Int, view: View, viewGroup: ViewGroup): View? {
        return null
    }
}
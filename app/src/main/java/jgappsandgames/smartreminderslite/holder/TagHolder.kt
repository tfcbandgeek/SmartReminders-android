package jgappsandgames.smartreminderslite.holder

// Views
import android.view.View
import android.widget.TextView

// App
import jgappsandgames.smartreminderslite.R

/**
 * TagHolder
 * Created by joshua on 12/26/2017.
 *
 * Class to Handle Tag Views in a list
 */
class TagHolder(private var tagText: String?, private var tagSelected: Boolean, private val switcher: TagSwitcher, view: View): View.OnClickListener {
    // Views ---------------------------------------------------------------------------------------
    private val textView: TextView = view.findViewById(R.id.tag)

    // Initializer ---------------------------------------------------------------------------------
    init {
        textView.text = tagText
        textView.setOnClickListener(this)
    }

    // Management Methods --------------------------------------------------------------------------
    fun updateView(tag: String, selected: Boolean) {
        this.tagSelected = selected
        this.tagText = tag

        textView.text = tagText
    }

    // Click Listeners -----------------------------------------------------------------------------
    override fun onClick(view: View) {
        switcher.moveTag(tagText, !tagSelected)
    }

    // Interfaces ----------------------------------------------------------------------------------
    interface TagSwitcher {
        fun moveTag(tag: String?, selected: Boolean)
    }
}
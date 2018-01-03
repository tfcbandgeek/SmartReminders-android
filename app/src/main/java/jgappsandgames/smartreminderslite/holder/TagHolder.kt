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
class TagHolder(private var tag_text: String?, private var tag_selected: Boolean, private val switcher: TagSwitcher, view: View): View.OnClickListener {
    // Views ---------------------------------------------------------------------------------------
    private val text_view: TextView = view.findViewById(R.id.tag)

    // Initializer ---------------------------------------------------------------------------------
    init {
        text_view.text = tag_text
        text_view.setOnClickListener(this)
    }

    // Management Methods --------------------------------------------------------------------------
    fun updateView(tag: String, selected: Boolean) {
        this.tag_selected = selected
        this.tag_text = tag

        text_view.text = tag_text
    }

    // Click Listeners -----------------------------------------------------------------------------
    override fun onClick(view: View) {
        switcher.moveTag(tag_text, !tag_selected)
    }

    // Interfaces ----------------------------------------------------------------------------------
    interface TagSwitcher {
        fun moveTag(tag: String?, selected: Boolean)
    }
}
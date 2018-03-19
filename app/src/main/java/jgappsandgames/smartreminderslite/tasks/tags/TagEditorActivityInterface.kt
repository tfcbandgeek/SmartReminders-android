package jgappsandgames.smartreminderslite.tasks.tags

// Android
import android.app.Activity
import android.os.Bundle

// View
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.holder.TagHolder
import jgappsandgames.smartreminderslite.utility.ActivityUtility

/**
 * TagEditorActivity
 * Created by joshua on 1/19/2018.
 */
abstract class TagEditorActivityInterface: Activity(), TextWatcher, View.OnClickListener, View.OnLongClickListener, TagHolder.TagSwitcher {
    // Views ---------------------------------------------------------------------------------------
    protected var search_text: EditText? = null
    protected var search_enter: Button? = null
    protected var selected: ListView? = null
    protected var unselected: ListView? = null

    // LifeCycle Methods ---------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set Content View
        setContentView(R.layout.activity_tag_editpr)

        // Find Views
        search_text = findViewById(R.id.search_text)
        search_enter = findViewById(R.id.search_enter)
        selected = findViewById(R.id.selected)
        unselected = findViewById(R.id.unselected)

        // Set Result Intent
        setResult(ActivityUtility.RESPONSE_NONE)
    }
}
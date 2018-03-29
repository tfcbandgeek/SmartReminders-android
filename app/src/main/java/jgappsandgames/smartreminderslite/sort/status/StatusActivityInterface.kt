package jgappsandgames.smartreminderslite.sort.status

// Android OS
import android.app.Activity
import android.os.Bundle

// Views
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.TextView

// Anko
import org.jetbrains.anko.toast

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.holder.TaskFolderHolder

/**
 * StatusActivityInterface
 * Created by joshua on 12/14/2017.
 *
 * Status View[StatusActivityInterface, StatusActivity]
 * A View for Viewing Tasks Based on Their Status
 *
 *     StatusActivityInterface: View and Application Focus
 *     StatusActivity: Data and User Input
 */
abstract class StatusActivityInterface: Activity(), TaskFolderHolder.OnTaskChangedListener {
    // Views ---------------------------------------------------------------------------------------
    protected var overdueText: TextView? = null
    protected var overdueList: ListView? = null
    protected var incompleteText: TextView? = null
    protected var incompleteList: ListView? = null
    protected var completeText: TextView? = null
    protected var completeList: ListView? = null

    // LifeCycle Methods ---------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status)

        // Find Views
        overdueText = findViewById(R.id.overdue_text)
        overdueList = findViewById(R.id.overdue_list)
        incompleteText = findViewById(R.id.incomplete_text)
        incompleteList = findViewById(R.id.incomplete_list)
        completeText = findViewById(R.id.done_text)
        completeList = findViewById(R.id.done_list)
    }

    // Menu Methods --------------------------------------------------------------------------------
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_auxilary, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.save -> {
                save()
                toast(R.string.saved).show()
                return true
            }

            R.id.close -> {
                finish()
                return true
            }
        }

        return false
    }

    // Abstract Methods ----------------------------------------------------------------------------
    protected abstract fun save()
}
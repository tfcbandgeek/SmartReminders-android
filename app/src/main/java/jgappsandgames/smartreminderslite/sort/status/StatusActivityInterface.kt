package jgappsandgames.smartreminderslite.sort.status

// Android OS
import android.app.Activity
import android.os.Bundle

// Views
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast

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
    protected var overdue_text: TextView? = null
    protected var overdue_list: ListView? = null
    protected var incomplete_text: TextView? = null
    protected var incomplete_list: ListView? = null
    protected var done_text: TextView? = null
    protected var done_list: ListView? = null

    // LifeCycle Methods ---------------------------------------------------------------------------
    /**
     * OnCreate
     *
     * Called To Create the View
     * Called By the Application
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status)

        // Find Views
        overdue_text = findViewById(R.id.overdue_text)
        overdue_list = findViewById(R.id.overdue_list)
        incomplete_text = findViewById(R.id.incomplete_text)
        incomplete_list = findViewById(R.id.incomplete_list)
        done_text = findViewById(R.id.done_text)
        done_list = findViewById(R.id.done_list)
    }

    // Menu Methods --------------------------------------------------------------------------------
    /**
     * OnCreateOptionsMenu
     *
     * Called To Create the Options Menu
     * Called By The Application
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_auxilary, menu)
        return true
    }

    /**
     * OnOptionsItemSelected
     *
     * Called When An Options Item Is Selected
     * Called By The Application
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.save -> {
                save()
                Toast.makeText(this, "Saved.", Toast.LENGTH_LONG).show()
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
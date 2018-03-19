package jgappsandgames.smartreminderslite.sort.priority

// Android OS
import android.app.Activity
import android.os.Bundle

// Views
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.holder.TaskFolderHolder


/**
 * PriorityActivityInterface
 * Created by joshua on 12/14/2017.
 *
 * Priority[PriorityActivityInterface, PriorityActivity]
 * A View for Viewing Tasks Based on Their Priority
 *
 *     PriorityActivityInterface: View and Application Focus
 *     PriorityActivity: Data and User Input
 */
abstract class PriorityActivityInterface: Activity(), View.OnClickListener, View.OnLongClickListener, TaskFolderHolder.OnTaskChangedListener {
    // Views ---------------------------------------------------------------------------------------
    protected var list: ListView? = null
    protected var up: Button? = null
    protected var down: Button? = null

    // Adapters ------------------------------------------------------------------------------------
    protected var ignore: BaseAdapter? = null
    protected var low: BaseAdapter? = null
    protected var normal: BaseAdapter? = null
    protected var high: BaseAdapter? = null
    protected var stared: BaseAdapter? = null

    // LifeCycle Methods ---------------------------------------------------------------------------
    /**
     * OnCreate
     *
     * Called To Create the Activity
     * Called By The Application
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_priority)

        // Find Views
        list = findViewById(R.id.tasks)
        up = findViewById(R.id.up)
        down = findViewById(R.id.down)

        // Set Activity Click Listeners
        up!!.setOnClickListener(this)
        up!!.setOnLongClickListener(this)
        down!!.setOnClickListener(this)
        down!!.setOnLongClickListener(this)
    }

    // Menu Methods --------------------------------------------------------------------------------
    /**
     * OnCreateOptionsMenu
     *
     * Called To Create the Options Menu
     * Called By the Application
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_auxilary, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * OnOptionsItemSelected
     *
     * Called When An Options Item Is Selected
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
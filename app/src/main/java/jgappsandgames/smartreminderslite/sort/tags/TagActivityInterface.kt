package jgappsandgames.smartreminderslite.sort.tags

// Activity
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
import jgappsandgames.smartreminderslite.holder.TagHolder
import jgappsandgames.smartreminderslite.holder.TaskFolderHolder

/**
 * TagActivityInterface
 * Created by joshua on 1/19/2018.
 */
abstract class TagActivityInterface: Activity(), TagHolder.TagSwitcher, TaskFolderHolder.OnTaskChangedListener {
    // Views ---------------------------------------------------------------------------------------
    protected var tasks_text: TextView? = null
    protected var tasks_list: ListView? = null
    protected var selected_text: TextView? = null
    protected var selected_list: ListView? = null
    protected var unselected_text: TextView? = null
    protected var unselected_list: ListView? = null

    // Adapters ------------------------------------------------------------------------------------
    protected var task_adapter: TaskAdapter? = null
    protected var selected_adapter: SelectedAdapter? = null
    protected var unselected_adapter: UnselectedAdapter? = null

    // LifeCycle Methods ---------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tag)

        // Find Views
        tasks_text = findViewById(R.id.tasks_title)
        tasks_list = findViewById(R.id.tasks)
        selected_text = findViewById(R.id.selected_text)
        selected_list = findViewById(R.id.selected)
        unselected_text = findViewById(R.id.unselected_text)
        unselected_list = findViewById(R.id.unselected)
    }

    // Menu ----------------------------------------------------------------------------------------
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_auxilary, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                save()
                Toast.makeText(this, "Saved.", Toast.LENGTH_LONG).show()
            }

            R.id.close -> finish()
        }

        return super.onOptionsItemSelected(item)
    }

    // Abstract Methods ----------------------------------------------------------------------------
    abstract fun save()
}
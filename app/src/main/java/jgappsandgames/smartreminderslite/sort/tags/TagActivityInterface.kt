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
import jgappsandgames.smartreminderslite.adapter.TagAdapterInterface
import jgappsandgames.smartreminderslite.holder.TagHolder
import jgappsandgames.smartreminderslite.holder.TaskFolderHolder

/**
 * TagActivityInterface
 * Created by joshua on 1/19/2018.
 */
abstract class TagActivityInterface: Activity(), TagHolder.TagSwitcher, TaskFolderHolder.OnTaskChangedListener {
    // Views ---------------------------------------------------------------------------------------
    private var tasksText: TextView? = null
    protected var tasksList: ListView? = null
    private var selectedText: TextView? = null
    protected var selectedList: ListView? = null
    private var unselectedText: TextView? = null
    protected var unselectedList: ListView? = null

    // Adapters ------------------------------------------------------------------------------------
    protected var taskAdapter: TaskAdapter? = null
    protected var selectedAdapter: TagAdapterInterface? = null
    protected var unselectedAdapter: TagAdapterInterface? = null

    // LifeCycle Methods ---------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tag)

        // Find Views
        tasksText = findViewById(R.id.tasks_title)
        tasksList = findViewById(R.id.tasks)
        selectedText = findViewById(R.id.selected_text)
        selectedList = findViewById(R.id.selected)
        unselectedText = findViewById(R.id.unselected_text)
        unselectedList = findViewById(R.id.unselected)
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
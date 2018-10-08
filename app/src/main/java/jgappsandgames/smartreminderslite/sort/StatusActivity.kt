package jgappsandgames.smartreminderslite.sort

// Android OS
import android.app.Activity
import android.os.Bundle

// Views
import android.view.Menu
import android.view.MenuItem

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.adapter.TaskAdapter
import jgappsandgames.smartreminderslite.utility.*
import jgappsandgames.smartreminderssave.saveMaster
import jgappsandgames.smartreminderssave.status.*

// KotlinX
import kotlinx.android.synthetic.main.activity_status.*

// Save
import jgappsandgames.smartreminderssave.tasks.Task

/**
 * StatusActivity
 * Created by joshua on 12/14/2017.
 */
class StatusActivity: Activity(), TaskAdapter.OnTaskChangedListener {
    // LifeCycle Methods ---------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status)

        // Handle Data
        loadClass(this)
    }

    override fun onResume() {
        super.onResume()

        // Create Status Manager
        createStatus()

        val i = ArrayList<Task>()
        i.addAll(getInProgress())
        i.addAll(getIncomplete())

        // Set Adapters
        status_overdue_list.adapter = TaskAdapter(this, this, TaskAdapter.swapTasks(getOverdue()), "")
        status_incomplete_list.adapter = TaskAdapter(this, this, TaskAdapter.swapTasks(i), "")
        status_done_list.adapter = TaskAdapter(this, this, TaskAdapter.swapTasks(getCompleted()), "")

        // Set Text
        status_overdue_text.text = getString(R.string.overdue_tasks, getOverdue().size)
        status_incomplete_text.text = getString(R.string.incomplete_tasks, i.size)
        status_done_text.text = getString(R.string.completed_tasks, getCompleted().size)
    }

    // Menu Methods --------------------------------------------------------------------------------
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_auxilary, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = onOptionsItemSelected(this, item, object: Save { override fun save() = this@StatusActivity.save() })

    // Task Change Listeners -----------------------------------------------------------------------
    override fun onTaskChanged() = onResume()

    // Parent Methods ------------------------------------------------------------------------------
    fun save() = saveMaster()
}
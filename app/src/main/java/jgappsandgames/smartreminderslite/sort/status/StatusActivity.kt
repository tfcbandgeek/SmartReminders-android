package jgappsandgames.smartreminderslite.sort.status

// Android OS
import android.app.Activity
import android.content.Intent
import android.os.Bundle

// Views
import android.view.Menu
import android.view.MenuItem

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.adapter.TaskAdapterInterface
import jgappsandgames.smartreminderslite.holder.TaskFolderHolder
import jgappsandgames.smartreminderslite.home.FirstRun
import jgappsandgames.smartreminderslite.utility.OptionsUtility

// KotlinX
import kotlinx.android.synthetic.main.activity_status.status_done_list
import kotlinx.android.synthetic.main.activity_status.status_done_text
import kotlinx.android.synthetic.main.activity_status.status_incomplete_list
import kotlinx.android.synthetic.main.activity_status.status_incomplete_text
import kotlinx.android.synthetic.main.activity_status.status_overdue_list
import kotlinx.android.synthetic.main.activity_status.status_overdue_text

// Save
import jgappsandgames.smartreminderssave.MasterManager
import jgappsandgames.smartreminderssave.status.StatusManager
import jgappsandgames.smartreminderssave.tasks.Task
import jgappsandgames.smartreminderssave.utility.FileUtility

/**
 * StatusActivity
 * Created by joshua on 12/14/2017.
 */
class StatusActivity: Activity(), TaskFolderHolder.OnTaskChangedListener {
    // LifeCycle Methods ---------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status)

        // Handle Data
        FileUtility.loadFilePaths(this)
        if (FileUtility.isFirstRun()) startActivity(Intent(this, FirstRun::class.java))
        else MasterManager.load()
    }

    override fun onResume() {
        super.onResume()

        // Create Status Manager
        StatusManager.create()

        val i = ArrayList<Task>()
        i.addAll(StatusManager.getInProgress())
        i.addAll(StatusManager.getIncomplete())

        // Set Adapters
        status_overdue_list.adapter = StatusAdapter(this, StatusManager.getOverdue())
        status_incomplete_list.adapter = StatusAdapter(this, i)
        status_done_list.adapter = StatusAdapter(this, StatusManager.getCompleted())

        // Set Text
        status_overdue_text.text = getString(R.string.overdue_tasks, StatusManager.getOverdue().size)
        status_incomplete_text.text = getString(R.string.incomplete_tasks, i.size)
        status_done_text.text = getString(R.string.completed_tasks, StatusManager.getCompleted().size)
    }

    // Menu Methods --------------------------------------------------------------------------------
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_auxilary, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return OptionsUtility.onOptionsItemSelected(this, item, object: OptionsUtility.Save {
            override fun save() {
                this@StatusActivity.save()
            }
        })
    }

    // Task Change Listeners -----------------------------------------------------------------------
    override fun onTaskChanged() {
        onResume()
    }

    // Parent Methods ------------------------------------------------------------------------------
    fun save() {
        MasterManager.save()
    }

    // Internal Classes ----------------------------------------------------------------------------
    class StatusAdapter(activity: StatusActivity, tasks: ArrayList<Task>):
            TaskAdapterInterface(activity, activity, tasks)
}
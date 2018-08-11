package jgappsandgames.smartreminderslite.sort

// Java
import android.app.Activity
import android.content.Intent
import java.util.Calendar

// Android OS
import android.os.Bundle

// Views
import android.view.Menu
import android.view.MenuItem

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.adapter.TaskAdapter
import jgappsandgames.smartreminderslite.home.FirstRun
import jgappsandgames.smartreminderslite.utility.Save
import jgappsandgames.smartreminderslite.utility.onOptionsItemSelected

// KotlinX
import kotlinx.android.synthetic.main.activity_date.date_next
import kotlinx.android.synthetic.main.activity_date.date_previous
import kotlinx.android.synthetic.main.activity_date.date_tasks

// Save
import jgappsandgames.smartreminderssave.MasterManager
import jgappsandgames.smartreminderssave.date.DateManager
import jgappsandgames.smartreminderssave.utility.FileUtility

/**
 * DayActivity
 * Created by joshua on 1/1/2018.
 */
class DayActivity: Activity(), TaskAdapter.OnTaskChangedListener {
    // Data ----------------------------------------------------------------------------------------
    private var dateActivity: Calendar = Calendar.getInstance()

    // LifeCycle Methods ---------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date)

        // Load Data
        FileUtility.loadFilePaths(this)
        if (FileUtility.isFirstRun()) startActivity(Intent(this, FirstRun::class.java))
        else MasterManager.load()

        dateActivity = Calendar.getInstance()
        setTitle()

        // Set Click Listeners
        date_previous.setOnClickListener {
            dateActivity.add(Calendar.DAY_OF_MONTH, -1)
            if (dateActivity.before(Calendar.getInstance())) dateActivity = Calendar.getInstance()
            date_tasks.adapter = TaskAdapter(this, this, TaskAdapter.swapTasks(DateManager.getDayTasks(dateActivity)), "")
            setTitle()
        }

        date_next.setOnClickListener {
            dateActivity.add(Calendar.DAY_OF_MONTH, 1)
            date_tasks.adapter = TaskAdapter(this, this, TaskAdapter.swapTasks(DateManager.getDayTasks(dateActivity)), "")
            setTitle()
        }
    }

    override fun onResume() {
        super.onResume()

        DateManager.create()
        date_tasks.adapter = TaskAdapter(this, this, TaskAdapter.swapTasks(DateManager.getDayTasks(dateActivity)), "")
    }

    // Menu ----------------------------------------------------------------------------------------
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_auxilary, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return onOptionsItemSelected(this, item, object: Save {
            override fun save() {
                this@DayActivity.save()
            }
        })
    }

    // Task Changed Listener -----------------------------------------------------------------------
    override fun onTaskChanged() {
        onResume()
    }

    // Private Class Methods -----------------------------------------------------------------------
    fun save() {
        MasterManager.save()
    }

    private fun setTitle() {
        title = (dateActivity.get(Calendar.MONTH) + 1).toString() + "/" + dateActivity.get(Calendar.DAY_OF_MONTH).toString()
    }
}
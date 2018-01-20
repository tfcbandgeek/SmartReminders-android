package jgappsandgames.smartreminderslite.sort.date

// Java
import java.util.Calendar

// Android OS
import android.app.Activity
import android.content.Intent
import android.os.Bundle

// Views
import android.view.Menu
import android.view.MenuItem
import android.widget.CalendarView
import android.widget.ListView
import android.widget.Toast

// Save
import jgappsandgames.smartreminderssave.MasterManager
import jgappsandgames.smartreminderssave.tasks.Task
import jgappsandgames.smartreminderssave.utility.FileUtility

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.holder.TaskFolderHolder
import jgappsandgames.smartreminderslite.home.FirstRun

/**
 * MonthActivityInterface
 * Created by joshua on 1/19/2018.
 */
abstract class MonthActivityInterface: Activity(), TaskFolderHolder.OnTaskChangedListener, CalendarView.OnDateChangeListener {
    // Data
    protected var selected: Calendar? = null
    protected var selected_tasks: ArrayList<Task>? = null

    // Views
    protected var calendar: CalendarView? = null
    protected var tasks: ListView? = null

    // LifeCycle Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_month)

        // Find Views
        calendar = findViewById(R.id.calendar)
        tasks = findViewById(R.id.tasks)

        // First Run
        FileUtility.loadFilePaths(this)
        if (FileUtility.isFirstRun()) {
            val first_run = Intent(this, FirstRun::class.java)
            startActivity(first_run)
        } else {
            // Load Data
            MasterManager.load(this)
        }

        // Set Listeners
        calendar!!.setOnDateChangeListener(this)

        // Set Calendar
        selected = Calendar.getInstance()
        selected!!.timeInMillis = calendar!!.date
    }

    // Menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_auxilary, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                MasterManager.save()
                Toast.makeText(this, "Saved.", Toast.LENGTH_LONG).show()
            }

            R.id.close -> finish()
        }

        return super.onOptionsItemSelected(item)
    }

    // Task Changed Listener
    override fun onTaskChanged() {
        onResume()
    }
}
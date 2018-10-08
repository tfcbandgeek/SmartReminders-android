package jgappsandgames.smartreminderslite.sort

// Java
import java.util.Calendar

// Android
import android.app.Activity
import android.os.Bundle

// Views
import android.view.Menu
import android.view.MenuItem
import android.widget.CalendarView

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.adapter.TaskAdapter
import jgappsandgames.smartreminderslite.utility.Save
import jgappsandgames.smartreminderslite.utility.loadClass
import jgappsandgames.smartreminderslite.utility.onOptionsItemSelected
import jgappsandgames.smartreminderssave.date.createDate
import jgappsandgames.smartreminderssave.date.getDayTasks
import jgappsandgames.smartreminderssave.saveMaster

// KotlinX
import kotlinx.android.synthetic.main.activity_month.month_calendar
import kotlinx.android.synthetic.main.activity_month.month_tasks

// Save
import jgappsandgames.smartreminderssave.tasks.Task

/**
 * MonthActivity
 * Created by joshua on 1/19/2018.
 */
class MonthActivity: Activity(), TaskAdapter.OnTaskChangedListener, CalendarView.OnDateChangeListener {
    // Data ----------------------------------------------------------------------------------------
    private var selected: Calendar = Calendar.getInstance()
    private var selectedTasks: ArrayList<Task> = getDayTasks(selected)

    // LifeCycleMethods ----------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_month)

        // Handle Data
        loadClass(this)
        month_calendar.date = selected.timeInMillis
        month_calendar.setOnDateChangeListener(this)
    }

    override fun onResume() {
        super.onResume()
        createDate()
        selectedTasks = getDayTasks(selected)
        month_tasks.adapter = TaskAdapter(this, this, TaskAdapter.swapTasks(selectedTasks), "")
    }

    // Menu ----------------------------------------------------------------------------------------
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_auxilary, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = onOptionsItemSelected(this, item, object: Save { override fun save() = saveMaster() })

    // On Date Set Presses -------------------------------------------------------------------------
    override fun onSelectedDayChange(calendar: CalendarView, year: Int, month: Int, day: Int) {
        selected.set(Calendar.YEAR, year)
        selected.set(Calendar.MONTH, month)
        selected.set(Calendar.DAY_OF_MONTH, day)
        selectedTasks = getDayTasks(selected)
        month_tasks.adapter = TaskAdapter(this, this, TaskAdapter.swapTasks(selectedTasks), "")
    }

    // Task Changed Listener -----------------------------------------------------------------------
    override fun onTaskChanged() = onResume()
}
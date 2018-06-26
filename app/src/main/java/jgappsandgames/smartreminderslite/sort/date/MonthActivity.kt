package jgappsandgames.smartreminderslite.sort.date

// Java
import java.util.Calendar

// Android
import android.app.Activity
import android.content.Intent
import android.os.Bundle

// Views
import android.view.Menu
import android.view.MenuItem
import android.widget.CalendarView

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.adapter.TaskAdapterInterface
import jgappsandgames.smartreminderslite.holder.TaskFolderHolder
import jgappsandgames.smartreminderslite.home.FirstRun
import jgappsandgames.smartreminderslite.utility.OptionsUtility

// KotlinX
import kotlinx.android.synthetic.main.activity_month.month_calendar
import kotlinx.android.synthetic.main.activity_month.month_tasks

// Save
import jgappsandgames.smartreminderssave.MasterManager
import jgappsandgames.smartreminderssave.date.DateManager
import jgappsandgames.smartreminderssave.tasks.Task
import jgappsandgames.smartreminderssave.utility.FileUtility

/**
 * MonthActivity
 * Created by joshua on 1/19/2018.
 */
class MonthActivity: Activity(), TaskFolderHolder.OnTaskChangedListener, CalendarView.OnDateChangeListener {
    // Data ----------------------------------------------------------------------------------------
    private var selected: Calendar = Calendar.getInstance()
    private var selectedTasks: ArrayList<Task> = DateManager.getDayTasks(selected)

    // LifeCycleMethods ----------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_month)

        // Handle Data
        FileUtility.loadFilePaths(this)
        if (FileUtility.isFirstRun()) startActivity(Intent(this, FirstRun::class.java))
        else MasterManager.load()

        // Setup Calendar
        month_calendar.date = selected.timeInMillis
        month_calendar.setOnDateChangeListener(this)
    }

    override fun onResume() {
        super.onResume()
        // Set List
        DateManager.create()
        selectedTasks = DateManager.getDayTasks(selected)
        month_tasks.adapter = MonthAdapter(this, selectedTasks)
    }

    // Menu ----------------------------------------------------------------------------------------
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_auxilary, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return OptionsUtility.onOptionsItemSelected(this, item, object: OptionsUtility.Save {
            override fun save() {
                MasterManager.save()
            }
        })
    }

    // On Date Set Presses -------------------------------------------------------------------------
    override fun onSelectedDayChange(calendar: CalendarView, year: Int, month: Int, day: Int) {
        selected.set(Calendar.YEAR, year)
        selected.set(Calendar.MONTH, month)
        selected.set(Calendar.DAY_OF_MONTH, day)
        selectedTasks = DateManager.getDayTasks(selected)
        month_tasks.adapter = MonthAdapter(this, selectedTasks)
    }

    // Task Changed Listener -----------------------------------------------------------------------
    override fun onTaskChanged() {
        onResume()
    }

    // Internal Classes ----------------------------------------------------------------------------
    class MonthAdapter(activity: MonthActivity, tasks: java.util.ArrayList<Task>):
            TaskAdapterInterface(activity, activity, tasks)
}
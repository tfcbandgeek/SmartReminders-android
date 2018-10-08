package jgappsandgames.smartreminderslite.sort

// Java
import java.util.Calendar

// Android OS
import android.app.Activity
import android.os.Bundle

// View
import android.view.Menu
import android.view.MenuItem

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.adapter.TaskAdapter

// Save

// KotlinX
import kotlinx.android.synthetic.main.activity_date.*

// App
import jgappsandgames.smartreminderslite.utility.*
import jgappsandgames.smartreminderssave.date.createDate
import jgappsandgames.smartreminderssave.date.getWeek
import jgappsandgames.smartreminderssave.date.getWeekTasks
import jgappsandgames.smartreminderssave.saveMaster

/**
 * WeekActivity
 * Created by joshua on 1/19/2018.
 */
class WeekActivity: Activity(), TaskAdapter.OnTaskChangedListener {
    // Data ----------------------------------------------------------------------------------------
    private var weekActive: Int = 0

    // LifeCycle Methods ---------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date)

        // First Run
        loadClass(this)

        // Set Title
        setTitle()

        // Set Click Listeners
        date_previous.setOnClickListener {
            weekActive--
            if (weekActive < 0) weekActive = 0
            date_tasks.adapter = TaskAdapter(this, this, TaskAdapter.swapTasks(getWeekTasks(weekActive)), "")
            setTitle()
        }

        date_next.setOnClickListener {
            weekActive++
            date_tasks.adapter = TaskAdapter(this, this, TaskAdapter.swapTasks(getWeekTasks(weekActive)), "")
            setTitle()
        }
    }

    override fun onResume() {
        super.onResume()
        createDate()
        date_tasks.adapter = TaskAdapter(this, this, TaskAdapter.swapTasks(getWeekTasks(weekActive)), "")
    }

    override fun onPause() {
        super.onPause()
        save()
    }

    // Menu ----------------------------------------------------------------------------------------
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_auxilary, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = onOptionsItemSelected(this, item, object: Save { override fun save() = this@WeekActivity.save() })

    // Task Changed Listener -----------------------------------------------------------------------
    override fun onTaskChanged() = onResume()

    // Private Class Methods -----------------------------------------------------------------------
    private fun setTitle() {
        val start = getWeek(weekActive).getStart()
        val end = getWeek(weekActive).getEnd()

        title = (start.get(Calendar.MONTH) + 1).toString() + "/" + start.get(Calendar.DAY_OF_MONTH).toString() + " - " +
                (end.get(Calendar.MONTH) + 1).toString() + "/" + end.get(Calendar.DAY_OF_MONTH).toString()
    }

    fun save() = saveMaster()
}
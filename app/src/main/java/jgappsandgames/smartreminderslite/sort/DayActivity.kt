package jgappsandgames.smartreminderslite.sort

// Java
import android.app.Activity
import java.util.Calendar

// Android OS
import android.os.Bundle

// Views
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.adapter.TaskAdapter
import jgappsandgames.smartreminderslite.utility.*

// KotlinX
import kotlinx.android.synthetic.main.activity_date.*

// Save
import jgappsandgames.smartreminderssave.MasterManager
import jgappsandgames.smartreminderssave.date.DateManager

/**
 * DayActivity
 * Created by joshua on 1/1/2018.
 */
class DayActivity: AppCompatActivity(), TaskAdapter.OnTaskChangedListener {
    // Data ----------------------------------------------------------------------------------------
    private var dateActivity: Calendar = Calendar.getInstance()

    // LifeCycle Methods ---------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date)

        // Load Data
        loadClass(this)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean = onOptionsItemSelected(this, item, object: Save { override fun save() = this@DayActivity.save() })

    // Task Changed Listener -----------------------------------------------------------------------
    override fun onTaskChanged() = onResume()

    // Private Class Methods -----------------------------------------------------------------------
    fun save() = MasterManager.save()

    private fun setTitle() {
        title = (dateActivity.get(Calendar.MONTH) + 1).toString() + "/" + dateActivity.get(Calendar.DAY_OF_MONTH).toString()
    }
}
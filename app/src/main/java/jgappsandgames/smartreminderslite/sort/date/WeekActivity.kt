package jgappsandgames.smartreminderslite.sort.date

// Java
import java.util.Calendar

// Android OS
import android.app.Activity
import android.content.Intent
import android.os.Bundle

// View
import android.view.Menu
import android.view.MenuItem

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.adapter.TaskAdapter

// Save
import jgappsandgames.smartreminderssave.MasterManager
import jgappsandgames.smartreminderssave.date.DateManager
import jgappsandgames.smartreminderssave.utility.FileUtility

// KotlinX
import kotlinx.android.synthetic.main.activity_date.date_next
import kotlinx.android.synthetic.main.activity_date.date_previous
import kotlinx.android.synthetic.main.activity_date.date_tasks

// App
import jgappsandgames.smartreminderslite.home.FirstRun
import jgappsandgames.smartreminderslite.utility.OptionsUtility

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
        FileUtility.loadFilePaths(this)
        if (FileUtility.isFirstRun()) startActivity(Intent(this, FirstRun::class.java))
        else MasterManager.load()
        weekActive = 0

        // Set Title
        setTitle()

        // Set Click Listeners
        date_previous.setOnClickListener {
            weekActive--
            if (weekActive < 0) weekActive = 0
            date_tasks.adapter = TaskAdapter(this, this, TaskAdapter.swapTasks(DateManager.getWeekTasks(weekActive)), "")
            setTitle()
        }

        date_next.setOnClickListener {
            weekActive++
            date_tasks.adapter = TaskAdapter(this, this, TaskAdapter.swapTasks(DateManager.getWeekTasks(weekActive)), "")
            setTitle()
        }
    }

    override fun onResume() {
        super.onResume()

        DateManager.create()
        date_tasks.adapter = TaskAdapter(this, this, TaskAdapter.swapTasks(DateManager.getWeekTasks(weekActive)), "")
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return OptionsUtility.onOptionsItemSelected(this, item, object: OptionsUtility.Save {
            override fun save() {
                this@WeekActivity.save()
            }
        })
    }

    // Task Changed Listener -----------------------------------------------------------------------
    override fun onTaskChanged() {
        onResume()
    }

    // Private Class Methods -----------------------------------------------------------------------
    private fun setTitle() {
        val start = DateManager.getWeek(weekActive).getStart()
        val end = DateManager.getWeek(weekActive).getEnd()

        title = (start.get(Calendar.MONTH) + 1).toString() + "/" +
                start.get(Calendar.DAY_OF_MONTH).toString() + " - " +
                (end.get(Calendar.MONTH) + 1).toString() + "/" +
                end.get(Calendar.DAY_OF_MONTH).toString()
    }

    fun save() {
        MasterManager.save()
    }
}
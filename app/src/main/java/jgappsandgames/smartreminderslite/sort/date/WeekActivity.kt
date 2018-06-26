package jgappsandgames.smartreminderslite.sort.date

// Java
import android.app.Activity
import java.util.Calendar

// Android OS
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.adapter.TaskAdapterInterface
import jgappsandgames.smartreminderslite.holder.TaskFolderHolder

// Save
import jgappsandgames.smartreminderssave.MasterManager
import jgappsandgames.smartreminderssave.date.DateManager
import jgappsandgames.smartreminderssave.utility.FileUtility

// App
import jgappsandgames.smartreminderslite.home.FirstRun
import jgappsandgames.smartreminderslite.utility.OptionsUtility
import kotlinx.android.synthetic.main.activity_date.*

/**
 * WeekActivity
 * Created by joshua on 1/19/2018.
 */
class WeekActivity: Activity(), TaskFolderHolder.OnTaskChangedListener {
    // Data ----------------------------------------------------------------------------------------
    protected var weekActive: Int = 0

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
            date_tasks.adapter = WeekAdapter(this, weekActive)
            setTitle()
        }

        date_next.setOnClickListener {
            weekActive++
            date_tasks.adapter = WeekAdapter(this, weekActive)
            setTitle()
        }
    }

    override fun onResume() {
        super.onResume()

        DateManager.create()
        date_tasks.adapter = WeekAdapter(this, weekActive)
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

    // Internal Classes ----------------------------------------------------------------------------
    class WeekAdapter(activity: WeekActivity, date_active: Int):
            TaskAdapterInterface(activity, activity, DateManager.getWeek(date_active).getAllTasks())
}
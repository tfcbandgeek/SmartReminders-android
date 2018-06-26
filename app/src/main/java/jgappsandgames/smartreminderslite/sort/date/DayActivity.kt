package jgappsandgames.smartreminderslite.sort.date

// Java
import android.app.Activity
import android.content.Intent
import java.util.Calendar

// Android OS
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

// View
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.adapter.TaskAdapterInterface
import jgappsandgames.smartreminderslite.holder.TaskFolderHolder
import jgappsandgames.smartreminderslite.home.FirstRun
import jgappsandgames.smartreminderslite.utility.OptionsUtility

// Save
import jgappsandgames.smartreminderssave.MasterManager
import jgappsandgames.smartreminderssave.date.DateManager
import jgappsandgames.smartreminderssave.utility.FileUtility
import kotlinx.android.synthetic.main.activity_date.*

/**
 * DayActivity
 * Created by joshua on 1/1/2018.
 */
class DayActivity: Activity(), TaskFolderHolder.OnTaskChangedListener {
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
            date_tasks.adapter = DayAdapter(this, dateActivity)
            setTitle()
        }

        date_next.setOnClickListener {
            dateActivity.add(Calendar.DAY_OF_MONTH, 1)
            date_tasks.adapter = DayAdapter(this, dateActivity)
            setTitle()
        }
    }

    override fun onResume() {
        super.onResume()

        DateManager.create()
        date_tasks.adapter = DayAdapter(this, dateActivity)
    }

    // Menu ----------------------------------------------------------------------------------------
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_auxilary, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return OptionsUtility.onOptionsItemSelected(this, item, object: OptionsUtility.Save {
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

    // Internal Classes ----------------------------------------------------------------------------
    class DayAdapter(activity: DayActivity, date_active: Calendar):
            TaskAdapterInterface(activity, activity, DateManager.getDayTasks(date_active))
}
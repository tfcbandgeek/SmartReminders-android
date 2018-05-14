package jgappsandgames.smartreminderslite.home

// Android OS
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

// Views
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.adapter.TaskAdapterInterface

// Anko
import org.jetbrains.anko.toast

// App
import jgappsandgames.smartreminderslite.holder.TaskFolderHolder
import jgappsandgames.smartreminderslite.sort.date.DayActivity
import jgappsandgames.smartreminderslite.sort.date.MonthActivity
import jgappsandgames.smartreminderslite.sort.date.WeekActivity
import jgappsandgames.smartreminderslite.sort.priority.PriorityActivity
import jgappsandgames.smartreminderslite.sort.status.StatusActivity
import jgappsandgames.smartreminderslite.sort.tags.TagActivity
import jgappsandgames.smartreminderslite.tasks.TaskActivity
import jgappsandgames.smartreminderslite.utility.ActivityUtility

// KotlinX
import kotlinx.android.synthetic.main.activity_home.home_fab
import kotlinx.android.synthetic.main.activity_home.home_tasks_list

// Save Library
import jgappsandgames.smartreminderssave.MasterManager
import jgappsandgames.smartreminderssave.tasks.Task
import jgappsandgames.smartreminderssave.tasks.TaskManager
import jgappsandgames.smartreminderssave.utility.FileUtility

/**
 * HomeActivity
 * Created by joshua on 12/13/2017.
 */
class HomeActivity: Activity(), TaskFolderHolder.OnTaskChangedListener {
    // LifeCycle Methods ---------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setTitle(R.string.app_name)

        // Load the FilePaths
        FileUtility.loadFilePaths(this)

        // Handle First Run
        if (FileUtility.isFirstRun()) startActivity(Intent(this, FirstRun::class.java))

        // Handle Loading Data
        else MasterManager.load()

        // Set Click Listeners
        home_fab.setOnClickListener {
            val task = Task("home", Task.TYPE_TASK).save()

            TaskManager.home.add(task.getFilename())
            TaskManager.tasks.add(task.getFilename())
            TaskManager.save()

            // Start Activity
            startActivity(Intent(this, TaskActivity::class.java)
                    .putExtra(ActivityUtility.TASK_NAME, task.getFilename()))
        }

        home_fab.setOnLongClickListener {
            val task = Task("home", Task.TYPE_FLDR).save()

            TaskManager.home.add(task.getFilename())
            TaskManager.tasks.add(task.getFilename())
            TaskManager.save()

            // Start Activity
            startActivity(Intent(this, TaskActivity::class.java)
                    .putExtra(ActivityUtility.TASK_NAME, task.getFilename()))
            return@setOnLongClickListener true
        }
    }

    /**
     * OnResume
     *
     * Called To Reload Changeable Data
     * Called by the Application
     */
    override fun onResume() {
        super.onResume()
        home_tasks_list.adapter = HomeAdapter(this)
    }

    /**
     * OnPause
     *
     * Called to Pause the Activity
     * Called by the Application
     */
    override fun onPause() {
        super.onPause()

        save()
    }

    // Menu Methods --------------------------------------------------------------------------------
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId) {
            R.id.tags -> {
                startActivity(Intent(this, TagActivity::class.java))
                return true
            }

            R.id.status -> {
                startActivity(Intent(this, StatusActivity::class.java))
                return true
            }

            R.id.priority -> {
                startActivity(Intent(this, PriorityActivity::class.java))
                return true
            }

            R.id.day -> {
                startActivity(Intent(this, DayActivity::class.java))
                return true
            }

            R.id.week -> {
                startActivity(Intent(this, WeekActivity::class.java))
                return true
            }

            R.id.month -> {
                startActivity(Intent(this, MonthActivity::class.java))
                return true
            }

            R.id.settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                return true
            }

           R.id.about -> {
                startActivity(Intent (this, AboutActivity::class.java))
                return true
            }

            R.id.save -> {
                save ()
                toast(R.string.saved).show()
                return true
            }

            R.id.close -> {
                finish()
                return true
            }
        }

        return false
    }

    // Task Listener -------------------------------------------------------------------------------
    /**
     * OnTaskChanged
     *
     * Handles the Instance Where A Task Held in This List is Changed
     */
    override fun onTaskChanged() {
        onResume()
    }

    // Auxiliary Methods ---------------------------------------------------------------------------
    /**
     * Save
     *
     * Method That Handles Any and All Data That May Have Changed
     */
    fun save() {
        MasterManager.save()
    }

    // Internal Classes ----------------------------------------------------------------------------
    class HomeAdapter(activity: HomeActivity): TaskAdapterInterface(activity, activity, TaskManager.getHomeTasks())
}
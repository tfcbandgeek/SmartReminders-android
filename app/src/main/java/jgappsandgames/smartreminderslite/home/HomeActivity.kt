package jgappsandgames.smartreminderslite.home

// Android OS
import android.app.Activity
import android.content.Intent
import android.os.Bundle

// Views
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.adapter.TaskAdapterInterface

// App
import jgappsandgames.smartreminderslite.holder.TaskFolderHolder
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
    /**
     * OnCreate
     *
     * Called to Create the View
     * Called by the Application
     */
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
            val task = Task("home", Task.TYPE_TASK)
            // TODO: Save  returns task and combine with previous statement
            task.save()

            TaskManager.home.add(task.getFilename())
            TaskManager.tasks.add(task.getFilename())
            TaskManager.save()

            // Start Activity
            startActivity(Intent(this, TaskActivity::class.java)
                    .putExtra(ActivityUtility.TASK_NAME, task.getFilename()))
        }

        home_fab.setOnLongClickListener {
            val task = Task("home", Task.TYPE_FLDR)
            // TODO: Save  returns task and combine with previous statement
            task.save()

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
    class HomeAdapter(activity: HomeActivity): TaskAdapterInterface(activity, activity, TaskManager.home, null)
}
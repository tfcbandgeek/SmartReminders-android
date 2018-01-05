package jgappsandgames.smartreminderslite.home

// Android OS
import android.content.Intent
import android.os.Bundle

// Views
import android.view.View

// Open Log
import me.jgappsandgames.openlog.Log

// App
import jgappsandgames.smartreminderslite.holder.TaskFolderHolder
import jgappsandgames.smartreminderslite.tasks.TaskActivity
import jgappsandgames.smartreminderslite.utility.ActivityUtility

// Save Library
import jgappsandgames.smartreminderssave.MasterManager
import jgappsandgames.smartreminderssave.tasks.Task
import jgappsandgames.smartreminderssave.tasks.TaskManager
import jgappsandgames.smartreminderssave.utility.FileUtility

/**
 * HomeActivity
 * Created by joshua on 12/13/2017.
 *
 * Home[HomeActivityInterface, HomeActivity]
 * The Main Entrypoint for the Application.  It serves as the Primary Test to see if it is the First
 *     run and Direct the User Where they Need To Go.
 *
 *     HomeActivityInterface: View and Application Focus
 *     HomeActivity: Data and User Input
 */
class HomeActivity: HomeActivityInterface(), TaskFolderHolder.OnTaskChangedListener {
    // LifeCycle Methods ---------------------------------------------------------------------------
    /**
     * OnCreate
     *
     * Called to Create the View
     * Called by the Application
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("HomeActivity", "OnCreate Called")

        // Load the FilePaths
        FileUtility.loadFilePaths(this)

        // Handle First Run
        if (FileUtility.isFirstRun()) startActivity(Intent(this, FirstRun::class.java))

        // Handle Loading Data
        else MasterManager.load(this)
    }

    /**
     * OnResume
     *
     * Called To Reload Changeable Data
     * Called by the Application
     */
    override fun onResume() {
        super.onResume()
        Log.d("HomeActivity", "OnResume Called")

        // Set Adapters
        adapter = HomeAdapter(this)
        list!!.adapter = adapter
    }

    /**
     * OnPause
     *
     * Called to Pause the Activity
     * Called by the Application
     */
    override fun onPause() {
        super.onPause()
        Log.d("HomeActivity", "OnPause Called")

        save()
    }

    // Click Listeners -----------------------------------------------------------------------------
    /**
     * OnClick
     *
     * The Click Listener For This Class
     *     [FAB] Called to Create a Task
     */
    override fun onClick(view: View) {
        Log.d("HomeActivity", "OnClick Called")

        // FAB
        if (view == fab) {
            val task = Task("home", Task.TYPE_TASK)
            task.save()

            TaskManager.home.add(task.getFilename())
            TaskManager.tasks.add(task.getFilename())
            TaskManager.save()

            // Create Intent
            val intent = Intent(this, TaskActivity::class.java)
            intent.putExtra(ActivityUtility.TASK_NAME, task.getFilename())

            // Start Activity
            startActivity(intent)
        }
    }

    /**
     * OnLongClick
     *
     * The Long Click Listener For This Class
     *     [FAB] Called to Create a Folder
     */
    override fun onLongClick(view: View): Boolean {
        Log.d("HomeActivity", "OnLongClick Called")

        // FAB
        if (view == fab) {
            val task = Task("home", Task.TYPE_FLDR)
            task.save()

            TaskManager.home.add(task.getFilename())
            TaskManager.tasks.add(task.getFilename())
            TaskManager.save()

            // Create Intent
            val intent = Intent(this, TaskActivity::class.java)
            intent.putExtra(ActivityUtility.TASK_NAME, task.getFilename())

            // Start Activity
            startActivity(intent)
            return true
        }

        // Default
        return false
    }

    // Task Listener -------------------------------------------------------------------------------
    /**
     * OnTaskChanged
     *
     * Handles the Instance Where A Task Held in This List is Changed
     */
    override fun onTaskChanged() {
        Log.d("HomeActivity", "OnTaskChanged Called")
        onResume()
    }

    // Auxiliary Methods ---------------------------------------------------------------------------
    /**
     * Save
     *
     * Method That Handles Any and All Data That May Have Changed
     */
    override fun save() {
        Log.d("HomeActivity", "Save Called")
        MasterManager.save()
    }
}
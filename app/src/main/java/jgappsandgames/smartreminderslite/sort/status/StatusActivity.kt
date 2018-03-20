package jgappsandgames.smartreminderslite.sort.status

// Android OS
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle

// App
import jgappsandgames.smartreminderslite.home.FirstRun

// Save
import jgappsandgames.smartreminderssave.MasterManager
import jgappsandgames.smartreminderssave.status.StatusManager
import jgappsandgames.smartreminderssave.tasks.Task
import jgappsandgames.smartreminderssave.utility.FileUtility

/**
 * StatusActivity
 * Created by joshua on 12/14/2017.
 *
 * Status View[StatusActivityInterface, StatusActivity]
 * A View for Viewing Tasks Based on Their Status
 *
 *     StatusActivityInterface: View and Application Focus
 *     StatusActivity: Data and User Input
 */
class StatusActivity: StatusActivityInterface() {
    // LifeCycle Methods ---------------------------------------------------------------------------
    /**
     * OnCreate
     *
     * Called To Create The View
     * Called By The Application
     */
    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load Filepaths
        FileUtility.loadFilePaths(this)

        // First Run
        if (FileUtility.isFirstRun()) startActivity(Intent(this, FirstRun::class.java))

        // Normal Run
        else MasterManager.load()
    }

    /**
     * OnResume
     *
     * Called To Reset The Adapters and Draw The View
     * Called By The Application
     */
    override fun onResume() {
        super.onResume()

        // Create Status Manager
        StatusManager.create()

        val i = ArrayList<Task>()
        i.addAll(StatusManager.getInProgress())
        i.addAll(StatusManager.getIncomplete())

        // Set Adapters
        overdue_list!!.adapter = StatusAdapter(this, StatusManager.getOverdue())
        incomplete_list!!.adapter = StatusAdapter(this, i)
        done_list!!.adapter = StatusAdapter(this, StatusManager.getCompleted())

        // Set Text
        overdue_text!!.text = "Overdue Tasks [${StatusManager.getOverdue().size}]"
        incomplete_text!!.text = "Incomplete Tasks [${i.size}]"
        done_text!!.text = "Completed Tasks [${StatusManager.getCompleted().size}]"
    }

    // Task Change Listeners -----------------------------------------------------------------------
    /**
     * OnTaskChanged
     *
     * Called When a Task in This Activity is Changed
     */
    override fun onTaskChanged() {
        onResume()
    }

    // Parent Methods ------------------------------------------------------------------------------
    /**
     * Save
     *
     * Called to Save Any Information That May of Changed
     */
    override fun save() {
        MasterManager.save()
    }
}
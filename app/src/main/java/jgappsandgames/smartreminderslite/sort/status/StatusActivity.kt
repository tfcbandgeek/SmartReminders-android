package jgappsandgames.smartreminderslite.sort.status

// Android OS
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import jgappsandgames.smartreminderslite.R

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
 * StatusActivity
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

        // Load Filepath
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
        overdueList!!.adapter = StatusAdapter(this, StatusManager.getOverdue())
        incompleteList!!.adapter = StatusAdapter(this, i)
        completeList!!.adapter = StatusAdapter(this, StatusManager.getCompleted())

        // Set Text
        overdueText!!.text = getString(R.string.overdue_tasks, StatusManager.getOverdue().size)
        incompleteText!!.text = getString(R.string.incomplete_tasks, i.size)
        completeText!!.text = getString(R.string.completed_tasks, StatusManager.getCompleted().size)
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
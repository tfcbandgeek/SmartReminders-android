package jgappsandgames.smartreminderslite.home

// Android OS
import android.app.Activity
import android.content.Intent
import android.os.Bundle

// Views

import android.view.Menu
import android.view.MenuItem
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.adapter.TaskAdapterInterface

// App
import jgappsandgames.smartreminderslite.holder.TaskFolderHolder
import jgappsandgames.smartreminderslite.tasks.TaskActivity
import jgappsandgames.smartreminderslite.utility.ActivityUtility
import jgappsandgames.smartreminderslite.utility.OptionsUtility

// KotlinX
import kotlinx.android.synthetic.main.activity_home.home_add_folder
import kotlinx.android.synthetic.main.activity_home.home_add_task
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

        // Handle the Data
        FileUtility.loadFilePaths(this)
        if (FileUtility.isFirstRun()) startActivity(Intent(this, FirstRun::class.java))
        else MasterManager.load()

        // Set Click Listeners
        home_add_task.setOnClickListener {
            startActivity(Intent(this, TaskActivity::class.java)
                    .putExtra(ActivityUtility.TASK_NAME,
                            TaskManager.addTask(Task("home", Task.TYPE_TASK).save(), true).getFilename()))
        }

        home_add_folder.setOnClickListener {
            startActivity(Intent(this, TaskActivity::class.java)
                    .putExtra(ActivityUtility.TASK_NAME,
                            TaskManager.addTask(Task("home", Task.TYPE_FLDR).save(), true).getFilename()))
        }
    }

    override fun onResume() {
        super.onResume()
        home_tasks_list.adapter = HomeAdapter(this)
    }

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
        return OptionsUtility.onOptionsItemSelected(this, item!!, object: OptionsUtility.Save {
            override fun save() {
                this@HomeActivity.save()
            }
        })
    }

    // Task Listener -------------------------------------------------------------------------------
    override fun onTaskChanged() {
        onResume()
    }

    // Auxiliary Methods ---------------------------------------------------------------------------
    fun save() {
        MasterManager.save()
    }

    // Internal Classes ----------------------------------------------------------------------------
    class HomeAdapter(activity: HomeActivity): TaskAdapterInterface(activity, activity, TaskManager.home, null)
}
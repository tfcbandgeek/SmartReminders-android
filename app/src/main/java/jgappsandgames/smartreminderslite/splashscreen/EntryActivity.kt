package jgappsandgames.smartreminderslite.splashscreen

// Android OS
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout

// App
import jgappsandgames.smartreminderslite.home.FirstRun
import jgappsandgames.smartreminderslite.home.HomeActivity
import jgappsandgames.smartreminderslite.main.PlannerHomeActivity
import jgappsandgames.smartreminderslite.main.TaskListHomeActivity
import jgappsandgames.smartreminderslite.utility.ActivityUtility

// Save
import jgappsandgames.smartreminderssave.MasterManager
import jgappsandgames.smartreminderssave.settings.SettingsManager
import jgappsandgames.smartreminderssave.utility.FileUtility
import org.jetbrains.anko.alert
import org.jetbrains.anko.customView
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.textView

/**
 * EntryActivity
 * The New Entrypoint into the App.
 *
 * Will notify the user and ask if the want to upgrade their saved files, or what version of the save System they want to
 * use if it is their first time running the App.  Finally it will ask what HomeScreen they Want to use.
 * Created by joshua on 2/27/2018.
 */
class EntryActivity: Activity() {
    // Lifecycle Methods ---------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setup the Log
        MasterManager.loadLog(applicationContext)

        // Load the FilePaths
        FileUtility.loadFilePaths(this)

        // Handle First Run
        if (FileUtility.isFirstRun()) startActivity(Intent(this, FirstRun::class.java))

        // Handle Loading Data
        else MasterManager.load()

        // Handle Pre 12 Smart Reminders
        if (SettingsManager.api_level <= 11) {
            alert {
                title = "Update Smart Reminders?"
                customView {
                    linearLayout {
                        orientation = LinearLayout.VERTICAL
                        textView {
                            text = "By upgrading Smart Reminders to version 12 you will be losing the ability to use the app with prior versions of the app or other apps that use the save system."
                        }
                        textView {
                            text = "If you would like to continue using Smart Reminders without problems you need to make sure you update all the apps using the save file at the same time."
                        }
                    }
                }
                positiveButton("Update Save", {
                    SettingsManager.api_level = 12
                    SettingsManager.save()
                    startActivity(Intent(this@EntryActivity, TaskListHomeActivity::class.java).putExtra(ActivityUtility.HOME_OPTION, ActivityUtility.HOME_PLANNER))
                })
                negativeButton("Maybe Later", {
                    startActivity(Intent(this@EntryActivity, HomeActivity::class.java))
                })
            }.show()
        }

        // Handle 12 Smart Reminders
        when (SettingsManager.home_screen) {
            SettingsManager.PLANNER_HOME -> startActivity(Intent(this, PlannerHomeActivity::class.java).putExtra(ActivityUtility.HOME_OPTION, ActivityUtility.HOME_PLANNER))
            SettingsManager.NO_HOME -> startActivity(Intent(this, TaskListHomeActivity::class.java).putExtra(ActivityUtility.HOME_OPTION, ActivityUtility.HOME_TASK))
            SettingsManager.TASK_HOME -> startActivity(Intent(this, TaskListHomeActivity::class.java).putExtra(ActivityUtility.HOME_OPTION, ActivityUtility.HOME_TASK))
            SettingsManager.ALL_HOME -> startActivity(Intent(this, TaskListHomeActivity::class.java).putExtra(ActivityUtility.HOME_OPTION, ActivityUtility.HOME_ALL))
            SettingsManager.DAY_HOME -> startActivity(Intent(this, TaskListHomeActivity::class.java).putExtra(ActivityUtility.HOME_OPTION, ActivityUtility.HOME_DAY))
            SettingsManager.WEEK_HOME -> startActivity(Intent(this, TaskListHomeActivity::class.java).putExtra(ActivityUtility.HOME_OPTION, ActivityUtility.HOME_WEEK))
            SettingsManager.MONTH_HOME -> startActivity(Intent(this, TaskListHomeActivity::class.java).putExtra(ActivityUtility.HOME_OPTION, ActivityUtility.HOME_MONTH))
            SettingsManager.STATUS_HOME -> startActivity(Intent(this, TaskListHomeActivity::class.java).putExtra(ActivityUtility.HOME_OPTION, ActivityUtility.HOME_STATUS))
            SettingsManager.TAG_HOME -> startActivity(Intent(this, TaskListHomeActivity::class.java).putExtra(ActivityUtility.HOME_OPTION, ActivityUtility.HOME_TAG))
            SettingsManager.PRIORITY_HOME -> startActivity(Intent(this, TaskListHomeActivity::class.java).putExtra(ActivityUtility.HOME_OPTION, ActivityUtility.HOME_PRIORITY))
        }

        finish()
    }
}
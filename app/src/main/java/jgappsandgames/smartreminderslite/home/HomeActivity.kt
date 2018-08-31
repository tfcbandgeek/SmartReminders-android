package jgappsandgames.smartreminderslite.home

// Android OS
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher

// Views
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import jgappsandgames.smartreminderslite.BuildConfig

// Anko
import org.jetbrains.anko.sdk25.coroutines.onClick

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.adapter.TaskAdapter
import jgappsandgames.smartreminderslite.sort.DayActivity
import jgappsandgames.smartreminderslite.sort.MonthActivity
import jgappsandgames.smartreminderslite.sort.WeekActivity
import jgappsandgames.smartreminderslite.sort.PriorityActivity
import jgappsandgames.smartreminderslite.sort.StatusActivity
import jgappsandgames.smartreminderslite.sort.TagActivity
import jgappsandgames.smartreminderslite.tasks.TaskActivity
import jgappsandgames.smartreminderslite.utility.FIRST_RUN
import jgappsandgames.smartreminderslite.utility.Save
import jgappsandgames.smartreminderslite.utility.TASK_NAME
import jgappsandgames.smartreminderslite.utility.onOptionsItemSelected

// KotlinX

// Save Library
import jgappsandgames.smartreminderssave.MasterManager
import jgappsandgames.smartreminderssave.settings.SettingsManager
import jgappsandgames.smartreminderssave.tasks.Task
import jgappsandgames.smartreminderssave.tasks.TaskManager
import jgappsandgames.smartreminderssave.utility.FileUtility
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.*

/**
 * HomeActivity
 * Created by joshua on 12/13/2017.
 */
class HomeActivity: Activity(), TaskAdapter.OnTaskChangedListener {
    private var alert: DialogInterface? = null

    // LifeCycle Methods ---------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setTitle(R.string.app_name)

        // Handle the Data
        FileUtility.loadFilePaths(this)
        if (FileUtility.isFirstRun()) startActivity(Intent(this, Settings2Activity::class.java).putExtra(FIRST_RUN, true))
        else MasterManager.load()

        // Handle Info
        if (SettingsManager.getLastVersionSplash() < BuildConfig.VERSION_CODE) {
            SettingsManager.displayedSplash(BuildConfig.VERSION_CODE)
            runStartDialog()
        }

        // Set Click Listeners
        home_add_task.setOnClickListener { _ ->
            home_fab.close(true)
            startActivity(Intent(this, TaskActivity::class.java)
                    .putExtra(TASK_NAME, TaskManager.addTask(Task("home", Task.TYPE_TASK).save(), true).getFilename()))
        }

        home_add_folder.setOnClickListener { _ ->
            home_fab.close(true)
            startActivity(Intent(this, TaskActivity::class.java)
                    .putExtra(TASK_NAME, TaskManager.addTask(Task("home", Task.TYPE_FOLDER).save(), true).getFilename()))
        }

        home_add_note.setOnClickListener { _ ->
            home_fab.close(true)
            startActivity(Intent(this, TaskActivity::class.java)
                    .putExtra(TASK_NAME, TaskManager.addTask(Task("home", Task.TYPE_NOTE).save(), true).getFilename()))
        }

        home_bottom_bar_search.setOnClickListener { _ ->
            if (home_bottom_bar_search_text.visibility == View.VISIBLE) searchVisibility(false)
            else searchVisibility(true)
        }

        home_bottom_bar_more.setOnClickListener { _ -> tagDialog() }

        home_bottom_bar_search_text.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (home_bottom_bar_search_text.visibility == View.VISIBLE) home_tasks_list.adapter =
                        TaskAdapter(this@HomeActivity, this@HomeActivity, TaskManager.getHome(), home_bottom_bar_search_text.text.toString())
            }
        })
    }

    override fun onResume() {
        super.onResume()
        home_tasks_list.adapter = TaskAdapter(this, this, TaskManager.getHome(), "")

        try {
            alert?.dismiss()
        } catch(e: Exception) {
        }
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
        return onOptionsItemSelected(this, item!!, object: Save {
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

    // Dialog Methods ------------------------------------------------------------------------------
    fun tagDialog() {
        alert = alert {
            title = this@HomeActivity.getString(R.string.sort_options)

            customView {
                verticalLayout {
                    button {
                        text = context.getString(R.string.sort_by_tags)
                        onClick {
                            startActivity(Intent(this@HomeActivity, TagActivity::class.java))
                        }
                    }.lparams(matchParent, wrapContent)

                    button {
                        text = context.getString(R.string.sort_by_status)
                        onClick {
                            startActivity(Intent(this@HomeActivity, StatusActivity::class.java))
                        }
                    }.lparams(matchParent, wrapContent)

                    button {
                        text = context.getString(R.string.sort_by_priority)
                        onClick {
                            startActivity(Intent(this@HomeActivity, PriorityActivity::class.java))
                        }
                    }.lparams(matchParent, wrapContent)

                    button {
                        text = context.getString(R.string.sort_by_days)
                        onClick {
                            startActivity(Intent(this@HomeActivity, DayActivity::class.java))
                        }
                    }.lparams(matchParent, wrapContent)

                    button {
                        text = context.getString(R.string.sort_by_week)
                        onClick {
                            startActivity(Intent(this@HomeActivity, WeekActivity::class.java))
                        }
                    }.lparams(matchParent, wrapContent)

                    button {
                        text = context.getString(R.string.sort_by_month)
                        onClick {
                            startActivity(Intent(this@HomeActivity, MonthActivity::class.java))
                        }
                    }.lparams(matchParent, wrapContent)
                }.layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
            }
        }.show()
    }

    fun runStartDialog() {
        alert {
            title = "What is new in 18.09.1"

            customView {
                verticalLayout {
                    textView("  - What's new Screen")
                    textView("  - Reduced Download and install size")
                    textView("  - Updated Save Defaults")
                    textView("  - General Fixes")
                }
            }

            okButton {  }
        }.show()
    }

    // Search Methods ------------------------------------------------------------------------------
    private fun searchVisibility(visible: Boolean = true) {
        if (visible) {
            home_bottom_bar_search_text.visibility = View.VISIBLE
            home_bottom_bar_search_text.setText("")
            home_tasks_list.adapter = TaskAdapter(this, this, TaskManager.getHome(), "")
        } else {
            home_bottom_bar_search_text.visibility = View.INVISIBLE
            home_bottom_bar_search_text.setText("")
            home_tasks_list.adapter = TaskAdapter(this, this, TaskManager.getHome(), "")
        }
    }
}
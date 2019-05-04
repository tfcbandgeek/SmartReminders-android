package jgappsandgames.smartreminderslite.home

// Android OS
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher

// Views
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity

// Anko
import org.jetbrains.anko.alert
import org.jetbrains.anko.button
import org.jetbrains.anko.customView
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.verticalLayout
import org.jetbrains.anko.wrapContent
import org.jetbrains.anko.sdk25.coroutines.onClick

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.adapter.TaskAdapter
import jgappsandgames.smartreminderslite.utility.*

// KotlinX
import kotlinx.android.synthetic.main.activity_home.*

// Save Library
import jgappsandgames.smartreminderssave.MasterManager
import jgappsandgames.smartreminderssave.tasks.Task
import jgappsandgames.smartreminderssave.tasks.TaskManager

/**
 * HomeActivity
 * Created by joshua on 12/13/2017.
 */
class HomeActivity: AppCompatActivity(), TaskAdapter.OnTaskChangedListener {
    // LifeCycle Methods ---------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setTitle(R.string.app_name)

        // Handle the Data
        loadClass(this)

        // Set Click Listeners
        home_add_task.setOnClickListener {
            home_fab.close(true)
            startActivity(buildTaskIntent(this, IntentOptions(), TaskOptions(task = TaskManager.addTask(TaskManager.taskPool.getPoolObject().load("home", Task.TYPE_TASK).save(), true))))
        }

        home_add_folder.setOnClickListener {
            home_fab.close(true)
            startActivity(buildTaskIntent(this, IntentOptions(), TaskOptions(task = TaskManager.addTask(TaskManager.taskPool.getPoolObject().load("home", Task.TYPE_FOLDER).save(), true))))
        }

        home_bottom_bar_search.setOnClickListener {
            if (home_bottom_bar_search_text.visibility == View.VISIBLE) searchVisibility(false)
            else searchVisibility(true)
        }

        home_bottom_bar_more.setOnClickListener {
            alert {
                title = this@HomeActivity.getString(R.string.sort_options)

                customView {
                    verticalLayout {
                        button {
                            text = context.getString(R.string.sort_by_tags)
                            onClick { startActivity(buildTagsIntent(this@HomeActivity, IntentOptions())) }
                        }.lparams(matchParent, wrapContent)

                        button {
                            text = context.getString(R.string.sort_by_status)
                            onClick { startActivity(buildStatusIntent(this@HomeActivity, IntentOptions())) }
                        }.lparams(matchParent, wrapContent)

                        button {
                            text = context.getString(R.string.sort_by_priority)
                            onClick { startActivity(buildPriorityIntent(this@HomeActivity, IntentOptions())) }
                        }.lparams(matchParent, wrapContent)

                        button {
                            text = context.getString(R.string.sort_by_days)
                            onClick { startActivity(buildDayIntent(this@HomeActivity, IntentOptions())) }
                        }.lparams(matchParent, wrapContent)

                        button {
                            text = context.getString(R.string.sort_by_week)
                            onClick { startActivity(buildWeekIntent(this@HomeActivity, IntentOptions())) }
                        }.lparams(matchParent, wrapContent)

                        button {
                            text = context.getString(R.string.sort_by_month)
                            onClick { startActivity(buildMonthIntent(this@HomeActivity, IntentOptions())) }
                        }.lparams(matchParent, wrapContent)
                    }.layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
                }
            }.show()
        }

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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = onOptionsItemSelected(this, item!!, object: Save { override fun save() = this@HomeActivity.save() })

    // Task Listener -------------------------------------------------------------------------------
    override fun onTaskChanged() = onResume()

    // Auxiliary Methods ---------------------------------------------------------------------------
    fun save() = MasterManager.save()

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
package jgappsandgames.smartreminderslite.sort

// Android OS
import android.app.Activity
import android.content.Intent
import android.os.Bundle

// Views
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.BaseAdapter

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.adapter.TaskAdapter
import jgappsandgames.smartreminderslite.home.Settings2Activity
import jgappsandgames.smartreminderslite.utility.FIRST_RUN
import jgappsandgames.smartreminderslite.utility.Save
import jgappsandgames.smartreminderslite.utility.onOptionsItemSelected

// KotlinX
import kotlinx.android.synthetic.main.activity_priority.priority_down
import kotlinx.android.synthetic.main.activity_priority.priority_tasks
import kotlinx.android.synthetic.main.activity_priority.priority_up

// Save
import jgappsandgames.smartreminderssave.MasterManager
import jgappsandgames.smartreminderssave.priority.PriorityManager
import jgappsandgames.smartreminderssave.utility.FileUtility

/**
 * PriorityActivity
 * Created by joshua on 12/14/2017.
 */
class PriorityActivity: Activity(), TaskAdapter.OnTaskChangedListener {
    // Data ----------------------------------------------------------------------------------------
    private var position: Int = 3

    // Adapters ------------------------------------------------------------------------------------
    private var folder: BaseAdapter? = null
    private var ignore: BaseAdapter? = null
    private var low: BaseAdapter? = null
    private var normal: BaseAdapter? = null
    private var high: BaseAdapter? = null
    private var stared: BaseAdapter? = null

    // LifeCycle Methods ---------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_priority)

        // Handle Data
        FileUtility.loadFilePaths(this)
        if (FileUtility.isFirstRun()) startActivity(Intent(this, Settings2Activity::class.java).putExtra(FIRST_RUN, true))
        else MasterManager.load()

        // Click Listeners
        priority_down.setOnClickListener {
            moveDown()
        }

        priority_up.setOnClickListener {
            moveUp()
        }
    }

    override fun onResume() {
        super.onResume()

        // Load Priority Manager
        PriorityManager.create()

        // Reset Adapters
        folder = null
        ignore = null
        low = null
        normal = null
        high = null
        stared = null

        // Set View
        setTitle()
    }

    override fun onPause() {
        super.onPause()
        save()
    }

    // Menu Methods --------------------------------------------------------------------------------
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_auxilary, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return onOptionsItemSelected(this, item, object: Save {
            override fun save() {
                this@PriorityActivity.save()
            }
        })
    }

    // Task Listeners ------------------------------------------------------------------------------
    override fun onTaskChanged() {
        onResume()
    }

    // Private Class Methods -----------------------------------------------------------------------
    private fun setTitle() {
        when (position) {
            0 -> {
                title = "Folders"
                priority_down.text = "Low"
                priority_up.setText(R.string.low)
                if (ignore == null) ignore = TaskAdapter(this, this, TaskAdapter.swapTasks(PriorityManager.getIgnored()), "")
                priority_tasks.adapter = ignore
                return
            }

            1 -> {
                title = getString(R.string.ignore)
                priority_down.text = "Folders"
                priority_up.setText(R.string.low)
                if (ignore == null) ignore = TaskAdapter(this, this, TaskAdapter.swapTasks(PriorityManager.getIgnored()), "")
                priority_tasks.adapter = ignore
                return
            }

            2 -> {
                title = getString(R.string.low_priority_default)
                priority_down.setText(R.string.ignore)
                priority_up.setText(R.string.normal)
                if (low == null) low = TaskAdapter(this, this, TaskAdapter.swapTasks(PriorityManager.getLow()), "")
                priority_tasks.adapter = low
                return
            }

            3 -> {
                title = getString(R.string.normal_priority)
                priority_down.setText(R.string.low)
                priority_up.setText(R.string.high)
                if (normal == null) normal = TaskAdapter(this, this, TaskAdapter.swapTasks(PriorityManager.getNormal()), "")
                priority_tasks.adapter = normal
                return
            }

            4 -> {
                title = getString(R.string.high_priority)
                priority_down.setText(R.string.normal)
                priority_up.setText(R.string.stared)
                if (high == null) high = TaskAdapter(this, this, TaskAdapter.swapTasks(PriorityManager.getHigh()), "")
                priority_tasks.adapter = high
                return
            }

            5 -> {
                title = getString(R.string.stared_tasks)
                priority_down.setText(R.string.high)
                priority_up.text = ""
                if (stared == null) stared = TaskAdapter(this, this, TaskAdapter.swapTasks(PriorityManager.getStared()), "")
                priority_tasks.adapter = stared
            }
        }
    }

    private fun moveUp() {
        when (position) {
            1 -> {
                position = 2
                priority_down.visibility = View.VISIBLE
                setTitle()
            }

            2 -> {
                position = 3
                setTitle()
            }

            3 -> {
                position = 4
                setTitle()
            }

            4 -> {
                position = 5
                priority_up.visibility = View.INVISIBLE
                setTitle()
            }
        }
    }

    /**
     * MoveDown
     *
     * Called To Move The Current Selection Down By One
     */
    private fun moveDown() {
        when (position) {
            2 -> {
                position = 1
                priority_down.visibility = View.INVISIBLE
                setTitle()
            }

            3 -> {
                position = 2
                setTitle()
            }

            4 -> {
                position = 3
                setTitle()
            }

            5 -> {
                position = 4
                priority_up.visibility = View.VISIBLE
                setTitle()
            }
        }
    }

    // Parent Overrides ----------------------------------------------------------------------------
    fun save() {
        MasterManager.save()
    }
}
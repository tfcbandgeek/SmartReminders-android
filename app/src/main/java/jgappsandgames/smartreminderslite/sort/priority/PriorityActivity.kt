package jgappsandgames.smartreminderslite.sort.priority

// Android OS
import android.app.Activity
import android.content.Intent

// Views
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.BaseAdapter

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.adapter.TaskAdapterInterface
import jgappsandgames.smartreminderslite.holder.TaskFolderHolder
import jgappsandgames.smartreminderslite.home.FirstRun
import jgappsandgames.smartreminderslite.utility.OptionsUtility

// Save
import jgappsandgames.smartreminderssave.MasterManager
import jgappsandgames.smartreminderssave.priority.PriorityManager
import jgappsandgames.smartreminderssave.tasks.Task
import jgappsandgames.smartreminderssave.utility.FileUtility
import kotlinx.android.synthetic.main.activity_priority.*

/**
 * PriorityActivity
 * Created by joshua on 12/14/2017.
 *
 * Priority[PriorityActivityInterface, PriorityActivity]
 * A View for Viewing Tasks Based on Their Priority
 *
 *     PriorityActivityInterface: View and Application Focus
 *     PriorityActivity: Data and User Input
 */
class PriorityActivity:
        Activity(), TaskFolderHolder.OnTaskChangedListener {
    // Data ----------------------------------------------------------------------------------------
    private var position: Int = 3

    // Adapters ------------------------------------------------------------------------------------
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
        if (FileUtility.isFirstRun()) startActivity(Intent(this, FirstRun::class.java))
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
        return OptionsUtility.onOptionsItemSelected(this, item, object: OptionsUtility.Save {
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
            1 -> {
                title = getString(R.string.ignore)
                priority_down.text = ""
                priority_up.setText(R.string.low)
                if (ignore == null) ignore = PriorityAdapter(this, PriorityManager.getIgnored())
                priority_tasks.adapter = ignore
                return
            }

            2 -> {
                title = getString(R.string.low_priority_default)
                priority_down.setText(R.string.ignore)
                priority_up.setText(R.string.normal)
                if (low == null) low = PriorityAdapter(this, PriorityManager.getLow())
                priority_tasks.adapter = low
                return
            }

            3 -> {
                title = getString(R.string.normal_priority)
                priority_down.setText(R.string.low)
                priority_up.setText(R.string.high)
                if (normal == null) normal = PriorityAdapter(this, PriorityManager.getNormal())
                priority_tasks.adapter = normal
                return
            }

            4 -> {
                title = getString(R.string.high_priority)
                priority_down.setText(R.string.normal)
                priority_up.setText(R.string.stared)
                if (high == null) high = PriorityAdapter(this, PriorityManager.getHigh())
                priority_tasks.adapter = high
                return
            }

            5 -> {
                title = getString(R.string.stared_tasks)
                priority_down.setText(R.string.high)
                priority_up.text = ""
                if (stared == null) stared = PriorityAdapter(this, PriorityManager.getStared())
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

    // Internal Classes ----------------------------------------------------------------------------
    class PriorityAdapter(activity: PriorityActivity, tasks: ArrayList<Task>):
            TaskAdapterInterface(activity, activity, tasks)
}
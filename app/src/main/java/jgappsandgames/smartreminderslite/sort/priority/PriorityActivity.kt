package jgappsandgames.smartreminderslite.sort.priority

// Android OS
import android.annotation.SuppressLint
import android.content.Intent

// Views
import android.os.Bundle
import android.view.View

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.home.FirstRun

// Save
import jgappsandgames.smartreminderssave.MasterManager
import jgappsandgames.smartreminderssave.priority.PriorityManager
import jgappsandgames.smartreminderssave.utility.FileUtility

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
class PriorityActivity: PriorityActivityInterface() {
    // Data ----------------------------------------------------------------------------------------
    private var position: Int = 3

    // LifeCycle Methods ---------------------------------------------------------------------------
    /**
     * OnCreate
     *
     * Called to Create The Activity
     * Called By the Application
     */
    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Load Filepath
        FileUtility.loadFilePaths(this)

        // First Run
        if (FileUtility.isFirstRun()) startActivity(Intent(this, FirstRun::class.java))

        // Normal Run
        else MasterManager.load(this)
    }

    /**
     * OnResume
     *
     * Called To Reset the Adapters Before Displaying the Data
     * Called By The Application
     */
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
        setAdapter()
    }

    /**
     * OnPause
     *
     * Called To Save and Pause the Activity
     * Called By The Application
     */
    override fun onPause() {
        super.onPause()
        save()
    }

    // Click Listeners -----------------------------------------------------------------------------
    /**
     * OnClick
     *
     * Called When A Click Event Happens
     * Called By The Application
     */
    override fun onClick(view: View) {
        // Up Button
        if (view == up) moveUp()

        // Down Button
        else if (view == down) moveDown()
    }

    /**
     * OnLongClick
     *
     * Called When A Long Click Event Happens
     * Called By The Application
     */
    override fun onLongClick(view: View): Boolean {
        return false
    }

    // Task Listeners ------------------------------------------------------------------------------
    /**
     * OnTaskChanged
     *
     * Called When A Task In One Of The Adapters Changes
     */
    override fun onTaskChanged() {
        onResume()
    }

    // Private Class Methods -----------------------------------------------------------------------
    /**
     * SetTitle
     *
     * Called to Set The Title and The Text in The Buttons
     */
    private fun setTitle() {
        when (position) {
            1 -> {
                title = "Ignore"
                down!!.setText("")
                up!!.setText(R.string.low)
                return
            }

            2 -> {
                title = "Low Priority (Default)"
                down!!.setText(R.string.ignore)
                up!!.setText(R.string.normal)
                return
            }

            3 -> {
                title = "Normal Priority"
                down!!.setText(R.string.low)
                up!!.setText(R.string.high)
                return
            }

            4 -> {
                title = "High Priority"
                down!!.setText(R.string.normal)
                up!!.setText(R.string.stared)
                return
            }

            5 -> {
                title = "Stared Tasks"
                down!!.setText(R.string.high)
                up!!.setText("")
            }
        }
    }

    /**
     * SetAdapter
     *
     * Called To Set The Adapter Currently Active
     */
    private fun setAdapter() {
        when (position) {
            1 -> {
                if (ignore == null) ignore = PriorityAdapter(this, PriorityManager.getIgnored())
                list!!.adapter = ignore
                return
            }

            2 -> {
                if (low == null) low = PriorityAdapter(this, PriorityManager.getLow())
                list!!.adapter = low
                return
            }

            3 -> {
                if (normal == null) normal = PriorityAdapter(this, PriorityManager.getNormal())
                list!!.adapter = normal
                return
            }

            4 -> {
                if (high == null) high = PriorityAdapter(this, PriorityManager.getHigh())
                list!!.adapter = high
                return
            }

            5 -> {
                if (stared == null) stared = PriorityAdapter(this, PriorityManager.getStared())
                list!!.adapter = stared
            }
        }
    }

    /**
     * MoveUp
     *
     * Called To Move The Current Selection Up By One
     */
    private fun moveUp() {
        when (position) {
            1 -> {
                position = 2
                down!!.visibility = View.VISIBLE
                setAdapter()
                setTitle()
            }

            2 -> {
                position = 3
                setAdapter()
                setTitle()
            }

            3 -> {
                position = 4
                setAdapter()
                setTitle()
            }

            4 -> {
                position = 5
                up!!.visibility = View.INVISIBLE
                setAdapter()
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
                down!!.visibility = View.INVISIBLE
                setAdapter()
                setTitle()
            }

            3 -> {
                position = 2
                setAdapter()
                setTitle()
            }

            4 -> {
                position = 3
                setAdapter()
                setTitle()
            }

            5 -> {
                position = 4
                down!!.visibility = View.VISIBLE
                setAdapter()
                setTitle()
            }
        }
    }

    // Parent Overrides ----------------------------------------------------------------------------
    /**
     * Save
     *
     * Called To Save The Application Data
     */
    override fun save() {
        MasterManager.save()
    }
}
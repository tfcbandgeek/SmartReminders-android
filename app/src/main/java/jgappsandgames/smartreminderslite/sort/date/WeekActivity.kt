package jgappsandgames.smartreminderslite.sort.date

// Java
import android.annotation.SuppressLint
import java.util.Calendar

// Android OS
import android.content.Intent
import android.os.Bundle
import android.view.View

// Save
import jgappsandgames.smartreminderssave.MasterManager
import jgappsandgames.smartreminderssave.date.DateManager
import jgappsandgames.smartreminderssave.utility.FileUtility

// App
import jgappsandgames.smartreminderslite.home.FirstRun

/**
 * WeekActivity
 * Created by joshua on 1/19/2018.
 */
class WeekActivity: WeekActivityInterface() {
    // LifeCycle Methods ---------------------------------------------------------------------------
    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // First Run
        FileUtility.loadFilePaths(this)
        if (FileUtility.isFirstRun()) {
            val first_run = Intent(this, FirstRun::class.java)
            startActivity(first_run)
        } else {
            // Load Data
            MasterManager.load(this)
        }

        week_active = 0

        // Set Title
        setTitle()
    }

    override fun onResume() {
        super.onResume()

        adapter = WeekAdapter(this, week_active)
        tasks!!.setAdapter(adapter)
    }

    override fun onPause() {
        super.onPause()

        save()
    }

    // Click Listeners -----------------------------------------------------------------------------
    override fun onClick(view: View) {
        // Previous
        if (view == previous) {
            week_active--
            if (week_active < 0) week_active = 0

            adapter = WeekAdapter(this, week_active)
            tasks!!.adapter = adapter

            setTitle()
        }

        // Next
        else if (view == next) {
            week_active++

            adapter = WeekAdapter(this, week_active)
            tasks!!.adapter = adapter

            setTitle()
        }
    }

    // Task Changed Listener -----------------------------------------------------------------------
    override fun onTaskChanged() {
        onResume()
    }

    // Private Class Methods -----------------------------------------------------------------------
    private fun setTitle() {
        val start = DateManager.getWeek(week_active).getStart()
        val end = DateManager.getWeek(week_active).getEnd()

        title = (start.get(Calendar.MONTH) + 1).toString() + "/" +
                start.get(Calendar.DAY_OF_MONTH).toString() + " - " +
                (end.get(Calendar.MONTH) + 1).toString() + "/" +
                end.get(Calendar.DAY_OF_MONTH).toString()
    }

    override fun save() {
        MasterManager.save()
    }
}
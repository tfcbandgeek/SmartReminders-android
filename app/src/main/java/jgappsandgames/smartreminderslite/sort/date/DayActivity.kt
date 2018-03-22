package jgappsandgames.smartreminderslite.sort.date

// Java
import java.util.Calendar

// Android OS
import android.os.Bundle

// View
import android.view.View

// Save
import jgappsandgames.smartreminderssave.MasterManager
import jgappsandgames.smartreminderssave.date.DateManager

/**
 * DayActivity
 * Created by joshua on 1/1/2018.
 */
class DayActivity: DayActivityInterface() {
    // Data
    private var dateActivity: Calendar = Calendar.getInstance()

    // LifeCycle Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dateActivity = Calendar.getInstance()
    }

    override fun onResume() {
        super.onResume()

        DateManager.create()
        adapter = DayAdapter(this, dateActivity)
        tasks!!.adapter = adapter
    }

    // Click Listeners
    override fun onClick(view: View) {
        // Previous
        if (view == previous) {
            dateActivity.add(Calendar.DAY_OF_MONTH, -1)

            if (dateActivity.before(Calendar.getInstance())) dateActivity = Calendar.getInstance()

            adapter = DayAdapter(this, dateActivity)
            tasks!!.adapter = adapter

            setTitle()
        } else if (view == next) {
            dateActivity.add(Calendar.DAY_OF_MONTH, 1)

            adapter = DayAdapter(this, dateActivity)
            tasks!!.adapter = adapter

            setTitle()
        }// Next
    }

    // Task Changed Listener
    override fun onTaskChanged() {
        onResume()
    }

    // Private Class Methods
    override fun save() {
        MasterManager.save()
    }

    // Private Class Methods
    override fun setTitle() {
        title = (dateActivity.get(Calendar.MONTH) + 1).toString() + "/" + dateActivity.get(Calendar.DAY_OF_MONTH).toString()
    }
}
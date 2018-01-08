package jgappsandgames.smartreminderslite.sort.date

// Java
import java.util.Calendar

// Android OS
import android.os.Bundle

// View
import android.view.View

// Save
import jgappsandgames.smartreminderssave.MasterManager

/**
 * DayActivity
 * Created by joshua on 1/1/2018.
 */
class DayActivity: DayActivityInterface() {
    // Data
    private var day_active: Calendar? = Calendar.getInstance()

    // LifeCycle Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        day_active = Calendar.getInstance()
    }

    override fun onResume() {
        super.onResume()

        adapter = DayAdapter(this, day_active!!)
        tasks!!.adapter = adapter
    }

    // Click Listeners
    override fun onClick(view: View) {
        // Previous
        if (view == previous) {
            day_active!!.add(Calendar.DAY_OF_MONTH, -1)

            if (day_active!!.before(Calendar.getInstance())) day_active = Calendar.getInstance()

            adapter = DayAdapter(this, day_active!!)
            tasks!!.adapter = adapter

            setTitle()
        } else if (view == next) {
            day_active!!.add(Calendar.DAY_OF_MONTH, 1)

            adapter = DayAdapter(this, day_active!!)
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
        try {
            title = (day_active!!.get(Calendar.MONTH) + 1).toString() + "/" + day_active!!.get(Calendar.DAY_OF_MONTH).toString()
        } catch (e: NullPointerException) {
            title = "Error"
        }
    }
}
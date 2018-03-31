package jgappsandgames.smartreminderslite.sort.date

// Java
import java.util.Calendar

// Views
import android.widget.BaseAdapter
import android.widget.CalendarView

// Save
import jgappsandgames.smartreminderssave.date.DateManager

/**
 * MonthActivity
 * Created by joshua on 1/19/2018.
 */
class MonthActivity: MonthActivityInterface() {
    // Adapter
    private var adapter: BaseAdapter? = null

    // LifeCycleMethods
    override fun onResume() {
        super.onResume()
        // Set List
        DateManager.create()
        selectedTasks = DateManager.getDayTasks(selected!!)
        adapter = MonthAdapter(this, selectedTasks!!)
        tasks!!.adapter = adapter
    }

    // On Date Set Presses
    override fun onSelectedDayChange(calendar: CalendarView, year: Int, month: Int, day: Int) {
        selected!!.set(Calendar.YEAR, year)
        selected!!.set(Calendar.MONTH, month)
        selected!!.set(Calendar.DAY_OF_MONTH, day)
        selectedTasks = DateManager.getDayTasks(selected!!)
        adapter = MonthAdapter(this, selectedTasks!!)
        tasks!!.adapter = adapter
    }
}
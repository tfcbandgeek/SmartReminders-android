package jgappsandgames.smartreminderslite.date;

// Java
import java.util.Calendar;

// Vies
import android.widget.BaseAdapter;
import android.widget.CalendarView;

// Save
import jgappsandgames.smartreminderssave.date.DateManagerKt;

/**
 * MonthActivity
 * Created by joshua on 10/12/17.
 * Last Edited on 10/14/17 (106).
 */
public class MonthActivity extends MonthActivityInterface {
    // Adapter
    private BaseAdapter adapter;

    // LifeCycleMethods
    @Override
    protected void onResume() {
        super.onResume();
        // Set List
        selected_tasks = DateManagerKt.getDayTasks(selected);
        adapter = new MonthAdapter(this, selected_tasks);
        tasks.setAdapter(adapter);
    }

    // On Date Set Presses
    @Override
    public void onSelectedDayChange(CalendarView calendar, int year, int month, int day) {
        selected.set(Calendar.YEAR, year);
        selected.set(Calendar.MONTH, month);
        selected.set(Calendar.DAY_OF_MONTH, day);
        selected_tasks = DateManagerKt.getDayTasks(selected);
        adapter = new MonthAdapter(this, selected_tasks);
        tasks.setAdapter(adapter);
    }
}
package jgappsandgames.smartreminderslite.date;

// Java
import java.util.Calendar;

// Jetbeans
import org.jetbrains.annotations.NotNull;

// Vies
import android.util.Log;
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
    // Log Constant
    private static final String LOG = "MonthActivity";

    // Adapter
    private BaseAdapter adapter;

    // LifeCycleMethods
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG, "onResume Called");

        // Set List
        Log.v(LOG, "Setting Lists");
        selected_tasks = DateManagerKt.getDayTasks(selected);
        adapter = new MonthAdapter(this, selected_tasks);
        tasks.setAdapter(adapter);

        Log.v(LOG, "onResume Done");
    }

    // On Date Set Presses
    @Override
    public void onSelectedDayChange(@NotNull CalendarView calendar, int year, int month, int day) {
        selected.set(Calendar.YEAR, year);
        selected.set(Calendar.MONTH, month);
        selected.set(Calendar.DAY_OF_MONTH, day);
        selected_tasks = DateManagerKt.getDayTasks(selected);
        adapter = new MonthAdapter(this, selected_tasks);
        tasks.setAdapter(adapter);
    }
}
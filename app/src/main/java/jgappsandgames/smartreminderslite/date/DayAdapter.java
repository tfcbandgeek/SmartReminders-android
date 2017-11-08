package jgappsandgames.smartreminderslite.date;

// Java
import java.util.Calendar;

// App
import jgappsandgames.smartreminderslite.adapter.TaskAdapterInterface;

// Save
import jgappsandgames.smartreminderssave.date.DateManagerKt;

/**
 * DayAdapter
 * Created by joshua on 10/9/17.
 * Last Edited on 10/9/17 (79).
 */
class DayAdapter extends TaskAdapterInterface {
    // Initializer
    DayAdapter(DayActivity activity, Calendar date_active) {
        super(activity, activity, DateManagerKt.getDayTasks(date_active));
    }
}
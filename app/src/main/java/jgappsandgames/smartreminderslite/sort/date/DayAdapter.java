package jgappsandgames.smartreminderslite.sort.date;

// Java
import java.util.Calendar;

// App
import jgappsandgames.smartreminderslite.adapter.TaskAdapterInterface;

// Save
import jgappsandgames.smartreminderssave.date.DateManager;

/**
 * DayAdapter
 * Created by joshua on 10/9/17.
 */
class DayAdapter extends TaskAdapterInterface {
    // Initializer
    DayAdapter(DayActivity activity, Calendar date_active) {
        super(activity, activity, DateManager.getDay(date_active));
    }
}
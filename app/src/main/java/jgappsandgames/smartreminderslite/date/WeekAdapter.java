package jgappsandgames.smartreminderslite.date;

// App
import jgappsandgames.smartreminderslite.adapter.TaskAdapterInterface;

// Save
import jgappsandgames.smartreminderssave.date.DateManager;

/**
 * WeekAdapter
 * Created by joshua on 10/9/17.
 */
class WeekAdapter extends TaskAdapterInterface {
    // Initializer
    WeekAdapter(WeekActivity activity, int date_active) {
        super(activity, activity, DateManager.getWeek(date_active).getAllTasks());
    }
}

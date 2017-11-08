package jgappsandgames.smartreminderslite.date;

import java.util.Calendar;

import jgappsandgames.smartreminderslite.adapter.TaskAdapterInterface;
import jgappsandgames.smartreminderssave.date.DateManagerKt;

/**
 * WeekAdapter
 * Created by joshua on 10/9/17.
 */
class WeekAdapter extends TaskAdapterInterface {
    // Initializer
    WeekAdapter(WeekActivity activity, Calendar date_active) {
        super(activity, activity, DateManagerKt.getWeek(date_active).getAllTasks());
    }
}
package jgappsandgames.smartreminderslite.date;

import jgappsandgames.smartreminderslite.adapter.TaskAdapterInterface;

/**
 * WeekAdapter
 * Created by joshua on 10/9/17.
 * Last Edited on 10/9/17 ().
 */
class WeekAdapter extends TaskAdapterInterface {
    // Initializer
    WeekAdapter(WeekActivity activity, int date_active) {
        super(activity, activity, DateManager.getWeek(date_active).getAllTasks());
    }
}

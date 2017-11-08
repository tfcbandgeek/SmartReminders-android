package jgappsandgames.smartreminderslite.date;

import java.util.Calendar;

import jgappsandgames.smartreminderslite.adapter.TaskAdapterInterface;

/**
 * DayAdapter
 * Created by joshua on 10/9/17.
 * Last Edited on 10/9/17 (79).
 */
class DayAdapter extends TaskAdapterInterface {
    // Initializer
    DayAdapter(DayActivity activity, Calendar date_active) {
        super(activity, activity, DateManager.getDay(date_active));
    }
}
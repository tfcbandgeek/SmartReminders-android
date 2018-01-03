package jgappsandgames.smartreminderslite.sort.date

// Java
import java.util.Calendar

// App
import jgappsandgames.smartreminderslite.adapter.TaskAdapterInterface

// Save
import jgappsandgames.smartreminderssave.date.DateManager

/**
 * DayAdapter
 * Created by joshua on 12/27/2017.
 */
class DayAdapter(activity: DayActivity, date_active: Calendar):
        TaskAdapterInterface(activity, activity, DateManager.getDayTasks(date_active))
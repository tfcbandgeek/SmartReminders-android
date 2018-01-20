package jgappsandgames.smartreminderslite.sort.date

import jgappsandgames.smartreminderslite.adapter.TaskAdapterInterface
import jgappsandgames.smartreminderssave.date.DateManager

/**
 * WeekAdapter
 * Created by joshua on 1/19/2018.
 */
class WeekAdapter(activity: WeekActivity, date_active: Int):
        TaskAdapterInterface(activity, activity, DateManager.getWeek(date_active).getAllTasks())
package jgappsandgames.smartreminderslite.sort.date

// Java
import java.util.ArrayList

// Save
import jgappsandgames.smartreminderssave.tasks.Task

// App
import jgappsandgames.smartreminderslite.adapter.TaskAdapterInterface

/**
 * MonthAdapter
 * Created by joshua on 1/19/2018.
 */
class MonthAdapter(activity: MonthActivity, tasks: ArrayList<Task>): TaskAdapterInterface(activity, activity, tasks)
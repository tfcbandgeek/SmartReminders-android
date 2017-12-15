package jgappsandgames.smartreminderslite.sort.status

// App
import jgappsandgames.smartreminderslite.adapter.TaskAdapterInterface

// Save
import jgappsandgames.smartreminderssave.tasks.Task

/**
 * StatusAdapter
 * Created by joshua on 12/14/2017.
 *
 * Adapter for The Status View Lists
 */
class StatusAdapter(activity: StatusActivity, tasks: ArrayList<Task>):
        TaskAdapterInterface(activity, activity, tasks)
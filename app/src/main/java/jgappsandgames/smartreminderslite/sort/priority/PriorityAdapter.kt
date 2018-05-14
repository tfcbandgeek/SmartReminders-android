package jgappsandgames.smartreminderslite.sort.priority

// App
import jgappsandgames.smartreminderslite.adapter.TaskAdapterInterface

// Save
import jgappsandgames.smartreminderssave.tasks.Task

/**
 * PriorityAdapter
 * Created by joshua on 12/14/2017.
 *
 * Task Adapter for the Priority Lists
 */
class PriorityAdapter(activity: PriorityActivity, tasks: ArrayList<Task>): TaskAdapterInterface(activity, activity, tasks)
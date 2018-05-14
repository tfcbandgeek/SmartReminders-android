package jgappsandgames.smartreminderslite.tasks

// App
import jgappsandgames.smartreminderslite.adapter.TaskAdapterInterface
import jgappsandgames.smartreminderssave.tasks.TaskManager

/**
 * ChildrenAdapter
 * Created by joshua on 12/16/2017.
 *
 * Displays the Tasks in the Folder View
 */
class ChildrenAdapter(activity: TaskActivity, tasks: ArrayList<String>): TaskAdapterInterface(activity, activity, TaskManager.getTasks(tasks))
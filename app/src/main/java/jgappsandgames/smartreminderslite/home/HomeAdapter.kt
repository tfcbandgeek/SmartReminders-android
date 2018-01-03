package jgappsandgames.smartreminderslite.home

// App
import jgappsandgames.smartreminderslite.adapter.TaskAdapterInterface

// Save
import jgappsandgames.smartreminderssave.tasks.TaskManager

/**
 * HomeAdapter
 * Created by joshua on 12/13/2017.
 *
 * Adapter for the Task List in the Home View
 */
class HomeAdapter(activity: HomeActivity): TaskAdapterInterface(activity, activity, TaskManager.home, null)
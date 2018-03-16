package jgappsandgames.smartreminderssave.status

// Save Library
import jgappsandgames.smartreminderssave.tasks.Task
import jgappsandgames.smartreminderssave.tasks.TaskManager
import java.util.*

/**
 * StatusManager
 * Created by joshua on 12/13/2017.
 *
 * Sorts Tasks Based on Their Status
 */
class StatusManager {
    companion object {
        // Data ------------------------------------------------------------------------------------
        private var folders_list: ArrayList<Task>? = null
        private var completed_list: ArrayList<Task>? = null
        private var in_progress_list: ArrayList<Task>? = null
        private var incomplete_list: ArrayList<Task>? = null
        private var overdue_list: ArrayList<Task>? = null

        // Management Methods ----------------------------------------------------------------------
        fun create() {
            folders_list = ArrayList()
            completed_list = ArrayList()
            in_progress_list = ArrayList()
            incomplete_list = ArrayList()
            overdue_list = ArrayList()

            for (t in TaskManager.tasks) {
                val temp = Task(t)

                // Folders
                if (temp.getType() == Task.TYPE_FLDR) folders_list!!.add(temp)

                // Completed
                else if (temp.isCompleted()) completed_list!!.add(temp)

                // Overdue
                else if (temp.getDateDue() != null) {
                    if (temp.getDateDue()!!.before(Calendar.getInstance())) overdue_list!!.add(temp)
                }

                // Incomplete
                else if (temp.getStatus() == 0) incomplete_list!!.add(temp)

                // In Progress
                else in_progress_list!!.add(temp)
            }
        }

        // Getters ---------------------------------------------------------------------------------
        fun getFolders(): ArrayList<Task> {
            if (folders_list == null) create()
            return folders_list!!
        }

        fun getCompleted(): ArrayList<Task> {
            if (completed_list == null) create()
            return completed_list!!
        }

        fun getInProgress(): ArrayList<Task> {
            if (in_progress_list == null) create()
            return in_progress_list!!
        }

        fun getIncomplete(): ArrayList<Task> {
            if (incomplete_list == null) create()
            return incomplete_list!!
        }

        fun getOverdue(): ArrayList<Task> {
            if (overdue_list == null) create()
            return overdue_list!!
        }
    }
}
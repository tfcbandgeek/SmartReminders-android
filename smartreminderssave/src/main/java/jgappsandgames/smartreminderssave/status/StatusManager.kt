package jgappsandgames.smartreminderssave.status

// Java
import java.util.Calendar

// Save Library
import jgappsandgames.smartreminderssave.tasks.Task
import jgappsandgames.smartreminderssave.tasks.TaskManager

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

                when (temp.getType()) {
                    Task.TYPE_FLDR -> {
                        folders_list?.add(temp)
                    }

                    Task.TYPE_TASK -> {
                        when (temp.getStatus()) {
                            Task.STATUS_DONE -> {
                                completed_list?.add(temp)
                            }

                            0 -> {
                                if (temp.getDateDue() != null) {
                                    if (temp.getDateDue()!!.before(Calendar.getInstance())) overdue_list?.add(temp)
                                    else incomplete_list?.add(temp)
                                } else {
                                    incomplete_list?.add(temp)
                                }
                            }

                            else -> {
                                if (temp.getDateDue() != null) {
                                    if (temp.getDateDue()!!.before(Calendar.getInstance())) overdue_list?.add(temp)
                                    else in_progress_list?.add(temp)
                                } else {
                                    in_progress_list?.add(temp)
                                }
                            }
                        }
                    }
                }
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
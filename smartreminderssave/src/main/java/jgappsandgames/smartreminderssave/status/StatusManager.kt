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
        private var foldersList: ArrayList<Task>? = null
        private var completedList: ArrayList<Task>? = null
        private var inProgressList: ArrayList<Task>? = null
        private var incompleteList: ArrayList<Task>? = null
        private var overdueList: ArrayList<Task>? = null

        // Management Methods ----------------------------------------------------------------------
        fun create() {
            foldersList = ArrayList()
            completedList = ArrayList()
            inProgressList = ArrayList()
            incompleteList = ArrayList()
            overdueList = ArrayList()

            for (t in TaskManager.tasks) {
                val temp = Task(t)

                when (temp.getType()) {
                    Task.TYPE_FOLDER -> {
                        foldersList?.add(temp)
                    }

                    Task.TYPE_TASK -> {
                        when (temp.getStatus()) {
                            Task.STATUS_DONE -> {
                                completedList?.add(temp)
                            }

                            0 -> {
                                if (temp.getDateDue() != null) {
                                    if (temp.getDateDue()!!.before(Calendar.getInstance())) overdueList?.add(temp)
                                    else incompleteList?.add(temp)
                                } else {
                                    incompleteList?.add(temp)
                                }
                            }

                            else -> {
                                if (temp.getDateDue() != null) {
                                    if (temp.getDateDue()!!.before(Calendar.getInstance())) overdueList?.add(temp)
                                    else inProgressList?.add(temp)
                                } else {
                                    inProgressList?.add(temp)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Getters ---------------------------------------------------------------------------------
        fun getFolders(): ArrayList<Task> {
            if (foldersList == null) create()
            return foldersList!!
        }

        fun getCompleted(): ArrayList<Task> {
            if (completedList == null) create()
            return completedList!!
        }

        fun getInProgress(): ArrayList<Task> {
            if (inProgressList == null) create()
            return inProgressList!!
        }

        fun getIncomplete(): ArrayList<Task> {
            if (incompleteList == null) create()
            return incompleteList!!
        }

        fun getOverdue(): ArrayList<Task> {
            if (overdueList == null) create()
            return overdueList!!
        }
    }
}
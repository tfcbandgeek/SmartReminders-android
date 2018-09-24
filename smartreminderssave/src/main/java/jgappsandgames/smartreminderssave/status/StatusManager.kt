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
        private var foldersList = ArrayList<Task>()
        private var completedList = ArrayList<Task>()
        private var inProgressList = ArrayList<Task>()
        private var incompleteList = ArrayList<Task>()
        private var overdueList = ArrayList<Task>()

        // Management Methods ----------------------------------------------------------------------
        fun create() {
            foldersList.clear()
            completedList.clear()
            inProgressList.clear()
            incompleteList.clear()
            overdueList.clear()

            val all = TaskManager.getAllTasks()
            for (temp in all) {
                when (temp.getType()) {
                    Task.TYPE_FOLDER -> foldersList.add(temp)

                    Task.TYPE_TASK -> {
                        when (temp.getStatus()) {
                            Task.STATUS_DONE -> completedList.add(temp)

                            0 -> {
                                if (temp.getDateDue() != null) {
                                    if (temp.getDateDue()!!.before(Calendar.getInstance())) overdueList.add(temp)
                                    else incompleteList.add(temp)
                                } else {
                                    incompleteList.add(temp)
                                }
                            }

                            else -> {
                                if (temp.getDateDue() != null) {
                                    if (temp.getDateDue()!!.before(Calendar.getInstance())) overdueList.add(temp)
                                    else inProgressList.add(temp)
                                } else {
                                    inProgressList.add(temp)
                                }
                            }
                        }
                    }
                }

                TaskManager.taskPool.returnPoolObject(temp)
            }
        }

        // Getters ---------------------------------------------------------------------------------
        fun getFolders(): ArrayList<Task> = foldersList

        fun getCompleted(): ArrayList<Task> = completedList

        fun getInProgress(): ArrayList<Task> = inProgressList

        fun getIncomplete(): ArrayList<Task> = incompleteList

        fun getOverdue(): ArrayList<Task> = overdueList
    }
}
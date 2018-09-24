package jgappsandgames.smartreminderssave.priority

// App
import jgappsandgames.smartreminderssave.tasks.Task
import jgappsandgames.smartreminderssave.tasks.TaskManager

/**
 * PriorityManager
 * Created by joshua on 12/13/2017.
 *
 * Sorts Tasks Based on Their Priority
 */
class PriorityManager {
    companion object {
        // Data ------------------------------------------------------------------------------------
        private var folder_list = ArrayList<Task>()
        private var ignored_tasks = ArrayList<Task>()
        private var low_tasks = ArrayList<Task>()
        private var normal_tasks = ArrayList<Task>()
        private var high_priority = ArrayList<Task>()
        private var stared_priority = ArrayList<Task>()

        // Management Methods ----------------------------------------------------------------------
        fun create() {
            folder_list = ArrayList()
            ignored_tasks = ArrayList()
            low_tasks = ArrayList()
            normal_tasks = ArrayList()
            high_priority = ArrayList()
            stared_priority = ArrayList()

            val all = TaskManager.getAllTasks()
            for (temp in all) {
                when {
                    temp.getType() == Task.TYPE_FOLDER -> folder_list.add(temp)
                    temp.getPriority() == 0 -> ignored_tasks.add(temp)
                    temp.getPriority() < 20 -> low_tasks.add(temp)
                    temp.getPriority() < 80 -> normal_tasks.add(temp)
                    temp.getPriority() < 100 -> high_priority.add(temp)
                    temp.getPriority() == 100 -> stared_priority.add(temp)
                }

                TaskManager.taskPool.returnPoolObject(temp)
            }
        }

        // Getters ---------------------------------------------------------------------------------
        fun getFolder(): ArrayList<Task> = folder_list

        fun getIgnored(): ArrayList<Task> = ignored_tasks

        fun getLow(): ArrayList<Task> = low_tasks

        fun getNormal(): ArrayList<Task> = normal_tasks

        fun getHigh(): ArrayList<Task> = high_priority

        fun getStared(): ArrayList<Task> = stared_priority
    }
}
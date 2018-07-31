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

            for (t in TaskManager.tasks) {
                val temp = Task(t)
                when {
                    temp.getType() == Task.TYPE_FOLDER -> folder_list.add(temp)
                    temp.getPriority() == 0 -> ignored_tasks.add(temp)
                    temp.getPriority() < 20 -> low_tasks.add(temp)
                    temp.getPriority() < 80 -> normal_tasks.add(temp)
                    temp.getPriority() < 100 -> high_priority.add(temp)
                    temp.getPriority() == 100 -> stared_priority.add(temp)
                }
            }
        }

        // Getters ---------------------------------------------------------------------------------
        fun getFolder(): ArrayList<Task> {
            return folder_list
        }

        fun getIgnored(): ArrayList<Task> {
            return ignored_tasks
        }

        fun getLow(): ArrayList<Task> {
            return low_tasks
        }

        fun getNormal(): ArrayList<Task> {
            return normal_tasks
        }

        fun getHigh(): ArrayList<Task> {
            return high_priority
        }

        fun getStared(): ArrayList<Task> {
            return stared_priority
        }
    }
}
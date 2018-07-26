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
        private var folder_list: ArrayList<Task>? = null
        private var ignored_tasks: ArrayList<Task>? = null
        private var low_tasks: ArrayList<Task>? = null
        private var normal_tasks: ArrayList<Task>? = null
        private var high_priority: ArrayList<Task>? = null
        private var stared_priority: ArrayList<Task>? = null

        // Management Methods ----------------------------------------------------------------------
        /**
         * Create
         *
         * Called To Sort the Tasks Based On Their Priority
         */
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
                    temp.getType() == Task.TYPE_FOLDER -> folder_list!!.add(temp)
                    temp.getPriority() == 0 -> ignored_tasks!!.add(temp)
                    temp.getPriority() < 20 -> low_tasks!!.add(temp)
                    temp.getPriority() < 80 -> normal_tasks!!.add(temp)
                    temp.getPriority() < 100 -> high_priority!!.add(temp)
                    temp.getPriority() == 100 -> stared_priority!!.add(temp)
                }
            }
        }

        // Getters ---------------------------------------------------------------------------------
        /**
         * GetFolder
         *
         * @return The List of Folders
         */
        fun getFolder(): ArrayList<Task> {
            if (folder_list == null) create()
            return folder_list!!
        }

        /**
         * GetIgnored
         *
         * @return The List of Ignored Tasks
         */
        fun getIgnored(): ArrayList<Task> {
            if (ignored_tasks == null) create()
            return ignored_tasks!!
        }

        /**
         * GetLow
         *
         * @return The List of Low Priority Tasks
         */
        fun getLow(): ArrayList<Task> {
            if (low_tasks == null) create()
            return low_tasks!!
        }

        /**
         * GetNormal
         *
         * @return The List of Normal Priority Tasks
         */
        fun getNormal(): ArrayList<Task> {
            if (normal_tasks == null) create()
            return normal_tasks!!
        }

        /**
         * GetHigh
         *
         * @return The List of High Priority Tasks
         */
        fun getHigh(): ArrayList<Task> {
            if (high_priority == null) create()
            return high_priority!!
        }

        /**
         * GetStared
         *
         * @return The List of Stared Tasks
         */
        fun getStared(): ArrayList<Task> {
            if (stared_priority == null) create()
            return stared_priority!!
        }
    }
}
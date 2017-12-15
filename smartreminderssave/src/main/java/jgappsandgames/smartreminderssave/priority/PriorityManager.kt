package jgappsandgames.smartreminderssave.priority

// Open Log
import me.jgappsandgames.openlog.Log

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
            Log.d("PriorityManager", "Create Called")
            folder_list = ArrayList()
            ignored_tasks = ArrayList()
            low_tasks = ArrayList()
            normal_tasks = ArrayList()
            high_priority = ArrayList()
            stared_priority = ArrayList()

            for (t in TaskManager.tasks) {
                val temp = Task(t)

                // Folder
                if (temp.getType() == Task.TYPE_FLDR) folder_list!!.add(temp)

                // Ignored Tasks
                else if (temp.getPriority() == 0) ignored_tasks!!.add(temp)

                // Low Tasks
                else if (temp.getPriority() < 20) low_tasks!!.add(temp)

                // Normal Tasks
                else if (temp.getPriority() < 80) normal_tasks!!.add(temp)

                // High Tasks
                else if (temp.getPriority() < 100) high_priority!!.add(temp)

                // Stared Tasks
                else if (temp.getPriority() == 100) stared_priority!!.add(temp)
            }
        }

        // Getters ---------------------------------------------------------------------------------
        /**
         * GetFolder
         *
         * @return The List of Folders
         */
        fun getFolder(): ArrayList<Task> {
            Log.d("PriorityManager", "GetFolder Called")
            if (folder_list == null) create()
            return folder_list!!
        }

        /**
         * GetIgnored
         *
         * @return The List of Ignored Tasks
         */
        fun getIgnored(): ArrayList<Task> {
            Log.d("PriorityManager", "GetIgnored Called")
            if (ignored_tasks == null) create()
            return ignored_tasks!!
        }

        /**
         * GetLow
         *
         * @return The List of Low Priority Tasks
         */
        fun getLow(): ArrayList<Task> {
            Log.d("PriorityManager", "GetLow Called")
            if (low_tasks == null) create()
            return low_tasks!!
        }

        /**
         * GetNormal
         *
         * @return The List of Normal Priority Tasks
         */
        fun getNormal(): ArrayList<Task> {
            Log.d("PriorityManager", "GtNormal Called")
            if (normal_tasks == null) create()
            return normal_tasks!!
        }

        /**
         * GetHigh
         *
         * @return The List of High Priority Tasks
         */
        fun getHigh(): ArrayList<Task> {
            Log.d("PriorityManager", "GetHigh Called")
            if (high_priority == null) create()
            return high_priority!!
        }

        /**
         * GetStared
         *
         * @return The List of Stared Tasks
         */
        fun getStared(): ArrayList<Task> {
            Log.d("PriorityManager", "GetStared Called")
            if (stared_priority == null) create()
            return stared_priority!!
        }
    }
}
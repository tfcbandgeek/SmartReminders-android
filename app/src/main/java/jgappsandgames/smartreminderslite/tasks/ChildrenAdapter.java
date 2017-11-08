package jgappsandgames.smartreminderslite.tasks;

// Java
import java.util.ArrayList;
import java.util.List;

// App
import jgappsandgames.smartreminderslite.adapter.TaskAdapterInterface;

/**
 * ChildrenAdapter
 * Created by joshua on 8/31/17.
 * Last Edited on 10/11/17 (85).
 * Edited on 10/5/17 (79).
 */
class ChildrenAdapter extends TaskAdapterInterface {
    // Initializer
    ChildrenAdapter(TaskActivity activity, ArrayList<String> tasks) {
        super(activity, activity, tasks, null);
    }
}
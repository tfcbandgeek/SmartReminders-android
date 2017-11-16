package jgappsandgames.smartreminderslite.tasks;

// Java
import java.util.ArrayList;

// App
import jgappsandgames.smartreminderslite.adapter.TaskAdapterInterface;

/**
 * ChildrenAdapter
 * Created by joshua on 8/31/17.
 */
class ChildrenAdapter extends TaskAdapterInterface {
    // Initializer
    ChildrenAdapter(TaskActivity activity, ArrayList<String> tasks) {
        super(activity, activity, tasks, null);
    }
}
package jgappsandgames.smartreminderslite.priority;

// Java
import java.util.ArrayList;

// App
import jgappsandgames.smartreminderslite.adapter.TaskAdapterInterface;

// Save
import jgappsandgames.smartreminderssave.tasks.Task;

/**
 * PriorityAdapter
 * Created by joshua on 10/9/17.
 */
class PriorityAdapter extends TaskAdapterInterface {
    // Initializers
    PriorityAdapter(PriorityActivity activity, ArrayList<Task> tasks) {
        super(activity, activity, tasks);
    }
}
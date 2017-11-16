package jgappsandgames.smartreminderslite.status;

// Java
import java.util.ArrayList;


// App
import jgappsandgames.smartreminderslite.adapter.TaskAdapterInterface;

// Save
import jgappsandgames.smartreminderssave.tasks.Task;

/**
 * StatusAdapter
 * Created by joshua on 9/4/17.
 */
class StatusAdapter extends TaskAdapterInterface {
    // Initializer
    StatusAdapter(StatusActivity activity, ArrayList<Task> tasks) {
        super(activity, activity, tasks);
    }
}
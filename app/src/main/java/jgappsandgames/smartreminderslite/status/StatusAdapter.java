package jgappsandgames.smartreminderslite.status;

// Java
import java.util.ArrayList;
import java.util.List;


// App
import jgappsandgames.smartreminderslite.adapter.TaskAdapterInterface;

// Save
import jgappsandgames.smartreminderssave.tasks.Task;

/**
 * StatusAdapter
 * Created by joshua on 9/4/17.
 * Last Edited on 10/11/17 (84).
 * Edited On 10/5/17 (77).
 */
class StatusAdapter extends TaskAdapterInterface {
    // Initializer
    StatusAdapter(StatusActivity activity, ArrayList<String> tasks) {
        super(activity, activity, tasks, null);
    }
}
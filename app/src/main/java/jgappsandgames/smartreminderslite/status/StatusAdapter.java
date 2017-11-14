package jgappsandgames.smartreminderslite.status;

// Java
import java.util.ArrayList;

// App
import jgappsandgames.smartreminderslite.adapter.TaskAdapterInterface;

/**
 * StatusAdapter
 * Created by joshua on 9/4/17.
 */
class StatusAdapter extends TaskAdapterInterface {
    // Initializer
    StatusAdapter(StatusActivity activity, ArrayList<String> tasks) {
        super(activity, activity, tasks, null);
    }
}
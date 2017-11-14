package jgappsandgames.smartreminderslite.priority;

// Java
import java.util.ArrayList;

import jgappsandgames.smartreminderslite.adapter.TaskAdapterInterface;

/**
 * PriorityAdapter
 * Created by joshua on 10/9/17.
 */
class PriorityAdapter extends TaskAdapterInterface {
    // Initializers
    PriorityAdapter(PriorityActivity activity, ArrayList<String> tasks) {
        super(activity, activity, tasks, null);
    }
}
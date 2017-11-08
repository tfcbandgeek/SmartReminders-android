package jgappsandgames.smartreminderslite.priority;

import java.util.ArrayList;
import java.util.List;

import jgappsandgames.smartreminderslite.adapter.TaskAdapterInterface;
import jgappsandgames.smartreminderssave.tasks.Task;

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
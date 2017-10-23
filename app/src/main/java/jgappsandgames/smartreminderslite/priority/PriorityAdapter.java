package jgappsandgames.smartreminderslite.priority;

import java.util.List;

import jgappsandgames.smartreminderslite.adapter.TaskAdapterInterface;
import jgappsandgames.smartreminderssave.tasks.Task;

/**
 * PriorityAdapter
 * Created by joshua on 10/9/17.
 */
class PriorityAdapter extends TaskAdapterInterface {
    // Initializers
    PriorityAdapter(PriorityActivity activity, List<Task> tasks) {
        super(activity, activity, tasks);
    }
}
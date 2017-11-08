package jgappsandgames.smartreminderslite.date;

// Java
import java.util.List;

// App
import jgappsandgames.smartreminderslite.adapter.TaskAdapterInterface;

// Save
import jgappsandgames.smartreminderssave.tasks.Task;

/**
 * MonthAdapter
 * Created by joshua on 10/14/17.
 * Last Edited on 10/14/17 (78).
 */
class MonthAdapter extends TaskAdapterInterface {
    MonthAdapter(MonthActivity activity, List<Task> tasks) {
        super(activity, activity, tasks);
    }
}
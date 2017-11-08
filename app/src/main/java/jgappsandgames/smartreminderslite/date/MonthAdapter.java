package jgappsandgames.smartreminderslite.date;

// Java
import java.util.ArrayList;

// App
import jgappsandgames.smartreminderslite.adapter.TaskAdapterInterface;

// Save
import jgappsandgames.smartreminderssave.tasks.Task;

/**
 * MonthAdapter
 * Created by joshua on 10/14/17.
 */
class MonthAdapter extends TaskAdapterInterface {
    MonthAdapter(MonthActivity activity, ArrayList<Task> tasks) {
        super(activity, activity, tasks);
    }
}
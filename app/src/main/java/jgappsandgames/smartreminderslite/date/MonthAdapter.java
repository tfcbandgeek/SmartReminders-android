package jgappsandgames.smartreminderslite.date;

import java.util.List;

import jgappsandgames.smartreminderslite.adapter.TaskAdapterInterface;
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
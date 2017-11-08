package jgappsandgames.smartreminderslite.home;

// Program
import jgappsandgames.smartreminderslite.adapter.TaskAdapterInterface;

// Save
import jgappsandgames.smartreminderssave.tasks.TaskManagerKt;

/**
 * HomeAdapter
 * Created by joshua on 8/31/17.
 * Last Edited on 10/5/17 (82).
 *
 * Extends the BaseAdapter
 */
class HomeAdapter extends TaskAdapterInterface {
    // Initializer
    HomeAdapter(HomeActivity activity) {
        super(activity, activity, TaskManagerKt.getHome(), null);
    }
}
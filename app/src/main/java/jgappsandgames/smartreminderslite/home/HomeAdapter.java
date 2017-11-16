package jgappsandgames.smartreminderslite.home;

// App
import jgappsandgames.smartreminderslite.adapter.TaskAdapterInterface;

// Save
import jgappsandgames.smartreminderssave.tasks.TaskManager;

/**
 * HomeAdapter
 * Created by joshua on 8/31/17.
 */
class HomeAdapter extends TaskAdapterInterface {
    // Initializer
    HomeAdapter(HomeActivity activity) {
        super(activity, activity, TaskManager.home, null);
    }
}
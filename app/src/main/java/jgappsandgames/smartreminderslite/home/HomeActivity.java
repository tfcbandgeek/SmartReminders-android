package jgappsandgames.smartreminderslite.home;

// Android OS
import android.content.Intent;
import android.os.Bundle;

// Views
import android.view.View;

// App
import jgappsandgames.smartreminderslite.holder.TaskFolderHolder.OnTaskChangedListener;
import jgappsandgames.smartreminderslite.tasks.TaskActivity;
import jgappsandgames.smartreminderslite.utility.ActivityUtility;

// Save
import jgappsandgames.smartreminderssave.MasterManager;
import jgappsandgames.smartreminderssave.tasks.Task;
import jgappsandgames.smartreminderssave.tasks.TaskManager;
import jgappsandgames.smartreminderssave.utility.FileUtility;

/**
 * HomeActivity
 * Created by joshua on 8/31/17.
 */
public class HomeActivity extends HomeActivityInterface implements OnTaskChangedListener {
    // Lifecycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // First Run
        FileUtility.loadFilePaths(this);
        if (FileUtility.isFirstRun()) {
            Intent first_run = new Intent(this, FirstRun.class);
            startActivity(first_run);
        } else {
            MasterManager.load(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Set Adapters
        adapter = new HomeAdapter(this);
        tasks.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Save
        save();
    }

    // Click Listeners
    @Override
    public void onClick(View view) {
        // Create Task
        Task task = new Task("home", Task.TYPE_TASK);
        task.save();

        TaskManager.home.add(task.getFilename());
        TaskManager.tasks.add(task.getFilename());
        TaskManager.save();

        // Create Intent
        Intent intent = new Intent(this, TaskActivity.class);
        intent.putExtra(ActivityUtility.TASK_NAME, task.getFilename());

        // Start Activity
        startActivity(intent);
    }

    @Override
    public boolean onLongClick(View view) {
        // Create Task
        Task task = new Task("home", Task.TYPE_FLDR);
        task.save();

        TaskManager.home.add(task.getFilename());
        TaskManager.tasks.add(task.getFilename());
        TaskManager.save();

        // Create Intent
        Intent intent = new Intent(this, TaskActivity.class);
        intent.putExtra(ActivityUtility.TASK_NAME, task.getFilename());

        // Start Activity
        startActivity(intent);
        return true;
    }

    @Override
    public void onTaskChanged() {
        onResume();
    }

    @Override
    protected void save() {
        MasterManager.save();
    }
}
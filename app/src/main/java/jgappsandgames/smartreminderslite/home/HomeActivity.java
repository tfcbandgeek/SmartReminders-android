package jgappsandgames.smartreminderslite.home;

// Android OS
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

// Views
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

// App
import jgappsandgames.smartreminderslite.R;

import jgappsandgames.smartreminderslite.date.DayActivity;
import jgappsandgames.smartreminderslite.date.MonthActivity;
import jgappsandgames.smartreminderslite.date.WeekActivity;
import jgappsandgames.smartreminderslite.holder.TaskFolderHolder.OnTaskChangedListener;
import jgappsandgames.smartreminderslite.priority.PriorityActivity;
import jgappsandgames.smartreminderslite.status.StatusActivity;
import jgappsandgames.smartreminderslite.tags.TagActivity;
import jgappsandgames.smartreminderslite.tasks.TaskActivity;
import jgappsandgames.smartreminderslite.utility.ActivityUtility;

// Save
import jgappsandgames.smartreminderssave.MasterManagerKt;
import jgappsandgames.smartreminderssave.tasks.Task;
import jgappsandgames.smartreminderssave.tasks.TaskManagerKt;
import jgappsandgames.smartreminderssave.utility.FileUtilityKt;

/**
 * HomeActivity
 * Created by joshua on 8/31/17.
 *
 * Main Entry Point For The Application
 */
public class HomeActivity extends Activity implements OnClickListener, OnLongClickListener, OnTaskChangedListener {
    // Log Constant
    private String LOG = "HomeActivity";

    // Views
    private ListView tasks;
    @SuppressWarnings("FieldCanBeLocal")
    private Button fab;

    // Adapters
    @SuppressWarnings("FieldCanBeLocal")
    private HomeAdapter adapter;

    // Lifecycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG, "onCreate Called");

        // Set Activity View
        Log.v(LOG, "Setting the View");
        setContentView(R.layout.activity_home);
        setTitle(R.string.app_name);

        // Load Filepath
        Log.v(LOG, "Loading Filepaths");
        FileUtilityKt.loadFilepaths(this);

        // First Run, Create Data
        if (FileUtilityKt.isFirstRun()) {
            Log.v(LOG, "First Run, Create all information and Launch the FirstRunActivity");
            MasterManagerKt.create();
            MasterManagerKt.save();
            Intent first_run = new Intent(this, FirstRun.class);
            startActivity(first_run);
        }

        // Every Other Run, Load Data
        else {
            Log.v(LOG, "Normal Run, Load the Data");
            MasterManagerKt.load();
        }

        // Find Views
        Log.v(LOG, "Finding Views");
        tasks = findViewById(R.id.tasks);
        fab = findViewById(R.id.fab);

        // Set Click Listeners
        Log.v(LOG, "Setting Click Listeners");
        fab.setOnClickListener(this);
        fab.setOnLongClickListener(this);

        Log.v(LOG, "onCreate is Complete");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG, "onResume called");

        // Set Adapters
        Log.v(LOG, "Setting Adapters");
        adapter = new HomeAdapter(this);
        tasks.setAdapter(adapter);

        Log.v(LOG, "onResume Done");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG, "onPause Called");

        // Save
        Log.v(LOG, "Saving");
        MasterManagerKt.save();

        Log.v(LOG, "onPause Done");
    }

    // Menu Methods
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tags:
                startActivity(new Intent(this, TagActivity.class));
                return true;

            case R.id.status:
                startActivity(new Intent(this, StatusActivity.class));
                return true;

            case R.id.priority:
                startActivity(new Intent(this, PriorityActivity.class));
                return true;

            case R.id.day:
                startActivity(new Intent(this, DayActivity.class));
                return true;

            case R.id.week:
                startActivity(new Intent(this, WeekActivity.class));
                return true;

            case R.id.month:
                startActivity(new Intent(this, MonthActivity.class));
                return true;

            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;

            case R.id.about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;

            case R.id.save:
                MasterManagerKt.save();
                Toast.makeText(this, "Saved.", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.close:
                finish();
                return true;
        }

        return false;
    }

    // Click Listeners
    @Override
    public void onClick(View view) {
        Log.d(LOG, "onClick Called");

        // Create Task
        Log.v(LOG, "Create Task");
        Task task = TaskManagerKt.createTask();

        // Create Intent
        Log.v(LOG, "Create Intent");
        Intent intent = new Intent(this, TaskActivity.class);
        intent.putExtra(ActivityUtility.TASK_NAME, task.getFilename());

        // Start Activity
        Log.v(LOG, "Start Activity");
        startActivity(intent);

        Log.v(LOG, "onClick Done");
    }

    @Override
    public boolean onLongClick(View view) {
        Log.d(LOG, "onLongClick Called");

        // Create Folder
        Log.v(LOG, "Create Folder");
        Task task = TaskManagerKt.createFolder();

        // Create Intent
        Log.v(LOG, "Create Intent");
        Intent intent = new Intent(this, TaskActivity.class);
        intent.putExtra(ActivityUtility.TASK_NAME, task.getFilename());

        // Start Activity
        Log.v(LOG, "Start Activity");
        startActivity(intent);
        return true;
    }

    @Override
    public void onTaskChanged() {
        onResume();
    }
}
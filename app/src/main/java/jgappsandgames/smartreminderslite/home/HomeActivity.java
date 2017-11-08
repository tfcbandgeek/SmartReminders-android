package jgappsandgames.smartreminderslite.home;

// Android OS
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

// Views
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

// App
import jgappsandgames.smartreminderslite.R;

import jgappsandgames.smartreminderslite.date.DayActivity;
import jgappsandgames.smartreminderslite.date.MonthActivity;
import jgappsandgames.smartreminderslite.date.WeekActivity;
import jgappsandgames.smartreminderslite.holder.TaskFolderHolder;
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
public class HomeActivity
        extends Activity
        implements View.OnClickListener, View.OnLongClickListener, TaskFolderHolder.OnTaskChangedListener {
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

        // Set Activity View
        setContentView(R.layout.activity_home);
        setTitle(R.string.app_name);

        // First Run
        FileUtilityKt.loadFilepaths(this);
        if (FileUtilityKt.isFirstRun()) {
            Intent first_run = new Intent(this, FirstRun.class);
            startActivity(first_run);
        } else {
            // Load Data
            MasterManagerKt.load();
        }

        // Find Views
        tasks = findViewById(R.id.tasks);
        fab = findViewById(R.id.fab);

        // Set Click Listeners
        fab.setOnClickListener(this);
        fab.setOnLongClickListener(this);
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
                save();
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
        // Create Task
        Task task = TaskManagerKt.createTask();

        // Create Intent
        Intent intent = new Intent(this, TaskActivity.class);
        intent.putExtra(ActivityUtility.TASK_NAME, task.getFilename());

        // Start Activity
        startActivity(intent);
    }

    @Override
    public boolean onLongClick(View view) {
        // Create Task
        Task task = TaskManagerKt.createFolder();
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

    private void save() {
        MasterManagerKt.save();
    }
}
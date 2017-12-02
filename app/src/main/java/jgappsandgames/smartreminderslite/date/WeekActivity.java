package jgappsandgames.smartreminderslite.date;

// Java
import java.util.Calendar;

// Android OS
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

// Views
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

// App
import jgappsandgames.smartreminderslite.R;

import jgappsandgames.smartreminderslite.holder.TaskFolderHolder.OnTaskChangedListener;
import jgappsandgames.smartreminderslite.home.FirstRun;

// Save
import jgappsandgames.smartreminderssave.date.DateManager;
import jgappsandgames.smartreminderssave.settings.Settings;
import jgappsandgames.smartreminderssave.tags.TagManager;
import jgappsandgames.smartreminderssave.tasks.TaskManager;
import jgappsandgames.smartreminderssave.utility.FileUtility;

/**
 * WeekActivity
 * Created by joshua on 10/9/17.
 */
public class WeekActivity extends Activity implements OnClickListener, OnTaskChangedListener {
    // Data
    private int week_active;

    // Views
    private ListView tasks;
    private Button previous;
    private Button next;

    // Adapters
    private BaseAdapter adapter;

    // LifeCycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set Content View
        setContentView(R.layout.activity_date);

        // First Run
        FileUtility.loadFilePaths(this);
        if (FileUtility.isFirstRun()) {
            Intent first_run = new Intent(this, FirstRun.class);
            startActivity(first_run);
        }

        // Load Data
        Settings.load();
        TaskManager.load();
        TagManager.load();

        week_active = 0;

        // Set Title
        setTitle();

        // Find Views
        tasks = findViewById(R.id.tasks);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);

        // Set Click Listeners
        previous.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        adapter = new WeekAdapter(this, week_active);
        tasks.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        save();
    }

    // Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_auxilary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                TaskManager.save();
                Toast.makeText(this, "Saved.", Toast.LENGTH_LONG).show();
                break;

            case R.id.close:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    // Click Listeners
    @Override
    public void onClick(View view) {
        // Previous
        if (view.equals(previous)) {
            week_active--;
            if (week_active < 0) week_active = 0;

            adapter = new WeekAdapter(this, week_active);
            tasks.setAdapter(adapter);

            setTitle();
        }

        // Next
        else if (view.equals(next)) {
            week_active++;

            adapter = new WeekAdapter(this, week_active);
            tasks.setAdapter(adapter);

            setTitle();
        }
    }

    // Task Changed Listener
    @Override
    public void onTaskChanged() {
        onResume();
    }

    // Private Class Methods
    private void setTitle() {
        Calendar start = DateManager.getWeek(week_active).getStart();
        Calendar end = DateManager.getWeek(week_active).getEnd();

        setTitle(String.valueOf(start.get(Calendar.MONTH) + 1) + "/" +
                String.valueOf(start.get(Calendar.DAY_OF_MONTH)) + " - " +
                String.valueOf(end.get(Calendar.MONTH) + 1) + "/" +
                String.valueOf(end.get(Calendar.DAY_OF_MONTH)));
    }

    private void save() {
        // Save
        TaskManager.save();
        TagManager.save();
        Settings.save();
    }
}
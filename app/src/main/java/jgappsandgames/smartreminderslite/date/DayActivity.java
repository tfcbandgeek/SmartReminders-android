package jgappsandgames.smartreminderslite.date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.Calendar;

import jgappsandgames.smartreminderslite.R;
import jgappsandgames.smartreminderslite.holder.TaskFolderHolder;
import jgappsandgames.smartreminderslite.home.FirstRun;
import jgappsandgames.smartreminderssave.settings.Settings;
import jgappsandgames.smartreminderssave.tags.TagManager;
import jgappsandgames.smartreminderssave.tasks.TaskManager;
import jgappsandgames.smartreminderssave.utility.FileUtility;

/**
 * DayActivity
 * Created by joshua on 10/9/17.
 * Last Edited on 10/9/17 (126).
 */
public class DayActivity extends Activity
        implements View.OnClickListener, TaskFolderHolder.OnTaskChangedListener {
    // Data
    private Calendar day_active;

    // Views
    private ListView tasks;
    private Button previous;
    private Button next;

    // Adapter
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

        day_active = Calendar.getInstance();

        // Set Title
        setTitle();

        // Find Views
        tasks = (findViewById(R.id.tasks));
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);

        // Set Click Listeners
        previous.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        adapter = new DayAdapter(this, day_active);
        tasks.setAdapter(adapter);
    }

    // Click Listeners
    @Override
    public void onClick(View view) {
        // Previous
        if (view.equals(previous)) {
            day_active.add(Calendar.DAY_OF_MONTH, -1);

            if (day_active.before(Calendar.getInstance())) day_active = Calendar.getInstance();

            adapter = new DayAdapter(this, day_active);
            tasks.setAdapter(adapter);

            setTitle();
        }

        // Next
        else if (view.equals(next)) {
            day_active.add(Calendar.DAY_OF_MONTH, 1);

            adapter = new DayAdapter(this, day_active);
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
    private void save() {
        // Save
        TaskManager.save();
        TagManager.save();
        Settings.save();
    }

    // Private Class Methods
    private void setTitle() {
        setTitle(String.valueOf(day_active.get(Calendar.MONTH) + 1) + "/" + String.valueOf(day_active.get(Calendar.DAY_OF_MONTH)));
    }
}
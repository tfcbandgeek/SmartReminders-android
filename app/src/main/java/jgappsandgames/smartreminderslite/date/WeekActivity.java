package jgappsandgames.smartreminderslite.date;

// Java
import java.util.Calendar;

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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

// App
import jgappsandgames.smartreminderslite.R;

import jgappsandgames.smartreminderslite.holder.TaskFolderHolder.OnTaskChangedListener;
import jgappsandgames.smartreminderslite.home.FirstRun;

// Save
import jgappsandgames.smartreminderssave.MasterManagerKt;
import jgappsandgames.smartreminderssave.date.DateManagerKt;
import jgappsandgames.smartreminderssave.utility.FileUtilityKt;


/**
 * WeekActivity
 * Created by joshua on 10/9/17.
 */
public class WeekActivity extends Activity implements OnClickListener, OnTaskChangedListener {
    // Log Constants
    private static final String LOG = "WeekActivity";

    // Data
    private Calendar week_active;

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
        Log.d(LOG, "onCreate Called");

        // Set Content View
        Log.v(LOG, "Setting the Content View");
        setContentView(R.layout.activity_date);

        // First Run
        FileUtilityKt.loadFilepaths(this);
        if (FileUtilityKt.isFirstRun()) {
            Log.v(LOG, "First Run, Create the Files");
            Intent first_run = new Intent(this, FirstRun.class);
            startActivity(first_run);
        } else {
            // Load Data
            Log.v(LOG, "Normal Run, Load the Files");
            MasterManagerKt.load();
        }

        week_active = Calendar.getInstance();
        week_active.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

        // Set Title
        Log.v(LOG, "Set the Title");
        setTitle();

        // Find Views
        Log.v(LOG, "Find the Views");
        tasks = findViewById(R.id.tasks);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);

        // Set Click Listeners
        Log.v(LOG, "Set the Click Listeners");
        previous.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG, "onResume Called");

        // Set Adapter
        Log.v(LOG, "Set Adapters");
        adapter = new WeekAdapter(this, week_active);
        tasks.setAdapter(adapter);

        Log.v(LOG, "onResume is Done");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG, "onResume Called");

        Log.v(LOG, "Saving");
        save();

        Log.v(LOG, "onResume Done");
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
                save();
                Toast.makeText(this, "Saved.", Toast.LENGTH_LONG).show();
                return true;

            case R.id.close:
                finish();
                return true;

            case R.id.refresh:
                DateManagerKt.createDates();
                DateManagerKt.saveDates();
                onResume();
                Toast.makeText(this, "Refreshed", Toast.LENGTH_SHORT).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Click Listeners
    @Override
    public void onClick(View view) {
        Log.d(LOG, "onClick Called");

        // Previous
        if (view.equals(previous)) {
            Log.v(LOG, "Previous Button is Pressed");
            week_active.add(Calendar.WEEK_OF_YEAR, -1);

            adapter = new WeekAdapter(this, week_active);
            tasks.setAdapter(adapter);

            setTitle();
        }

        // Next
        else if (view.equals(next)) {
            Log.v(LOG, "Next Button is Pressed");
            week_active.add(Calendar.WEEK_OF_YEAR, 1);

            adapter = new WeekAdapter(this, week_active);
            tasks.setAdapter(adapter);

            setTitle();
        }

        Log.v(LOG, "onClick Done");
    }

    // Task Changed Listener
    @Override
    public void onTaskChanged() {
        onResume();
    }

    // Private Class Methods
    private void setTitle() {
        Calendar start = DateManagerKt.getWeek(week_active).getStart();//DateManager.getWeek(week_active).getStart();
        Calendar end = DateManagerKt.getWeek(week_active).getEnd();

        setTitle(String.valueOf(start.get(Calendar.MONTH) + 1) + "/" +
                String.valueOf(start.get(Calendar.DAY_OF_MONTH)) + " - " +
                String.valueOf(end.get(Calendar.MONTH) + 1) + "/" +
                String.valueOf(end.get(Calendar.DAY_OF_MONTH)));
    }

    private void save() {
        MasterManagerKt.save();
    }
}
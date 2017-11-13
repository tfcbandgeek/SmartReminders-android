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
import jgappsandgames.smartreminderssave.utility.FileUtilityKt;


/**
 * DayActivity
 * Created by joshua on 10/9/17.
 */
public class DayActivity extends Activity implements OnClickListener, OnTaskChangedListener {
    // Log Constants
    private static final String LOG = "DayActivity";

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

        Log.d(LOG, "onCreate Called");

        // Set Content View
        Log.v(LOG, "Setting the Content View");
        setContentView(R.layout.activity_date);

        // First Run
        FileUtilityKt.loadFilepaths(this);
        if (FileUtilityKt.isFirstRun()) {
            Log.v(LOG, "It is the First Run, Call the First Run Activity.");
            Intent first_run = new Intent(this, FirstRun.class);
            startActivity(first_run);
        } else {
            // Load Data
            Log.v(LOG, "It is not the first Run Loading data");
            MasterManagerKt.load();
        }

        day_active = Calendar.getInstance();

        // Set Title
        Log.v(LOG, "Setting the Title");
        setTitle();

        // Find Views
        Log.v(LOG, "Finding Views");
        tasks = (findViewById(R.id.tasks));
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);

        // Set Click Listeners
        Log.v(LOG, "Setting Click Listeners");
        previous.setOnClickListener(this);
        next.setOnClickListener(this);

        Log.v(LOG, "onCreate Done");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(LOG, "onResume Called");

        // Reset the Adapter (It is possible that the information it was based on has changed)
        adapter = new DayAdapter(this, day_active);
        tasks.setAdapter(adapter);

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
                MasterManagerKt.save();
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
        Log.d(LOG, "onClick Called");

        // Previous
        if (view.equals(previous)) {
            Log.v(LOG, "Previous Button Pressed");
            day_active.add(Calendar.DAY_OF_MONTH, -1);

            if (day_active.before(Calendar.getInstance())) day_active = Calendar.getInstance();

            adapter = new DayAdapter(this, day_active);
            tasks.setAdapter(adapter);

            setTitle();
        }

        // Next
        else if (view.equals(next)) {
            Log.v(LOG, "Next Button Pressed");
            day_active.add(Calendar.DAY_OF_MONTH, 1);

            adapter = new DayAdapter(this, day_active);
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

    // Method to Set the Days Title
    private void setTitle() {
        setTitle(String.valueOf(day_active.get(Calendar.MONTH) + 1) + "/" + String.valueOf(day_active.get(Calendar.DAY_OF_MONTH)));
    }
}
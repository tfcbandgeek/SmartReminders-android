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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

// App
import jgappsandgames.smartreminderslite.R;

import jgappsandgames.smartreminderslite.holder.TaskFolderHolder;
import jgappsandgames.smartreminderslite.home.FirstRun;

// Save
import jgappsandgames.smartreminderssave.MasterManagerKt;
import jgappsandgames.smartreminderssave.utility.FileUtilityKt;


/**
 * DayActivity
 * Created by joshua on 10/9/17.
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
        FileUtilityKt.loadFilepaths(this);
        if (FileUtilityKt.isFirstRun()) {
            Intent first_run = new Intent(this, FirstRun.class);
            startActivity(first_run);
        }

        // Load Data
        MasterManagerKt.load();

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

        // Reset the Adapter (It is possible that the information it was based on has changed)
        adapter = new DayAdapter(this, day_active);
        tasks.setAdapter(adapter);
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

    // Method to Set the Days Title
    private void setTitle() {
        setTitle(String.valueOf(day_active.get(Calendar.MONTH) + 1) + "/" + String.valueOf(day_active.get(Calendar.DAY_OF_MONTH)));
    }
}
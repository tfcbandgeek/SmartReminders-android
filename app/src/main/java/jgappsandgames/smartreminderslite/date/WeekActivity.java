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
import jgappsandgames.smartreminderssave.MasterManagerKt;
import jgappsandgames.smartreminderssave.daily.DailyManagerKt;
import jgappsandgames.smartreminderssave.date.DateManagerKt;
import jgappsandgames.smartreminderssave.utility.FileUtilityKt;

// Save


/**
 * WeekActivity
 * Created by joshua on 10/9/17.
 * Last Last Edited on 10/12/17 (175).
 * Edited on 10/11/17 (149).
 * Edited on 10/9/17 (140).
 */
public class WeekActivity
        extends Activity
        implements OnClickListener, OnTaskChangedListener {
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

        week_active = Calendar.getInstance();
        week_active.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

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
                save();
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
            week_active.add(Calendar.WEEK_OF_YEAR, -1);

            adapter = new WeekAdapter(this, week_active);
            tasks.setAdapter(adapter);

            setTitle();
        }

        // Next
        else if (view.equals(next)) {
            week_active.add(Calendar.WEEK_OF_YEAR, 1);

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
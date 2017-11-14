package jgappsandgames.smartreminderslite.date;

// Java
import java.util.ArrayList;
import java.util.Calendar;

// Android OS
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

// Vies
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.ListView;
import android.widget.Toast;

// App
import jgappsandgames.smartreminderslite.R;
import jgappsandgames.smartreminderslite.holder.TaskFolderHolder.OnTaskChangedListener;
import jgappsandgames.smartreminderslite.home.FirstRun;

// Save
import jgappsandgames.smartreminderssave.MasterManagerKt;
import jgappsandgames.smartreminderssave.date.DateManagerKt;
import jgappsandgames.smartreminderssave.tasks.Task;
import jgappsandgames.smartreminderssave.utility.FileUtilityKt;

/**
 * MonthActivityInterface
 * Created by joshua on 10/18/17.
 */
abstract class MonthActivityInterface extends Activity implements OnTaskChangedListener, OnDateChangeListener {
    // Log Constants
    private static final String LOG = "MonthActivityInterface";

    // Data
    Calendar selected;
    ArrayList<Task> selected_tasks;

    // Views
    @SuppressWarnings("FieldCanBeLocal")
    private CalendarView calendar;
    ListView tasks;

    // LifeCycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG, "onCreate Called");

        // Set Activity View
        Log.v(LOG, "Set the Content View");
        setContentView(R.layout.activity_month);

        // First Run
        FileUtilityKt.loadFilepaths(this);
        if (FileUtilityKt.isFirstRun()) {
            Log.v(LOG, "First Run, Launching the First Run Activity");
            Intent first_run = new Intent(this, FirstRun.class);
            startActivity(first_run);
        } else {
            // Load Data
            Log.v(LOG, "Normal Run, Loading the Data");
            MasterManagerKt.load();
        }

        // Find Views
        Log.v(LOG, "Finding the Views");
        calendar = findViewById(R.id.calendar);
        tasks = findViewById(R.id.tasks);

        // Set Calendar
        Log.v(LOG, "Setting the Calendar");
        calendar.setOnDateChangeListener(this);
        selected = Calendar.getInstance();
        selected.setTimeInMillis(calendar.getDate());

        Log.v(LOG, "onCreate Done");
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
                return true;

            case R.id.close:
                finish();

            case R.id.refresh:
                DateManagerKt.createDates();
                DateManagerKt.saveDates();
                onResume();
                Toast.makeText(this, "Refreshed", Toast.LENGTH_SHORT).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Task Changed Listener
    @Override
    public void onTaskChanged() {
        onResume();
    }
}
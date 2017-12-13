package jgappsandgames.smartreminderslite.date;

// Java
import java.util.ArrayList;
import java.util.Calendar;

// Android OS
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

// Views
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.ListView;
import android.widget.Toast;

// App
import jgappsandgames.smartreminderslite.R;
import jgappsandgames.smartreminderslite.holder.TaskFolderHolder.OnTaskChangedListener;

// Save
import jgappsandgames.smartreminderslite.home.FirstRun;
import jgappsandgames.smartreminderssave.MasterManager;
import jgappsandgames.smartreminderssave.tasks.Task;
import jgappsandgames.smartreminderssave.tasks.TaskManager;
import jgappsandgames.smartreminderssave.utility.FileUtility;

/**
 * MonthActivityInterface
 * Created by joshua on 10/18/17.
 */
abstract class MonthActivityInterface extends Activity implements OnTaskChangedListener, OnDateChangeListener {
    // Data
    protected Calendar selected;
    protected ArrayList<Task> selected_tasks;

    // Views
    protected CalendarView calendar;
    protected ListView tasks;

    // LifeCycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_month);

        // Find Views
        calendar = findViewById(R.id.calendar);
        tasks = findViewById(R.id.tasks);

        // First Run
        FileUtility.loadFilePaths(this);
        if (FileUtility.isFirstRun()) {
            Intent first_run = new Intent(this, FirstRun.class);
            startActivity(first_run);
        } else {
            // Load Data
            MasterManager.load(this);
        }

        // Set Listeners
        calendar.setOnDateChangeListener(this);

        // Set Calendar
        selected = Calendar.getInstance();
        selected.setTimeInMillis(calendar.getDate());
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
                MasterManager.save();
                Toast.makeText(this, "Saved.", Toast.LENGTH_LONG).show();
                break;

            case R.id.close:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    // Task Changed Listener
    @Override
    public void onTaskChanged() {
        onResume();
    }
}
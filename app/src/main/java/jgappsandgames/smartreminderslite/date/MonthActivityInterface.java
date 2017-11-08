package jgappsandgames.smartreminderslite.date;

// Java
import java.util.Calendar;
import java.util.List;

// Android OS
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

// Vies
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

// App
import jgappsandgames.smartreminderslite.R;
import jgappsandgames.smartreminderslite.holder.TaskFolderHolder;
import jgappsandgames.smartreminderslite.home.FirstRun;

// Save
import jgappsandgames.smartreminderssave.MasterManagerKt;
import jgappsandgames.smartreminderssave.tasks.Task;
import jgappsandgames.smartreminderssave.utility.FileUtilityKt;

/**
 * MonthActivityInterface
 * Created by joshua on 10/18/17.
 */
abstract class MonthActivityInterface
        extends Activity
        implements TaskFolderHolder.OnTaskChangedListener, CalendarView.OnDateChangeListener {
    // Data
    protected Calendar selected;
    protected List<Task> selected_tasks;

    // Views
    protected CalendarView calendar;
    protected ListView tasks;

    // LifeCycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set Activity View
        setContentView(R.layout.activity_month);

        // First Run
        FileUtilityKt.loadFilepaths(this);
        if (FileUtilityKt.isFirstRun()) {
            Intent first_run = new Intent(this, FirstRun.class);
            startActivity(first_run);
        }

        // Load Data
        MasterManagerKt.load();

        // Find Views
        calendar = findViewById(R.id.calendar);
        tasks = findViewById(R.id.tasks);

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
                save();
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

    // Class Methods
    public void save() {
        MasterManagerKt.save();
    }
}
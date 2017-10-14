package jgappsandgames.smartreminderslite.date;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.BaseAdapter;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import jgappsandgames.smartreminderslite.R;
import jgappsandgames.smartreminderslite.holder.TaskFolderHolder.OnTaskChangedListener;
import jgappsandgames.smartreminderssave.date.DateManager;
import jgappsandgames.smartreminderssave.tasks.Task;
import jgappsandgames.smartreminderssave.tasks.TaskManager;

/**
 * MonthActivity
 * Created by joshua on 10/12/17.
 * Last Edited on 10/14/17 (106).
 */
public class MonthActivity extends Activity implements OnDateChangeListener, OnTaskChangedListener {
    // Data
    private Calendar selected;
    private List<Task> selected_tasks;

    // Views
    private CalendarView calendar;
    private ListView tasks;

    // Adapter
    private BaseAdapter adapter;

    // LifeCycleMethods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month);

        // Find Views
        calendar = findViewById(R.id.calendar);
        tasks = findViewById(R.id.tasks);

        // Set Listeners
        calendar.setOnDateChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Set Calendar
        selected = Calendar.getInstance();
        selected.setTimeInMillis(calendar.getDate());

        // Set List
        DateManager.create();
        selected_tasks  = DateManager.getDay(selected);
        adapter = new MonthAdapter(this, selected_tasks);
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
                TaskManager.save();
                Toast.makeText(this, "Saved.", Toast.LENGTH_LONG).show();
                break;

            case R.id.close:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    // On Date Set Presses
    @Override
    public void onSelectedDayChange(CalendarView calendar, int year, int month, int day) {
        selected.set(Calendar.YEAR, year);
        selected.set(Calendar.MONTH, month);
        selected.set(Calendar.DAY_OF_MONTH, day);
        selected_tasks = DateManager.getDay(selected);
        adapter = new MonthAdapter(this, selected_tasks);
        tasks.setAdapter(adapter);
    }

    // Task Changed Listener
    @Override
    public void onTaskChanged() {
        onResume();
    }
}
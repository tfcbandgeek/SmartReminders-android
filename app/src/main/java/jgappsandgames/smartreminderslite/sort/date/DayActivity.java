package jgappsandgames.smartreminderslite.sort.date;

// Java
import java.util.Calendar;

// Views
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

// Save
import jgappsandgames.smartreminderssave.MasterManager;

/**
 * DayActivity
 * Created by joshua on 10/9/17.
 */
public class DayActivity extends DayActivityInterface  {
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
    @Override
    public void save() {
        MasterManager.save();
    }

    // Private Class Methods
    @Override
    public void setTitle() {
        setTitle(String.valueOf(day_active.get(Calendar.MONTH) + 1) + "/" + String.valueOf(day_active.get(Calendar.DAY_OF_MONTH)));
    }
}
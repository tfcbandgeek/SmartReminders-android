package jgappsandgames.smartreminderslite.date;

// Java
import java.util.Calendar;

// Android OS
import android.content.Intent;
import android.os.Bundle;

// Views
import android.view.View;

// App
import jgappsandgames.smartreminderslite.home.FirstRun;

// Save
import jgappsandgames.smartreminderssave.date.DateManager;
import jgappsandgames.smartreminderssave.settings.Settings;
import jgappsandgames.smartreminderssave.tags.TagManager;
import jgappsandgames.smartreminderssave.tasks.TaskManager;
import jgappsandgames.smartreminderssave.utility.FileUtility;

/**
 * WeekActivity
 * Created by joshua on 10/9/17.
 */
public class WeekActivity extends WeekActivityInterface {
    // LifeCycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        week_active = 0;

        // Set Title
        setTitle();
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

    // Click Listeners
    @Override
    public void onClick(View view) {
        // Previous
        if (view.equals(previous)) {
            week_active--;
            if (week_active < 0) week_active = 0;

            adapter = new WeekAdapter(this, week_active);
            tasks.setAdapter(adapter);

            setTitle();
        }

        // Next
        else if (view.equals(next)) {
            week_active++;

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
        Calendar start = DateManager.getWeek(week_active).getStart();
        Calendar end = DateManager.getWeek(week_active).getEnd();

        setTitle(String.valueOf(start.get(Calendar.MONTH) + 1) + "/" +
                String.valueOf(start.get(Calendar.DAY_OF_MONTH)) + " - " +
                String.valueOf(end.get(Calendar.MONTH) + 1) + "/" +
                String.valueOf(end.get(Calendar.DAY_OF_MONTH)));
    }

    @Override
    public void save() {
        TaskManager.save();
        TagManager.save();
        Settings.save();
    }
}
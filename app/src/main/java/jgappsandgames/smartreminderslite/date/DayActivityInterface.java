package jgappsandgames.smartreminderslite.date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Calendar;

import jgappsandgames.smartreminderslite.R;
import jgappsandgames.smartreminderslite.holder.TaskFolderHolder;
import jgappsandgames.smartreminderslite.home.FirstRun;
import jgappsandgames.smartreminderssave.settings.Settings;
import jgappsandgames.smartreminderssave.tags.TagManager;
import jgappsandgames.smartreminderssave.tasks.TaskManager;
import jgappsandgames.smartreminderssave.utility.FileUtility;

/**
 * Created by joshu on 11/29/2017.
 */

public abstract class DayActivityInterface extends Activity implements View.OnClickListener, TaskFolderHolder.OnTaskChangedListener {
    // Data
    protected Calendar day_active;

    // Views
    protected ListView tasks;
    protected Button previous;
    protected Button next;

    // Adapter
    protected BaseAdapter adapter;

    // LifeCycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set Content View
        setContentView(R.layout.activity_date);

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

    // Abstract Methods
    public abstract void setTitle();
    public abstract void save();
}

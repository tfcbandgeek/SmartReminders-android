package jgappsandgames.smartreminderslite.priority;

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

// Program
import jgappsandgames.smartreminderslite.R;

import jgappsandgames.smartreminderslite.holder.TaskFolderHolder.OnTaskChangedListener;
import jgappsandgames.smartreminderslite.home.FirstRun;

// Save
import jgappsandgames.smartreminderssave.MasterManagerKt;
import jgappsandgames.smartreminderssave.date.DateManagerKt;
import jgappsandgames.smartreminderssave.priority.PriorityManagerKt;
import jgappsandgames.smartreminderssave.utility.FileUtilityKt;

/**
 * PriorityActivity
 * Created by joshua on 10/1/17.
 * Last Edited on 10/12/17 (293).
 * Edited on 10/9/17 (263).
 */
public class PriorityActivity
        extends Activity
        implements OnClickListener, OnTaskChangedListener {
    // Data
    private int position = 3;

    // Views
    private ListView tasks;
    private Button down;
    private Button up;

    // Adapters
    private BaseAdapter ignore_adapter;
    private BaseAdapter low_adapter;
    private BaseAdapter normal_adapter;
    private BaseAdapter high_adapter;
    private BaseAdapter stared_adapter;

    // LifeCycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set Activity View
        setContentView(R.layout.activity_priority);

        // First Run
        FileUtilityKt.loadFilepaths(this);
        if (FileUtilityKt.isFirstRun()) {
            Intent first_run = new Intent(this, FirstRun.class);
            startActivity(first_run);
        } else {
            // Load Data
            MasterManagerKt.load();
        }

        tasks = findViewById(R.id.tasks);
        down = findViewById(R.id.down);
        up = findViewById(R.id.up);

        down.setOnClickListener(this);
        up.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ignore_adapter = null;
        low_adapter = null;
        normal_adapter = null;
        high_adapter = null;
        stared_adapter = null;

        setAdapter();
        setTitle();
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
                return true;

            case R.id.close:
                finish();
                return true;

            case R.id.refresh:
                DateManagerKt.createDates();
                DateManagerKt.saveDates();
                onResume();
                Toast.makeText(this, "Refreshed", Toast.LENGTH_SHORT).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Click Listeners
    @Override
    public void onClick(View view) {
        if (view.equals(down)) {
            switch (position) {
                case 1:
                    break;

                case 2:
                    position = 1;
                    setAdapter();
                    setTitle();
                    break;

                case 3:
                    position = 2;
                    setAdapter();
                    setTitle();
                    break;

                case 4:
                    position = 3;
                    setAdapter();
                    setTitle();
                    break;

                case 5:
                    position  = 4;
                    setAdapter();
                    setTitle();
                    break;
            }
        } else if (view.equals(up)) {
            switch (position) {
                case 1:
                    position = 2;
                    setAdapter();
                    setTitle();
                    break;

                case 2:
                    position = 3;
                    setAdapter();
                    setTitle();
                    break;

                case 3:
                    position = 4;
                    setAdapter();
                    setTitle();
                    break;

                case 4:
                    position = 5;
                    setAdapter();
                    setTitle();
                    break;

                case 5:
                    break;
            }
        }
    }

    // Task Changed Listeners
    @Override
    public void onTaskChanged() {
        onResume();
    }

    // Private Class Methods
    private void setTitle() {
        switch (position) {
            case 1:
                setTitle("Ignore");
                down.setText("");
                up.setText(R.string.low);
                return;

            case 2:
                setTitle("Low Priority (Default)");
                down.setText(R.string.ignore);
                up.setText(R.string.normal);
                return;

            case 3:
                setTitle("Normal Priority");
                down.setText(R.string.low);
                up.setText(R.string.high);
                return;

            case 4:
                setTitle("High Priority");
                down.setText(R.string.normal);
                up.setText(R.string.stared);
                return;

            case 5:
                setTitle("Stared Tasks");
                down.setText(R.string.high);
                up.setText("");
                break;
        }
    }

    private void setAdapter() {
        switch (position) {
            case 1:
                if (ignore_adapter == null) ignore_adapter = new PriorityAdapter(this, PriorityManagerKt.getIgnore());
                tasks.setAdapter(ignore_adapter);
                return;

            case 2:
                if (low_adapter == null) low_adapter = new PriorityAdapter(this, PriorityManagerKt.getLow());
                tasks.setAdapter(low_adapter);
                return;

            case 3:
                if (normal_adapter == null) normal_adapter = new PriorityAdapter(this, PriorityManagerKt.getNormal());
                tasks.setAdapter(normal_adapter);
                return;

            case 4:
                if (high_adapter == null) high_adapter = new PriorityAdapter(this, PriorityManagerKt.getHigh());
                tasks.setAdapter(high_adapter);
                return;

            case 5:
                if (stared_adapter == null) stared_adapter = new PriorityAdapter(this, PriorityManagerKt.getStar());
                tasks.setAdapter(stared_adapter);
                break;
        }
    }

    private void save() {
        MasterManagerKt.save();
    }
}
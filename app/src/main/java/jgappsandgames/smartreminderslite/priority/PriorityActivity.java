package jgappsandgames.smartreminderslite.priority;

// Java
import java.util.ArrayList;
import java.util.List;

// Android OS
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

// Views
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

// Program
import jgappsandgames.smartreminderslite.R;

import jgappsandgames.smartreminderslite.holder.TaskFolderHolder.OnTaskChangedListener;
import jgappsandgames.smartreminderslite.home.FirstRun;

// Save
import jgappsandgames.smartreminderssave.settings.Settings;
import jgappsandgames.smartreminderssave.tags.TagManager;
import jgappsandgames.smartreminderssave.tasks.Task;
import jgappsandgames.smartreminderssave.tasks.TaskManager;
import jgappsandgames.smartreminderssave.utility.FileUtility;

/**
 * PriorityActivity
 * Created by joshua on 10/1/17.
 * Last Edited on 10/9/17 (263).
 */
public class PriorityActivity
        extends Activity
        implements OnClickListener, OnTaskChangedListener {
    // Data
    private int position = 3;

    private List<Task> ignore_tasks;
    private List<Task> low_tasks;
    private List<Task> normal_tasks;
    private List<Task> high_tasks;
    private List<Task> stared_tasks;

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
        if (FileUtility.isFirstRun()) {
            Intent first_run = new Intent(this, FirstRun.class);
            startActivity(first_run);
        }

        // Load Data
        Settings.load();
        TaskManager.load();
        TagManager.load();

        ignore_tasks = new ArrayList<>();
        low_tasks = new ArrayList<>();
        normal_tasks = new ArrayList<>();
        high_tasks = new ArrayList<>();
        stared_tasks = new ArrayList<>();

        tasks = findViewById(R.id.tasks);
        down = findViewById(R.id.down);
        up = findViewById(R.id.up);

        for (String t : TaskManager.tasks) {
            Task task= new Task(t);

            if (task.getType() == Task.TYPE_FLDR) continue;

            if (task.getPriority() == 0) ignore_tasks.add(task);
            else if (task.getPriority() <= 33) low_tasks.add(task);
            else if (task.getPriority() <= 66) normal_tasks.add(task);
            else if (task.getPriority() <= 99) high_tasks.add(task);
            else stared_tasks.add(task);
        }

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
                up.setText("Low");
                return;

            case 2:
                setTitle("Low Priority (Default)");
                down.setText("Ignore");
                up.setText("Normal");
                return;

            case 3:
                setTitle("Normal Priority");
                down.setText("Low");
                up.setText("High");
                return;

            case 4:
                setTitle("High Priority");
                down.setText("Normal");
                up.setText("Stared");
                return;

            case 5:
                setTitle("Stared Tasks");
                down.setText("High");
                up.setText("");
                break;
        }
    }

    private void setAdapter() {
        switch (position) {
            case 1:
                if (ignore_adapter == null) ignore_adapter = new PriorityAdapter(this, ignore_tasks);
                tasks.setAdapter(ignore_adapter);
                return;

            case 2:
                if (low_adapter == null) low_adapter = new PriorityAdapter(this, low_tasks);
                tasks.setAdapter(low_adapter);
                return;

            case 3:
                if (normal_adapter == null) normal_adapter = new PriorityAdapter(this, normal_tasks);
                tasks.setAdapter(normal_adapter);
                return;

            case 4:
                if (high_adapter == null) high_adapter = new PriorityAdapter(this, high_tasks);
                tasks.setAdapter(high_adapter);
                return;

            case 5:
                if (stared_adapter == null) stared_adapter = new PriorityAdapter(this, stared_tasks);
                tasks.setAdapter(stared_adapter);
                break;
        }
    }

    private void save() {
        // Save
        TaskManager.save();
        TagManager.save();
        Settings.save();
    }
}
package jgappsandgames.smartreminderslite.priority;

// Java
import java.util.ArrayList;

// Android OS
import android.content.Intent;
import android.os.Bundle;

// Views
import android.view.View;

// Program
import jgappsandgames.smartreminderslite.R;
import jgappsandgames.smartreminderslite.home.FirstRun;

// Save
import jgappsandgames.smartreminderssave.MasterManager;
import jgappsandgames.smartreminderssave.tags.TagManager;
import jgappsandgames.smartreminderssave.tasks.Task;
import jgappsandgames.smartreminderssave.tasks.TaskManager;
import jgappsandgames.smartreminderssave.utility.FileUtility;

/**
 * PriorityActivity
 * Created by joshua on 10/1/17.
 */
public class PriorityActivity extends PriorityActivityInterface {
    // Data
    private ArrayList<Task> ignore_tasks;
    private ArrayList<Task> low_tasks;
    private ArrayList<Task> normal_tasks;
    private ArrayList<Task> high_tasks;
    private ArrayList<Task> stared_tasks;

    // LifeCycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // First Run
        if (FileUtility.isFirstRun()) {
            Intent first_run = new Intent(this, FirstRun.class);
            startActivity(first_run);
        } else {
            MasterManager.load(this);
        }

        ignore_tasks = new ArrayList<>();
        low_tasks = new ArrayList<>();
        normal_tasks = new ArrayList<>();
        high_tasks = new ArrayList<>();
        stared_tasks = new ArrayList<>();

        for (String t : TaskManager.tasks) {
            Task task= new Task(t);

            if (task.getType() == Task.TYPE_FLDR) continue;

            if (task.getPriority() == 0) ignore_tasks.add(task);
            else if (task.getPriority() <= 33) low_tasks.add(task);
            else if (task.getPriority() <= 66) normal_tasks.add(task);
            else if (task.getPriority() <= 99) high_tasks.add(task);
            else stared_tasks.add(task);
        }
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

    @Override
    public void save() {
        MasterManager.save();
    }
}
package jgappsandgames.smartreminderslite.sort.tags;

// Java
import java.util.ArrayList;

// Android OS
import android.os.Bundle;

// App

// Save
import jgappsandgames.smartreminderssave.MasterManager;
import jgappsandgames.smartreminderssave.tasks.Task;
import jgappsandgames.smartreminderssave.tasks.TaskManager;

/**
 * TagActivity
 * Created by joshua on 9/2/17.
 */
public class TagActivity extends TagActivityInterface {
    // Data
    private ArrayList<String> selected_tags;
    private ArrayList<Task> tasks;

    // LifeCycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load Data
        selected_tags = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();

        tasks = new ArrayList<>();
        for (int i = 0; i < TaskManager.tasks.size(); i++) tasks.add(new Task(TaskManager.tasks.get(i)));

        // Set Adapters
        task_adapter = new TaskAdapter(this, selected_tags, tasks);
        selected_adapter = new SelectedAdapter(this, selected_tags);
        unselected_adapter = new UnselectedAdapter(this, selected_tags);

        tasks_list.setAdapter(task_adapter);
        selected_list.setAdapter(selected_adapter);
        unselected_list.setAdapter(unselected_adapter);
    }

    // Tag Switcher
    @Override
    public void moveTag(String tag, boolean selected) {
        if (selected && !selected_tags.contains(tag)) selected_tags.add(tag);
        else if (!selected && selected_tags.contains(tag)) selected_tags.remove(tag);

        task_adapter = new TaskAdapter(this, selected_tags, tasks);
        selected_adapter = new SelectedAdapter(this, selected_tags);
        unselected_adapter = new UnselectedAdapter(this, selected_tags);

        tasks_list.setAdapter(task_adapter);
        selected_list.setAdapter(selected_adapter);
        unselected_list.setAdapter(unselected_adapter);
    }

    @Override
    public void onTaskChanged() {
        onResume();
    }

    @Override
    public void save() {
        MasterManager.save();
    }
}
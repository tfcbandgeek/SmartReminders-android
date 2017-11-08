package jgappsandgames.smartreminderslite.tags;

// Java
import java.util.ArrayList;
import java.util.List;

// Android OS
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

// App
import jgappsandgames.smartreminderslite.R;

import jgappsandgames.smartreminderslite.holder.TaskFolderHolder;
import jgappsandgames.smartreminderslite.holder.TagHolder;

// Save
import jgappsandgames.smartreminderslite.home.FirstRun;
import jgappsandgames.smartreminderssave.MasterManagerKt;
import jgappsandgames.smartreminderssave.tasks.Task;
import jgappsandgames.smartreminderssave.tasks.TaskManagerKt;
import jgappsandgames.smartreminderssave.utility.FileUtilityKt;

/**
 * TagActivity
 * Created by joshua on 9/2/17.
 * Last Edited on 10/12/17 (134).
 * Edited on 10/11/17 (106).
 * Edited on 10/5/17 (100).
 */
public class TagActivity
        extends Activity
        implements TagHolder.TagSwitcher, TaskFolderHolder.OnTaskChangedListener {
    // Data
    private List<String> selected_tags;
    private List<Task> tasks;

    // Views
    private TextView tasks_text;
    private ListView tasks_list;
    private TextView selected_text;
    private ListView selected_list;
    private TextView unselected_text;
    private ListView unselected_list;

    // Adapters
    private TaskAdapter task_adapter;
    private SelectedAdapter selected_adapter;
    private UnselectedAdapter unselected_adapter;

    // Management Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set View
        setContentView(R.layout.activity_tag);

        // Load Data
        selected_tags = new ArrayList<>();

        // First Run
        FileUtilityKt.loadFilepaths(this);
        if (FileUtilityKt.isFirstRun()) {
            Intent first_run = new Intent(this, FirstRun.class);
            startActivity(first_run);
        }

        // Load Data
        MasterManagerKt.load();

        // Find Views
        tasks_text = findViewById(R.id.tasks_title);
        tasks_list = findViewById(R.id.tasks);
        selected_text = findViewById(R.id.selected_text);
        selected_list = findViewById(R.id.selected);
        unselected_text = findViewById(R.id.unselected_text);
        unselected_list = findViewById(R.id.unselected);
    }

    @Override
    protected void onResume() {
        super.onResume();

        tasks = new ArrayList<>();
        for (int i = 0; i < TaskManagerKt.getTasks().size(); i++) tasks.add(new Task(TaskManagerKt.getTasks().get(i)));

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
                MasterManagerKt.save();
                Toast.makeText(this, "Saved.", Toast.LENGTH_LONG).show();
                break;

            case R.id.close:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskChanged() {
        onResume();
    }
}
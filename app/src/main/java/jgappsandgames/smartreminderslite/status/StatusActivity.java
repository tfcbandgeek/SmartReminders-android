package jgappsandgames.smartreminderslite.status;

// Java
import java.util.ArrayList;
import java.util.Calendar;

// Android OS
import android.app.Activity;
import android.os.Bundle;

// Android View
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

// App
import jgappsandgames.smartreminderslite.R;
import jgappsandgames.smartreminderslite.holder.TaskFolderHolder.OnTaskChangedListener;

// Save
import jgappsandgames.smartreminderssave.tasks.Task;
import jgappsandgames.smartreminderssave.tasks.TaskManager;

/**
 * Status Activity
 * Created by joshua on 9/4/17.
 */
public class StatusActivity extends StatusActivityInterface {
    // Data
    private ArrayList<Task> overdue_array;
    private ArrayList<Task> incomplete_array;
    private ArrayList<Task> done_array;

    // LifeCycle Methods
    @Override
    protected void onResume() {
        super.onResume();

        // Load Data
        overdue_array = new ArrayList<>();
        incomplete_array = new ArrayList<>();
        done_array = new ArrayList<>();

        for (int i = 0; i < TaskManager.tasks.size(); i++) {
            Task temp = new Task(TaskManager.tasks.get(i));
            if (temp.getType() == Task.TYPE_TASK) {
                if (temp.getStatus() == Task.STATUS_DONE) done_array.add(temp);
                else if (temp.getDateDue() == null) incomplete_array.add(temp);
                else if (temp.getDateDue().before(Calendar.getInstance())) overdue_array.add(temp);
                else incomplete_array.add(temp);
            }
        }

        // Set Adapters
        overdue_adapter = new StatusAdapter(this, overdue_array);
        incomplete_adapter = new StatusAdapter(this, incomplete_array);
        done_adapter = new StatusAdapter(this, done_array);

        try {
            overdue_list.setAdapter(overdue_adapter);
        } catch (NullPointerException n) {
            n.printStackTrace();
        }

        try {
            incomplete_list.setAdapter(incomplete_adapter);
        } catch (NullPointerException n) {
            n.printStackTrace();
        }

        try {
            done_list.setAdapter(done_adapter);
        } catch (NullPointerException n) {
            n.printStackTrace();
        }
    }

    @Override
    public void onTaskChanged() {
        onResume();
    }

    @Override
    public void save() {
        TaskManager.save();
    }
}
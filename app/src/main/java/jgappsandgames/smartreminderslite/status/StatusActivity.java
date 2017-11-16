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
public class StatusActivity extends Activity implements OnTaskChangedListener {
    // Data
    private ArrayList<Task> overdue_array;
    private ArrayList<Task> incomplete_array;
    private ArrayList<Task> done_array;

    // Views
    private TextView overdue_text;
    private ListView overdue_list;
    private TextView incomplete_text;
    private ListView incomplete_list;
    private TextView done_text;
    private ListView done_list;

    // Adapters
    private StatusAdapter overdue_adapter;
    private StatusAdapter incomplete_adapter;
    private StatusAdapter done_adapter;

    // LifeCycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set Content View
        setContentView(R.layout.activity_status);

        // Find Views
        overdue_text = findViewById(R.id.overdue_text);
        overdue_list = findViewById(R.id.overdue_list);
        incomplete_text = findViewById(R.id.incomplete_text);
        incomplete_list = findViewById(R.id.incomplete_list);
        done_text = findViewById(R.id.done_text);
        done_list = findViewById(R.id.done_list);
    }

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

    @Override
    public void onTaskChanged() {
        onResume();
    }
}
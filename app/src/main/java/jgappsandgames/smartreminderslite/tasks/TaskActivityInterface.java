package jgappsandgames.smartreminderslite.tasks;

// Android OS
import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;

// Views
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

// App
import jgappsandgames.smartreminderslite.R;
import jgappsandgames.smartreminderslite.utility.ActivityUtility;

// Save.
import jgappsandgames.smartreminderssave.tasks.Task;

/**
 * TaskActivityInterface
 * Created by joshua on 11/29/2017.
 */
public abstract class TaskActivityInterface extends Activity implements TextWatcher, View.OnClickListener,
        View.OnLongClickListener, SeekBar.OnSeekBarChangeListener, DatePickerDialog.OnDateSetListener {
    // Views
    protected EditText title;
    protected EditText note;
    protected TextView tags;
    protected Button date;
    protected Button status;
    protected SeekBar priority;
    protected ListView list;
    protected Button fab;

    // Adapters
    protected BaseAdapter adapter;

    // Data
    private int type = -1;

    // Life Cycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Find Type
        type = getIntent().getIntExtra(ActivityUtility.TASK_TYPE, -1);
        if (type == -1) type = new Task(getIntent().getStringExtra(ActivityUtility.TASK_NAME)).getType();

        // Set Content View
        if (type == Task.TYPE_TASK) setContentView(R.layout.activity_task);
        else setContentView(R.layout.activity_folder);

        // Find Views
        title = findViewById(R.id.title);
        note = findViewById(R.id.note);
        tags = findViewById(R.id.tags);
        list = findViewById(R.id.tasks);

        fab = findViewById(R.id.fab);

        // Task Specific Items
        if (type == Task.TYPE_TASK) {
            // Find Views
            date = findViewById(R.id.date);
            status = findViewById(R.id.status);
            priority = findViewById(R.id.priority);
        }

        // Set TextWatcher
        title.addTextChangedListener(this);
        note.addTextChangedListener(this);

        // Set Click Listener
        fab.setOnClickListener(this);
        fab.setOnLongClickListener(this);

        tags.setOnClickListener(this);
        tags.setOnLongClickListener(this);
    }

    // Menu Methods
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                save();
                Toast.makeText(this, "Saved.", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.close:
                finish();
                return true;
        }

        return false;
    }

    protected abstract void save();
}
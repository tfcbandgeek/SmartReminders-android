package jgappsandgames.smartreminderslite.priority;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import jgappsandgames.smartreminderslite.R;
import jgappsandgames.smartreminderslite.holder.TaskFolderHolder;

/**
 * PriorityActivityInterface
 * Created by joshua on 11/29/2017.
 */
public abstract class PriorityActivityInterface extends Activity implements View.OnClickListener, TaskFolderHolder.OnTaskChangedListener {
    // Data
    protected int position = 3;

    // Views
    protected ListView tasks;
    protected Button down;
    protected Button up;

    // Adapters
    protected BaseAdapter ignore_adapter;
    protected BaseAdapter low_adapter;
    protected BaseAdapter normal_adapter;
    protected BaseAdapter high_adapter;
    protected BaseAdapter stared_adapter;

    // LifeCycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set Activity View
        setContentView(R.layout.activity_priority);

        tasks = findViewById(R.id.tasks);
        down = findViewById(R.id.down);
        up = findViewById(R.id.up);

        down.setOnClickListener(this);
        up.setOnClickListener(this);
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
    public abstract void save();
}
package jgappsandgames.smartreminderslite.sort.date;

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
 * WeekActivityInterface
 * Created by joshua on 11/29/2017.
 */
public abstract class WeekActivityInterface extends Activity implements View.OnClickListener, TaskFolderHolder.OnTaskChangedListener {
    // Data
    protected int week_active;

    // Views
    protected ListView tasks;
    protected Button previous;
    protected Button next;

    // Adapters
    protected BaseAdapter adapter;

    // LifeCycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set Content View
        setContentView(R.layout.activity_date);

        // Find Views
        tasks = findViewById(R.id.tasks);
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
    public abstract void save();
}

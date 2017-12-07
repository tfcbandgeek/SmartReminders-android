package jgappsandgames.smartreminderslite.tags;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import jgappsandgames.smartreminderslite.R;
import jgappsandgames.smartreminderslite.holder.TagHolder;
import jgappsandgames.smartreminderslite.holder.TaskFolderHolder;

/**
 * TagActivityInterface
 * Created by joshua on 11/29/2017.
 */
public abstract class TagActivityInterface extends Activity implements TagHolder.TagSwitcher, TaskFolderHolder.OnTaskChangedListener {
    // Views
    protected TextView tasks_text;
    protected ListView tasks_list;
    protected TextView selected_text;
    protected ListView selected_list;
    protected TextView unselected_text;
    protected ListView unselected_list;

    // Adapters
    protected TaskAdapter task_adapter;
    protected SelectedAdapter selected_adapter;
    protected UnselectedAdapter unselected_adapter;

    // LifeCycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set View
        setContentView(R.layout.activity_tag);


        // Find Views
        tasks_text = findViewById(R.id.tasks_title);
        tasks_list = findViewById(R.id.tasks);
        selected_text = findViewById(R.id.selected_text);
        selected_list = findViewById(R.id.selected);
        unselected_text = findViewById(R.id.unselected_text);
        unselected_list = findViewById(R.id.unselected);
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

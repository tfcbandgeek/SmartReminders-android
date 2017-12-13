package jgappsandgames.smartreminderslite.status;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import jgappsandgames.smartreminderslite.R;
import jgappsandgames.smartreminderslite.holder.TaskFolderHolder;

/**
 * StatusActivityInterface
 * Created by joshua on 11/29/2017.
 */
public abstract class StatusActivityInterface extends Activity implements TaskFolderHolder.OnTaskChangedListener {
    // Views
    protected TextView overdue_text;
    protected ListView overdue_list;
    protected TextView incomplete_text;
    protected ListView incomplete_list;
    protected TextView done_text;
    protected ListView done_list;

    // Adapters
    protected StatusAdapter overdue_adapter;
    protected StatusAdapter incomplete_adapter;
    protected StatusAdapter done_adapter;

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

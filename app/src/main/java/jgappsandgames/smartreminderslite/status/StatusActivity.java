package jgappsandgames.smartreminderslite.status;

// Java
import java.util.ArrayList;

// Android OS
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

// Android View
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

// App
import jgappsandgames.smartreminderslite.R;
import jgappsandgames.smartreminderslite.holder.TaskFolderHolder;

// Save
import jgappsandgames.smartreminderslite.home.FirstRun;
import jgappsandgames.smartreminderssave.MasterManagerKt;
import jgappsandgames.smartreminderssave.status.StatusManagerKt;
import jgappsandgames.smartreminderssave.utility.FileUtilityKt;

/**
 * Status Activity
 * Created by joshua on 9/4/17.
 */
@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class StatusActivity
        extends Activity
        implements TaskFolderHolder.OnTaskChangedListener {
    // Views
    @SuppressWarnings("unused")
    private TextView overdue_text;
    private ListView overdue_list;
    @SuppressWarnings("unused")
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

        // First Run
        FileUtilityKt.loadFilepaths(this);
        if (FileUtilityKt.isFirstRun()) {
            Intent first_run = new Intent(this, FirstRun.class);
            startActivity(first_run);
        }

        // Load Data
        MasterManagerKt.load();

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

        // Set Adapters
        ArrayList<String> incomplete = new ArrayList<>();
        incomplete.addAll(StatusManagerKt.getNot_yet_done());
        incomplete.addAll(StatusManagerKt.getNo_date());
        overdue_adapter = new StatusAdapter(this, StatusManagerKt.getOverdue());
        incomplete_adapter = new StatusAdapter(this, incomplete);
        done_adapter = new StatusAdapter(this, StatusManagerKt.getCompleted());

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
package jgappsandgames.smartreminderslite.home;

// Android OS
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

// Views
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

// App
import jgappsandgames.smartreminderslite.R;
import jgappsandgames.smartreminderslite.date.DayActivity;
import jgappsandgames.smartreminderslite.date.MonthActivity;
import jgappsandgames.smartreminderslite.date.WeekActivity;
import jgappsandgames.smartreminderslite.priority.PriorityActivity;
import jgappsandgames.smartreminderslite.status.StatusActivity;
import jgappsandgames.smartreminderslite.tags.TagActivity;

/**
 * HomeActivityInterface
 * Created by joshua on 11/29/2017.
 */
public abstract class HomeActivityInterface extends Activity implements View.OnClickListener, View.OnLongClickListener {
    // Views
    protected ListView tasks;
    protected Button fab;

    // Adapters
    protected BaseAdapter adapter;

    // LifeCycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set Activity View
        setContentView(R.layout.activity_home);
        setTitle(R.string.app_name);

        // Find Views
        tasks = findViewById(R.id.tasks);
        fab = findViewById(R.id.fab);

        // Set Click Listeners
        fab.setOnClickListener(this);
        fab.setOnLongClickListener(this);
    }

    // Menu Methods
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tags:
                startActivity(new Intent(this, TagActivity.class));
                return true;

            case R.id.status:
                startActivity(new Intent(this, StatusActivity.class));
                return true;

            case R.id.priority:
                startActivity(new Intent(this, PriorityActivity.class));
                return true;

            case R.id.day:
                startActivity(new Intent(this, DayActivity.class));
                return true;

            case R.id.week:
                startActivity(new Intent(this, WeekActivity.class));
                return true;

            case R.id.month:
                startActivity(new Intent(this, MonthActivity.class));
                return true;

            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;

            case R.id.about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;

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
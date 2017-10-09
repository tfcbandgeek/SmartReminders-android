package jgappsandgames.smartreminderslite.tasks;

// Android OS
import android.app.Activity;
import android.os.Bundle;

// Program
import jgappsandgames.smartreminderslite.R;

/**
 * TaskManagementActivity
 * Created by joshua on 8/31/17.
 */
public class TaskManagementActivity extends Activity {
    // LifeCycle MEthods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set Content View
        setContentView(R.layout.activity_task_management);
    }
}
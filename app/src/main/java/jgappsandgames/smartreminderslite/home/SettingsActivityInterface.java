package jgappsandgames.smartreminderslite.home;

// Android OS
import android.app.Activity;
import android.os.Bundle;

// Views
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

// App
import jgappsandgames.smartreminderslite.R;

// Save
import jgappsandgames.smartreminderssave.settings.Settings;

/**
 * SettingsActivityInterface
 * Created by joshua on 11/29/2017.
 */
public abstract class SettingsActivityInterface extends Activity implements View.OnClickListener {
    // Views
    protected EditText your_name;
    protected EditText device_name;
    protected Button app_directory;
    protected Button tutorial;

    // LifeCycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set Content View
        setContentView(R.layout.activity_settings);

        // Find Views
        your_name = findViewById(R.id.yourname);
        device_name = findViewById(R.id.device_name);
        app_directory = findViewById(R.id.app_directory);
        tutorial = findViewById(R.id.tutorial);

        // Set Text
        your_name.setText(Settings.user_name);
        device_name.setText(Settings.device_name);
        if (Settings.use_external_file) app_directory.setText(R.string.save_external);
        else app_directory.setText(R.string.save_app);

        // Set Listeners
        app_directory.setOnClickListener(this);
        tutorial.setOnClickListener(this);
    }
}
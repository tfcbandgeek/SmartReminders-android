package jgappsandgames.smartreminderslite.home;

// Android OS
import android.app.Activity;
import android.os.Bundle;

// Views
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

// App
import jgappsandgames.smartreminderslite.R;
import jgappsandgames.smartreminderssave.settings.SettingsManager;

/**
 * FirstRunActivityInterface
 * Created by joshua on 11/29/2017.
 */
public abstract class FirstRunActivityInterface extends Activity implements View.OnClickListener, TextWatcher {
    // Views
    protected EditText your_name;
    protected EditText device_name;
    protected Button app_directory;
    protected Button settings;
    protected Button tutorial;
    protected Button con;

    // LifeCycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set Content View
        setContentView(R.layout.activity_first_run);

        // Find Views
        your_name = findViewById(R.id.yourname);
        device_name = findViewById(R.id.device_name);
        app_directory = findViewById(R.id.app_directory);
        settings = findViewById(R.id.settings);
        tutorial = findViewById(R.id.tutorial);
        con = findViewById(R.id.con);

        // Set Text
        device_name.setText(SettingsManager.device_name);

        // Set Listeners
        your_name.addTextChangedListener(this);
        device_name.addTextChangedListener(this);
        app_directory.setOnClickListener(this);
        settings.setOnClickListener(this);
        tutorial.setOnClickListener(this);
        con.setOnClickListener(this);
    }
}
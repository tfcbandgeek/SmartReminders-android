package jgappsandgames.smartreminderslite.home;

// Android OS
import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

// Views
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

// Program
import jgappsandgames.smartreminderslite.R;
import jgappsandgames.smartreminderslite.utility.ActivityUtility;

// Save


/**
 * // SettingsActivity
 * Created by joshua on 10/2/17.
 * Last Edited on 10/11/17 (101).
 */
public class SettingsActivity extends Activity implements OnClickListener{
    // Views
    private EditText your_name;
    private EditText device_name;
    private Button app_directory;
    private Button tutorial;

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

    @Override
    protected void onPause() {
        super.onPause();

        Settings.user_name = your_name.getText().toString();
        Settings.device_name = device_name.getText().toString();
        Settings.save();
    }

    // Click Listener
    @Override
    public void onClick(View view) {
        // App Directory
        if (view.equals(app_directory)) {
            if (Settings.use_external_file) {
                Settings.use_external_file = false;
                app_directory.setText(R.string.save_app);
            } else {
                Settings.use_external_file = true;
                app_directory.setText(R.string.save_external);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int permision = this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

                    if (permision == PackageManager.PERMISSION_DENIED) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, ActivityUtility.REQUEST_EXTERNAL_STORAGE_PERMISSION);
                    }
                }
            }
        }

        // Tutorial
        if (view.equals(tutorial)) {
            Toast.makeText(this, "Coming Soon.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case ActivityUtility.REQUEST_EXTERNAL_STORAGE_PERMISSION:
                if (grantResults.length > 0) {
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                        Settings.use_external_file = false;
                        app_directory.setText(R.string.save_app);
                    }
                }
        }
    }
}
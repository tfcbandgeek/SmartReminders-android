package jgappsandgames.smartreminderslite.home;

// Android OS
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
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
import jgappsandgames.smartreminderssave.settings.Settings;

/**
 * FirstRun
 * Created by joshua on 8/31/17.
 */
public class FirstRun extends Activity implements OnClickListener {
    // Views
    private EditText your_name;
    private EditText device_name;
    private Button app_directory;
    private Button settings;
    private Button tutorial;
    private Button con;

    // LifeCycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set Content View
        setContentView(R.layout.activity_first_run);

        // Create Settings Page
        Settings.create();

        // Find Views
        your_name = findViewById(R.id.yourname);
        device_name = findViewById(R.id.device_name);
        app_directory = findViewById(R.id.app_directory);
        settings = findViewById(R.id.settings);
        tutorial = findViewById(R.id.tutorial);
        con = findViewById(R.id.con);

        // Set Listeners
        app_directory.setOnClickListener(this);
        settings.setOnClickListener(this);
        tutorial.setOnClickListener(this);
        con.setOnClickListener(this);
    }

    // Click Listeners
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

        // Settings
        if (view.equals(settings)) {
            Toast.makeText(this, "Coming Soon.", Toast.LENGTH_SHORT).show();
        }

        // Tutorial
        if (view.equals(tutorial)) {
            Toast.makeText(this, "Coming Soon.", Toast.LENGTH_SHORT).show();
        }

        // Continue
        if (view.equals(con)) {
            Settings.save();
            Intent home = new Intent(this, HomeActivity.class);
            home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(home);
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
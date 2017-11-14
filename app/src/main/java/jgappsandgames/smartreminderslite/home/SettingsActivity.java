package jgappsandgames.smartreminderslite.home;

// Android OS
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

// Views
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

// Program
import org.jetbrains.annotations.NotNull;

import jgappsandgames.smartreminderslite.R;
import jgappsandgames.smartreminderslite.utility.ActivityUtility;

// Save
import jgappsandgames.smartreminderssave.MasterManagerKt;
import jgappsandgames.smartreminderssave.settings.SettingsManagerKt;
import jgappsandgames.smartreminderssave.utility.FileUtilityKt;

/**
 * // SettingsActivity
 * Created by joshua on 10/2/17.
 */
public class SettingsActivity extends Activity implements OnClickListener {
    // Log Constant
    private static final String LOG = "SettingsActivity";

    // Views
    private EditText your_name;
    private EditText device_name;
    private Button app_directory;
    private Button tutorial;

    // LifeCycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG, "onCreate Called");

        // Set Content View
        Log.v(LOG, "Set Content View");
        setContentView(R.layout.activity_settings);

        // First Run
        FileUtilityKt.loadFilepaths(this);
        if (FileUtilityKt.isFirstRun()) {
            Log.v(LOG, "First Run, Create the Data");
            Intent first_run = new Intent(this, FirstRun.class);
            startActivity(first_run);
        } else {
            // Load Data
            Log.v(LOG, "Normal Run, Load the Data");
            MasterManagerKt.load();
        }

        // Find Views
        Log.v(LOG, "Find Views");
        your_name = findViewById(R.id.yourname);
        device_name = findViewById(R.id.device_name);
        app_directory = findViewById(R.id.app_directory);
        tutorial = findViewById(R.id.tutorial);

        // Set Text
        Log.v(LOG, "Set Text");
        your_name.setText(SettingsManagerKt.getUser_name());
        device_name.setText(SettingsManagerKt.getDevice_name());
        //noinspection ConstantConditions
        if (SettingsManagerKt.getExternal_file()) app_directory.setText(R.string.save_external);
        else app_directory.setText(R.string.save_app);

        // Set Listeners
        Log.v(LOG, "Set Listeners");
        app_directory.setOnClickListener(this);
        tutorial.setOnClickListener(this);

        Log.v(LOG, "onCreate Done");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG, "onPause Called");

        // Handle Text
        Log.v(LOG, "Handle Text");
        SettingsManagerKt.setUser_name(your_name.getText().toString());
        SettingsManagerKt.setDevice_name(device_name.getText().toString());

        Log.v(LOG, "onPause Done");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(LOG, "onDestroy Called");

        // Save
        Log.v(LOG, "Save");
        MasterManagerKt.save();

        Log.v(LOG, "onDestroy Done");
    }

    // Click Listener
    @Override
    public void onClick(View view) {
        Log.d(LOG, "onClick Called");

        // App Directory
        if (view.equals(app_directory)) {
            Log.v(LOG, "App Directory Pressed");
            //noinspection ConstantConditions
            if (SettingsManagerKt.getExternal_file()) {
                SettingsManagerKt.setExternal_file(false);
                app_directory.setText(R.string.save_app);
            } else {
                SettingsManagerKt.setExternal_file(true);
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
            Log.v(LOG, "Tutorial Pressed");
            Toast.makeText(this, "Coming Soon.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        switch (requestCode) {
            case ActivityUtility.REQUEST_EXTERNAL_STORAGE_PERMISSION:
                if (grantResults.length > 0) {
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                        SettingsManagerKt.setExternal_file(false);
                        app_directory.setText(R.string.save_app);
                    }
                }
        }
    }
}
package jgappsandgames.smartreminderslite.home;

// Jetbrains
import org.jetbrains.annotations.NotNull;

// Android OS
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

// Views
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

// Program
import jgappsandgames.smartreminderslite.R;

import jgappsandgames.smartreminderslite.utility.ActivityUtility;

// Save
import jgappsandgames.smartreminderssave.MasterManagerKt;
import jgappsandgames.smartreminderssave.settings.SettingsManagerKt;


/**
 * FirstRun
 * Created by joshua on 8/31/17.
 *
 * Activity class that is called on the First one of the Application.
 * Contains info for settings, save Location, User Data, Device Name and Tutorial.
 */
public class FirstRun extends Activity implements OnClickListener, TextWatcher {
    // Log Constant
    private static final String LOG = "FirstRun";

    // Views
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private EditText your_name;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private EditText device_name;
    private Button app_directory;
    private Button settings;
    private Button tutorial;
    private Button con;

    // LifeCycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG, "onCreate Called");

        // Set Content View
        Log.v(LOG, "Set Content View");
        setContentView(R.layout.activity_first_run);

        // Create Data
        Log.v(LOG, "Create Date");
        MasterManagerKt.create();
        MasterManagerKt.save();

        // Find Views
        Log.v(LOG, "Find Views");
        your_name = findViewById(R.id.yourname);
        device_name = findViewById(R.id.device_name);
        device_name.setText(SettingsManagerKt.getDevice_name());
        app_directory = findViewById(R.id.app_directory);
        settings = findViewById(R.id.settings);
        tutorial = findViewById(R.id.tutorial);
        con = findViewById(R.id.con);

        // Set Listeners
        Log.v(LOG, "Set Listeners");
        app_directory.setOnClickListener(this);
        settings.setOnClickListener(this);
        tutorial.setOnClickListener(this);
        con.setOnClickListener(this);
        your_name.addTextChangedListener(this);
        device_name.addTextChangedListener(this);

        Log.v(LOG, "onCreate Done");
    }

    // TextWatchers
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        SettingsManagerKt.setUser_name(your_name.getText().toString());
        SettingsManagerKt.setDevice_name(device_name.getText().toString());
        SettingsManagerKt.saveSettings();
    }

    // Click Listeners
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

        // Settings
        if (view.equals(settings)) {
            Log.v(LOG, "Setting Pressed");
            Toast.makeText(this, "Coming Soon.", Toast.LENGTH_SHORT).show();
        }

        // Tutorial
        if (view.equals(tutorial)) {
            Log.v(LOG, "Tutorial Pressed");
            Toast.makeText(this, "Coming Soon.", Toast.LENGTH_SHORT).show();
        }

        // Continue
        if (view.equals(con)) {
            Log.v(LOG,"Continue Pressed");
            MasterManagerKt.save();
            Intent home = new Intent(this, HomeActivity.class);
            home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(home);
        }

        Log.v(LOG, "onClick Done");
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
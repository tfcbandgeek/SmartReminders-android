package jgappsandgames.smartreminderslite.home;

// Android OS
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

// Views
import android.text.Editable;
import android.view.View;
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
public class FirstRun extends FirstRunActivityInterface  {
    // LifeCycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create Settings Page
        Settings.create();
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
    protected void onStop() {
        super.onStop();
        Settings.save();
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

    // Text Watchers
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        Settings.user_name = your_name.getText().toString();
        Settings.device_name = device_name.getText().toString();
        Settings.save();
    }
}
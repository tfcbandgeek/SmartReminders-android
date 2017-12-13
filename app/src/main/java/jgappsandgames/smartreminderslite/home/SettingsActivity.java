package jgappsandgames.smartreminderslite.home;

// Android OS
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;

// Views
import android.view.View;
import android.widget.Toast;

// App
import org.jetbrains.annotations.NotNull;

import jgappsandgames.smartreminderslite.R;
import jgappsandgames.smartreminderslite.utility.ActivityUtility;

// Save
import jgappsandgames.smartreminderslite.utility.MoveUtility;
import jgappsandgames.smartreminderssave.MasterManager;
import jgappsandgames.smartreminderssave.settings.SettingsManager;
import jgappsandgames.smartreminderssave.tags.TagManager;
import jgappsandgames.smartreminderssave.tasks.TaskManager;

/**
 * SettingsActivity
 * Created by joshua on 10/2/17.
 */
public class SettingsActivity extends SettingsActivityInterface {
    // LifeCycle Methods
    @Override
    protected void onPause() {
        super.onPause();

        SettingsManager.user_name = your_name.getText().toString();
        SettingsManager.device_name = device_name.getText().toString();
        MasterManager.save();
    }

    // Click Listener
    @Override
    public void onClick(View view) {
        // App Directory
        if (view.equals(app_directory)) {
            if (SettingsManager.use_external_file) {
                SettingsManager.use_external_file = false;
                app_directory.setText(R.string.save_app);
                TaskManager.load();
                TagManager.load();
            } else {
                SettingsManager.use_external_file = true;
                app_directory.setText(R.string.save_external);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int permission = this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

                    if (permission == PackageManager.PERMISSION_DENIED) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, ActivityUtility.REQUEST_EXTERNAL_STORAGE_PERMISSION);
                    } else {
                        TaskManager.load();
                        TagManager.load();
                    }
                }
            }
        }

        // Tutorial
        if (view.equals(tutorial)) {
            Toast.makeText(this, R.string.coming_soon, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onLongClick(View view) {
        // App Directory
        if (view.equals(app_directory)) {
            if (SettingsManager.use_external_file) {
                SettingsManager.use_external_file = false;
                app_directory.setText(R.string.save_app);
                MoveUtility.moveToInternal();
                TaskManager.load();
                TagManager.load();
            } else {
                SettingsManager.use_external_file = true;
                app_directory.setText(R.string.save_external);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int permission = this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

                    if (permission == PackageManager.PERMISSION_DENIED) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, ActivityUtility.REQUEST_EXTERNAL_STORAGE_PERMISSION);
                    } else {
                        MoveUtility.moveToExternal();
                        TaskManager.load();
                        TagManager.load();
                    }
                }
            }

            return true;
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        switch (requestCode) {
            case ActivityUtility.REQUEST_EXTERNAL_STORAGE_PERMISSION:
                if (grantResults.length > 0) {
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                        SettingsManager.use_external_file = false;
                        app_directory.setText(R.string.save_app);
                    }
                }
        }
    }
}
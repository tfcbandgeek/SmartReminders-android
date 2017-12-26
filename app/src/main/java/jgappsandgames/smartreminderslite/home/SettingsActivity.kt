package jgappsandgames.smartreminderslite.home

// Android OS
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.*

// Views
import android.view.View
import android.widget.Toast

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.utility.ActivityUtility
import jgappsandgames.smartreminderslite.utility.MoveUtility

// Save
import jgappsandgames.smartreminderssave.MasterManager
import jgappsandgames.smartreminderssave.settings.SettingsManager
import jgappsandgames.smartreminderssave.tags.TagManager
import jgappsandgames.smartreminderssave.tasks.TaskManager

/**
 * SettingsActivity
 * Created by joshua on 12/25/2017.
 */
class SettingsActivity: SettingsActivityInterface() {
    // LifeCycle Methods
    override fun onPause() {
        super.onPause()

        SettingsManager.user_name = your_name!!.getText().toString()
        SettingsManager.device_name = device_name!!.getText().toString()
        MasterManager.save()
    }

    // Click Listener
    @SuppressLint("NewApi")
    override fun onClick(view: View) {
        // App Directory
        if (view == app_directory) {
            if (SettingsManager.use_external_file) {
                SettingsManager.use_external_file = false
                app_directory!!.setText(R.string.save_app)
                TaskManager.load()
                TagManager.load()
            } else {
                SettingsManager.use_external_file = true
                app_directory!!.setText(R.string.save_external)

                if (VERSION.SDK_INT >= VERSION_CODES.M) {
                    val permission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)

                    if (permission == PackageManager.PERMISSION_DENIED) {
                        requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), ActivityUtility.REQUEST_EXTERNAL_STORAGE_PERMISSION)
                    } else {
                        TaskManager.load()
                        TagManager.load()
                    }
                }
            }
        }

        // Tutorial
        if (view == tutorial) {
            Toast.makeText(this, R.string.coming_soon, Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("NewApi")
    override fun onLongClick(view: View): Boolean {
        // App Directory
        if (view == app_directory) {
            if (SettingsManager.use_external_file) {
                SettingsManager.use_external_file = false
                app_directory!!.setText(R.string.save_app)
                MoveUtility.moveToInternal()
                TaskManager.load()
                TagManager.load()
            } else {
                SettingsManager.use_external_file = true
                app_directory!!.setText(R.string.save_external)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val permission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)

                    if (permission == PackageManager.PERMISSION_DENIED) {
                        requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), ActivityUtility.REQUEST_EXTERNAL_STORAGE_PERMISSION)
                    } else {
                        MoveUtility.moveToExternal()
                        TaskManager.load()
                        TagManager.load()
                    }
                }
            }

            return true
        }

        return false
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            ActivityUtility.REQUEST_EXTERNAL_STORAGE_PERMISSION -> if (grantResults.isNotEmpty()) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    SettingsManager.use_external_file = false
                    app_directory!!.setText(R.string.save_app)
                } else {
                    TaskManager.load()
                    TagManager.load()
                }
            }
        }
    }
}
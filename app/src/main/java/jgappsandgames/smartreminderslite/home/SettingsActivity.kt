package jgappsandgames.smartreminderslite.home

// Android OS
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.*
import android.os.Bundle

// Views
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.utility.ActivityUtility
import jgappsandgames.smartreminderslite.utility.MoveUtility
import jgappsandgames.smartreminderslite.utility.ShortcutUtility

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set Check
        tag!!.isChecked = SettingsManager.has_tag_shortcut
        priority!!.isChecked = SettingsManager.has_priority_shortcut
        status!!.isChecked = SettingsManager.has_status_shortcut
        day!!.isChecked = SettingsManager.has_today_shortcut
        week!!.isChecked = SettingsManager.has_week_shortcut
        month!!.isChecked = SettingsManager.has_month_shortcut

        // Set Listeners
        tag!!.setOnCheckedChangeListener(this)
        priority!!.setOnCheckedChangeListener(this)
        status!!.setOnCheckedChangeListener(this)
        day!!.setOnCheckedChangeListener(this)
        week!!.setOnCheckedChangeListener(this)
        month!!.setOnCheckedChangeListener(this)
    }
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
                MasterManager.load(this)
            } else {
                SettingsManager.use_external_file = true
                app_directory!!.setText(R.string.save_external)

                if (VERSION.SDK_INT >= VERSION_CODES.M) {
                    val permission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)

                    if (permission == PackageManager.PERMISSION_DENIED) requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), ActivityUtility.REQUEST_EXTERNAL_STORAGE_PERMISSION)
                    else MasterManager.load(this)
                }
            }
        }

        // Tutorial
        if (view == tutorial) Toast.makeText(this, R.string.coming_soon, Toast.LENGTH_SHORT).show()
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

                    if (permission == PackageManager.PERMISSION_DENIED) requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), ActivityUtility.REQUEST_EXTERNAL_STORAGE_PERMISSION)
                    else {
                        MoveUtility.moveToExternal()
                        MasterManager.load(this)
                    }
                }
            }

            return true
        }

        return false
    }

    override fun onCheckedChanged(switch: CompoundButton?, state: Boolean) {
        // Tag
        if (switch == tag) {
            SettingsManager.has_tag_shortcut = !SettingsManager.has_tag_shortcut
            SettingsManager.save()
            if (SettingsManager.has_tag_shortcut) ShortcutUtility.createTagShortcut(this)
            else ShortcutUtility.removeTagShortcut(this)
        }

        // Priority
        else if (switch == priority) {
            SettingsManager.has_priority_shortcut = !SettingsManager.has_priority_shortcut
            SettingsManager.save()
            if (SettingsManager.has_priority_shortcut) ShortcutUtility.createPriorityShortcut(this)
            else ShortcutUtility.removePriorityShortcut(this)
        }

        // Status
        else if (switch == status) {
            SettingsManager.has_status_shortcut = !SettingsManager.has_status_shortcut
            SettingsManager.save()
            if (SettingsManager.has_status_shortcut) ShortcutUtility.createStatusShortcut(this)
            else ShortcutUtility.removeStatusShortcut(this)
        }

        // Day
        else if (switch == day) {
            SettingsManager.has_today_shortcut = !SettingsManager.has_today_shortcut
            SettingsManager.save()
            if (SettingsManager.has_today_shortcut) ShortcutUtility.createTodayShortcut(this)
            else ShortcutUtility.removeTodayShortcut(this)
        }

        // Week
        else if (switch == week) {
            SettingsManager.has_week_shortcut = !SettingsManager.has_week_shortcut
            SettingsManager.save()
            if (SettingsManager.has_week_shortcut) ShortcutUtility.createWeekShortcut(this)
            else ShortcutUtility.removeWeekShortcut(this)
        }

        // Month
        else if (switch == month) {
            SettingsManager.has_month_shortcut = !SettingsManager.has_month_shortcut
            SettingsManager.save()
            if (SettingsManager.has_month_shortcut) ShortcutUtility.createMonthShortcut(this)
            else ShortcutUtility.removeMonthShortcut(this)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            ActivityUtility.REQUEST_EXTERNAL_STORAGE_PERMISSION -> if (grantResults.isNotEmpty()) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    SettingsManager.use_external_file = false
                    app_directory!!.setText(R.string.save_app)
                } else {
                    MasterManager.load(this)
                }
            }
        }
    }
}
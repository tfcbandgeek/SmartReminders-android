package jgappsandgames.smartreminderslite.home

// Android OS
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle

// Anko
import org.jetbrains.anko.toast

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.utility.*

// KotlinX
import kotlinx.android.synthetic.main.activity_first_run.first_run_app_directory_button
import kotlinx.android.synthetic.main.activity_first_run.first_run_continue_button
import kotlinx.android.synthetic.main.activity_first_run.first_run_day_switch
import kotlinx.android.synthetic.main.activity_first_run.first_run_device_name_edit_text
import kotlinx.android.synthetic.main.activity_first_run.first_run_month_switch
import kotlinx.android.synthetic.main.activity_first_run.first_run_priority_switch
import kotlinx.android.synthetic.main.activity_first_run.first_run_status_switch
import kotlinx.android.synthetic.main.activity_first_run.first_run_tag_switch
import kotlinx.android.synthetic.main.activity_first_run.first_run_tutorial_button
import kotlinx.android.synthetic.main.activity_first_run.first_run_week_switch
import kotlinx.android.synthetic.main.activity_first_run.first_run_your_name_edit_text
import kotlinx.android.synthetic.main.activity_first_run.list

// Save
import jgappsandgames.smartreminderssave.MasterManager
import jgappsandgames.smartreminderssave.settings.SettingsManager
import jgappsandgames.smartreminderssave.tags.TagManager
import jgappsandgames.smartreminderssave.tasks.TaskManager

/**
 * SettingsActivity
 * Created by joshua on 12/25/2017.
 */
class SettingsActivity: Activity() {
    // LifeCycle Methods ---------------------------------------------------------------------------
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_run)
        setTitle(R.string.settings)

        // Set Text
        first_run_your_name_edit_text.setText(SettingsManager.user_name)
        first_run_device_name_edit_text.setText(SettingsManager.device_name)
        if (SettingsManager.use_external_file) first_run_app_directory_button.setText(R.string.external)
        else first_run_app_directory_button.setText(R.string.internal)

        // Set Check
        first_run_tag_switch.isChecked = SettingsManager.has_tag_shortcut
        first_run_priority_switch.isChecked = SettingsManager.has_priority_shortcut
        first_run_status_switch.isChecked = SettingsManager.has_status_shortcut
        first_run_day_switch.isChecked = SettingsManager.has_today_shortcut
        first_run_week_switch.isChecked = SettingsManager.has_week_shortcut
        first_run_month_switch.isChecked = SettingsManager.has_month_shortcut

        // Set Listeners
        first_run_app_directory_button.setOnClickListener {
            if (SettingsManager.use_external_file) {
                SettingsManager.use_external_file = false
                first_run_app_directory_button.setText(R.string.save_app)
                MasterManager.load()
            } else {
                SettingsManager.use_external_file = true
                first_run_app_directory_button.setText(R.string.save_external)

                if (VERSION.SDK_INT >= VERSION_CODES.M) {
                    val permission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)

                    if (permission == PackageManager.PERMISSION_DENIED) requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_EXTERNAL_STORAGE_PERMISSION)
                    else MasterManager.load()
                }
            }
        }

        first_run_app_directory_button.setOnLongClickListener {
            if (SettingsManager.use_external_file) {
                SettingsManager.use_external_file = false
                first_run_app_directory_button.setText(R.string.save_app)
                moveToInternal()
                TaskManager.load()
                TagManager.load()
            } else {
                SettingsManager.use_external_file = true
                first_run_app_directory_button.setText(R.string.save_external)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val permission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)

                    if (permission == PackageManager.PERMISSION_DENIED) requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_EXTERNAL_STORAGE_PERMISSION)
                    else {
                        moveToExternal()
                        MasterManager.load()
                    }
                }
            }

            return@setOnLongClickListener true
        }

        first_run_tutorial_button.setOnClickListener {
            toast(R.string.coming_soon).show()
        }

        first_run_tag_switch.setOnCheckedChangeListener { _, _ ->
            SettingsManager.has_tag_shortcut = !SettingsManager.has_tag_shortcut
            SettingsManager.save()
            if (SettingsManager.has_tag_shortcut) createTagShortcut(this)
            else removeTagShortcut(this)
        }

        first_run_priority_switch.setOnCheckedChangeListener { _, _ ->
            SettingsManager.has_priority_shortcut = !SettingsManager.has_priority_shortcut
            SettingsManager.save()
            if (SettingsManager.has_priority_shortcut) createPriorityShortcut(this)
            else removePriorityShortcut(this)
        }

        first_run_status_switch.setOnCheckedChangeListener { _, _ ->
            SettingsManager.has_status_shortcut = !SettingsManager.has_status_shortcut
            SettingsManager.save()
            if (SettingsManager.has_status_shortcut) createStatusShortcut(this)
            else removeStatusShortcut(this)
        }

        first_run_day_switch.setOnCheckedChangeListener { _, _ ->
            SettingsManager.has_today_shortcut = !SettingsManager.has_today_shortcut
            SettingsManager.save()
            if (SettingsManager.has_today_shortcut) createTodayShortcut(this)
            else removeTodayShortcut(this)
        }

        first_run_week_switch.setOnCheckedChangeListener { _, _ ->
            SettingsManager.has_week_shortcut = !SettingsManager.has_week_shortcut
            SettingsManager.save()
            if (SettingsManager.has_week_shortcut) createWeekShortcut(this)
            else removeWeekShortcut(this)
        }

        first_run_month_switch.setOnCheckedChangeListener { _, _ ->
            SettingsManager.has_month_shortcut = !SettingsManager.has_month_shortcut
            SettingsManager.save()
            if (SettingsManager.has_month_shortcut) createMonthShortcut(this)
            else removeMonthShortcut(this)
        }

        list.removeView(first_run_continue_button)
    }
    override fun onPause() {
        super.onPause()

        SettingsManager.user_name = first_run_your_name_edit_text.text.toString()
        SettingsManager.device_name = first_run_device_name_edit_text.text.toString()
        MasterManager.save()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_EXTERNAL_STORAGE_PERMISSION -> if (grantResults.isNotEmpty()) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    SettingsManager.use_external_file = false
                    first_run_app_directory_button.setText(R.string.save_app)
                } else {
                    MasterManager.load()
                }
            }
        }
    }
}
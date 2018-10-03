package jgappsandgames.smartreminderslite.home

// Android OS
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle

// Anko
import org.jetbrains.anko.alert

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.utility.*

// KotlinX
import kotlinx.android.synthetic.main.activity_settings.*

// Library
import jgappsandgames.smartreminderssave.MasterManager
import jgappsandgames.smartreminderssave.settings.SettingsManager
import jgappsandgames.smartreminderssave.tags.TagManager
import jgappsandgames.smartreminderssave.tasks.TaskManager
import jgappsandgames.smartreminderssave.utility.API

/**
 * Settings2Activity
 */
class Settings2Activity: Activity() {
    // LifeCycle Methods ---------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setTitle(R.string.settings)

        // First Run
        if (intent.getBooleanExtra(FIRST_RUN, false)) {
            cleanForFirstRun()
            setAll()
            setContinue()
        }

        // Settings
        else {
            cleanForSettings()
            setAll()
            setUpgrade()
            setHomeScreenSwitched()
        }
    }

    override fun onPause() {
        super.onPause()

        SettingsManager.setUserName(settings_your_name_edit_text.text.toString())
        SettingsManager.setDeviceName(settings_device_name_edit_text.text.toString())
        MasterManager.save()
    }

    // View Adjustments ----------------------------------------------------------------------------
    private fun cleanForFirstRun() {
        settings_list.removeView(settings_tag_switch)
        settings_list.removeView(settings_priority_switch)
        settings_list.removeView(settings_status_switch)
        settings_list.removeView(settings_day_switch)
        settings_list.removeView(settings_week_switch)
        settings_list.removeView(settings_month_switch)

        settings_list.removeView(settings_upgrade)
    }

    private fun cleanForSettings() = settings_root.removeView(settings_continue_button)

    // View Setters --------------------------------------------------------------------------------
    private fun setAll() {
        // Set Data
        settings_your_name_edit_text.setText(SettingsManager.getUserName())
        settings_device_name_edit_text.setText(SettingsManager.getDeviceName())
        if (SettingsManager.getUseExternal()) settings_app_directory_button.setText(R.string.external)
        else settings_app_directory_button.setText(R.string.internal)

        // Set Click Listeners
        settings_app_directory_button.setOnClickListener {
            if (SettingsManager.getUseExternal()) {
                SettingsManager.setUseExternal(false)
                settings_app_directory_button.setText(R.string.save_app)
                MasterManager.load()
            } else {
                SettingsManager.setUseExternal(true)
                settings_app_directory_button.setText(R.string.save_external)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val permission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)

                    if (permission == PackageManager.PERMISSION_DENIED) requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_EXTERNAL_STORAGE_PERMISSION)
                    else MasterManager.load()
                }
            }
        }

        settings_app_directory_button.setOnLongClickListener {
            if (SettingsManager.getUseExternal()) {
                SettingsManager.setUseExternal(false)
                settings_app_directory_button.setText(R.string.save_app)
                moveToInternal()
                TaskManager.load()
                TagManager.load()
            } else {
                SettingsManager.setUseExternal(true)
                settings_app_directory_button.setText(R.string.save_external)

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
    }

    private fun setUpgrade() {
        // Set Data
        if (SettingsManager.getUseVersion() <= API.MANAGEMENT) settings_upgrade.text = getText(R.string.upgrade_to_api_12_testing)
        else settings_upgrade.text = getText(R.string.downgrade_to_api_11)

        // Set Click Listeners
        settings_upgrade.setOnClickListener {
            if (SettingsManager.getUseVersion() <= API.MANAGEMENT) {
                alert {
                    title = "Are you sure you want to continue?"
                    message = "This move may cause you to lose portions or all of your data"

                    positiveButton("Continue") {
                        SettingsManager.setUseVersion(API.SHRINKING)
                        SettingsManager.save()
                        settings_upgrade.text = getText(R.string.downgrade_to_api_11)
                    }

                    negativeButton("Cancel") { }
                }.show()
            } else {
                alert {
                    title = "Are you sure you want to continue?"
                    message = "This move may cause you to lose portions or all of your data"

                    positiveButton("Continue") {
                        SettingsManager.setUseVersion(API.MANAGEMENT)
                        SettingsManager.save()
                        settings_upgrade.text = getText(R.string.upgrade_to_api_12_testing)
                    }

                    negativeButton("Cancel") { }
                }.show()
            }
        }
    }

    private fun setHomeScreenSwitched() {
        // Set Data
        settings_tag_switch.isChecked = SettingsManager.hasTagShorcut()
        settings_priority_switch.isChecked = SettingsManager.hasPriorityShortcut()
        settings_status_switch.isChecked = SettingsManager.hasStatusShortcut()
        settings_day_switch.isChecked = SettingsManager.hasDayShortcut()
        settings_week_switch.isChecked = SettingsManager.hasWeekShortcut()
        settings_month_switch.isChecked = SettingsManager.hasMonthShortcut()

        // Set Click Listeners
        settings_tag_switch.setOnCheckedChangeListener { _, _ ->
            SettingsManager.setTagShortcut(!SettingsManager.hasTagShorcut())
            SettingsManager.save()
            if (SettingsManager.hasTagShorcut()) createTagShortcut(this)
            else removeTagShortcut(this)
        }

        settings_priority_switch.setOnCheckedChangeListener { _, _ ->
            SettingsManager.setPriorityShortcut(!SettingsManager.hasPriorityShortcut())
            SettingsManager.save()
            if (SettingsManager.hasPriorityShortcut()) createPriorityShortcut(this)
            else removePriorityShortcut(this)
        }

        settings_status_switch.setOnCheckedChangeListener { _, _ ->
            SettingsManager.setStatusShortcut(!SettingsManager.hasStatusShortcut())
            SettingsManager.save()
            if (SettingsManager.hasStatusShortcut()) createStatusShortcut(this)
            else removeStatusShortcut(this)
        }

        settings_day_switch.setOnCheckedChangeListener { _, _ ->
            SettingsManager.setDayShortcut(!SettingsManager.hasDayShortcut())
            SettingsManager.save()
            if (SettingsManager.hasDayShortcut()) createTodayShortcut(this)
            else removeTodayShortcut(this)
        }

        settings_week_switch.setOnCheckedChangeListener { _, _ ->
            SettingsManager.setWeekShorcut(!SettingsManager.hasWeekShortcut())
            SettingsManager.save()
            if (SettingsManager.hasWeekShortcut()) createWeekShortcut(this)
            else removeWeekShortcut(this)
        }

        settings_month_switch.setOnCheckedChangeListener { _, _ ->
            SettingsManager.setMonthShortcut(!SettingsManager.hasMonthShortcut())
            SettingsManager.save()
            if (SettingsManager.hasMonthShortcut()) createMonthShortcut(this)
            else removeMonthShortcut(this)
        }
    }

    private fun setContinue() {
        settings_continue_button.setOnClickListener {
            MasterManager.save()
            val home = Intent(this, HomeActivity::class.java)
            home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(home)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_EXTERNAL_STORAGE_PERMISSION -> if (grantResults.isNotEmpty()) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    SettingsManager.setUseExternal(false)
                    settings_app_directory_button.setText(R.string.save_app)
                } else {
                    MasterManager.load()
                }
            }
        }
    }
}
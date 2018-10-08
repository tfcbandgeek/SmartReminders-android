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
import jgappsandgames.smartreminderssave.loadMaster
import jgappsandgames.smartreminderssave.saveMaster
import jgappsandgames.smartreminderssave.settings.*
import jgappsandgames.smartreminderssave.tags.loadTags
import jgappsandgames.smartreminderssave.tasks.loadTasks
import jgappsandgames.smartreminderssave.utility.MANAGEMENT
import jgappsandgames.smartreminderssave.utility.SHRINKING

// KotlinX
import kotlinx.android.synthetic.main.activity_settings.*

// Library

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

        setUserName(settings_your_name_edit_text.text.toString())
        setDeviceName(settings_device_name_edit_text.text.toString())
        saveMaster()
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
        settings_your_name_edit_text.setText(getUserName())
        settings_device_name_edit_text.setText(getDeviceName())
        if (getUseExternal()) settings_app_directory_button.setText(R.string.external)
        else settings_app_directory_button.setText(R.string.internal)

        // Set Click Listeners
        settings_app_directory_button.setOnClickListener {
            if (getUseExternal()) {
                setUseExternal(false)
                settings_app_directory_button.setText(R.string.save_app)
                loadMaster()
            } else {
                setUseExternal(true)
                settings_app_directory_button.setText(R.string.save_external)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val permission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)

                    if (permission == PackageManager.PERMISSION_DENIED) requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_EXTERNAL_STORAGE_PERMISSION)
                    else loadMaster()
                }
            }
        }

        settings_app_directory_button.setOnLongClickListener {
            if (getUseExternal()) {
                setUseExternal(false)
                settings_app_directory_button.setText(R.string.save_app)
                moveToInternal()
                loadTasks()
                loadTags()
            } else {
                setUseExternal(true)
                settings_app_directory_button.setText(R.string.save_external)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val permission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)

                    if (permission == PackageManager.PERMISSION_DENIED) requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_EXTERNAL_STORAGE_PERMISSION)
                    else {
                        moveToExternal()
                        loadMaster()
                    }
                }
            }

            return@setOnLongClickListener true
        }
    }

    private fun setUpgrade() {
        // Set Data
        if (getUseVersion() <= MANAGEMENT) settings_upgrade.text = getText(R.string.upgrade_to_api_12_testing)
        else settings_upgrade.text = getText(R.string.downgrade_to_api_11)

        // Set Click Listeners
        settings_upgrade.setOnClickListener {
            if (getUseVersion() <= MANAGEMENT) {
                alert {
                    title = "Are you sure you want to continue?"
                    message = "This move may cause you to lose portions or all of your data"

                    positiveButton("Continue") {
                        setUseVersion(SHRINKING)
                        saveSettings()
                        settings_upgrade.text = getText(R.string.downgrade_to_api_11)
                    }

                    negativeButton("Cancel") { }
                }.show()
            } else {
                alert {
                    title = "Are you sure you want to continue?"
                    message = "This move may cause you to lose portions or all of your data"

                    positiveButton("Continue") {
                        setUseVersion(MANAGEMENT)
                        saveSettings()
                        settings_upgrade.text = getText(R.string.upgrade_to_api_12_testing)
                    }

                    negativeButton("Cancel") { }
                }.show()
            }
        }
    }

    private fun setHomeScreenSwitched() {
        // Set Data
        settings_tag_switch.isChecked = hasTagShorcut()
        settings_priority_switch.isChecked = hasPriorityShortcut()
        settings_status_switch.isChecked = hasStatusShortcut()
        settings_day_switch.isChecked = hasDayShortcut()
        settings_week_switch.isChecked = hasWeekShortcut()
        settings_month_switch.isChecked = hasMonthShortcut()

        // Set Click Listeners
        settings_tag_switch.setOnCheckedChangeListener { _, _ ->
            setTagShortcut(!hasTagShorcut())
            saveSettings()
            if (hasTagShorcut()) createTagShortcut(this)
            else removeTagShortcut(this)
        }

        settings_priority_switch.setOnCheckedChangeListener { _, _ ->
            setPriorityShortcut(!hasPriorityShortcut())
            saveSettings()
            if (hasPriorityShortcut()) createPriorityShortcut(this)
            else removePriorityShortcut(this)
        }

        settings_status_switch.setOnCheckedChangeListener { _, _ ->
            setStatusShortcut(!hasStatusShortcut())
            saveSettings()
            if (hasStatusShortcut()) createStatusShortcut(this)
            else removeStatusShortcut(this)
        }

        settings_day_switch.setOnCheckedChangeListener { _, _ ->
            setDayShortcut(!hasDayShortcut())
            saveSettings()
            if (hasDayShortcut()) createTodayShortcut(this)
            else removeTodayShortcut(this)
        }

        settings_week_switch.setOnCheckedChangeListener { _, _ ->
            setWeekShorcut(!hasWeekShortcut())
            saveSettings()
            if (hasWeekShortcut()) createWeekShortcut(this)
            else removeWeekShortcut(this)
        }

        settings_month_switch.setOnCheckedChangeListener { _, _ ->
            setMonthShortcut(!hasMonthShortcut())
            saveSettings()
            if (hasMonthShortcut()) createMonthShortcut(this)
            else removeMonthShortcut(this)
        }
    }

    private fun setContinue() {
        settings_continue_button.setOnClickListener {
            saveMaster()
            val home = Intent(this, HomeActivity::class.java)
            home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(home)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_EXTERNAL_STORAGE_PERMISSION -> if (grantResults.isNotEmpty()) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    setUseExternal(false)
                    settings_app_directory_button.setText(R.string.save_app)
                } else {
                    loadMaster()
                }
            }
        }
    }
}
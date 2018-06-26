package jgappsandgames.smartreminderslite.home

// Android OS
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle

// Views
import android.text.Editable
import android.text.TextWatcher

// Anko
import org.jetbrains.anko.toast

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.utility.ActivityUtility

// KotlinX
import kotlinx.android.synthetic.main.activity_first_run.first_run_app_directory_button
import kotlinx.android.synthetic.main.activity_first_run.first_run_continue_button
import kotlinx.android.synthetic.main.activity_first_run.first_run_day_switch
import kotlinx.android.synthetic.main.activity_first_run.first_run_device_name_edit_text
import kotlinx.android.synthetic.main.activity_first_run.first_run_month_switch
import kotlinx.android.synthetic.main.activity_first_run.first_run_priority_switch
import kotlinx.android.synthetic.main.activity_first_run.first_run_settings_button
import kotlinx.android.synthetic.main.activity_first_run.first_run_status_switch
import kotlinx.android.synthetic.main.activity_first_run.first_run_tag_switch
import kotlinx.android.synthetic.main.activity_first_run.first_run_tutorial_button
import kotlinx.android.synthetic.main.activity_first_run.first_run_week_switch
import kotlinx.android.synthetic.main.activity_first_run.first_run_your_name_edit_text
import kotlinx.android.synthetic.main.activity_first_run.list

// Save
import jgappsandgames.smartreminderssave.MasterManager
import jgappsandgames.smartreminderssave.settings.SettingsManager

/**
 * FirstRunActivity
 * Created by joshua on 12/30/2017.
 *
 * Activity Called on the First Run to Setup the App
 */
class FirstRun: Activity() {
    // LifeCycle Methods ---------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_run)
        setTitle(R.string.app_name)

        // Create Settings Page
        MasterManager.create()

        // Set Text
        first_run_device_name_edit_text.setText(SettingsManager.device_name)

        // Set Listeners
        first_run_your_name_edit_text.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                SettingsManager.user_name = first_run_your_name_edit_text.text.toString()
                SettingsManager.save()
            }
        })

        first_run_device_name_edit_text.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                SettingsManager.device_name = first_run_device_name_edit_text.text.toString()
                SettingsManager.save()
            }
        })

        first_run_app_directory_button.setOnClickListener {
            if (SettingsManager.use_external_file) {
                SettingsManager.use_external_file = false
                first_run_app_directory_button.setText(R.string.save_app)
            } else {
                SettingsManager.use_external_file = true
                first_run_app_directory_button.setText(R.string.save_external)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val permission = this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)

                    if (permission == PackageManager.PERMISSION_DENIED) {
                        requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), ActivityUtility.REQUEST_EXTERNAL_STORAGE_PERMISSION)
                    }
                }
            }
        }

        first_run_settings_button.setOnClickListener {
            toast(R.string.coming_soon).show()
        }

        first_run_tutorial_button.setOnClickListener {
            toast(R.string.coming_soon).show()
        }

        first_run_continue_button.setOnClickListener {
            MasterManager.save()
            val home = Intent(this, HomeActivity::class.java)
            home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(home)
        }

        // Hide Views
        list.removeView(first_run_tag_switch)
        list.removeView(first_run_priority_switch)
        list.removeView(first_run_status_switch)
        list.removeView(first_run_day_switch)
        list.removeView(first_run_week_switch)
        list.removeView(first_run_month_switch)
    }

    override fun onStop() {
        super.onStop()
        MasterManager.save()
    }

    // Activity Result -----------------------------------------------------------------------------
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            ActivityUtility.REQUEST_EXTERNAL_STORAGE_PERMISSION -> if (grantResults.isNotEmpty()) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    SettingsManager.use_external_file = false
                    first_run_app_directory_button.setText(R.string.save_app)
                }
            }
        }
    }
}
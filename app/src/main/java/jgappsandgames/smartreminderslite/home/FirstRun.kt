package jgappsandgames.smartreminderslite.home

// Android OS
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Editable

// Views
import android.view.View
import android.widget.Toast

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.utility.ActivityUtility

// Save
import jgappsandgames.smartreminderssave.MasterManager
import jgappsandgames.smartreminderssave.settings.SettingsManager
import org.jetbrains.anko.toast

/**
 * FirstRunActivity
 * Created by joshua on 12/30/2017.
 *
 * Activity Called on the First Run to Setup the App
 */
class FirstRun: FirstRunActivityInterface() {
    // LifeCycle Methods ---------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create Settings Page
        MasterManager.create()
    }

    // Click Listeners -----------------------------------------------------------------------------
    @SuppressLint("NewApi")
    override fun onClick(view: View) {
        // App Directory
        if (view == app_directory) {
            if (SettingsManager.use_external_file) {
                SettingsManager.use_external_file = false
                app_directory!!.setText(R.string.save_app)
            } else {
                SettingsManager.use_external_file = true
                app_directory!!.setText(R.string.save_external)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val permission = this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)

                    if (permission == PackageManager.PERMISSION_DENIED) {
                        requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), ActivityUtility.REQUEST_EXTERNAL_STORAGE_PERMISSION)
                    }
                }
            }
        }

        // Settings
        if (view == settings) {
            toast(R.string.coming_soon).show()
        }

        // Tutorial
        if (view == tutorial) {
            toast(R.string.coming_soon).show()
        }

        // Continue
        if (view == con) {
            MasterManager.save()
            val home = Intent(this, HomeActivity::class.java)
            home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(home)
        }
    }

    override fun onStop() {
        super.onStop()
        MasterManager.save()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            ActivityUtility.REQUEST_EXTERNAL_STORAGE_PERMISSION -> if (grantResults.isNotEmpty()) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    SettingsManager.use_external_file = false
                    app_directory!!.setText(R.string.save_app)
                }
            }
        }
    }

    // Text Watchers
    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
    override fun afterTextChanged(editable: Editable) {
        SettingsManager.user_name = your_name!!.text.toString()
        SettingsManager.device_name = device_name!!.text.toString()
        SettingsManager.save()
    }
}
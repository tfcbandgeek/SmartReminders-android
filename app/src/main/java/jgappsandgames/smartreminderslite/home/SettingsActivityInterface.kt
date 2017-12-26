package jgappsandgames.smartreminderslite.home

// Android OS
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle

// Views
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText

// App
import jgappsandgames.smartreminderslite.R

// Save
import jgappsandgames.smartreminderssave.settings.SettingsManager

/**
 * SettingsActivityInterface
 * Created by joshua on 12/25/2017.
 */
abstract class SettingsActivityInterface: Activity(), View.OnClickListener, View.OnLongClickListener {
    // Views ---------------------------------------------------------------------------------------
    protected var your_name: EditText? = null
    protected var device_name: EditText? = null
    protected var app_directory: Button? = null
    protected var tutorial: Button? = null

    // LifeCycle Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set Content View
        setContentView(R.layout.activity_first_run)

        // Find Views
        your_name = findViewById(R.id.yourname)
        device_name = findViewById(R.id.device_name)
        app_directory = findViewById(R.id.app_directory)
        tutorial = findViewById(R.id.tutorial)

        (findViewById<View>(R.id.settings).parent as ViewGroup).removeView(findViewById(R.id.settings))
        (findViewById<View>(R.id.con).parent as ViewGroup).removeView(findViewById(R.id.con))

        // Set Text
        your_name!!.setText(SettingsManager.user_name)
        device_name!!.setText(SettingsManager.device_name)
        if (SettingsManager.use_external_file) app_directory!!.setText(R.string.save_external)
        else app_directory!!.setText(R.string.save_app)

        // Set Listeners
        app_directory!!.setOnClickListener(this)
        app_directory!!.setOnLongClickListener(this)
        tutorial!!.setOnClickListener(this)
    }
}
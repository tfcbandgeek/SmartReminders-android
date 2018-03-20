package jgappsandgames.smartreminderslite.home

// Android OS
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle

// Views
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.Switch

// App
import jgappsandgames.smartreminderslite.R

// Save
import jgappsandgames.smartreminderssave.settings.SettingsManager

/**
 * SettingsActivityInterface
 * Created by joshua on 12/25/2017.
 */
abstract class SettingsActivityInterface: Activity(), View.OnClickListener, View.OnLongClickListener, CompoundButton.OnCheckedChangeListener {
    // Views ---------------------------------------------------------------------------------------
    protected var yourName: EditText? = null
    protected var deviceName: EditText? = null
    protected var app_directory: Button? = null
    protected var tutorial: Button? = null

    protected var tag: Switch? = null
    protected var priority: Switch? = null
    protected var status: Switch? = null
    protected var day: Switch? = null
    protected var week: Switch? = null
    protected var month: Switch? = null

    // LifeCycle Methods ---------------------------------------------------------------------------
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set Content View
        setContentView(R.layout.activity_first_run)

        // Find Views
        yourName = findViewById(R.id.yourname)
        deviceName = findViewById(R.id.device_name)
        app_directory = findViewById(R.id.app_directory)
        tutorial = findViewById(R.id.tutorial)

        (findViewById<Button>(R.id.settings).parent as ViewGroup).removeView(findViewById(R.id.settings))
        (findViewById<Button>(R.id.con).parent as ViewGroup).removeView(findViewById(R.id.con))

        tag = findViewById(R.id.tag)
        priority = findViewById(R.id.priority)
        status = findViewById(R.id.status)
        day = findViewById(R.id.day)
        week = findViewById(R.id.week)
        month = findViewById(R.id.month)

        // Set Text
        yourName!!.setText(SettingsManager.user_name)
        deviceName!!.setText(SettingsManager.device_name)
        if (SettingsManager.use_external_file) app_directory!!.setText(R.string.save_external)
        else app_directory!!.setText(R.string.save_app)

        // Set Listeners
        app_directory!!.setOnClickListener(this)
        app_directory!!.setOnLongClickListener(this)
        tutorial!!.setOnClickListener(this)
    }
}
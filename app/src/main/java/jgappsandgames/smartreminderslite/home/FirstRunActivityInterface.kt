package jgappsandgames.smartreminderslite.home

// Android OS
import android.app.Activity
import android.os.Bundle
import android.text.TextWatcher

// Views
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout

// App
import jgappsandgames.smartreminderslite.R

// Save
import jgappsandgames.smartreminderssave.settings.SettingsManager

/**
 * FirstRunActivityInterface
 * Created by joshua on 12/30/2017.
 *
 * Activity Called on the First Run to Setup the App
 */
abstract class FirstRunActivityInterface : Activity(), View.OnClickListener, TextWatcher {
    // Views
    protected var your_name: EditText? = null
    protected var device_name: EditText? = null
    protected var app_directory: Button? = null
    protected var settings: Button? = null
    protected var tutorial: Button? = null
    protected var con: Button? = null

    // LifeCycle Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set Content View
        setContentView(R.layout.activity_first_run)

        // Find Views
        your_name = findViewById(R.id.yourname)
        device_name = findViewById(R.id.device_name)
        app_directory = findViewById(R.id.app_directory)
        settings = findViewById(R.id.settings)
        tutorial = findViewById(R.id.tutorial)
        con = findViewById(R.id.con)

        // Set Text
        device_name!!.setText(SettingsManager.device_name)

        // Set Listeners
        your_name!!.addTextChangedListener(this)
        device_name!!.addTextChangedListener(this)
        app_directory!!.setOnClickListener(this)
        settings!!.setOnClickListener(this)
        tutorial!!.setOnClickListener(this)
        con!!.setOnClickListener(this)

        // Hide Views
        val parent = findViewById<LinearLayout>(R.id.list)
        parent.removeView(findViewById(R.id.tag))
        parent.removeView(findViewById(R.id.priority))
        parent.removeView(findViewById(R.id.status))
        parent.removeView(findViewById(R.id.day))
        parent.removeView(findViewById(R.id.week))
        parent.removeView(findViewById(R.id.month))
    }
}
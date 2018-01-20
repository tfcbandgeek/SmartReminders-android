package jgappsandgames.smartreminderslite.tasks.checkpoint

import android.app.Activity
import android.os.Bundle
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.utility.ActivityUtility

/**
 * Created by joshu on 1/19/2018.
 */
abstract class CheckpointActivityInterface: Activity(), TextWatcher, View.OnClickListener, View.OnLongClickListener {
    // Views
    protected var text_view: EditText? = null
    protected var status_button: Button? = null

    // Lifecycle Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set Content View
        setContentView(R.layout.activity_checkpoint)

        // Set Empty Return Intent
        setResult(ActivityUtility.RESPONSE_NONE)

        // Find
        text_view = findViewById(R.id.text)
        status_button = findViewById(R.id.status)
    }
}
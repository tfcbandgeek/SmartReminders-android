package jgappsandgames.smartreminderslite.tasks.checkpoint

// Android
import android.app.Activity
import android.os.Bundle

// Views
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.utility.ActivityUtility

/**
 * CheckpointActivityInterface
 * Created by joshua on 1/19/2018.
 */
abstract class CheckpointActivityInterface: Activity(), TextWatcher, View.OnClickListener, View.OnLongClickListener {
    // Views ---------------------------------------------------------------------------------------
    protected var text_view: EditText? = null
    protected var status_button: Button? = null

    // Lifecycle Methods ---------------------------------------------------------------------------
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
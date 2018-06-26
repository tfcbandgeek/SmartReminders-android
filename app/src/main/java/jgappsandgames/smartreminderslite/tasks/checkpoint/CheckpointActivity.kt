package jgappsandgames.smartreminderslite.tasks.checkpoint

// Android
import android.app.Activity
import android.content.Intent
import android.os.Bundle

// Views
import android.text.Editable
import android.text.TextWatcher

// JSON
import org.json.JSONException
import org.json.JSONObject

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.utility.ActivityUtility

// Save
import jgappsandgames.smartreminderssave.tasks.Task
import kotlinx.android.synthetic.main.activity_checkpoint.*

/**
 * CheckpointActivity
 * Created by joshua on 1/19/2018.
 */
class CheckpointActivity:  Activity(), TextWatcher {
    // Data ----------------------------------------------------------------------------------------
    private var position: Int = 0
    private var status: Boolean = false
    private var text: String? = null

    // Lifecycle Methods ---------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set Content View
        setContentView(R.layout.activity_checkpoint)

        // Set Empty Return Intent
        setResult(ActivityUtility.RESPONSE_NONE)

        // Load Data
        try {
            val data = JSONObject(intent.getStringExtra(ActivityUtility.CHECKPOINT))

            position = data.getInt(Task.CHECKPOINT_POSITION)
            status = data.getBoolean(Task.CHECKPOINT_STATUS)
            text = data.getString(Task.CHECKPOINT_TEXT)
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

        // Set Views
        checkpoint_text.setText(text)
        checkpoint_text.addTextChangedListener(this)

        checkpoint_status.setOnClickListener {
            status = !status
            setStatus()
            setReturnIntent()
        }

        checkpoint_continue.setOnClickListener {
            finish()
        }

        setStatus()
    }

    // TextWatcher ---------------------------------------------------------------------------------
    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
    override fun afterTextChanged(editable: Editable) {
        text = editable.toString()
        setReturnIntent()
    }

    // Class Methods -------------------------------------------------------------------------------
    private fun setStatus() {
        if (status) checkpoint_status.setText(R.string.completed)
        else checkpoint_status.setText(R.string.incomplete)
    }

    private fun setReturnIntent() {
        val r = JSONObject()

        try {
            r.put(Task.CHECKPOINT_POSITION, position)
            r.put(Task.CHECKPOINT_STATUS, status)
            r.put(Task.CHECKPOINT_TEXT, text)

            setResult(ActivityUtility.RESPONSE_CHANGE, Intent().putExtra(ActivityUtility.CHECKPOINT, r.toString()))
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }
}
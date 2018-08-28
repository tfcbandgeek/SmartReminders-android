package jgappsandgames.smartreminderslite.tasks

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
import jgappsandgames.smartreminderslite.utility.CHECKPOINT
import jgappsandgames.smartreminderslite.utility.RESPONSE_CHANGE
import jgappsandgames.smartreminderslite.utility.RESPONSE_NONE
import jgappsandgames.smartreminderssave.tasks.Checkpoint

// KotlinX
import kotlinx.android.synthetic.main.activity_checkpoint.checkpoint_continue
import kotlinx.android.synthetic.main.activity_checkpoint.checkpoint_status
import kotlinx.android.synthetic.main.activity_checkpoint.checkpoint_text

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
        setContentView(R.layout.activity_checkpoint)

        // Set Empty Return Intent
        setResult(RESPONSE_NONE)

        // Load Data
        try {
            val data = JSONObject(intent.getStringExtra(CHECKPOINT))

            position = data.getInt(Checkpoint.ID)
            status = data.getBoolean(Checkpoint.STATUS)
            text = data.getString(Checkpoint.TEXT)
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
            r.put(Checkpoint.ID, position)
            r.put(Checkpoint.STATUS, status)
            r.put(Checkpoint.TEXT, text)

            setResult(RESPONSE_CHANGE, Intent().putExtra(CHECKPOINT, r.toString()))
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }
}
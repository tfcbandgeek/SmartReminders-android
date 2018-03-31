package jgappsandgames.smartreminderslite.tasks.checkpoint

// Android
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle

// Views
import android.text.Editable
import android.view.View

// JSON
import org.json.JSONException
import org.json.JSONObject

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.utility.ActivityUtility

// Save
import jgappsandgames.smartreminderssave.tasks.Task

/**
 * CheckpointActivity
 * Created by joshua on 1/19/2018.
 */
class CheckpointActivity: CheckpointActivityInterface() {
    // Data ----------------------------------------------------------------------------------------
    private var position: Int = 0
    private var status: Boolean = false
    private var text: String? = null

    // Lifecycle Methods ---------------------------------------------------------------------------
    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        textView!!.setText(text)
        textView!!.addTextChangedListener(this)
        statusButton!!.setOnClickListener(this)
        statusButton!!.setOnLongClickListener(this)
        continueButton!!.setOnClickListener {
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

    // Click Listeners
    override fun onClick(view: View) {
        status = !status

        setStatus()
        setReturnIntent()
    }

    override fun onLongClick(view: View): Boolean {
        return false
    }

    // Class Methods -------------------------------------------------------------------------------
    private fun setStatus() {
        if (status) statusButton!!.setText(R.string.completed)
        else statusButton!!.setText(R.string.incomplete)
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
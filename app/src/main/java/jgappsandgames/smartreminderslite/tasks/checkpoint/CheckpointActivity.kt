package jgappsandgames.smartreminderslite.tasks.checkpoint

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.utility.ActivityUtility
import jgappsandgames.smartreminderssave.tasks.Task
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by joshu on 1/19/2018.
 */
class CheckpointActivity: CheckpointActivityInterface() {
    // Data
    private var position: Int = 0
    private var status: Boolean = false
    private var text: String? = null

    // Lifecycle Methods
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
        text_view!!.setText(text)
        text_view!!.addTextChangedListener(this)
        status_button!!.setOnClickListener(this)
        status_button!!.setOnLongClickListener(this)
        setStatus()
    }

    // TextWatcher
    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
        // Only included because it is required by the TextWatcher Interface
    }

    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
        // Only included because it is required b the TextWatcher Interface
    }

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

    // Class Methods
    private fun setStatus() {
        if (status) status_button!!.setText(R.string.completed)
        else status_button!!.setText(R.string.incomplete)
    }

    private fun setReturnIntent() {
        val r_data = JSONObject()

        try {
            r_data.put(Task.CHECKPOINT_POSITION, position)
            r_data.put(Task.CHECKPOINT_STATUS, status)
            r_data.put(Task.CHECKPOINT_TEXT, text)

            val intent = Intent().putExtra(ActivityUtility.CHECKPOINT, r_data.toString())

            setResult(ActivityUtility.RESPONSE_CHANGE, intent)
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }
}
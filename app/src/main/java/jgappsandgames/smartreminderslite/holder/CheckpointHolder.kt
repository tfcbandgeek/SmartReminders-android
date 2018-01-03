package jgappsandgames.smartreminderslite.holder

// Android OS
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Vibrator

// Views
import android.view.View
import android.widget.Button
import android.widget.CompoundButton
import android.widget.TextView

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.tasks.TaskActivity
import jgappsandgames.smartreminderslite.tasks.checkpoint.CheckpointActivity
import jgappsandgames.smartreminderslite.utility.ActivityUtility

// Save
import jgappsandgames.smartreminderssave.tasks.Checkpoint

/**
 * CheckpointHolder
 * Created by joshua on 12/30/2017.
 *
 * Handles the View For Drawing the Checkpoints in the Task
 */
class CheckpointHolder(private val activity: TaskActivity, private val task: String, private val checkpoint: Checkpoint, view: View):
        View.OnClickListener, View.OnLongClickListener, CompoundButton.OnCheckedChangeListener {
    // Views ---------------------------------------------------------------------------------------
    private val text: TextView
    private val edit: Button

    // Constructor
    init {
        // Find Views
        text = view.findViewById(R.id.text)
        edit = view.findViewById(R.id.edit)

        // Set Click Listeners
        text.setOnClickListener(this)
        text.setOnLongClickListener(this)
        edit.setOnClickListener(this)
        edit.setOnLongClickListener(this)

        // Set Views
        setViews()
    }

    // View Handler
    private fun setViews() {
        if (checkpoint.status) text.paintFlags = text.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        else text.paintFlags = text.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        text.text = checkpoint.text
    }

    // Click Handlers
    override fun onClick(view: View) {
        if (view == edit) {
            val intent = Intent(activity, CheckpointActivity::class.java)
            intent.putExtra(ActivityUtility.CHECKPOINT, checkpoint.toString())
            intent.putExtra(ActivityUtility.TASK_NAME, task)
            activity.startActivityForResult(intent, ActivityUtility.REQUEST_CHECKPOINT)
        } else if (view == text) {
            checkpoint.status = !checkpoint.status
            setViews()
            activity.editCheckpoint(checkpoint)
        }
    }

    override fun onLongClick(view: View): Boolean {
        activity.deleteCheckpoint(checkpoint)

        val v = activity.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
        try {
            if (v != null && v.hasVibrator()) v.vibrate(100)
        } catch (n: NullPointerException) {
            n.printStackTrace()
        }

        return true
    }

    // Check Handler
    override fun onCheckedChanged(view: CompoundButton, status: Boolean) {
        checkpoint.status = status
        activity.editCheckpoint(checkpoint)
    }
}
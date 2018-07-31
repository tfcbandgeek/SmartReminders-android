package jgappsandgames.smartreminderslite.holder

// Android OS
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

// Views
import android.view.View
import android.widget.Button
import android.widget.TextView

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.tasks.TaskActivity
import jgappsandgames.smartreminderslite.tasks.CheckpointActivity
import jgappsandgames.smartreminderslite.utility.CHECKPOINT
import jgappsandgames.smartreminderslite.utility.REQUEST_CHECKPOINT
import jgappsandgames.smartreminderslite.utility.TASK_NAME

// Save
import jgappsandgames.smartreminderssave.tasks.Checkpoint

/**
 * CheckpointHolder
 * Created by joshua on 12/30/2017.
 *
 * Handles the View For Drawing the Checkpoints in the Task
 */
class CheckpointHolder(private val activity: TaskActivity, private val task: String, private val checkpoint: Checkpoint, view: View):
        View.OnClickListener, View.OnLongClickListener {
    // Views ---------------------------------------------------------------------------------------
    private val text: TextView = view.findViewById(R.id.text)
    private val edit: Button = view.findViewById(R.id.edit)

    // Constructor ---------------------------------------------------------------------------------
    init {
        // Set Click Listeners
        text.setOnClickListener(this)
        text.setOnLongClickListener(this)
        edit.setOnClickListener(this)
        edit.setOnLongClickListener(this)

        // Set Views
        setViews()
    }

    // View Handler --------------------------------------------------------------------------------
    private fun setViews() {
        if (checkpoint.status) text.paintFlags = text.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        else text.paintFlags = text.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        text.text = checkpoint.text
    }

    // Click Handlers ------------------------------------------------------------------------------
    override fun onClick(view: View) {
        if (view == edit) {
            val intent = Intent(activity, CheckpointActivity::class.java)
            intent.putExtra(CHECKPOINT, checkpoint.toString())
            intent.putExtra(TASK_NAME, task)
            activity.startActivityForResult(intent, REQUEST_CHECKPOINT)
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
            if (v != null && v.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    v.vibrate(100)
                }
            }
        } catch (n: NullPointerException) {
            n.printStackTrace()
        }

        return true
    }
}
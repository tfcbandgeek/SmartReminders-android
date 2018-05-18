package jgappsandgames.smartreminderslite.holder

// Android OS
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect

// Views
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.tasks.TaskActivity
import jgappsandgames.smartreminderslite.utility.ActivityUtility

// Save
import jgappsandgames.smartreminderssave.tasks.Task
import jgappsandgames.smartreminderssave.tasks.TaskManager
import org.jetbrains.anko.vibrator

/**
 * TaskFolderHolder
 * Created by joshua on 12/13/2017.
 *
 * Task/Folder Holder for The TaskAdapter
 */
class TaskFolderHolder(private val activity: Activity, view: View,
        private var task: Task, private val onTaskChanged: OnTaskChangedListener, private val delete: Boolean = false):
        View.OnClickListener, View.OnLongClickListener, CompoundButton.OnCheckedChangeListener {
    // Views ---------------------------------------------------------------------------------------
    private var status: CheckBox? = null
    private var title: TextView = view.findViewById(R.id.title)
    private var note: TextView = view.findViewById(R.id.note)

    // Initializer ---------------------------------------------------------------------------------
    init {
        title.setOnClickListener(this)
        title.setOnLongClickListener(this)

        note.setOnClickListener(this)
        note.setOnLongClickListener(this)

        if (task.getType() == Task.TYPE_TASK) {
            status = view.findViewById(R.id.status)
            status!!.setOnCheckedChangeListener(this)
        }

        setViews()
    }

    // View Handler --------------------------------------------------------------------------------
    /**
     * SetViews
     *
     * Draw Method
     */
    fun setViews() {
        title.text = task.getTitle()
        note.text = task.getNote()

        if (task.getType() == Task.TYPE_TASK) status!!.isChecked = task.isCompleted()
    }

    fun update(_task: Task) {
        task = _task
        setViews()
    }

    // Click Handlers ------------------------------------------------------------------------------
    /**
     * OnClick
     *
     * Handles What Happens When The View is Pressed
     */
    override fun onClick(view: View) {
        val intent = Intent(activity, TaskActivity::class.java)
        intent.putExtra(ActivityUtility.TASK_NAME, task.getFilename())
        activity.startActivity(intent)
    }

    /**
     * OnLongClick
     *
     * Handles What Happens on A Long Click
     */
    override fun onLongClick(view: View): Boolean {
        if (delete) TaskManager.deleteTask(task)
        else TaskManager.archiveTask(task)

        try {
            if (activity.vibrator.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    activity.vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    activity.vibrator.vibrate(100)
                }
            }
        } catch (n: NullPointerException) {
            n.printStackTrace()
        }

        onTaskChanged.onTaskChanged()
        return true
    }

    // Check Listeners -----------------------------------------------------------------------------
    /**
     * OnCheckedChanged
     *
     * Handles What Happens When the Checkpoint Changed
     */
    override fun onCheckedChanged(compoundButton: CompoundButton, b: Boolean) {
        task.markComplete(b)
        task.save()
    }

    // Task Change Listener ------------------------------------------------------------------------
    /**
     * TaskChangeListener
     *
     * Listener For Handling Task Changing
     */
    interface OnTaskChangedListener {
        fun onTaskChanged()
    }
}
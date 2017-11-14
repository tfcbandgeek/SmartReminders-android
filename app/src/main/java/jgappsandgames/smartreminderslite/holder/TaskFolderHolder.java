package jgappsandgames.smartreminderslite.holder;

// Android OS
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;

// Views
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

// Program
import jgappsandgames.smartreminderslite.R;

import jgappsandgames.smartreminderslite.tasks.TaskActivity;
import jgappsandgames.smartreminderslite.utility.ActivityUtility;

// Save
import jgappsandgames.smartreminderssave.tasks.Task;
import jgappsandgames.smartreminderssave.tasks.TaskKt;
import jgappsandgames.smartreminderssave.tasks.TaskManagerKt;

/**
 * TaskFolderHolder
 * Created by joshua on 8/31/17.
 *
 * Implements OnClickListener, OnLongClickListener, OnCheckedChangeListener
 */
public class TaskFolderHolder implements OnClickListener, OnLongClickListener, OnCheckedChangeListener {
    // Log Constant
    private static final String LOG = "TaskFolderHolder";

    // Data
    public Task task;
    private final Activity activity;
    private final OnTaskChangedListener onTaskChanged;

    // Views
    private CheckBox status;
    private final TextView title;
    private final TextView note;

    // Initializer
    public TaskFolderHolder(Task task, View view, Activity activity, OnTaskChangedListener taskChangedListener) {
        Log.d(LOG, "Initializer Called");

        // Setting the Data
        Log.v(LOG, "Setting the Data");
        this.task = task;
        this.activity = activity;
        this.onTaskChanged = taskChangedListener;

        // Finding Views
        Log.v(LOG, "Finding Views");
        title = view.findViewById(R.id.title);
        note = view.findViewById(R.id.note);

        // Set Click Listeners
        Log.v(LOG, "Setting Click Listeners");
        title.setOnClickListener(this);
        title.setOnLongClickListener(this);
        note.setOnClickListener(this);
        note.setOnLongClickListener(this);

        // Task Actions
        if (task.getType() == TaskKt.getTYPE_TASK()) {
            Log.v(LOG, "Task Actions");
            status = view.findViewById(R.id.status);
            status.setOnCheckedChangeListener(this);
        }

        Log.v(LOG, "Set Views");
        setViews();

        Log.v(LOG, "Initializer Done");
    }

    // Viwe Handler
    public void setViews() {
        Log.d(LOG, "setViews Called");

        // Tasks and Folders
        Log.v(LOG, "Task and Folder Actions");
        title.setText(task.getTitle());
        note.setText(task.getNote());

        // Task Actions
        if (task.getType() == TaskKt.getTYPE_TASK()) {
            Log.v(LOG, "Task Actions");
            status.setChecked(task.isCompleted());
        }

        Log.v(LOG, "setViews Done");
    }

    // Click Handler
    @Override
    public void onClick(View view) {
        Log.d(LOG, "onClick Called");

        Intent intent = new Intent(activity, TaskActivity.class);
        intent.putExtra(ActivityUtility.TASK_NAME, task.getFilename());
        activity.startActivity(intent);

        Log.v(LOG, "onClick Done");
    }

    @Override
    public boolean onLongClick(View view) {
        Log.d(LOG, "onLongClick");

        // Archive the Task
        Log.v(LOG, "Archive the Task");
        TaskManagerKt.archiveTask(task);

        // Vibrate
        Log.v(LOG, "Vibrate");
        Vibrator v = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
        try {
            if (v != null && v.hasVibrator()) v.vibrate(100);
        } catch(NullPointerException n) {
            n.printStackTrace();
        }

        // Notify of Task Change
        Log.v(LOG, "Notify of Task Changed");
        if (onTaskChanged != null) onTaskChanged.onTaskChanged();
        return true;
    }

    // Check Listeners
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        Log.d(LOG, "onCheckedChange Called");

        // Change the Task
        Log.d(LOG, "Change the Task");
        task.markComplete(b);
        task.save();

        Log.v(LOG, "onCheckedChanged Done");
    }

    // Task Change Listener
    public interface OnTaskChangedListener {
        void onTaskChanged();
    }
}
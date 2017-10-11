package jgappsandgames.smartreminderslite.holder;

// Android OS
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

// Views
import android.os.Vibrator;
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
import jgappsandgames.smartreminderssave.tasks.TaskManager;

/**
 * TaskFolderHolder
 * Created by joshua on 8/31/17.
 * Last Edited on 10/5/17 (131).
 *
 * Implements OnClickListener, OnLongClickListener, OnCheckedChangeListener
 */
public class TaskFolderHolder implements OnClickListener, OnLongClickListener, OnCheckedChangeListener {
    // Data
    public Task task;
    private Activity activity;
    private OnTaskChangedListener onTaskChanged;

    // Views
    private CheckBox status;
    private TextView title;
    private TextView note;

    // Initializer
    @Deprecated
    public TaskFolderHolder(Task task, View view, Activity activity) {
        // Set data
        this.task = task;
        this.activity = activity;

        // Find Views
        title = view.findViewById(R.id.title);
        note = view.findViewById(R.id.note);

        title.setOnClickListener(this);
        title.setOnLongClickListener(this);

        note.setOnClickListener(this);
        note.setOnLongClickListener(this);

        if (task.getType() == Task.TYPE_TASK) {
            status = view.findViewById(R.id.status);
            status.setOnCheckedChangeListener(this);
        }
    }

    // Initializer
    public TaskFolderHolder(Task task, View view, Activity activity, OnTaskChangedListener taskChangedListener) {
        this.task = task;
        this.activity = activity;
        this.onTaskChanged = taskChangedListener;

        title = view.findViewById(R.id.title);
        title.setOnClickListener(this);
        title.setOnLongClickListener(this);

        note = view.findViewById(R.id.note);
        note.setOnClickListener(this);
        note.setOnLongClickListener(this);

        if (task.getType() == Task.TYPE_TASK) {
            status = view.findViewById(R.id.status);
            status.setOnCheckedChangeListener(this);
        }

        setViews();
    }

    // Viwe Handler
    public void setViews() {
        title.setText(task.getTitle());
        note.setText(task.getNote());

        if (task.getType() == Task.TYPE_TASK) {
            status.setChecked(task.isCompleted());
        }
    }

    // Click Handler
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(activity, TaskActivity.class);
        intent.putExtra(ActivityUtility.TASK_NAME, task.getFilename());
        activity.startActivity(intent);
    }

    @Override
    public boolean onLongClick(View view) {
        TaskManager.archiveTask(task);

        Vibrator v = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
        if (v.hasVibrator()) v.vibrate(100);

        if (onTaskChanged != null) onTaskChanged.onTaskChanged();
        return true;
    }

    // Check Listeners
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        task.markComplete(b);
        task.save();
    }

    // Task Change Listener
    public interface OnTaskChangedListener {
        public void onTaskChanged();
    }
}
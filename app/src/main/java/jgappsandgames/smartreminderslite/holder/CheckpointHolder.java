package jgappsandgames.smartreminderslite.holder;

// Android OS
import android.content.Context;
import android.content.Intent;

// Views
import android.os.Vibrator;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

// Program
import jgappsandgames.smartreminderslite.R;

import jgappsandgames.smartreminderslite.tasks.TaskActivity;
import jgappsandgames.smartreminderslite.tasks.checkpoint.CheckpointActivity;
import jgappsandgames.smartreminderslite.utility.ActivityUtility;

// Save
import jgappsandgames.smartreminderssave.tasks.Checkpoint;
import jgappsandgames.smartreminderssave.tasks.Task;

/**
 * CheckpointHolder
 * Created by joshua on 9/1/17.
 * Last Edited On 10/5/17 ().
 */
public class CheckpointHolder implements View.OnClickListener, View.OnLongClickListener, CompoundButton.OnCheckedChangeListener {
    // Data
    private Checkpoint checkpoint;
    private TaskActivity activity;
    private String task;

    // Views
    private CheckBox status;
    private TextView text;

    // Initializer
    public CheckpointHolder(TaskActivity activity, String task, Checkpoint checkpoint, View view) {
        super();

        // Set Data
        this.activity = activity;
        this.task = task;
        this.checkpoint = checkpoint;

        // Find Views
        status = view.findViewById(R.id.status);
        text = view.findViewById(R.id.text);

        // Set Click Listeners
        status.setOnCheckedChangeListener(this);
        text.setOnClickListener(this);
        text.setOnLongClickListener(this);

        // Set Views
        setViews();
    }

    // View Handler
    private void setViews() {
        status.setChecked(checkpoint.status);
        text.setText(checkpoint.text);
    }

    // Click Handlers
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(activity, CheckpointActivity.class);

        intent.putExtra(ActivityUtility.CHECKPOINT, checkpoint.toString());
        intent.putExtra(ActivityUtility.TASK_NAME, task);

        activity.startActivityForResult(intent, ActivityUtility.REQUEST_CHECKPOINT);
    }

    @Override
    public boolean onLongClick(View view) {
        activity.deleteCheckpoint(checkpoint);

        Vibrator v = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
        if (v.hasVibrator()) v.vibrate(100);

        return true;
    }

    // Check Handler
    @Override
    public void onCheckedChanged(CompoundButton view, boolean status) {
        checkpoint.status = status;
        activity.editCheckpoint(checkpoint);
    }
}
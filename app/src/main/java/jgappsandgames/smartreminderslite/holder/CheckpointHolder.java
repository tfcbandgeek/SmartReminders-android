package jgappsandgames.smartreminderslite.holder;

// Android OS
import android.content.Context;

// Views
import android.content.Intent;
import android.graphics.Paint;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

// Program
import jgappsandgames.smartreminderslite.R;
import jgappsandgames.smartreminderslite.tasks.TaskActivity;

// Save
import jgappsandgames.smartreminderslite.tasks.checkpoint.CheckpointActivity;
import jgappsandgames.smartreminderslite.utility.ActivityUtility;
import jgappsandgames.smartreminderssave.tasks.Checkpoint;

/**
 * CheckpointHolder
 * Created by joshua on 9/1/17.
 */
public class CheckpointHolder implements View.OnClickListener, View.OnLongClickListener, CompoundButton.OnCheckedChangeListener {
    // Data
    private final Checkpoint checkpoint;
    private final TaskActivity activity;
    private final String task;

    // Views
    //private final CheckBox status;
    private final TextView text;
    private final Button edit;

    // Initializer
    public CheckpointHolder(TaskActivity activity, String task, Checkpoint checkpoint, View view) {
        super();

        // Set Data
        this.activity = activity;
        this.task = task;
        this.checkpoint = checkpoint;

        // Find Views
        //status = view.findViewById(R.id.status);
        text = view.findViewById(R.id.text);
        edit = view.findViewById(R.id.edit);

        // Set Click Listeners
        //status.setOnCheckedChangeListener(this);
        text.setOnClickListener(this);
        text.setOnLongClickListener(this);
        edit.setOnClickListener(this);
        edit.setOnLongClickListener(this);

        // Set Views
        setViews();
    }

    // View Handler
    private void setViews() {
        //status.setChecked(checkpoint.status);
        text.setText(checkpoint.text);
        if (checkpoint.status) text.setPaintFlags(text.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        else text.setPaintFlags(text.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
    }

    // Click Handlers
    @Override
    public void onClick(View view) {
        if (view.equals(edit)) {
            Intent intent = new Intent(activity, CheckpointActivity.class);

            intent.putExtra(ActivityUtility.CHECKPOINT, checkpoint.toString());
            intent.putExtra(ActivityUtility.TASK_NAME, task);

            activity.startActivityForResult(intent, ActivityUtility.REQUEST_CHECKPOINT);
        } else if (view.equals(text)) {
            checkpoint.status = !checkpoint.status;
            activity.editCheckpoint(checkpoint);
            if (checkpoint.status) text.setPaintFlags(text.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            else text.setPaintFlags(text.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    @Override
    public boolean onLongClick(View view) {
        activity.deleteCheckpoint(checkpoint);

        Vibrator v = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
        try {
            if (v != null && v.hasVibrator()) v.vibrate(100);
        } catch (NullPointerException n) {
            n.printStackTrace();
        }

        return true;
    }

    // Check Handler
    @Override
    public void onCheckedChanged(CompoundButton view, boolean status) {
        checkpoint.status = status;
        activity.editCheckpoint(checkpoint);
    }
}
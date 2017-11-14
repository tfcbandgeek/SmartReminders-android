package jgappsandgames.smartreminderslite.holder;

// Android OS
import android.content.Context;

// Views
import android.content.Intent;
import android.graphics.Paint;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
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
public class CheckpointHolder implements OnClickListener, OnLongClickListener {
    // Log Constant
    private static final String LOG = "CheckpointHolder";

    // Data
    private final Checkpoint checkpoint;
    private final TaskActivity activity;
    private final String task;

    // Views
    private final TextView text;
    private final Button edit;

    // Initializer
    public CheckpointHolder(TaskActivity activity, String task, Checkpoint checkpoint, View view) {
        super();
        Log.d(LOG, "Initializer Called");

        // Set Data
        Log.v(LOG, "Setting the Data");
        this.activity = activity;
        this.task = task;
        this.checkpoint = checkpoint;

        // Find Views
        Log.v(LOG, "Finding Views");
        text = view.findViewById(R.id.text);
        edit = view.findViewById(R.id.edit);

        // Set Click Listeners
        Log.v(LOG, "Set Click Listeners");
        text.setOnClickListener(this);
        text.setOnLongClickListener(this);
        edit.setOnClickListener(this);
        edit.setOnLongClickListener(this);

        // Set Views
        Log.v(LOG, "Set the Views");
        setViews();

        Log.v(LOG, "Initializer Done");
    }

    // View Handler
    private void setViews() {
        Log.d(LOG, "SetViews Called");
        text.setText(checkpoint.text);
        if (checkpoint.status) text.setPaintFlags(text.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        else text.setPaintFlags(text.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);

        Log.v(LOG, "SetViews Done");
    }

    // Click Handlers
    @Override
    public void onClick(View view) {
        Log.d(LOG, "onClick Called");

        // Edit Button Pressed
        if (view.equals(edit)) {
            Log.v(LOG, "Edit Button Pressed");
            Intent intent = new Intent(activity, CheckpointActivity.class);

            intent.putExtra(ActivityUtility.CHECKPOINT, checkpoint.toString());
            intent.putExtra(ActivityUtility.TASK_NAME, task);

            activity.startActivityForResult(intent, ActivityUtility.REQUEST_CHECKPOINT);
        }

        // Text Pressed
        else if (view.equals(text)) {
            Log.v(LOG, "Text Pressed, Cycle Status");
            checkpoint.status = !checkpoint.status;
            activity.editCheckpoint(checkpoint);
            if (checkpoint.status) text.setPaintFlags(text.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            else text.setPaintFlags(text.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }

        Log.v(LOG, "onClick Done");
    }

    @Override
    public boolean onLongClick(View view) {
        Log.d(LOG, "onLongClick Pressed");
        activity.deleteCheckpoint(checkpoint);

        Vibrator v = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
        try {
            if (v != null && v.hasVibrator()) v.vibrate(100);
        } catch (NullPointerException n) {
            n.printStackTrace();
        }

        return true;
    }
}
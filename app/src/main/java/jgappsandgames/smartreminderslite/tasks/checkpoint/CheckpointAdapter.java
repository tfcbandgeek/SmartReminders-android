package jgappsandgames.smartreminderslite.tasks.checkpoint;

// Views
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

// Program
import java.util.List;

import jgappsandgames.smartreminderslite.R;
import jgappsandgames.smartreminderslite.holder.CheckpointHolder;
import jgappsandgames.smartreminderslite.tasks.TaskActivity;
import jgappsandgames.smartreminderssave.tasks.Checkpoint;
import jgappsandgames.smartreminderssave.tasks.Task;

/**
 * CheckpointAdapter
 * Created by joshua on 8/31/17.
 */
public class CheckpointAdapter extends BaseAdapter {
    // Data
    private TaskActivity activity;
    private String task;
    private List<Checkpoint> checkpoints;

    // Initializer
    public CheckpointAdapter(TaskActivity activity, String task, List<Checkpoint> checkpoints) {
        super();

        // Load Data
        this.activity = activity;
        this.task = task;
        this.checkpoints = checkpoints;
    }

    // List Methods

    @Override
    public int getCount() {
        return checkpoints.size();
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    // Item Methods
    @Override
    public Checkpoint getItem(int position) {
        return checkpoints.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // TODO: Only inflate when neccesary
        view = LayoutInflater.from(activity).inflate(R.layout.list_checkpoint, parent, false);

        CheckpointHolder holder = new CheckpointHolder(activity, task, getItem(position), view);
        view.setTag(holder);

        return view;
    }
}
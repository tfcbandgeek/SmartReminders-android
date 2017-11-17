package jgappsandgames.smartreminderslite.tasks;

// Views
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

// Program
import java.util.List;

import jgappsandgames.smartreminderslite.R;
import jgappsandgames.smartreminderslite.holder.CheckpointHolder;
import jgappsandgames.smartreminderssave.tasks.Checkpoint;

/**
 * CheckpointAdapter
 * Created by joshua on 8/31/17.
 */
class CheckpointAdapter extends BaseAdapter {
    // Data
    private final TaskActivity activity;
    private final String task;
    private final List<Checkpoint> checkpoints;

    // Initializer
    CheckpointAdapter(TaskActivity activity, String task, List<Checkpoint> checkpoints) {
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

    @Override
    public boolean hasStableIds() {
        return false;
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
        if (view == null) {
            view = LayoutInflater.from(activity).inflate(R.layout.list_checkpoint, parent, false);

            final CheckpointHolder holder = new CheckpointHolder(activity, task, getItem(position), view);
            view.setTag(holder);
        } else {
            final CheckpointHolder holder = new CheckpointHolder(activity, task, getItem(position), view);
            view.setTag(holder);
        }

        return view;
    }
}
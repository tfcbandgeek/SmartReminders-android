package jgappsandgames.smartreminderslite.priority;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import jgappsandgames.smartreminderslite.R;
import jgappsandgames.smartreminderslite.holder.TaskFolderHolder;
import jgappsandgames.smartreminderssave.tasks.Task;

/**
 * PriorityAdapter
 * Created by joshua on 10/9/17.
 */
class PriorityAdapter extends BaseAdapter {
    // Data
    private final PriorityActivity activity;
    private final List<Task> tasks;

    // Initializers
    PriorityAdapter(PriorityActivity activity, List<Task> tasks) {
        this.activity = activity;
        this.tasks = tasks;
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public Task getItem(int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getID();
    }



    @Override
    public View getView(int position, View convert_view, ViewGroup parent) {
        TaskFolderHolder holder;
        if (convert_view == null) {
            if (getItem(position).getType() == Task.TYPE_FLDR) convert_view = LayoutInflater.from(activity).inflate(R.layout.list_folder, parent, false);
            else convert_view = LayoutInflater.from(activity).inflate(R.layout.list_task, parent, false);

            holder = new TaskFolderHolder(getItem(position), convert_view, activity, activity);
            convert_view.setTag(holder);
        } else {
            holder = (TaskFolderHolder) convert_view.getTag();

            holder.task = getItem(position);
        }

        holder.setViews();

        return convert_view;
    }
}
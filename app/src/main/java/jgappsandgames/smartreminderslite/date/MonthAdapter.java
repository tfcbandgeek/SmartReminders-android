package jgappsandgames.smartreminderslite.date;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import jgappsandgames.smartreminderslite.R;
import jgappsandgames.smartreminderslite.holder.TaskFolderHolder;
import jgappsandgames.smartreminderssave.tasks.Task;

/**
 * MonthAdapter
 * Created by joshua on 10/14/17.
 * Last Edited on 10/14/17 (78).
 */
class MonthAdapter extends BaseAdapter {
    // Data
    private final MonthActivity activity;
    private final List<Task> tasks;

    public MonthAdapter(MonthActivity activity, List<Task> tasks) {
        this.activity = activity;
        this.tasks = tasks;
    }

    // List Methods
    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    // Item Methods
    @Override
    public Task getItem(int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getID();
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getType();
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
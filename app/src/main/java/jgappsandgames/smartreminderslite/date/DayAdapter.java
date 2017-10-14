package jgappsandgames.smartreminderslite.date;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.Calendar;
import java.util.List;

import jgappsandgames.smartreminderslite.R;
import jgappsandgames.smartreminderslite.holder.TaskFolderHolder;
import jgappsandgames.smartreminderssave.date.DateManager;
import jgappsandgames.smartreminderssave.tasks.Task;

/**
 * DayAdapter
 * Created by joshua on 10/9/17.
 * Last Edited on 10/9/17 (79).
 */
public class DayAdapter extends BaseAdapter {
    // Data
    private final List<Task> tasks;
    private final DayActivity activity;

    // Initializer
    public DayAdapter(DayActivity activity, Calendar date_active) {
        this.activity = activity;

        tasks = DateManager.getDay(date_active);
    }

    // List Methods
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
    public Task getItem(int i) {
        return tasks.get(i);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getType();
    }

    @Override
    public long getItemId(int i) {
        return getItem(i).getID();
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
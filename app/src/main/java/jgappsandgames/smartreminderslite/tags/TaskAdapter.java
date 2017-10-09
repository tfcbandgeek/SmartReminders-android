package jgappsandgames.smartreminderslite.tags;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import jgappsandgames.smartreminderslite.R;
import jgappsandgames.smartreminderslite.holder.TaskFolderHolder;
import jgappsandgames.smartreminderssave.tasks.Task;

/**
 * TaskAdapter
 * Created by joshua on 9/2/17.
 * Last Edited on 10/5/17 (97).
 */
public class TaskAdapter extends BaseAdapter {
    private TagActivity activity;
    private ArrayList<Task> tasks;

    public TaskAdapter(TagActivity activity, ArrayList<String> selected, ArrayList<Task> n_tasks) {
        super();

        this.activity = activity;

        tasks = new ArrayList<>();
        for (Task task : n_tasks) {
            boolean task_clear = true;

            for (String t : selected) {
                boolean tag_clear = false;

                for (int i = 0; i < task.getTags().size(); i++) {
                    if (task.getTags().get(i).equals(t)) {
                        tag_clear = true;
                        break;
                    }
                }

                if (!tag_clear) {
                    task_clear = false;
                    break;
                }
            }

            if (task_clear) tasks.add(task);
        }
    }

    // List Methods
    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public int getViewTypeCount() {
        return 1000;
    }

    @Override
    public Task getItem(int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getType();
    }

    @Override
    public View getView(int position, View convert_view, ViewGroup parent) {
        TaskFolderHolder holder = null;
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
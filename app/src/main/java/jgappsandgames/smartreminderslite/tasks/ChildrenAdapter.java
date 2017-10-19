package jgappsandgames.smartreminderslite.tasks;

// Java
import java.util.List;

// Views
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

// App
import jgappsandgames.smartreminderslite.R;
import jgappsandgames.smartreminderslite.holder.TaskFolderHolder;

// Save
import jgappsandgames.smartreminderssave.tasks.Task;

/**
 * ChildrenAdapter
 * Created by joshua on 8/31/17.
 * Last Edited on 10/11/17 (85).
 * Edited on 10/5/17 (79).
 */
class ChildrenAdapter extends BaseAdapter {
    // Data
    private final TaskActivity activity;
    private final List<String> tasks;

    // Initializer
    ChildrenAdapter(TaskActivity activity, List<String> tasks) {
        super();

        // Set Data
        this.activity = activity;
        this.tasks = tasks;
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
    public Task getItem(int position) {
        return new Task(tasks.get(position));
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
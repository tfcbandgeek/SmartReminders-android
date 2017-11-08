package jgappsandgames.smartreminderslite.adapter;

// Java
import java.util.ArrayList;
import java.util.List;

// Jetbrains
import org.jetbrains.annotations.Nullable;

// Android OS
import android.app.Activity;

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
 * TaskAdapterInterface
 * Created by joshua on 10/22/17.
 */
public abstract class TaskAdapterInterface extends BaseAdapter {
    // Data
    public final Activity activity;
    private final TaskFolderHolder.OnTaskChangedListener listener;
    public final List<Task> tasks;

    // Initializers
    public TaskAdapterInterface(Activity activity, TaskFolderHolder.OnTaskChangedListener listener, List<Task> tasks) {
        this.activity = activity;
        this.listener = listener;
        this.tasks = tasks;
    }

    public TaskAdapterInterface(Activity activity, TaskFolderHolder.OnTaskChangedListener listener, List<String> tasks, @Nullable String unused) {
        this.activity = activity;
        this.listener = listener;

        this.tasks = new ArrayList<>();
        for (String t : tasks) this.tasks.add(new Task(t));
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
        TaskFolderHolder holder;
        if (convert_view == null) {
            if (getItem(position).getType() == Task.TYPE_FLDR) convert_view = LayoutInflater.from(activity).inflate(R.layout.list_folder, parent, false);
            else convert_view = LayoutInflater.from(activity).inflate(R.layout.list_task, parent, false);

            holder = new TaskFolderHolder(getItem(position), convert_view, activity, listener);
            convert_view.setTag(holder);
        } else {
            holder = (TaskFolderHolder) convert_view.getTag();

            holder.task = getItem(position);
        }

        holder.setViews();

        return convert_view;
    }
}
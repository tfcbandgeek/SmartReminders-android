package jgappsandgames.smartreminderslite.adapter;

// Java
import java.util.ArrayList;

// Jetbrains
import org.jetbrains.annotations.Nullable;

// Android OS
import android.app.Activity;
import android.util.Log;

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
 *
 * The Main Adapter for any Task List
 * It Takes in an Array of either Task filepaths or Tasks and Displays them
 */
public abstract class TaskAdapterInterface extends BaseAdapter {
    // Log Constant
    private static final String LOG = "TaskAdapterInterface";

    // Task Type Constant
    private static final int TASK_COUNT = 3;

    // Data
    private final Activity activity;
    private final TaskFolderHolder.OnTaskChangedListener listener;
    private final ArrayList<Task> tasks;

    // Initializers
    protected TaskAdapterInterface(Activity activity, TaskFolderHolder.OnTaskChangedListener listener, ArrayList<Task> tasks) {
        Log.d(LOG, "Initializing.");
        this.activity = activity;
        this.listener = listener;
        this.tasks = tasks;

        Log.v(LOG, String.valueOf(this.tasks.size()));

        Log.v(LOG, "Finished Initializing.");
    }

    protected TaskAdapterInterface(Activity activity, TaskFolderHolder.OnTaskChangedListener listener, ArrayList<String> tasks, @SuppressWarnings({"SameParameterValue", "unused"}) @Nullable String unused) {
        Log.d(LOG, "Initializing.");
        this.activity = activity;
        this.listener = listener;

        this.tasks = new ArrayList<>();
        for (String t : tasks) this.tasks.add(new Task(t));

        Log.v(LOG, String.valueOf(this.tasks.size()));

        Log.v(LOG, "Finished Initializing.");
    }

    // List Methods
    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public int getViewTypeCount() {
        return TASK_COUNT;
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
        Log.d(LOG, "Get View Called");
        TaskFolderHolder holder;

        // View Does Not Exist so Create it
        if (convert_view == null) {
            Log.v(LOG, "Create the View");
            if (getItem(position).getType() == Task.TYPE_FLDR) convert_view = LayoutInflater.from(activity).inflate(R.layout.list_folder, parent, false);
            else convert_view = LayoutInflater.from(activity).inflate(R.layout.list_task, parent, false);

            holder = new TaskFolderHolder(getItem(position), convert_view, activity, listener);
            convert_view.setTag(holder);
        }

        // View Does Exist so Just Get it
        else {
            Log.v(LOG, "Load the View");
            holder = (TaskFolderHolder) convert_view.getTag();
            holder.task = getItem(position);
        }

        // Update View
        Log.v(LOG, "Update the View");
        holder.setViews();

        // Return the View
        Log.v(LOG, "Return the View");
        return convert_view;
    }
}
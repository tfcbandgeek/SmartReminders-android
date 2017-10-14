package jgappsandgames.smartreminderslite.tasks.tags;

// Java
import java.util.ArrayList;
import java.util.List;

// Views
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

// App
import jgappsandgames.smartreminderslite.R;
import jgappsandgames.smartreminderslite.holder.TagHolder;

// Save
import jgappsandgames.smartreminderssave.tags.TagManager;
import jgappsandgames.smartreminderssave.tasks.Task;

/**
 * TagUnselected Adapter
 * Created by joshua on 8/31/17.
 * Last Edited on 10/14/17 (92).
 * Edited on 10/5/17 (88).
 */
public class TagUnselectedAdapter extends BaseAdapter {
    // Data
    private final TagEditorActivity activity;
    public final List<String> tags;

    // Initializers
    public TagUnselectedAdapter(TagEditorActivity activity, Task task) {
        super();

        // Set Activity
        this.activity = activity;

        // Set Tags
        tags = new ArrayList<>();
        for (String tag : TagManager.tags) {
            if (!task.getTags().contains(tag)) tags.add(tag);
        }
    }

    public TagUnselectedAdapter(TagEditorActivity activity, Task task, String search) {
        // Set Activity
        this.activity = activity;


        // Set Tags
        tags = new ArrayList<>();
        for (String tag : TagManager.tags) {
            if (tag.toLowerCase().contains(search)) {
                if (!task.getTags().contains(tag)) tags.add(tag);
            }
        }
    }

    // List Methods
    @Override
    public int getCount() {
        return tags.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    // Item Methods

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public String getItem(int position) {
        return tags.get(position);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = LayoutInflater.from(activity).inflate(R.layout.list_tag, parent, false);

        TagHolder holder = new TagHolder(getItem(position), false, activity, view);
        view.setTag(holder);

        return view;
    }
}
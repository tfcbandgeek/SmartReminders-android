package jgappsandgames.smartreminderslite.tasks.tags;

// Java
import java.util.ArrayList;
import java.util.List;

// JSON
import org.json.JSONException;

// Views
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

// Program
import jgappsandgames.smartreminderslite.R;

import jgappsandgames.smartreminderslite.holder.TagHolder;
import jgappsandgames.smartreminderslite.tasks.tags.TagEditorActivity;
import jgappsandgames.smartreminderssave.tasks.Task;

/**
 * TagSelectedActivity
 * Created by joshua on 8/31/17.
 * Last Edited On 10/5/17 (89).
 */
public class TagSelectedAdapter extends BaseAdapter {
    // Constants
    private static final String ID = "TagSelectedAdapter";

    // Data
    private TagEditorActivity activity;
    public List<String> tags;

    // Initializers
    public TagSelectedAdapter(TagEditorActivity activity, Task task) {
        super();

        // Set Activity
        this.activity = activity;

        // Set Tags
        tags = task.getTags();
    }

    public TagSelectedAdapter(TagEditorActivity activity, Task task, String search) {
        // Set Activity
        this.activity = activity;

        // Set Tags
        tags = new ArrayList<>(task.getTags().size());
        for (String tag : task.getTags()) {
            if (tag.toLowerCase().contains(search.toLowerCase())) tags.add(tag);
        }
    }

    // List Methods
    @Override
    public int getCount() {
        return tags.size();
    }

    @Override
    public int getViewTypeCount() {
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

        TagHolder holder = new TagHolder(getItem(position), true, activity, view);
        view.setTag(holder);

        return view;
    }
}
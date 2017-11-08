package jgappsandgames.smartreminderslite.tags;

// Java
import java.util.ArrayList;

// Views
import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

// App
import jgappsandgames.smartreminderslite.R;
import jgappsandgames.smartreminderslite.holder.TagHolder;

// Save
import jgappsandgames.smartreminderssave.tags.TagManagerKt;

/**
 * UnselectedAdapter
 */
class UnselectedAdapter extends BaseAdapter {
    private final TagActivity activity;
    private final ArrayList<String> tags;

    @SuppressWarnings("ConstantConditions")
    UnselectedAdapter(TagActivity activity, ArrayList<String> selected) {
        super();

        this.activity = activity;

        tags = new ArrayList<>();
        for (int i = 0; i < TagManagerKt.getTags().size(); i++)
            if (!selected.contains(TagManagerKt.getTags().get(i))) tags.add(TagManagerKt.getTags().get(i));
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
    public String getItem(int position) {
        return tags.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // TODO: Implement ViewHolder Design
    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = LayoutInflater.from(activity).inflate(R.layout.list_tag, parent, false);

        TagHolder holder = new TagHolder(getItem(position), false, activity, view);
        view.setTag(holder);

        return view;
    }
}
package jgappsandgames.smartreminderslite.sort.tags;

// Java
import java.util.ArrayList;

// Views
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

// App
import jgappsandgames.smartreminderslite.R;
import jgappsandgames.smartreminderslite.holder.TagHolder;

// Program
import jgappsandgames.smartreminderssave.tags.TagManager;

/**
 * UnselectedAdapter
 * Created by joshua on 9/2/17.
 */
class UnselectedAdapter extends BaseAdapter {
    private final TagActivity activity;
    private final ArrayList<String> tags;

    UnselectedAdapter(TagActivity activity, ArrayList<String> selected) {
        super();

        this.activity = activity;

        tags = new ArrayList<>();
        for (int i = 0; i < TagManager.tags.size(); i++)
            if (!selected.contains(TagManager.tags.get(i))) tags.add(TagManager.tags.get(i));
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

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = LayoutInflater.from(activity).inflate(R.layout.list_tag, parent, false);

        TagHolder holder = new TagHolder(getItem(position), false, activity, view);
        view.setTag(holder);

        return view;
    }
}
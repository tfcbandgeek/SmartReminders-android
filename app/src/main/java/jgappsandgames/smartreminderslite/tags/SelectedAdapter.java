package jgappsandgames.smartreminderslite.tags;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import jgappsandgames.smartreminderslite.R;
import jgappsandgames.smartreminderslite.holder.TagHolder;

/**
 * SelectedAdapter
 * Created by joshua on 9/2/17.
 */
public class SelectedAdapter extends BaseAdapter {
    private TagActivity activity;
    private ArrayList<String> tags;

    // Initializer
    public SelectedAdapter(TagActivity activity, ArrayList<String> selected) {
        super();

        this.activity = activity;
        this.tags = selected;
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

        TagHolder holder = new TagHolder(getItem(position), true, activity, view);
        view.setTag(holder);

        return view;
    }
}
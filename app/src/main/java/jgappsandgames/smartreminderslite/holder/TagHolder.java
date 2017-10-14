package jgappsandgames.smartreminderslite.holder;

// Views
import android.view.View;
import android.widget.TextView;

// PRogram
import jgappsandgames.smartreminderslite.R;

/**
 * TagHolder
 * Created by joshua on 8/31/17.
 * Last Edited on 10/11/17 (60).
 */
public class TagHolder implements View.OnClickListener {
    // Data
    private final TagSwitcher switcher;

    private boolean tag_selected;
    private String tag_text;

    // Views
    private final TextView text_view;

    // Initializer
    public TagHolder(String tag, boolean selected, TagSwitcher switcher, View view) {
        super();

        // Setting Data
        this.switcher = switcher;
        this.tag_selected = selected;
        this.tag_text = tag;

        // Finding Views
        text_view = view.findViewById(R.id.tag);

        // Setting Views
        text_view.setText(tag_text);
        text_view.setOnClickListener(this);
    }

    // Interfaces
    public interface TagSwitcher {
        void moveTag(String tag, boolean selected);
    }

    // Management Methods
    public void updateView(String tag, boolean selected) {
        this.tag_selected = selected;
        this.tag_text = tag;

        text_view.setText(tag_text);
    }

    // Click Listener
    @Override
    public void onClick(View view) {
        switcher.moveTag(tag_text, !tag_selected);
    }
}
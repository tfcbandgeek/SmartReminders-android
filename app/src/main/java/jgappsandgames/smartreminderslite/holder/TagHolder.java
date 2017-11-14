package jgappsandgames.smartreminderslite.holder;

// Android
import android.util.Log;

// Views
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

// App
import jgappsandgames.smartreminderslite.R;

/**
 * TagHolder
 * Created by joshua on 8/31/17.
 */
public class TagHolder implements OnClickListener {
    // Log Constant
    private static final String LOG = "TagHolder";

    // Data
    private final TagSwitcher switcher;

    private boolean tag_selected;
    private String tag_text;

    // Views
    private final TextView text_view;

    // Initializer
    public TagHolder(String tag, boolean selected, TagSwitcher switcher, View view) {
        super();
        Log.d(LOG, "Initializer Called");

        // Setting Data
        Log.v(LOG, "Setting the Data");
        this.switcher = switcher;
        this.tag_selected = selected;
        this.tag_text = tag;

        // Finding Views
        Log.v(LOG, "Finding Views");
        text_view = view.findViewById(R.id.tag);

        // Setting Views
        Log.v(LOG, "Setting Views");
        text_view.setText(tag_text);
        text_view.setOnClickListener(this);

        Log.d(LOG, "Initializer Done");
    }
    // Management Methods
    @SuppressWarnings("unused")
    public void updateView(String tag, boolean selected) {
        Log.d(LOG, "updateView Called");
        this.tag_selected = selected;
        this.tag_text = tag;

        text_view.setText(tag_text);
        Log.v(LOG, "updateView Done");
    }

    // Click Listener
    @Override
    public void onClick(View view) {
        switcher.moveTag(tag_text, !tag_selected);
    }

    // Interfaces
    public interface TagSwitcher {
        void moveTag(String tag, boolean selected);
    }
}
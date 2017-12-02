package jgappsandgames.smartreminderslite.tasks.tags;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import jgappsandgames.smartreminderslite.R;
import jgappsandgames.smartreminderslite.holder.TagHolder;
import jgappsandgames.smartreminderslite.utility.ActivityUtility;

/**
 * TagEditorActivityInterface
 * Created by joshua on 11/29/2017.
 */
public abstract class TagEditorActivityInterface extends Activity implements TextWatcher, View.OnClickListener, View.OnLongClickListener, TagHolder.TagSwitcher {
    // Views
    protected EditText search_text;
    protected Button search_enter;
    protected ListView selected;
    protected ListView unselected;

    // LifeCycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set Content View
        setContentView(R.layout.activity_tag_editpr);

        // Find Views
        search_text = findViewById(R.id.search_text);
        search_enter = findViewById(R.id.search_enter);
        selected = findViewById(R.id.selected);
        unselected = findViewById(R.id.unselected);

        // Set Listeners
        search_enter.setOnClickListener(this);
        search_enter.setOnLongClickListener(this);
        search_enter.addTextChangedListener(this);

        // Set Result Intent
        setResult(ActivityUtility.RESPONSE_NONE);
    }
}

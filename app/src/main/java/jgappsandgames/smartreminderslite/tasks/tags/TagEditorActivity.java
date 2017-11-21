package jgappsandgames.smartreminderslite.tasks.tags;

// JSON
import org.json.JSONArray;
import org.json.JSONException;

// Android OS
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

// Views
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

// App
import jgappsandgames.smartreminderslite.R;
import jgappsandgames.smartreminderslite.holder.TagHolder.TagSwitcher;
import jgappsandgames.smartreminderslite.utility.ActivityUtility;

// Save
import jgappsandgames.smartreminderssave.tags.TagManager;
import jgappsandgames.smartreminderssave.tasks.Task;

/**
 * Tag Editor Activity
 * Created by joshua on 8/31/17.
 */
public class TagEditorActivity extends Activity implements TextWatcher, OnClickListener, OnLongClickListener, TagSwitcher {
    // Data
    private Task task;

    // Views
    private EditText search_text;
    private Button search_enter;
    private ListView selected;
    private ListView unselected;

    // LifeCycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set Content View
        setContentView(R.layout.activity_tag_editpr);

        // Load Data
        task = new Task(getIntent().getStringExtra(ActivityUtility.TASK_NAME));

        // Find Views
        search_text = findViewById(R.id.search_text);
        search_enter = findViewById(R.id.search_enter);
        selected = findViewById(R.id.selected);
        unselected = findViewById(R.id.unselected);

        // Set Adapters
        selected.setAdapter(new TagSelectedAdapter(this, task));
        unselected.setAdapter(new TagUnselectedAdapter(this, task));

        // Set Listeners
        search_enter.setOnClickListener(this);
        search_enter.setOnLongClickListener(this);
        search_enter.addTextChangedListener(this);

        // Set Result Intent
        setResult(ActivityUtility.RESPONSE_NONE);
    }

    // Text Watcher Methods
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // Not Needed, Only included because it is required by the TextWatcher
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // Not Needed, OnLy included because it is required by the TextWatcher
    }

    @Override
    public void afterTextChanged(Editable editable) {
        selected.setAdapter(new TagSelectedAdapter(this, task, editable.toString()));
        unselected.setAdapter(new TagUnselectedAdapter(this, task, editable.toString()));

        if (TagManager.contains(editable.toString())) search_enter.setText(R.string.select);
        else search_enter.setText(R.string.add);
    }

    // Click Listeners
    @Override
    public void onClick(View view) {
        if (TagManager.addTag(search_text.getText().toString())) {
            task.addTag(search_text.getText().toString());
            search_text.setText("");

            selected.setAdapter(new TagSelectedAdapter(this, task));
            unselected.setAdapter(new TagUnselectedAdapter(this, task));

            // Set new Result Intent
            try {
                JSONArray tags  = new JSONArray();
                for (String tag : task.getTags()) tags.put(tag);
                setResult(ActivityUtility.RESPONSE_CHANGE, new Intent().putExtra(ActivityUtility.TAG_LIST, tags.toString(4)));
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
            }
        } else if (search_text.getText().toString().equals("")) {
            task.addTag(search_text.getText().toString());
            search_text.setText("");

            selected.setAdapter(new TagSelectedAdapter(this, task));
            unselected.setAdapter(new TagUnselectedAdapter(this, task));

            // Set new Result Intent
            try {
                JSONArray tags  = new JSONArray();
                for (String tag : task.getTags()) tags.put(tag);
                setResult(ActivityUtility.RESPONSE_CHANGE, new Intent().putExtra(ActivityUtility.TAG_LIST, tags.toString(4)));
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }

    // TagSwitcher
    @Override
    public void moveTag(String tag, boolean selected) {
        if (selected) task.addTag(tag);
        else task.removeTag(tag);

        // Set Adapters
        this.selected.setAdapter(new TagSelectedAdapter(this, task));
        this.unselected.setAdapter(new TagUnselectedAdapter(this, task));

        // Set New Result Intent
        try {
            JSONArray tags  = new JSONArray();
            for (String ta : task.getTags()) tags.put(ta);
            setResult(ActivityUtility.RESPONSE_CHANGE, new Intent().putExtra(ActivityUtility.TAG_LIST, tags.toString(4)));
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }
    }
}
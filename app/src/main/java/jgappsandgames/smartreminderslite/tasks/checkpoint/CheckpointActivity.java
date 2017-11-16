package jgappsandgames.smartreminderslite.tasks.checkpoint;

// JSON
import org.json.JSONException;
import org.json.JSONObject;

// Android OS
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

// Views
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

// Program
import jgappsandgames.smartreminderslite.R;
import jgappsandgames.smartreminderslite.utility.ActivityUtility;

// Save
import jgappsandgames.smartreminderssave.tasks.Task;

/**
 * CheckpointActivity
 * Created by joshua on 8/31/17.
 */
public class CheckpointActivity
        extends Activity
        implements TextWatcher, View.OnClickListener, View.OnLongClickListener {
    // Data
    private int position;
    private boolean status;
    private String text;

    // Views
    private EditText text_view;
    private Button status_button;

    // Lifecycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set Content View
        setContentView(R.layout.activity_checkpoint);

        // Load Data
        try {
            JSONObject data = new JSONObject(getIntent().getStringExtra(ActivityUtility.CHECKPOINT));

            position = data.getInt(Task.CHECKPOINT_POSITION);
            status = data.getBoolean(Task.CHECKPOINT_STATUS);
            text = data.getString(Task.CHECKPOINT_TEXT);
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }

        // Set Empty REturn Intent
        setResult(ActivityUtility.RESPONSE_NONE);

        // Find
        text_view = findViewById(R.id.text);
        status_button = findViewById(R.id.status);

        // Set Views
        text_view.setText(text);
        text_view.addTextChangedListener(this);
        status_button.setOnClickListener(this);
        status_button.setOnLongClickListener(this);
        setStatus();
    }

    // TextWatcher
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // Only included because it is required by the TextWatcher Interface
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // Only included because it is required b the TextWatcher Interface
    }

    @Override
    public void afterTextChanged(Editable editable) {
        text = editable.toString();

        setReturnIntent();
    }

    // Click Listeners
    @Override
    public void onClick(View view) {
        status = !status;

        setStatus();
        setReturnIntent();
    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }

    // Class Methods
    private void setStatus() {
        if (status) status_button.setText(R.string.completed);
        else status_button.setText(R.string.incomplete);
    }

    private void setReturnIntent() {
        JSONObject r_data = new JSONObject();

        try {
            r_data.put(Task.CHECKPOINT_POSITION, position);
            r_data.put(Task.CHECKPOINT_STATUS, status);
            r_data.put(Task.CHECKPOINT_TEXT, text);

            Intent intent = new Intent();
            intent.putExtra(ActivityUtility.CHECKPOINT, r_data.toString());

            setResult(ActivityUtility.RESPONSE_CHANGE, intent);
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }
    }
}
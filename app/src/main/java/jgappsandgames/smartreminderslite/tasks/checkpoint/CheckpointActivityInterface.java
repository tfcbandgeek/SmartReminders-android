package jgappsandgames.smartreminderslite.tasks.checkpoint;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import jgappsandgames.smartreminderslite.R;
import jgappsandgames.smartreminderslite.utility.ActivityUtility;
import jgappsandgames.smartreminderssave.tasks.Task;

/**
 * CheckpointActivityInterface
 * Created by joshua on 11/29/2017.
 */
public abstract class CheckpointActivityInterface extends Activity implements TextWatcher, View.OnClickListener, View.OnLongClickListener{
    // Views
    protected EditText text_view;
    protected Button status_button;

    // Lifecycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set Content View
        setContentView(R.layout.activity_checkpoint);

        // Set Empty REturn Intent
        setResult(ActivityUtility.RESPONSE_NONE);

        // Find
        text_view = findViewById(R.id.text);
        status_button = findViewById(R.id.status);
    }
}
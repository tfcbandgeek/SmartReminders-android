package jgappsandgames.smartreminderslite.tasks;

// Java
import java.util.ArrayList;

// JSON
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// Android OS
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

// Views
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

// App
import jgappsandgames.smartreminderslite.R;

import jgappsandgames.smartreminderslite.holder.TaskFolderHolder;
import jgappsandgames.smartreminderslite.tasks.checkpoint.CheckpointActivity;
import jgappsandgames.smartreminderslite.tasks.checkpoint.CheckpointAdapter;
import jgappsandgames.smartreminderslite.tasks.tags.TagEditorActivity;
import jgappsandgames.smartreminderslite.utility.ActivityUtility;

// Save
import jgappsandgames.smartreminderssave.tags.TagManager;
import jgappsandgames.smartreminderssave.tasks.Checkpoint;
import jgappsandgames.smartreminderssave.tasks.Task;
import jgappsandgames.smartreminderssave.tasks.TaskManager;

/**
 * TaskActivity
 * Created by joshua on 8/31/17.
 * Last Edited on 10/11/17 (350).
 * Edited On 10/5/17 (334).
 *
 * Main Task View
 */
public class TaskActivity
        extends Activity
        implements TextWatcher, View.OnClickListener, View.OnLongClickListener, TaskFolderHolder.OnTaskChangedListener, SeekBar.OnSeekBarChangeListener {
    // Data
    private Task task;

    // Views
    private EditText title;
    private EditText note;
    private TextView tags;
    private Button status;
    private SeekBar priority;
    private ListView list;
    private Button fab;

    // Adapters
    private BaseAdapter adapter;

    // Life Cycle MEthods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load Data
        task = new Task(getIntent().getStringExtra(ActivityUtility.TASK_NAME));

        // Set Title
        setTitle(task.getTitle());

        // Set Content View
        if (task.getType() == Task.TYPE_TASK) setContentView(R.layout.activity_task);
        else setContentView(R.layout.activity_folder);

        // Find Views
        title = findViewById(R.id.title);
        note = findViewById(R.id.note);
        tags = findViewById(R.id.tags);
        list = findViewById(R.id.tasks);

        fab = findViewById(R.id.fab);

        if (task.getType() == Task.TYPE_TASK) {
            status = findViewById(R.id.status);
            priority = findViewById(R.id.priority);
        }

        // Set Text
        title.setText(task.getTitle());
        note.setText(task.getNote());

        if (task.getType() == Task.TYPE_TASK) {
            setStatus();
            priority.setMax(100);
            priority.setProgress(task.getPriority());
            priority.setOnSeekBarChangeListener(this);
        }

        // Set TextWatcher
        title.addTextChangedListener(this);
        note.addTextChangedListener(this);

        // Set Click Listener
        fab.setOnClickListener(this);
        fab.setOnLongClickListener(this);

        if (task.getType() == Task.TYPE_TASK) {
            status.setOnClickListener(this);
        }

        tags.setOnClickListener(this);
        tags.setOnLongClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        task = new Task(getIntent().getStringExtra(ActivityUtility.TASK_NAME));

        if (task.getType() == Task.TYPE_FLDR) {
            adapter = new ChildrenAdapter(this, task.getChildren());
            list.setAdapter(adapter);
        } else {
            adapter = new CheckpointAdapter(this, task.getFilename(), task.getCheckpoints());
            list.setAdapter(adapter);
        }

        setTags();
    }

    @Override
    protected void onPause() {
        super.onPause();

        task.save();
    }

    // Activity Result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Checkpoint Result
            case ActivityUtility.REQUEST_CHECKPOINT:
                if (resultCode == ActivityUtility.RESPONSE_CHANGE) {
                    try {
                        editCheckpoint(new Checkpoint(new JSONObject(data.getStringExtra(ActivityUtility.CHECKPOINT))));
                    } catch (JSONException | NullPointerException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case ActivityUtility.REQUEST_TAGS:
                if (resultCode == ActivityUtility.RESPONSE_CHANGE) {
                    try {
                        JSONArray temp = new JSONArray(data.getStringExtra(ActivityUtility.TAG_LIST));
                        ArrayList<String> t = new ArrayList<>();

                        for (int i = 0; i < temp.length(); i++) {
                            t.add(temp.optString(i));
                        }

                        task.setTags(t);
                    } catch (JSONException | NullPointerException e) {
                        e.printStackTrace();
                    }
                    TagManager.save();
                    task.save();

                    setTags();
                }
                break;
        }
    }

    // Menu Methods
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                task.save();
                Toast.makeText(this, "Saved.", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.management:
                Intent intent = new Intent(this, TaskManagementActivity.class);
                intent.putExtra(ActivityUtility.TASK_NAME, task.getFilename());
                startActivityForResult(intent, ActivityUtility.REQUEST_Management);
                return true;

            case R.id.close:
                finish();
                return true;
        }

        return false;
    }

    // TextWatcher
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // Not needed , Only included because it is required by the TextWatcher
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // Not Needed, Only included because it is required by the TextWatcher
    }

    @Override
    public void afterTextChanged(Editable editable) {
        task.setTitle(title.getText().toString());
        task.setNote(note.getText().toString());
    }

    // Click Listeners
    @Override
    public void onClick(View view) {
        // Fab Click
        if (view.equals(fab)) {
            // Called in A Folder
            if (task.getType() == Task.TYPE_FLDR) {
                // Create Task
                Task t = new Task(task.getFilename(), Task.TYPE_TASK);
                t.save();

                task.addChild(t.getFilename());
                task.save();

                TaskManager.tasks.add(t.getFilename());
                TaskManager.save();

                // Create Intent
                Intent task_intent = new Intent(this, TaskActivity.class);
                task_intent.putExtra(ActivityUtility.TASK_NAME, t.getFilename());

                startActivity(task_intent);
            }

            // Called in A Task
            else {
                Checkpoint checkpoint = new Checkpoint(task.getCheckpoints().get(task.getCheckpoints().size() - 1).id + 1, "");

                Intent check_intent = new Intent(this, CheckpointActivity.class);
                check_intent.putExtra(ActivityUtility.CHECKPOINT, checkpoint.toString());

                startActivityForResult(check_intent, ActivityUtility.REQUEST_CHECKPOINT);
            }
        } else if (view.equals(status)) {
            if (task.getStatus() == Task.STATUS_DONE) task.markComplete(false);
            else task.markComplete(true);

            task.save();
            setStatus();
        } else if (view.equals(tags)) {
            Intent tag_intent = new Intent(this, TagEditorActivity.class);
            tag_intent.putExtra(ActivityUtility.TASK_NAME, task.getFilename());
            startActivityForResult(tag_intent, ActivityUtility.REQUEST_TAGS);
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if (view.equals(fab)) {
            if (task.getType() == Task.TYPE_FLDR) {
                // Create Task
                Task t = new Task(task.getFilename(), Task.TYPE_FLDR);
                t.save();

                task.addChild(t.getFilename());
                task.save();

                TaskManager.tasks.add(t.getFilename());
                TaskManager.save();

                // Create Intent
                Intent intent = new Intent(this, TaskActivity.class);
                intent.putExtra(ActivityUtility.TASK_NAME, t.getFilename());

                // Start Activity
                startActivity(intent);

                return true;
            }
        } else if (view.equals(status)) {
            return false;
        }

        return false;
    }

    // Priority Seekbar Listeners
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        task.setPriority(seekBar.getProgress());
    }

    // Class Methods
    private void setStatus() {
        if (task.getType() == Task.TYPE_TASK) {
            if (task.getStatus() == Task.STATUS_DONE) status.setText("Completed.");
            else status.setText("In Progress.");
        }
    }

    public void editCheckpoint(Checkpoint checkpoint) {
        task.editCheckpoint(checkpoint);
        task.save();
    }

    public void deleteCheckpoint(Checkpoint checkpoint) {
        task.removeCheckpoint(checkpoint);
        task.save();
    }

    private void setTags() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < task.getTags().size(); i++) builder.append(task.getTags().get(i)).append(", ");
        if (builder.length() >= 2) builder.setLength(builder.length() - 2);
        tags.setText(builder.toString());
    }

    @Override
    public void onTaskChanged() {
        onResume();
    }
}
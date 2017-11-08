package jgappsandgames.smartreminderslite.tasks;

// Java
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

// JSON
import android.app.DatePickerDialog;
import android.widget.*;
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
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.SeekBar.OnSeekBarChangeListener;

// App
import jgappsandgames.smartreminderslite.R;

import jgappsandgames.smartreminderslite.holder.TaskFolderHolder.OnTaskChangedListener;
import jgappsandgames.smartreminderslite.tasks.checkpoint.CheckpointActivity;
import jgappsandgames.smartreminderslite.tasks.tags.TagEditorActivity;
import jgappsandgames.smartreminderslite.utility.ActivityUtility;

// Save
import jgappsandgames.smartreminderssave.tags.TagManager;
import jgappsandgames.smartreminderssave.tasks.Checkpoint;
import jgappsandgames.smartreminderssave.tasks.Task;

/**
 * TaskActivity
 * Created by joshua on 8/31/17.
 * Last Edited on 10/15/17 (350).
 * Edited on 10/11/17 (350).
 * Edited On 10/5/17 (334).
 *
 * Main Task View
 */
public class TaskActivity
        extends Activity
        implements TextWatcher, OnClickListener, OnLongClickListener, OnSeekBarChangeListener, DatePickerDialog.OnDateSetListener,
        OnTaskChangedListener  {
    // Data
    private Task task;

    // Views
    private EditText title;
    private EditText note;
    private TextView tags;
    private Button date;
    private Button status;
    private SeekBar priority;
    private ListView list;
    private Button fab;

    // Life Cycle Methods
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
            date = findViewById(R.id.date);
            status = findViewById(R.id.status);
            priority = findViewById(R.id.priority);

            date.setText(task.getDateDueString());
            setStatus();
            priority.setMax(100);
            priority.setProgress(task.getPriority());
            priority.setOnSeekBarChangeListener(this);

            date.setOnClickListener(this);
            date.setOnLongClickListener(this);
            status.setOnClickListener(this);
        }

        // Set Text
        title.setText(task.getTitle());
        note.setText(task.getNote());

        // Set TextWatcher
        title.addTextChangedListener(this);
        note.addTextChangedListener(this);

        // Set Click Listener
        fab.setOnClickListener(this);
        fab.setOnLongClickListener(this);

        tags.setOnClickListener(this);
        tags.setOnLongClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        task = new Task(getIntent().getStringExtra(ActivityUtility.TASK_NAME));

        BaseAdapter adapter;
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
                Checkpoint checkpoint;
                if (task.getCheckpoints().size()== 0) checkpoint = new Checkpoint(1, "");
                else checkpoint = new Checkpoint(task.getCheckpoints().get(task.getCheckpoints().size() - 1).id + 1, "");
                task.addCheckpoint(checkpoint);
                task.save();

                Intent check_intent = new Intent(this, CheckpointActivity.class);
                check_intent.putExtra(ActivityUtility.CHECKPOINT, checkpoint.toString());

                startActivityForResult(check_intent, ActivityUtility.REQUEST_CHECKPOINT);
            }
        } else if (view.equals(date)) {
            if (task.getDateDue() == null) {
                Calendar c = Calendar.getInstance();

                new DatePickerDialog(
                        this,
                        this,
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH))
                        .show();
            } else {
                new DatePickerDialog(
                        this,
                        this,
                        task.getDateDue().get(Calendar.YEAR),
                        task.getDateDue().get(Calendar.MONTH),
                        task.getDateDue().get(Calendar.DAY_OF_MONTH))
                        .show();
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
        } else if (view.equals(date)) {
            task.setDateDue(null);
            task.save();
            date.setText(task.getDateDueString());
        }else if (view.equals(status)) {
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
            if (task.getStatus() == Task.STATUS_DONE) status.setText(R.string.complete);
            else status.setText(R.string.incomplete);
        }
    }

    public void editCheckpoint(Checkpoint checkpoint) {
        task.editCheckpoint(checkpoint);
        task.save();
        onResume();
    }

    public void deleteCheckpoint(Checkpoint checkpoint) {
        task.removeCheckpoint(checkpoint);
        task.save();
    }

    private void setTags() {
        tags.setText(task.getTagString());
    }

    @Override
    public void onTaskChanged() {
        onResume();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        task.setDateDue(new GregorianCalendar(year, month, day));
        task.save();
        date.setText(task.getDateDueString());
    }
}
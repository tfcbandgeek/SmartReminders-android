package jgappsandgames.smartreminderslite.tasks;

// Java
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

// JSON
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// Android OS
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;

// Views
import android.view.View;
import android.widget.DatePicker;
import android.widget.SeekBar;

// App
import jgappsandgames.smartreminderslite.R;

import jgappsandgames.smartreminderslite.holder.TaskFolderHolder.OnTaskChangedListener;
import jgappsandgames.smartreminderslite.tasks.checkpoint.CheckpointActivity;
import jgappsandgames.smartreminderslite.tasks.tags.TagEditorActivity;
import jgappsandgames.smartreminderslite.utility.ActivityUtility;

// Save
import jgappsandgames.smartreminderssave.MasterManager;
import jgappsandgames.smartreminderssave.tags.TagManager;
import jgappsandgames.smartreminderssave.tasks.Checkpoint;
import jgappsandgames.smartreminderssave.tasks.Task;
import jgappsandgames.smartreminderssave.tasks.TaskManager;

/**
 * TaskActivity
 * Created by joshua on 8/31/17.
 * TODO: Clean Activity and Clearify What is Going On Here
 */
public class TaskActivity extends TaskActivityInterface implements OnTaskChangedListener {
    // Data
    private Task task;

    // Life Cycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load Data
        task = new Task(getIntent().getStringExtra(ActivityUtility.TASK_NAME));

        // Task Specific Items
        if (task.getType() == Task.TYPE_TASK) {
            // Set Views
            setStatus();
            status.setOnClickListener(this);

            setDate();
            date.setOnClickListener(this);
            date.setOnLongClickListener(this);

            priority.setMax(100);
            priority.setProgress(task.getPriority());
            priority.setOnSeekBarChangeListener(this);
        }

        setTitle(task.getTitle());

        // Set Text
        title.setText(task.getTitle());
        note.setText(task.getNote());

        // Set TextWatcher
        title.addTextChangedListener(this);
        note.addTextChangedListener(this);

        // Set Click Listeners
        fab.setOnClickListener(this);
        fab.setOnLongClickListener(this);

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
                else checkpoint = new Checkpoint(task.getCheckpoints().get(task.getCheckpoints().size() - 1).getId() + 1, "");
                task.addCheckpoint(checkpoint);
                task.save();

                Intent check_intent = new Intent(this, CheckpointActivity.class);
                check_intent.putExtra(ActivityUtility.CHECKPOINT, checkpoint.toString());

                startActivityForResult(check_intent, ActivityUtility.REQUEST_CHECKPOINT);
            }
        } else if (view.equals(date)) {
            if (task.getDateDue() == null) new DatePickerDialog(this, this, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show();
            else new DatePickerDialog(this, this, task.getDateDue().get(Calendar.YEAR), task.getDateDue().get(Calendar.MONTH), task.getDateDue().get(Calendar.DAY_OF_MONTH)).show();
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
            setDate();
            return true;
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

    private void setDate() {
        date.setText(task.getDateDueString());
    }

    @Override
    public void onTaskChanged() {
        onResume();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        task.setDateDue(new GregorianCalendar(year, month, day, 0, 0, 1));
        setDate();
    }

    @Override
    protected void save() {
        MasterManager.save();
        task.save();
    }
}
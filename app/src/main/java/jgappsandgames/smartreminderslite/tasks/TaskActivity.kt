package jgappsandgames.smartreminderslite.tasks

// Java
import java.util.*

// Android OS
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable

// Views
import android.view.View
import android.widget.DatePicker
import android.widget.SeekBar

// JSON
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.home.FirstRun
import jgappsandgames.smartreminderslite.tasks.checkpoint.CheckpointActivity
import jgappsandgames.smartreminderslite.tasks.checkpoint.CheckpointAdapter
import jgappsandgames.smartreminderslite.tasks.tags.TagEditorActivity
import jgappsandgames.smartreminderslite.utility.ActivityUtility

// Save
import jgappsandgames.smartreminderssave.MasterManager
import jgappsandgames.smartreminderssave.tags.TagManager
import jgappsandgames.smartreminderssave.tasks.Checkpoint
import jgappsandgames.smartreminderssave.tasks.Task
import jgappsandgames.smartreminderssave.tasks.TaskManager
import jgappsandgames.smartreminderssave.utility.FileUtility




/**
 * TaskActivity
 * Created by joshua on 12/16/2017.
 *
 * Task View[TaskActivityInterface, TaskActivity]
 * The Task View is Likely the Most Important View in the Entire Application.  It Displays and Allows
 *     for the Editing of Both Tasks and Folders.  Because of this it should be checked for stability
 *     with every build.
 *
 *     TaskActivityInterface: Manages the Orientation, Building Views and the Menu
 *     TaskActivity: Handles the Data
 */
class TaskActivity: TaskActivityInterface() {
    // Data ----------------------------------------------------------------------------------------
    var task: Task? = null

    // LifeCycle Methods ---------------------------------------------------------------------------
    /**
     * OnCreate
     *
     * Called to Create the Activity
     * Called By the Application
     */
    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Load Filepaths
        FileUtility.loadFilePaths(this)

        // First Run
        if (FileUtility.isFirstRun()) startActivity(Intent(this, FirstRun::class.java))

        // Normal Run
        else MasterManager.load()
    }

    /**
     * OnResume
     *
     * Called To Set the Data for All Of The Views
     * Called By The Application
     */
    override fun onResume() {
        super.onResume()
        // Load Data
        task = Task(intent.getStringExtra(ActivityUtility.TASK_NAME))

        // Set Generic Text
        setTitle(task!!.getTitle())
        title!!.setText(task!!.getTitle())
        note!!.setText(task!!.getNote())
        drawTags()

        // Set Task Text and Adapter
        if (task!!.getType() == Task.TYPE_TASK) {
            drawStatus()
            drawDate()
            priority!!.max = 100
            priority!!.progress = task!!.getPriority()

            adapter = CheckpointAdapter(this, task!!.getFilename(), task!!.getCheckpoints())
            list!!.adapter = adapter
        }

        // Set Folder Adapter
        if (task!!.getType() == Task.TYPE_FLDR) {
            adapter = ChildrenAdapter(this, task!!.getChildren())
            list!!.adapter = adapter
        }
    }

    /**
     * OnPause
     *
     * Called to Handle The View Moving Out Of Focus
     * Called By The Application
     */
    override fun onPause() {
        super.onPause()
       save()
    }

    // Activity Result -----------------------------------------------------------------------------
    /**
     * OnActivityResult
     *
     * Called To Handle Activity Results From Activity Started From Here
     * Called By The Application
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
        // Checkpoint Result
            ActivityUtility.REQUEST_CHECKPOINT -> if (resultCode == ActivityUtility.RESPONSE_CHANGE) {
                try {
                    editCheckpoint(Checkpoint(JSONObject(data!!.getStringExtra(ActivityUtility.CHECKPOINT))))
                } catch (e: JSONException) {
                    e.printStackTrace()
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }

            }

            ActivityUtility.REQUEST_TAGS -> if (resultCode == ActivityUtility.RESPONSE_CHANGE) {
                try {
                    val temp = JSONArray(data!!.getStringExtra(ActivityUtility.TAG_LIST))
                    val t = ArrayList<String>()

                    for (i in 0 until temp.length()) {
                        t.add(temp.optString(i))
                    }

                    task!!.setTags(t)
                } catch (e: JSONException) {
                    e.printStackTrace()
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }

                TagManager.save()
                task!!.save()

                drawTags()
            }
        }
    }

    // Click Listeners -----------------------------------------------------------------------------
    /**
     * OnClick
     *
     * Listener For On Click Events In This Activity
     * Called By The Application
     */
    override fun onClick(button: View?) {
        // Fab Click
        if (button == fab) {
            // Called in A Folder
            if (task!!.getType() == Task.TYPE_FLDR) {
                // Create Task
                val t = Task(task!!.getFilename(), Task.TYPE_TASK)
                t.save()

                task!!.addChild(t.getFilename())
                task!!.save()

                TaskManager.tasks.add(t.getFilename())
                TaskManager.save()

                // Create Intent
                val task_intent = Intent(this, TaskActivity::class.java)
                task_intent.putExtra(ActivityUtility.TASK_NAME, t.getFilename())

                startActivity(task_intent)
            } else {
                val checkpoint: Checkpoint

                if (task!!.getCheckpoints().size == 0) checkpoint = Checkpoint(1, "")
                else checkpoint = Checkpoint(task!!.getCheckpoints()[task!!.getCheckpoints().size - 1].id + 1, "")

                task!!.addCheckpoint(checkpoint)
                task!!.save()

                val check_intent = Intent(this, CheckpointActivity::class.java)
                check_intent.putExtra(ActivityUtility.CHECKPOINT, checkpoint.toString())

                startActivityForResult(check_intent, ActivityUtility.REQUEST_CHECKPOINT)
            }// Called in A Task
        }

        // Date Click
        else if (button == date) {
            if (task!!.getDateDue() == null) DatePickerDialog(this, this, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show()
            else DatePickerDialog(this, this, task!!.getDateDue()!!.get(Calendar.YEAR), task!!.getDateDue()!!.get(Calendar.MONTH), task!!.getDateDue()!!.get(Calendar.DAY_OF_MONTH)).show()
        }

        // Status Click
        else if (button == status) {
            if (task!!.getStatus() == Task.STATUS_DONE) task!!.markComplete(false)
            else task!!.markComplete(true)

            task!!.save()
            drawStatus()
        }

        // Tags
        else if (button == tags) {
            val tag_intent = Intent(this, TagEditorActivity::class.java)
            tag_intent.putExtra(ActivityUtility.TASK_NAME, task!!.getFilename())
            startActivityForResult(tag_intent, ActivityUtility.REQUEST_TAGS)
        }
    }

    /**
     * OnLongClick
     *
     * Listener For The Long Click Events In This Activity
     * Called By The Application
     */
    override fun onLongClick(button: View?): Boolean {
        // FAB Click
        if (button == fab) {
            if (task!!.getType() == Task.TYPE_FLDR) {
                // Create Task
                val t = Task(task!!.getFilename(), Task.TYPE_FLDR)
                t.save()

                task!!.addChild(t.getFilename())
                task!!.save()

                TaskManager.tasks.add(t.getFilename())
                TaskManager.save()

                // Create Intent
                val intent = Intent(this, TaskActivity::class.java)
                intent.putExtra(ActivityUtility.TASK_NAME, t.getFilename())

                // Start Activity
                startActivity(intent)
                return true
            }
        }

        // Date Click
        else if (button == date) {
            task!!.setDateDue(null)
            drawDate()
            return true
        }

        return false
    }

    // Text Watcher --------------------------------------------------------------------------------
    /**
     * BeforeTextedChanged
     *
     * Ignore
     */
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    /**
     * OnTextedChanged
     *
     * Ignore
     */
    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    /**
     * AfterTextChanged
     *
     * Called To Handle Text Views In This Activity Changing
     * Called By The Application
     */
    override fun afterTextChanged(editable: Editable?) {
        task!!.setTitle(title!!.text.toString())
        task!!.setNote(note!!.text.toString())
    }

    // Seekbar Listener ----------------------------------------------------------------------------
    /**
     * OnStartTrackingTouch
     *
     * Ignore
     */
    override fun onStartTrackingTouch(p0: SeekBar?) {}

    /**
     * OnStopTrackingTouch
     *
     * Ignore
     */
    override fun onStopTrackingTouch(p0: SeekBar?) {}

    /**
     * OnProgressChanged
     *
     * Called To Handle The Changing of the Priority
     * Called By The Application
     */
    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
        task!!.setPriority(priority!!.progress)
    }

    // Date Listener -------------------------------------------------------------------------------
    /**
     * OnDateSet
     *
     * Called To Handle The Due Date Being Set
     * Called By The Application
     */
    override fun onDateSet(date_picker: DatePicker?, year: Int, month: Int, day: Int) {
        task!!.setDateDue(GregorianCalendar(year, month, day, 0, 0, 1))
        drawDate()
    }

    // Task Listener -------------------------------------------------------------------------------
    /**
     * OnTaskChanged
     *
     * Called To Handle When A Task In This  List Changes
     */
    override fun onTaskChanged() {
        onResume()
    }

    // Parent Methods ------------------------------------------------------------------------------
    /**
     * Save
     *
     * Called To Save Any Information That May of Changed
     */
    override fun save() {
        MasterManager.save()
        task!!.save()
    }

    // Class Methods -------------------------------------------------------------------------------
    /**
     * DrawStatus
     *
     * Called To Set The Status Text
     */
    fun drawStatus() {
        if (task!!.getType() == Task.TYPE_TASK) {
            if (task!!.getStatus() == Task.STATUS_DONE) status!!.setText(R.string.complete)
            else status!!.setText(R.string.incomplete)
        }
    }

    /**
     * DrawTags
     *
     * Called To Set The Tags Text
     */
    fun drawTags() {
        tags!!.text = task!!.getTagString()
    }

    /**
     * DrawDate
     *
     * Called To Set The Date Text
     */
    fun drawDate() {
        date!!.text = task!!.getDateDueString()
    }

    /**
     * EditCheckpoint
     *
     * Called To Handle The Checkpoint Changing
     */
    fun editCheckpoint(checkpoint: Checkpoint) {
        task!!.editCheckpoint(checkpoint)
        task!!.save()
        onResume()
    }

    /**
     * DeleteCheckpoint
     *
     * Called To Handle The Deletion of A Checkpoint
     */
    fun deleteCheckpoint(checkpoint: Checkpoint) {
        task!!.removeCheckpoint(checkpoint)
        task!!.save()
    }
}
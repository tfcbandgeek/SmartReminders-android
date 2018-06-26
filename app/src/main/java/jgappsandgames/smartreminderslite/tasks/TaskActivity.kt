package jgappsandgames.smartreminderslite.tasks

// Java
import java.util.Calendar
import java.util.GregorianCalendar

// Android OS
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher


// Views
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ListView
import android.widget.SeekBar
import android.widget.TextView
import com.github.clans.fab.FloatingActionButton

// JSON
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.adapter.TaskAdapterInterface
import jgappsandgames.smartreminderslite.holder.CheckpointHolder
import jgappsandgames.smartreminderslite.holder.TaskFolderHolder
import jgappsandgames.smartreminderslite.home.FirstRun
import jgappsandgames.smartreminderslite.tasks.checkpoint.CheckpointActivity
import jgappsandgames.smartreminderslite.tasks.tags.TagEditorActivity
import jgappsandgames.smartreminderslite.utility.ActivityUtility
import jgappsandgames.smartreminderslite.utility.OptionsUtility

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
 */
class TaskActivity: Activity(), View.OnClickListener, View.OnLongClickListener, TextWatcher,
        SeekBar.OnSeekBarChangeListener, DatePickerDialog.OnDateSetListener, TaskFolderHolder.OnTaskChangedListener {
    // View Orientation ----------------------------------------------------------------------------
    companion object {
        private const val TASK_PORTRAIT = 1
        private const val TASK_LANDSCAPE = 2
        private const val TASK_MULTI = 3
        private const val FOLDER_PORTRAIT = 11
        private const val FOLDER_LANDSCAPE = 12
        private const val FOLDER_MULTI = 13
    }

    private var view = 0

    // Views ---------------------------------------------------------------------------------------
    private lateinit var title: EditText
    private lateinit var note: EditText
    private lateinit var tags: TextView
    private var date: Button? = null
    private var status: Button? = null
    private var priority: SeekBar? = null
    private lateinit var list: ListView
    private var fab: FloatingActionButton? = null
    private var faf: FloatingActionButton? = null
    private var fat: FloatingActionButton? = null

    // Data ----------------------------------------------------------------------------------------
    lateinit var task: Task
    var load: Boolean = false

    // LifeCycle Methods ---------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Handle Data
        FileUtility.loadFilePaths(this)
        if (FileUtility.isFirstRun()) startActivity(Intent(this, FirstRun::class.java))
        else MasterManager.load()

        // Find Type
        var type = intent.getIntExtra(ActivityUtility.TASK_TYPE, - 1)
        if (type == -1) type = Task(intent.getStringExtra(ActivityUtility.TASK_NAME), true).getType()

        // Set View Type
        view = if (type == Task.TYPE_TASK) TASK_PORTRAIT
        else FOLDER_PORTRAIT

        // Set Content View
        when (view) {
            TASK_PORTRAIT -> setContentView(R.layout.activity_task)
            FOLDER_PORTRAIT -> setContentView(R.layout.activity_folder)
            else -> throw RuntimeException("Invalid View Type")
        }

        // Find Generic Views
        title = findViewById(R.id.title)
        note = findViewById(R.id.note)
        tags = findViewById(R.id.tags)
        list = findViewById(R.id.tasks)

        // Set TextWatcher
        title.addTextChangedListener(this)
        note.addTextChangedListener(this)

        // Set Click Listener
        tags.setOnClickListener(this)
        tags.setOnLongClickListener(this)

        // Task Specific Views
        if (type == Task.TYPE_TASK) {
            date = findViewById(R.id.date)
            date!!.setOnClickListener(this)
            date!!.setOnLongClickListener(this)

            status = findViewById(R.id.status)
            status!!.setOnClickListener(this)

            priority = findViewById(R.id.priority)
            priority!!.setOnSeekBarChangeListener(this)

            fab = findViewById(R.id.task_add_checkpoint)
            fab!!.setOnClickListener {
                val checkpoint: Checkpoint = if (task.getCheckpoints().size == 0) Checkpoint(1, "")
                else Checkpoint(task.getCheckpoints()[task.getCheckpoints().size - 1].id + 1, "")

                task.addCheckpoint(checkpoint)
                task.save()
                startActivityForResult(
                        Intent(this, CheckpointActivity::class.java).putExtra(ActivityUtility.CHECKPOINT, checkpoint.toString()),
                        ActivityUtility.REQUEST_CHECKPOINT)
            }
        }

        // Folder Specific Views -------------------------------------------------------------------
        else if (type == Task.TYPE_FLDR) {
            faf = findViewById(R.id.folder_add_folder)
            faf!!.setOnClickListener {
                // Create Task
                val t = Task(task.getFilename(), Task.TYPE_FLDR)
                t.save()

                task.addChild(t.getFilename())
                task.save()

                TaskManager.tasks.add(t.getFilename())
                TaskManager.save()

                // Create Intent
                val intent = Intent(this, TaskActivity::class.java)
                intent.putExtra(ActivityUtility.TASK_NAME, t.getFilename())

                // Start Activity
                startActivity(intent)
            }

            fat = findViewById(R.id.folder_add_task)
            fat!!.setOnClickListener {
                // Create Task
                val t = Task(task.getFilename(), Task.TYPE_TASK)
                t.save()

                task.addChild(t.getFilename())
                task.save()

                TaskManager.tasks.add(t.getFilename())
                TaskManager.save()

                startActivity(Intent(this, TaskActivity::class.java).putExtra(ActivityUtility.TASK_NAME, t.getFilename()))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Load Data
        load = true
        task = Task(intent.getStringExtra(ActivityUtility.TASK_NAME))

        // Set Generic Text
        setTitle(task.getTitle())
        title.setText(task.getTitle())
        note.setText(task.getNote())
        drawTags()

        // Set Task Text and Adapter
        if (task.getType() == Task.TYPE_TASK) {
            drawStatus()
            drawDate()
            priority!!.max = 100
            priority!!.progress = task.getPriority()

            list.adapter = CheckpointAdapter(this, task.getFilename(), task.getCheckpoints())
        }

        // Set Folder Adapter
        if (task.getType() == Task.TYPE_FLDR) {
            list.adapter = ChildrenAdapter(this, task.getChildren())
        }

        load = false
    }

    override fun onPause() {
        super.onPause()
       save()
    }

    // Activity Result -----------------------------------------------------------------------------
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

                    task.setTags(t)
                } catch (e: JSONException) {
                    e.printStackTrace()
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }

                TagManager.save()
                task.save()

                drawTags()
            }
        }
    }

    // Menu Methods --------------------------------------------------------------------------------
    /**
     * OnCreateOptionsMenu
     *
     * Called To Create The Options Menu
     * Called By The Application
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_task, menu)
        return true
    }

    /**
     * OnOptionsItemSelected
     *
     * Called When an Options Item Is Press
     * Called By The Application
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return OptionsUtility.onOptionsItemSelected(this, item!!, object: OptionsUtility.Save {
            override fun save() {
                this@TaskActivity.save()
            }
        })
    }

    // Click Listeners -----------------------------------------------------------------------------
    override fun onClick(button: View?) {
        // Date Click
        if (button == date) {
            if (task.getDateDue() == null) DatePickerDialog(this, this, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show()
            else DatePickerDialog(this, this, task.getDateDue()!!.get(Calendar.YEAR), task.getDateDue()!!.get(Calendar.MONTH), task.getDateDue()!!.get(Calendar.DAY_OF_MONTH)).show()
        }

        // Status Click
        else if (button == status) {
            if (task.getStatus() == Task.STATUS_DONE) task.markComplete(false)
            else task.markComplete(true)

            task.save()
            drawStatus()
        }

        // Tags
        else if (button == tags) {
            startActivityForResult(
                    Intent(this, TagEditorActivity::class.java).putExtra(ActivityUtility.TASK_NAME, task.getFilename()),
                    ActivityUtility.REQUEST_TAGS)
        }
    }

    override fun onLongClick(button: View?): Boolean {
        // Date Click
        if (button == date) {
            task.setDateDue(null)
            drawDate()
            return true
        }

        return false
    }

    // Text Watcher --------------------------------------------------------------------------------
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    override fun afterTextChanged(editable: Editable?) {
        if (!load) {
            task.setTitle(title.text.toString())
            task.setNote(note.text.toString())
        }
    }

    // Seekbar Listener ----------------------------------------------------------------------------
    override fun onStartTrackingTouch(p0: SeekBar?) {}
    override fun onStopTrackingTouch(p0: SeekBar?) {}
    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
        task.setPriority(priority!!.progress)
    }

    // Date Listener -------------------------------------------------------------------------------
    override fun onDateSet(date_picker: DatePicker?, year: Int, month: Int, day: Int) {
        task.setDateDue(GregorianCalendar(year, month, day, 0, 0, 1))
        drawDate()
    }

    // Task Listener -------------------------------------------------------------------------------
    override fun onTaskChanged() {
        onResume()
    }

    // Parent Methods ------------------------------------------------------------------------------
    fun save() {
        MasterManager.save()
        task.save()
    }

    // Class Methods -------------------------------------------------------------------------------
    private fun drawStatus() {
        if (task.getType() == Task.TYPE_TASK) {
            if (task.getStatus() == Task.STATUS_DONE) status!!.setText(R.string.complete)
            else status!!.setText(R.string.incomplete)
        }
    }

    private fun drawTags() {
        tags.text = task.getTagString()
    }

    private fun drawDate() {
        date!!.text = task.getDateDueString()
    }

    fun editCheckpoint(checkpoint: Checkpoint) {
        task.editCheckpoint(checkpoint)
        task.save()
        onResume()
    }

    fun deleteCheckpoint(checkpoint: Checkpoint) {
        task.removeCheckpoint(checkpoint)
        task.save()
    }


    // Internal Classes ----------------------------------------------------------------------------
    class ChildrenAdapter(activity: TaskActivity, tasks: ArrayList<String>):
            TaskAdapterInterface(activity, activity, tasks, null)

    class CheckpointAdapter(private val activity: TaskActivity, private val task: String, private val checkpoints: List<Checkpoint>):
            BaseAdapter() {
        // List Methods --------------------------------------------------------------------------------
        override fun getCount(): Int {
            return checkpoints.size
        }

        override fun getViewTypeCount(): Int {
            return 1
        }

        override fun hasStableIds(): Boolean {
            return false
        }

        // Item Methods --------------------------------------------------------------------------------
        override fun getItem(position: Int): Checkpoint {
            return checkpoints[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convert_view: View?, parent: ViewGroup): View {
            var view = convert_view
            if (view == null) {
                view = LayoutInflater.from(activity).inflate(R.layout.list_checkpoint, parent, false)

                val holder = CheckpointHolder(activity, task, getItem(position), view!!)
                view.tag = holder
            } else {
                val holder = CheckpointHolder(activity, task, getItem(position), view)
                view.tag = holder
            }

            return view
        }
    }
}
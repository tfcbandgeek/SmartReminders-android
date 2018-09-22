package jgappsandgames.smartreminderslite.tasks

// Java
import java.util.Calendar
import java.util.GregorianCalendar

// Android OS
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
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
import com.github.clans.fab.FloatingActionMenu

// JSON
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.adapter.TaskAdapter
import jgappsandgames.smartreminderslite.home.Settings2Activity
import jgappsandgames.smartreminderslite.utility.*

// KotlinX
import kotlinx.android.synthetic.main.activity_folder.folder_bottom_bar_search
import kotlinx.android.synthetic.main.activity_folder.folder_bottom_bar_search_text
import kotlinx.android.synthetic.main.activity_task.task_bottom_bar_search
import kotlinx.android.synthetic.main.activity_task.task_bottom_bar_search_text

// Save
import jgappsandgames.smartreminderssave.MasterManager
import jgappsandgames.smartreminderssave.tags.TagManager
import jgappsandgames.smartreminderssave.tasks.Checkpoint
import jgappsandgames.smartreminderssave.tasks.Task
import jgappsandgames.smartreminderssave.tasks.TaskManager
import jgappsandgames.smartreminderssave.utility.FileUtility
import org.jetbrains.anko.toast

/**
 * TaskActivity
 * Created by joshua on 12/16/2017.
 */
class TaskActivity: Activity(), View.OnClickListener, View.OnLongClickListener, TextWatcher,
        SeekBar.OnSeekBarChangeListener, DatePickerDialog.OnDateSetListener, TaskAdapter.OnTaskChangedListener {
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
    private var fam: FloatingActionMenu? = null
    private var faf: FloatingActionButton? = null
    private var fat: FloatingActionButton? = null

    // Data ----------------------------------------------------------------------------------------
    lateinit var task: Task
    var load = false

    // LifeCycle Methods ---------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Handle Data
        FileUtility.loadFilePaths(this)
        if (FileUtility.isFirstRun()) startActivity(Intent(this, Settings2Activity::class.java).putExtra(FIRST_RUN, true))
        else MasterManager.load()

        // Find Type
        var type = intent.getIntExtra(TASK_TYPE, - 1)
        if (type == -1) type = Task(intent.getStringExtra(TASK_NAME), true).getType()

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
                        Intent(this, CheckpointActivity::class.java).putExtra(CHECKPOINT, checkpoint.toString()), REQUEST_CHECKPOINT)
            }

            task_bottom_bar_search.setOnClickListener {
                if (task_bottom_bar_search_text.visibility == View.VISIBLE) searchTaskVisibility(false)
                else searchTaskVisibility()
            }

            task_bottom_bar_search_text.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    if (task_bottom_bar_search_text.visibility == View.VISIBLE) list.adapter = CheckpointSearchAdapter(this@TaskActivity, task.getFilename(), task_bottom_bar_search_text.text.toString(), task.getCheckpoints())
                }
            })
        }

        // Folder Specific Views -------------------------------------------------------------------
        else if (type == Task.TYPE_FOLDER) {
            fam = findViewById(R.id.folder_fab)

            faf = findViewById(R.id.folder_add_folder)
            faf!!.setOnClickListener {
                fam?.close(true)

                // Create Task
                startActivity(buildTaskIntent(this, IntentOptions(),
                        TaskOptions(task = TaskManager.addTask(Task(task.getFilename(), Task.TYPE_FOLDER).save(), false))))
            }

            fat = findViewById(R.id.folder_add_task)
            fat!!.setOnClickListener {
                fam?.close(true)

                // Create Task
                startActivity(buildTaskIntent(this, IntentOptions(),
                        TaskOptions(task = TaskManager.addTask(Task(task.getFilename(), Task.TYPE_TASK).save(), false))))
            }

            folder_bottom_bar_search.setOnClickListener {
                if (folder_bottom_bar_search_text.visibility == View.VISIBLE) searchFolderVisibility(false)
                else searchFolderVisibility()
            }

            folder_bottom_bar_search_text.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    if (folder_bottom_bar_search_text.visibility == View.VISIBLE) list.adapter = TaskAdapter(this@TaskActivity, this@TaskActivity, task.getChildren(), folder_bottom_bar_search_text.text.toString())
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        // Load Data
        load = true
        task = Task(intent.getStringExtra(TASK_NAME))

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

            task.sortTags()
            list.adapter = CheckpointAdapter(this, task.getFilename(), task.getCheckpoints())
        }

        // Set Folder Adapter
        if (task.getType() == Task.TYPE_FOLDER) {
            list.adapter = TaskAdapter(this, this, task.getChildren(), "")
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
            REQUEST_CHECKPOINT -> if (resultCode == RESPONSE_CHANGE) {
                try {
                    editCheckpoint(Checkpoint(JSONObject(data!!.getStringExtra(CHECKPOINT))))
                } catch (e: JSONException) {
                    e.printStackTrace()
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }

            }

            REQUEST_TAGS -> if (resultCode == RESPONSE_CHANGE) {
                try {
                    val temp = JSONArray(data!!.getStringExtra(TAG_LIST))
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
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_task, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.more) {
            toast("Coming Very Soon")
            //startActivity(Intent(this, TaskManagementActivity::class.java).putExtra(TASK_NAME, task.getFilename()))
            return true
        }

        return onOptionsItemSelected(this, item, object: Save {
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
                    Intent(this, TagEditorActivity::class.java).putExtra(TASK_NAME, task.getFilename()), REQUEST_TAGS)
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
            setTitle(task.getTitle())
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

    // Search Methods ------------------------------------------------------------------------------
    private fun searchTaskVisibility(visible: Boolean = true) {
        if (visible) {
            task_bottom_bar_search_text.visibility = View.VISIBLE
            task_bottom_bar_search_text.setText("")
            list.adapter = CheckpointSearchAdapter(this, task.getFilename(), "", task.getCheckpoints())
        } else {
            task_bottom_bar_search_text.visibility = View.INVISIBLE
            task_bottom_bar_search_text.setText("")
            list.adapter = CheckpointAdapter(this, task.getFilename(), task.getCheckpoints())
        }
    }

    private fun searchFolderVisibility(visible: Boolean = true) {
        if (visible) {
            folder_bottom_bar_search_text.visibility = View.VISIBLE
            folder_bottom_bar_search_text.setText("")
            list.adapter = TaskAdapter(this, this, task.getChildren(), "")
        } else {
            folder_bottom_bar_search_text.visibility = View.INVISIBLE
            folder_bottom_bar_search_text.setText("")
            list.adapter = TaskAdapter(this, this, task.getChildren(), "")
        }
    }

    // Internal Classes ----------------------------------------------------------------------------
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

    class CheckpointSearchAdapter(private val activity: TaskActivity, private val task: String, search: String, checkpoints: ArrayList<Checkpoint>):
            BaseAdapter() {
        private val temp = ArrayList<Checkpoint>()
        // Constructor -----------------------------------------------------------------------------
        init {
            for (i in 0 until checkpoints.size) {
                if (checkpoints[i].text.toLowerCase().contains(search.toLowerCase())) temp.add(checkpoints[i])
            }
        }

        // List Methods ----------------------------------------------------------------------------
        override fun getCount(): Int {
            return temp.size
        }

        override fun getViewTypeCount(): Int {
            return 1
        }

        override fun hasStableIds(): Boolean {
            return false
        }

        // Item Methods ----------------------------------------------------------------------------
        override fun getItem(position: Int): Checkpoint {
            return temp[position]
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

// Checkpoint Holder Class ---------------------------------------------------------------------------------------------------------------------------
class CheckpointHolder(private val activity: TaskActivity, private val task: String, private val checkpoint: Checkpoint, view: View):
        View.OnClickListener, View.OnLongClickListener {
    // Views ---------------------------------------------------------------------------------------
    private val text: TextView = view.findViewById(R.id.text)
    private val edit: Button = view.findViewById(R.id.edit)

    // Constructor ---------------------------------------------------------------------------------
    init {
        // Set Click Listeners
        text.setOnClickListener(this)
        text.setOnLongClickListener(this)
        edit.setOnClickListener(this)
        edit.setOnLongClickListener(this)

        // Set Views
        setViews()
    }

    // View Handler --------------------------------------------------------------------------------
    private fun setViews() {
        if (checkpoint.status) text.paintFlags = text.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        else text.paintFlags = text.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        text.text = checkpoint.text
    }

    // Click Handlers ------------------------------------------------------------------------------
    override fun onClick(view: View) {
        if (view == edit) {
            val intent = Intent(activity, CheckpointActivity::class.java)
            intent.putExtra(CHECKPOINT, checkpoint.toString())
            intent.putExtra(TASK_NAME, task)
            activity.startActivityForResult(intent, REQUEST_CHECKPOINT)
        } else if (view == text) {
            checkpoint.status = !checkpoint.status
            setViews()
            activity.editCheckpoint(checkpoint)
        }
    }

    override fun onLongClick(view: View): Boolean {
        activity.deleteCheckpoint(checkpoint)

        val v = activity.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
        try {
            if (v != null && v.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    v.vibrate(100)
                }
            }
        } catch (n: NullPointerException) {
            n.printStackTrace()
        }

        return true
    }
}
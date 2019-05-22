package jgappsandgames.smartreminderslite.tasks

// Java
import java.util.Calendar
import java.util.GregorianCalendar

// Android OS
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Paint
import android.graphics.Point
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
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

// JSON
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.adapter.TaskAdapter
import jgappsandgames.smartreminderslite.utility.*

// Anko
import org.jetbrains.anko.toast

// KotlinX
import kotlinx.android.synthetic.main.activity_folder.*
import kotlinx.android.synthetic.main.activity_folder_landscape.*
import kotlinx.android.synthetic.main.activity_task.*
import kotlinx.android.synthetic.main.activity_task_landscape.*

// Save
import jgappsandgames.smartreminderssave.MasterManager
import jgappsandgames.smartreminderssave.tags.TagManager
import jgappsandgames.smartreminderssave.tasks.*
import kotlinx.android.synthetic.main.activity_note.*

/**
 * TaskActivity
 * Created by joshua on 12/16/2017.
 */
class TaskActivity: AppCompatActivity(), View.OnClickListener, View.OnLongClickListener, TextWatcher,
        SeekBar.OnSeekBarChangeListener, DatePickerDialog.OnDateSetListener, TaskAdapter.OnTaskChangedListener {
    // View Orientation ----------------------------------------------------------------------------
    companion object {
        private const val PORTRAIT = 10
        private const val LANDSCAPE = 20
        private const val MULTIWINDOW = 30

        private const val TASK_PORTRAIT = Task.TYPE_TASK + PORTRAIT
        private const val TASK_LANDSCAPE = Task.TYPE_TASK + LANDSCAPE
        private const val FOLDER_PORTRAIT = Task.TYPE_FOLDER + PORTRAIT
        private const val FOLDER_LANDSCAPE = Task.TYPE_FOLDER + LANDSCAPE
        private const val NOTE_PORTRAIT = Task.TYPE_NOTE + PORTRAIT
        private const val NOTE_LANDSCAPE = Task.TYPE_NOTE + LANDSCAPE
    }

    private var view = 0

    private fun getOrientation(): Int {
        // if (hasNougat()) if (isInMultiWindowMode) return MULTIWINDOW
        var temp = Point()
        windowManager.defaultDisplay.getSize(temp)

        if (temp.x > temp.y) return LANDSCAPE
        return PORTRAIT
    }

    // Data ----------------------------------------------------------------------------------------
    lateinit var task: Task
    private var load = false

    // Core Methods --------------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Handle Data
        loadClass(this)
        var type = intent.getIntExtra(TASK_TYPE, - 1)
        val t = TaskManager.taskPool.getPoolObject().load(intent.getStringExtra(TASK_NAME))
        if (type == -1) type = t.getType()
        TaskManager.taskPool.returnPoolObject(t)
        view = type + getOrientation()

        when (view) {
            TASK_PORTRAIT -> onCreateTaskPortrait()
            TASK_LANDSCAPE -> onCreateTaskLandscape()
            FOLDER_PORTRAIT -> onCreateFolderPortrait()
            FOLDER_LANDSCAPE -> onCreateFolderLandscape()
            NOTE_PORTRAIT -> onCreateNote()
            NOTE_LANDSCAPE -> onCreateNote()
            else -> IllegalOrientationException("Orientation $view does not exist")
        }
    }

    override fun onResume() {
        super.onResume()
        // Load Data
        load = true
        task = TaskManager.taskPool.getPoolObject().load(intent.getStringExtra(TASK_NAME))
        title = if (task.getTitle().isEmpty()) {
            if (intent.getBooleanExtra(CREATE, false)) "New Task/Folder"
            else "Unnamed Task/Folder"
        } else {
            task.getTitle()
        }

        when (view) {
            TASK_PORTRAIT -> onResumeTaskPortrait()
            TASK_LANDSCAPE -> onResumeTaskLandscape()
            FOLDER_PORTRAIT -> onResumeFolderPortrait()
            FOLDER_LANDSCAPE -> onResumeFolderLandscape()
            NOTE_PORTRAIT -> onResumeNote()
            NOTE_LANDSCAPE -> onResumeNote()
        }

        load = false
    }

    override fun onDestroy() {
        super.onDestroy()
        TaskManager.taskPool.returnPoolObject(task)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CHECKPOINT -> if (resultCode == RESPONSE_CHANGE) {
                try {
                    editCheckpoint(Checkpoint(JSONObject(data!!.getStringExtra(CHECKPOINT))))
                } catch (e: JSONException) {
                    e.printStackTrace()
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }
            } else if (resultCode == RESPONSE_NONE) {
                for (checkpoint in task.getCheckpoints()) {
                    if (checkpoint.text.trim() == "") {
                        task.removeCheckpoint(checkpoint)
                        onActivityResult(requestCode, resultCode, data)
                    }
                }
            }

            REQUEST_TAGS -> if (resultCode == RESPONSE_CHANGE) {
                try {
                    val temp = JSONArray(data!!.getStringExtra(TAG_LIST))
                    val t = ArrayList<String>()
                    for (i in 0 until temp.length()) t.add(temp.optString(i))
                    task.setTags(t)
                } catch (e: JSONException) {
                    e.printStackTrace()
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }

                TagManager.save()
                task.save()

                when (view) {
                    TASK_PORTRAIT -> task_tags.text = task.getTagString()
                    TASK_LANDSCAPE -> task_landscape_tags.text = task.getTagString()
                    FOLDER_PORTRAIT -> folder_tags.text = task.getTagString()
                    FOLDER_LANDSCAPE -> folder_landscape_tags.text = task.getTagString()
                    NOTE_PORTRAIT -> note_tags.text = task.getTagString()
                    NOTE_LANDSCAPE -> note_tags.text = task.getTagString()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_task, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.more) {
            startActivity(Intent(this, TaskManagementActivity::class.java)
                    .putExtra(TASK_TYPE, task.getType())
                    .putExtra(TASK_NAME, task.getFilename()))
            return true
        }

        return onOptionsItemSelected(this, item, object: Save { override fun save() = this@TaskActivity.save() })
    }

    override fun onClick(button: View?) {
        when (view) {
            TASK_PORTRAIT -> onClickTaskPortrait(button)
            TASK_LANDSCAPE -> onClickTaskLandscape(button)
            FOLDER_PORTRAIT -> onClickFolderPortrait(button)
            FOLDER_LANDSCAPE -> onClickFolderLandscape(button)
            NOTE_PORTRAIT -> onClickNote(button)
            NOTE_LANDSCAPE -> onClickNote(button)
        }
    }

    override fun onLongClick(button: View?): Boolean {
        return when (view) {
            TASK_PORTRAIT -> onLongClickTaskPortrait(button)
            TASK_LANDSCAPE -> onLongClickTaskLandscape(button)
            FOLDER_PORTRAIT -> onLongClickFolderPortrait(button)
            FOLDER_LANDSCAPE -> onLongClickFolderLandscape(button)
            NOTE_PORTRAIT -> onLongClickNote(button)
            NOTE_LANDSCAPE -> onLongClickNote(button)
            else -> false
        }
    }

    override fun afterTextChanged(editable: Editable?) {
        if (!load) {
            when (view) {
                TASK_PORTRAIT -> textChangedTaskPortrait(editable)
                TASK_LANDSCAPE -> textChangedTaskLandscape(editable)
                FOLDER_PORTRAIT -> textChangedFolderPortrait(editable)
                FOLDER_LANDSCAPE -> textChangedFolderLandscape(editable)
                NOTE_PORTRAIT -> textChangedNote(editable)
                NOTE_LANDSCAPE -> textChangedNote(editable)
            }
        }
    }

    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
        when (view) {
            TASK_PORTRAIT -> onProgressChangedTaskPortrait()
            TASK_LANDSCAPE -> onProgressChangedTaskLandscape()
        }
    }

    override fun onDateSet(date_picker: DatePicker?, year: Int, month: Int, day: Int) {
        when (view) {
            TASK_PORTRAIT -> onDateSetTaskPortrait(year, month, day)
            TASK_LANDSCAPE -> onDateSetTaskLandscape(year, month, day)
        }
    }

    // Orientation Specific Methods ----------------------------------------------------------------
    private fun onCreateTaskPortrait() {
        setContentView(R.layout.activity_task)

        task_title.addTextChangedListener(this)
        task_note.addTextChangedListener(this)
        task_tags.setOnClickListener(this)
        task_tags.setOnLongClickListener(this)

        task_date.setOnClickListener(this)
        task_date.setOnLongClickListener(this)
        task_status.setOnClickListener(this)
        task_priority.setOnSeekBarChangeListener(this)

        task_add_checkpoint.setOnClickListener(this)
        task_bottom_bar_search.setOnClickListener(this)
        task_bottom_bar_search_text.addTextChangedListener(this)
    }

    private fun onCreateTaskLandscape() {
        setContentView(R.layout.activity_task_landscape)

        task_landscape_title.addTextChangedListener(this)
        task_landscape_note.addTextChangedListener(this)
        task_landscape_tags.setOnClickListener(this)
        task_landscape_tags.setOnLongClickListener(this)

        task_landscape_date.setOnClickListener(this)
        task_landscape_date.setOnLongClickListener(this)
        task_landscape_status.setOnClickListener(this)
        task_landscape_priority.setOnClickListener(this)

        task_landscape_add_checkpoint.setOnLongClickListener(this)
        task_landscape_bottom_bar_search.setOnClickListener(this)
        task_landscape_bottom_bar_search_text.addTextChangedListener(this)
    }

    private fun onCreateFolderPortrait() {
        setContentView(R.layout.activity_folder)

        folder_title.addTextChangedListener(this)
        folder_note.addTextChangedListener(this)
        folder_tags.setOnClickListener(this)
        folder_tags.setOnLongClickListener(this)

        folder_add_task.setOnClickListener(this)
        folder_add_folder.setOnClickListener(this)
        folder_add_note.setOnClickListener(this)
        folder_bottom_bar_search.setOnClickListener(this)
        folder_bottom_bar_search_text.addTextChangedListener(this)
    }

    private fun onCreateFolderLandscape() {
        setContentView(R.layout.activity_folder_landscape)

        folder_landscape_title.addTextChangedListener(this)
        folder_landscape_note.addTextChangedListener(this)
        folder_landscape_tags.setOnClickListener(this)
        folder_landscape_tags.setOnLongClickListener(this)

        folder_landscape_add_task.setOnClickListener(this)
        folder_landscape_add_folder.setOnClickListener(this)
        folder_landscape_add_note.setOnClickListener(this)
        folder_landscape_bottom_bar_search.setOnClickListener(this)
        folder_landscape_bottom_bar_search_text.addTextChangedListener(this)
    }

    private fun onCreateNote() {
        setContentView(R.layout.activity_note)

        note_title.addTextChangedListener(this)
        note_note.addTextChangedListener(this)
        note_tags.setOnClickListener(this)
        note_tags.setOnLongClickListener(this)
    }

    private fun onResumeTaskPortrait() {
        task_title.setText(task.getTitle())
        task_note.setText(task.getNote())
        task_tags.text = task.getTagString()

        if (task.getStatus() == Task.STATUS_DONE) task_status.setText(R.string.complete)
        else task_status.setText(R.string.incomplete)
        task_date.text = task.getDateDueString()
        task_priority.progress = task.getPriority()

        task.sortTags()
        task_checkpoints.adapter = CheckpointAdapter(this, task.getFilename(), task.getCheckpoints())
    }

    private fun onResumeTaskLandscape() {
        task_landscape_title.setText(task.getTitle())
        task_landscape_note.setText(task.getNote())
        task_landscape_tags.text = task.getTagString()

        if (task.getStatus() == Task.STATUS_DONE) task_landscape_status.setText(R.string.complete)
        else task_landscape_status.setText(R.string.incomplete)
        task_landscape_date.text = task.getDateDueString()
        task_landscape_priority.progress = task.getPriority()

        task.sortTags()
        task_landscape_checkpoints.adapter = CheckpointAdapter(this, task.getFilename(), task.getCheckpoints())
    }

    private fun onResumeFolderPortrait() {
        folder_title.setText(task.getTitle())
        folder_note.setText(task.getNote())
        folder_tags.text = task.getTagString()

        folder_tasks.adapter = TaskAdapter(this, this, task.getChildren(), "")
    }

    private fun onResumeFolderLandscape() {
        folder_landscape_title.setText(task.getTitle())
        folder_landscape_note.setText(task.getNote())
        folder_landscape_tags.text = task.getTagString()

        folder_landscape_tasks.adapter = TaskAdapter(this, this, task.getChildren(), "")
    }

    private fun onResumeNote() {
        note_title.setText(task.getTitle())
        note_note.setText(task.getNote())
        note_tags.text = task.getTagString()
    }

    private fun onClickTaskPortrait(button: View?) {
        when (button) {
            task_tags -> startActivityForResult(buildTaskTagsIntent(this, IntentOptions(), TaskOptions(filename = task.getFilename())), REQUEST_TAGS)

            task_date -> {
                if (task.getDateDue() == null) DatePickerDialog(this, this, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show()
                else DatePickerDialog(this, this, task.getDateDue()!!.get(Calendar.YEAR), task.getDateDue()!!.get(Calendar.MONTH), task.getDateDue()!!.get(Calendar.DAY_OF_MONTH)).show()
                task.save()
            }

            task_status -> {
                if (task.getStatus() == Task.STATUS_DONE) task.markComplete(false)
                else task.markComplete(true)
                task.save()

                if (task.getStatus() == Task.STATUS_DONE) task_status.setText(R.string.complete)
                else task_status.setText(R.string.incomplete)
            }

            task_add_checkpoint -> {
                val checkpoint: Checkpoint = if (task.getCheckpoints().size == 0) Checkpoint(1, "")
                else Checkpoint(task.getCheckpoints()[task.getCheckpoints().size - 1].id + 1, "")

                task.addCheckpoint(checkpoint)
                task.save()
                startActivityForResult(Intent(this, CheckpointActivity::class.java).putExtra(CHECKPOINT, checkpoint.toString()), REQUEST_CHECKPOINT)
            }

            task_bottom_bar_search -> {
                if (task_bottom_bar_search_text.visibility == View.VISIBLE) searchTaskVisibility(false)
                else searchTaskVisibility()
            }
        }
    }

    private fun onClickTaskLandscape(button: View?) {
        when (button) {
            task_landscape_tags -> startActivityForResult(buildTaskTagsIntent(this, IntentOptions(), TaskOptions(filename = task.getFilename())), REQUEST_TAGS)

            task_landscape_date -> {
                if (task.getDateDue() == null) DatePickerDialog(this, this, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show()
                else DatePickerDialog(this, this, task.getDateDue()!!.get(Calendar.YEAR), task.getDateDue()!!.get(Calendar.MONTH), task.getDateDue()!!.get(Calendar.DAY_OF_MONTH)).show()
                task.save()
            }

            task_landscape_status -> {
                if (task.getStatus() == Task.STATUS_DONE) task.markComplete(false)
                else task.markComplete(true)
                task.save()

                if (task.getStatus() == Task.STATUS_DONE) task_landscape_status.setText(R.string.complete)
                else task_landscape_status.setText(R.string.incomplete)
            }

            task_landscape_add_checkpoint -> {
                val checkpoint: Checkpoint = if (task.getCheckpoints().size == 0) Checkpoint(1, "")
                else Checkpoint(task.getCheckpoints()[task.getCheckpoints().size - 1].id + 1, "")

                task.addCheckpoint(checkpoint)
                task.save()
                startActivityForResult(Intent(this, CheckpointActivity::class.java).putExtra(CHECKPOINT, checkpoint.toString()), REQUEST_CHECKPOINT)
            }

            task_landscape_bottom_bar_search -> {
                if (task_landscape_bottom_bar_search_text.visibility == View.VISIBLE) searchTaskVisibility(false)
                else searchTaskVisibility()
            }
        }
    }

    private fun onClickFolderPortrait(button: View?) {
        when (button) {
            folder_tags -> startActivityForResult(buildTaskTagsIntent(this, IntentOptions(), TaskOptions(filename = task.getFilename())), REQUEST_TAGS)

            folder_add_folder -> {
                folder_fab.close(true)
                startActivity(buildTaskIntent(this, IntentOptions(), TaskOptions(task = TaskManager.addTask(Task(task.getFilename(), Task.TYPE_FOLDER).save(), false))))
            }

            folder_add_task -> {
                folder_fab.close(true)
                startActivity(buildTaskIntent(this, IntentOptions(), TaskOptions(task = TaskManager.addTask(Task(task.getFilename(), Task.TYPE_TASK).save(), false))))
            }

            folder_add_note -> {
                folder_fab.close(true)
                startActivity(buildTaskIntent(this, IntentOptions(), TaskOptions(task = TaskManager.addTask(Task(task.getFilename(), Task.TYPE_NOTE).save(), false))))
            }

            folder_bottom_bar_search -> {
                if (folder_bottom_bar_search_text.visibility == View.VISIBLE) searchFolderVisibility(false)
                else searchFolderVisibility()
            }
        }
    }

    private fun onClickFolderLandscape(button: View?) {
        when (button) {
            folder_landscape_tags -> startActivityForResult(buildTaskTagsIntent(this, IntentOptions(), TaskOptions(filename = task.getFilename())), REQUEST_TAGS)

            folder_landscape_add_folder -> {
                folder_landscape_fab.close(true)
                startActivity(buildTaskIntent(this, IntentOptions(), TaskOptions(task = TaskManager.addTask(Task(task.getFilename(), Task.TYPE_FOLDER).save(), false))))
            }

            folder_landscape_add_task -> {
                folder_landscape_fab.close(true)
                startActivity(buildTaskIntent(this, IntentOptions(), TaskOptions(task = TaskManager.addTask(Task(task.getFilename(), Task.TYPE_TASK).save(), false))))
            }

            folder_landscape_add_note -> {
                folder_fab.close(true)
                startActivity(buildTaskIntent(this, IntentOptions(), TaskOptions(task = TaskManager.addTask(Task(task.getFilename(), Task.TYPE_NOTE).save(), false))))
            }

            folder_landscape_bottom_bar_search -> {
                if (folder_landscape_bottom_bar_search_text.visibility == View.VISIBLE) searchFolderVisibility(false)
                else searchFolderVisibility()
            }
        }
    }

    private fun onClickNote(button: View?) {
        startActivityForResult(buildTaskTagsIntent(this, IntentOptions(), TaskOptions(filename = task.getFilename())), REQUEST_TAGS)
    }

    private fun onLongClickTaskPortrait(button: View?): Boolean {
        when (button) {
            task_date -> {
                task.setDateDue(null)
                task.save()
                task_date.text = task.getDateDueString()
                return true
            }
        }

        return false
    }

    private fun onLongClickTaskLandscape(button: View?): Boolean {
        when (button) {
            task_landscape_date -> {
                task.setDateDue(null)
                task.save()
                task_landscape_date.text = task.getDateDueString()
                return true
            }
        }

        return false
    }

    private fun onLongClickFolderPortrait(button: View?): Boolean {
        return false
    }

    private fun onLongClickFolderLandscape(button: View?): Boolean {
        return false
    }

    private fun onLongClickNote(button: View?): Boolean {
        return false
    }

    private fun textChangedTaskPortrait(editable: Editable?) {
        if (editable == null) throw NullPointerException()

        when (editable.hashCode()) {
            task_title.editableText.hashCode() -> {
                task.setTitle(editable.toString()).save()
                title = task.getTitle()
            }

            task_note.editableText.hashCode() -> task.setNote(editable.toString()).save()

            task_bottom_bar_search_text.editableText.hashCode() -> {
                if (task_bottom_bar_search_text.visibility == View.VISIBLE) {
                    task_checkpoints.adapter = CheckpointSearchAdapter(this@TaskActivity, task.getFilename(), editable.toString(), task.getCheckpoints())
                }
            }
        }
    }

    private fun textChangedTaskLandscape(editable: Editable?) {
        if (editable == null) throw NullPointerException()

        when (editable.hashCode()) {
            task_landscape_title.editableText.hashCode() -> {
                task.setTitle(editable.toString()).save()
                title = task.getTitle()
            }

            task_landscape_note.editableText.hashCode() -> task.setNote(editable.toString()).save()

            task_landscape_bottom_bar_search_text.editableText.hashCode() -> {
                if (task_landscape_bottom_bar_search_text.visibility == View.VISIBLE) {
                    task_landscape_checkpoints.adapter = CheckpointSearchAdapter(this@TaskActivity, task.getFilename(), editable.toString(), task.getCheckpoints())
                }
            }
        }
    }

    private fun textChangedFolderPortrait(editable: Editable?) {
        if (editable == null) throw NullPointerException()

        when (editable.hashCode()) {
            folder_title.editableText.hashCode() -> {
                task.setTitle(editable.toString()).save()
                title = task.getTitle()
            }

            folder_note.editableText.hashCode() -> task.setNote(editable.toString()).save()

            folder_bottom_bar_search_text.editableText.hashCode() -> {
                if (folder_bottom_bar_search_text.visibility == View.VISIBLE) {
                    if (folder_bottom_bar_search_text.visibility == View.VISIBLE) folder_tasks.adapter = TaskAdapter(this@TaskActivity, this@TaskActivity, task.getChildren(), editable.toString())
                }
            }
        }
    }

    private fun textChangedFolderLandscape(editable: Editable?) {
        if (editable == null) throw NullPointerException()

        when (editable.hashCode()) {
            folder_landscape_title.editableText.hashCode() -> {
                task.setTitle(editable.toString()).save()
                title = task.getTitle()
            }

            folder_landscape_note.editableText.hashCode() -> task.setNote(editable.toString()).save()

            folder_landscape_bottom_bar_search_text.editableText.hashCode() -> {
                if (folder_landscape_bottom_bar_search_text.visibility == View.VISIBLE) {
                    if (folder_landscape_bottom_bar_search_text.visibility == View.VISIBLE) folder_landscape_tasks.adapter = TaskAdapter(this@TaskActivity, this@TaskActivity, task.getChildren(), editable.toString())
                }
            }
        }
    }

    private fun textChangedNote(editable: Editable?) {
        if (editable == null) throw NullPointerException()

        when (editable.hashCode()) {
            note_title.editableText.hashCode() -> {
                task.setTitle(editable.toString()).save()
                title = task.getTitle()
            }

            note_note.editableText.hashCode() -> task.setNote(editable.toString()).save()
        }
    }

    private fun onProgressChangedTaskPortrait() = task.setPriority(task_priority.progress).save()

    private fun onProgressChangedTaskLandscape() = task.setPriority(task_landscape_priority.progress).save()

    private fun onDateSetTaskPortrait(year: Int, month: Int, day: Int) {
        task.setDateDue(GregorianCalendar(year, month, day, 0, 0, 1)).save()
        task_date.text = task.getDateDueString()
    }

    private fun onDateSetTaskLandscape(year: Int, month: Int, day: Int) {
        task.setDateDue(GregorianCalendar(year, month, day, 0, 0, 1)).save()
        task_landscape_date.text = task.getDateDueString()
    }
    // Text Watcher --------------------------------------------------------------------------------
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    // Seekbar Listener ----------------------------------------------------------------------------
    override fun onStartTrackingTouch(p0: SeekBar?) {}
    override fun onStopTrackingTouch(p0: SeekBar?) {}
    // Task Listener -------------------------------------------------------------------------------
    override fun onTaskChanged() = onResume()

    // Parent Methods ------------------------------------------------------------------------------
    fun save() {
        MasterManager.save()
        task.save()
    }

    fun editCheckpoint(checkpoint: Checkpoint) {
        task.editCheckpoint(checkpoint).save()
        onResume()
    }

    fun deleteCheckpoint(checkpoint: Checkpoint) {
        task.removeCheckpoint(checkpoint).save()
        onTaskChanged()
    }

    // Search Methods ------------------------------------------------------------------------------
    private fun searchTaskVisibility(visible: Boolean = true) {
        if (visible) {
            task_bottom_bar_search_text.visibility = View.VISIBLE
            task_bottom_bar_search_text.setText("")
            when (view) {
                TASK_PORTRAIT -> task_checkpoints.adapter = CheckpointSearchAdapter(this, task.getFilename(), "", task.getCheckpoints())
                TASK_LANDSCAPE -> task_landscape_checkpoints.adapter = CheckpointSearchAdapter(this, task.getFilename(), "", task.getCheckpoints())
            }
        } else {
            task_bottom_bar_search_text.visibility = View.INVISIBLE
            task_bottom_bar_search_text.setText("")
            when (view) {
                TASK_PORTRAIT -> task_checkpoints.adapter = CheckpointAdapter(this, task.getFilename(), task.getCheckpoints())
                TASK_LANDSCAPE -> task_landscape_checkpoints.adapter = CheckpointAdapter(this, task.getFilename(), task.getCheckpoints())
            }
        }
    }

    private fun searchFolderVisibility(visible: Boolean = true) {
        if (visible) {
            folder_bottom_bar_search_text.visibility = View.VISIBLE
            folder_bottom_bar_search_text.setText("")
            when (view) {
                FOLDER_PORTRAIT -> folder_tasks.adapter = TaskAdapter(this, this, task.getChildren(), "")
                FOLDER_LANDSCAPE -> folder_landscape_tasks.adapter = TaskAdapter(this, this, task.getChildren(), "")
            }
        } else {
            folder_bottom_bar_search_text.visibility = View.INVISIBLE
            folder_bottom_bar_search_text.setText("")
            when (view) {
                FOLDER_PORTRAIT -> folder_tasks.adapter = TaskAdapter(this, this, task.getChildren(), "")
                FOLDER_LANDSCAPE -> folder_landscape_tasks.adapter = TaskAdapter(this, this, task.getChildren(), "")
            }
        }
    }

    // Internal Classes ----------------------------------------------------------------------------
    class CheckpointAdapter(private val activity: TaskActivity, private val task: String, private val checkpoints: List<Checkpoint>): BaseAdapter() {
        // List Methods --------------------------------------------------------------------------------
        override fun getCount(): Int = checkpoints.size

        override fun getViewTypeCount(): Int = 1

        override fun hasStableIds(): Boolean = false

        // Item Methods --------------------------------------------------------------------------------
        override fun getItem(position: Int): Checkpoint = checkpoints[position]

        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, convert_view: View?, parent: ViewGroup): View {
            var view: View? = convert_view
            if (view == null) {
                view = LayoutInflater.from(activity).inflate(R.layout.list_checkpoint, parent, false)
                view.tag = CheckpointHolder(activity, task, getItem(position), view!!)
            } else {
                view.tag = CheckpointHolder(activity, task, getItem(position), view)
            }

            return view
        }
    }

    class CheckpointSearchAdapter(private val activity: TaskActivity, private val task: String, search: String, checkpoints: ArrayList<Checkpoint>): BaseAdapter() {
        private val temp = ArrayList<Checkpoint>()
        // Constructor -----------------------------------------------------------------------------
        init {
            for (i in 0 until checkpoints.size) if (checkpoints[i].text.toLowerCase().contains(search.toLowerCase())) temp.add(checkpoints[i])
        }

        // List Methods ----------------------------------------------------------------------------
        override fun getCount(): Int = temp.size

        override fun getViewTypeCount(): Int = 1

        override fun hasStableIds(): Boolean = false

        // Item Methods ----------------------------------------------------------------------------
        override fun getItem(position: Int): Checkpoint = temp[position]

        override fun getItemId(position: Int): Long = position.toLong()

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
class CheckpointHolder(private val activity: TaskActivity, private val task: String, private val checkpoint: Checkpoint, view: View): View.OnClickListener, View.OnLongClickListener {
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
        vibrate(activity)
        return true
    }
}

class IllegalOrientationException(message: String): Exception(message)
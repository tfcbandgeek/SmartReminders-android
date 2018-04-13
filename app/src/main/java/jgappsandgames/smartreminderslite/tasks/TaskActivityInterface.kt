package jgappsandgames.smartreminderslite.tasks

// Activity
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextWatcher

// Views
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.SeekBar
import android.widget.TextView

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.holder.TaskFolderHolder
import jgappsandgames.smartreminderslite.home.HomeActivity
import jgappsandgames.smartreminderslite.utility.ActivityUtility

// Save
import jgappsandgames.smartreminderssave.tasks.Task
import org.jetbrains.anko.toast

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
abstract class TaskActivityInterface:
        Activity(), View.OnClickListener, View.OnLongClickListener, TextWatcher,
        SeekBar.OnSeekBarChangeListener, DatePickerDialog.OnDateSetListener, TaskFolderHolder.OnTaskChangedListener {
    // View Orientation ----------------------------------------------------------------------------
    @Suppress("unused")
    companion object {
        protected const val TASK_PORTRAIT = 1
        protected const val TASK_LANDSCAPE = 2
        protected const val TASK_MULTI = 3
        protected const val FOLDER_PORTRAIT = 11
        protected const val FOLDER_LANDSCAPE = 12
        protected const val FOLDER_MULTI = 13
    }

    protected var view = 0

    // Views ---------------------------------------------------------------------------------------
    protected var title: EditText? = null
    protected var note: EditText? = null
    protected var tags: TextView? = null
    protected var date: Button? = null
    protected var status: Button? = null
    protected var priority: SeekBar? = null
    protected var list: ListView? = null
    protected var fab: Button? = null

    // Adapters ------------------------------------------------------------------------------------
    protected var adapter: BaseAdapter? = null

    // LifeCycle Methods ---------------------------------------------------------------------------
    /**
     * OnCreate
     *
     * Called to Decide which Layout Should Be Used and and Create The Activity and Find Views
     * Called By The Application
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        fab = findViewById(R.id.fab)

        // Set TextWatcher
        title!!.addTextChangedListener(this)
        note!!.addTextChangedListener(this)

        // Set Click Listener
        fab!!.setOnClickListener(this)
        fab!!.setOnLongClickListener(this)

        tags!!.setOnClickListener(this)
        tags!!.setOnLongClickListener(this)

        // Task Specific Views
        if (type == Task.TYPE_TASK) {
            date = findViewById(R.id.date)
            date!!.setOnClickListener(this)
            date!!.setOnLongClickListener(this)

            status = findViewById(R.id.status)
            status!!.setOnClickListener(this)

            priority = findViewById(R.id.priority)
            priority!!.setOnSeekBarChangeListener(this)
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
        when (item!!.itemId) {
            R.id.save -> {
                save()
                toast("Saved.").show()
                return true
            }

            R.id.home -> {
                startActivity(Intent(this, HomeActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }

            R.id.close -> {
                finish()
                return true
            }
        }

        return false
    }

    // Abstract Methods ----------------------------------------------------------------------------
    protected abstract fun save()
}
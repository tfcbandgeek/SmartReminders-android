package jgappsandgames.smartreminderslite.sort.date

// Android OS
import android.app.Activity
import android.content.Intent
import android.os.Bundle

// Views
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.holder.TaskFolderHolder
import jgappsandgames.smartreminderslite.home.FirstRun

// Save
import jgappsandgames.smartreminderssave.MasterManager
import jgappsandgames.smartreminderssave.utility.FileUtility

/**
 * DayActivityInterface
 * Created by joshua on 1/1/2018.
 *
 * Activity That Handles the Day Sort View
 */
abstract class DayActivityInterface: Activity(), View.OnClickListener, TaskFolderHolder.OnTaskChangedListener {
    // Views ---------------------------------------------------------------------------------------
    protected var tasks: ListView? = null
    protected var previous: Button? = null
    protected var next: Button? = null

    // Adapter -------------------------------------------------------------------------------------
    protected var adapter: BaseAdapter? = null

    // LifeCycle Methods ---------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set Content View
        setContentView(R.layout.activity_date)

        // First Run
        FileUtility.loadFilePaths(this)
        if (FileUtility.isFirstRun()) startActivity(Intent(this, FirstRun::class.java))

        // Normal Run
        else MasterManager.load()

        // Set Title
        setTitle()

        // Find Views
        tasks = findViewById(R.id.tasks)
        previous = findViewById(R.id.previous)
        next = findViewById(R.id.next)

        // Set Click Listeners
        previous!!.setOnClickListener(this)
        next!!.setOnClickListener(this)
    }

    // Menu ----------------------------------------------------------------------------------------
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_auxilary, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                save()
                Toast.makeText(this, "Saved.", Toast.LENGTH_LONG).show()
            }

            R.id.close -> finish()
        }

        return super.onOptionsItemSelected(item)
    }

    // Abstract Methods ----------------------------------------------------------------------------
    abstract fun setTitle()
    abstract fun save()
}
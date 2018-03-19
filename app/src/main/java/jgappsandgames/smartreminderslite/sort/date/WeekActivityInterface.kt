package jgappsandgames.smartreminderslite.sort.date

// Android OS
import android.app.Activity
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

/**
 * WeekActivityInterface
 * Created by joshua on 1/19/2018.
 */
abstract class WeekActivityInterface: Activity(), View.OnClickListener, TaskFolderHolder.OnTaskChangedListener {
    // Data
    protected var week_active: Int = 0

    // Views
    protected var tasks: ListView? = null
    protected var previous: Button? = null
    protected var next: Button? = null

    // Adapters
    protected var adapter: BaseAdapter? = null

    // LifeCycle Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set Content View
        setContentView(R.layout.activity_date)

        // Find Views
        tasks = findViewById(R.id.tasks)
        previous = findViewById(R.id.previous)
        next = findViewById(R.id.next)

        // Set Click Listeners
        previous!!.setOnClickListener(this)
        next!!.setOnClickListener(this)
    }

    // Menu
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

    // Abstract Methods
    abstract fun save()
}
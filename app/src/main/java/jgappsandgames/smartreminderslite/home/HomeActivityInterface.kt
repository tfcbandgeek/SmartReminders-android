package jgappsandgames.smartreminderslite.home

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

// Open Log
import me.jgappsandgames.openlog.Log

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.sort.date.DayActivity
import jgappsandgames.smartreminderslite.sort.date.MonthActivity
import jgappsandgames.smartreminderslite.sort.date.WeekActivity
import jgappsandgames.smartreminderslite.sort.priority.PriorityActivity
import jgappsandgames.smartreminderslite.sort.status.StatusActivity
import jgappsandgames.smartreminderslite.sort.tags.TagActivity

/**
 * HomeActivityInterface
 * Created by joshua on 12/13/2017.
 *
 * Home[HomeActivityInterface, HomeActivity]
 * The Main Entrypoint for the Application.  It serves as the Primary Test to see if it is the First
 *     run and Direct the User Where they Need To Go.
 *
 *     HomeActivityInterface: View and Application Focus
 *     HomeActivity: Data and User Input
 */
abstract class HomeActivityInterface: Activity(), View.OnClickListener, View.OnLongClickListener {
    // Views ---------------------------------------------------------------------------------------
    protected var list: ListView? = null
    protected var fab: Button? = null

    // Adapters ------------------------------------------------------------------------------------
    protected var adapter: BaseAdapter? = null

    // LifeCycle Methods ---------------------------------------------------------------------------
    /**
     * OnCreate
     *
     * Called to Initialize the View and Do One Time Settings
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("HomeActivityInterface", "OnCreate Called")

        // Set Activity View
        setContentView(R.layout.activity_home)
        setTitle(R.string.app_name)

        // Find Views
        list = findViewById(R.id.tasks)
        fab = findViewById(R.id.fab)

        // Set Activity Click Listeners
        fab!!.setOnClickListener(this)
        fab!!.setOnLongClickListener(this)
    }

    // Menu Methods --------------------------------------------------------------------------------
    /**
     * OnCreateOptionsMenu
     *
     * Called to Create the Options Menu
     * Called By the Application
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.d("HomeActivityInterface", "OnCreateOptionsMenu Called")

        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    /**
     * OnOptionsItemsSelected
     *
     * Called When an Options Menu Item is Selected
     * Called By the Application
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        Log.d("HomeActivityInterface", "OnOptionsItemSelected Called")

        when(item!!.itemId) {
            R.id.tags -> {
                startActivity(Intent(this, TagActivity::class.java))
                return true
            }

            R.id.status -> {
                startActivity(Intent(this, StatusActivity::class.java))
                return true
            }

            R.id.priority -> {
                startActivity(Intent(this, PriorityActivity::class.java))
                return true
            }

            R.id.day -> {
                startActivity(Intent(this, DayActivity::class.java))
                return true
            }

            R.id.week -> {
                startActivity(Intent(this, WeekActivity::class.java))
                return true
            }

            R.id.month -> {
                startActivity(Intent(this, MonthActivity::class.java))
                return true
            }

            R.id.settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                return true
            }

            R.id.about -> {
                startActivity(Intent (this, AboutActivity::class.java))
                return true
            }

            R.id.save -> {
                save ()
                Toast.makeText(this, "Saved.", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.close -> {
                finish()
                return true
            }
        }

        return false
    }

    // Abstract Class Methods ----------------------------------------------------------------------
    protected abstract fun save()
}
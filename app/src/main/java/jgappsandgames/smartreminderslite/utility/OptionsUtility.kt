package jgappsandgames.smartreminderslite.utility

// Android OS
import android.app.Activity
import android.content.Intent

// Views
import android.view.MenuItem

// Anko
import org.jetbrains.anko.toast

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.home.AboutActivity
import jgappsandgames.smartreminderslite.home.HomeActivity
import jgappsandgames.smartreminderslite.home.Settings2Activity
import jgappsandgames.smartreminderslite.sort.DayActivity
import jgappsandgames.smartreminderslite.sort.MonthActivity
import jgappsandgames.smartreminderslite.sort.WeekActivity
import jgappsandgames.smartreminderslite.sort.PriorityActivity
import jgappsandgames.smartreminderslite.sort.StatusActivity
import jgappsandgames.smartreminderslite.sort.TagActivity

/**
 * OptionsUtility
 * Created by Joshua Garner on 6/25/2018.
 */
fun onOptionsItemSelected(context: Activity, item: MenuItem, save: Save? = null): Boolean {
    when(item.itemId) {
        R.id.home -> {
            context.startActivity(Intent(context, HomeActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }

        R.id.tags -> {
            context.startActivity(Intent(context, TagActivity::class.java))
            return true
        }

        R.id.status -> {
            context.startActivity(Intent(context, StatusActivity::class.java))
            return true
        }

        R.id.priority -> {
            context.startActivity(Intent(context, PriorityActivity::class.java))
            return true
        }

        R.id.settings -> {
            context.startActivity(Intent(context, Settings2Activity::class.java))
            return true
        }

        R.id.about -> {
            context.startActivity(Intent (context, AboutActivity::class.java))
            return true
        }

        R.id.save -> {
            if (save != null) {
                save.save()
                context.toast(R.string.saved).show()
                return true
            }

            return false
        }

        R.id.close -> {
            context.finish()
            return true
        }
    }

    return false
}

interface Save {
    fun save()
}
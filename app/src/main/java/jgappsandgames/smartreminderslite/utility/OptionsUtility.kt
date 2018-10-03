package jgappsandgames.smartreminderslite.utility

// Android OS
import android.app.Activity

// Views
import android.view.MenuItem

// Anko
import org.jetbrains.anko.toast

// App
import jgappsandgames.smartreminderslite.R

/**
 * OptionsUtility
 * Created by Joshua Garner on 6/25/2018.
 */
fun onOptionsItemSelected(context: Activity, item: MenuItem, save: Save? = null): Boolean {
    when(item.itemId) {
        R.id.home -> {
            context.startActivity(buildHomeIntent(context, IntentOptions(clearStack = true, option = true)))
            return true
        }

        /**R.id.tags -> {
            context.startActivity(buildTagsIntent(context, IntentOptions(option = true)))
            return true
        }**/

        R.id.status -> {
            context.startActivity(buildStatusIntent(context, IntentOptions(option = true)))
            return true
        }

        /**R.id.priority -> {
            context.startActivity(buildPriorityIntent(context, IntentOptions(option = true)))
            return true
        }**/

        R.id.settings -> {
            context.startActivity(buildSettingsIntent(context, IntentOptions(option = true)))
            return true
        }

        R.id.about -> {
            context.startActivity(buildAboutIntent(context, IntentOptions(option = true)))
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
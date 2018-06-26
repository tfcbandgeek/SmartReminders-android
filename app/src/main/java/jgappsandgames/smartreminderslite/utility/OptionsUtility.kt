package jgappsandgames.smartreminderslite.utility

import android.app.Activity
import android.content.Intent
import android.view.MenuItem
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.home.AboutActivity
import jgappsandgames.smartreminderslite.home.SettingsActivity
import jgappsandgames.smartreminderslite.sort.date.DayActivity
import jgappsandgames.smartreminderslite.sort.date.MonthActivity
import jgappsandgames.smartreminderslite.sort.date.WeekActivity
import jgappsandgames.smartreminderslite.sort.priority.PriorityActivity
import jgappsandgames.smartreminderslite.sort.status.StatusActivity
import jgappsandgames.smartreminderslite.sort.tags.TagActivity
import org.jetbrains.anko.toast

class OptionsUtility {
    companion object {
        fun onOptionsItemSelected(context: Activity, item: MenuItem, save: Save? = null): Boolean {
            when(item.itemId) {
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

                R.id.day -> {
                    context.startActivity(Intent(context, DayActivity::class.java))
                    return true
                }

                R.id.week -> {
                    context.startActivity(Intent(context, WeekActivity::class.java))
                    return true
                }

                R.id.month -> {
                    context.startActivity(Intent(context, MonthActivity::class.java))
                    return true
                }

                R.id.settings -> {
                    context.startActivity(Intent(context, SettingsActivity::class.java))
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
    }

    interface Save {
        fun save()
    }
}
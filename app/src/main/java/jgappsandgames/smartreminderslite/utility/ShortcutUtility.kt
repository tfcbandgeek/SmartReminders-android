package jgappsandgames.smartreminderslite.utility

// Java
import java.util.Arrays

// Android OS
import android.app.Activity
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.sort.DayActivity
import jgappsandgames.smartreminderslite.sort.MonthActivity
import jgappsandgames.smartreminderslite.sort.WeekActivity
import jgappsandgames.smartreminderslite.sort.PriorityActivity
import jgappsandgames.smartreminderslite.sort.StatusActivity
import jgappsandgames.smartreminderslite.sort.TagActivity
import jgappsandgames.smartreminderslite.tasks.TaskActivity

// Save
import jgappsandgames.smartreminderssave.tasks.Task

/**
 * ShortcutUtility
 * Created by joshua on 1/5/2018.
 */
// Create Shortcuts ------------------------------------------------------------------------
fun createTagShortcut(activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
        val manager = activity.getSystemService(ShortcutManager::class.java)

        val intent = Intent(activity, TagActivity::class.java)
                .setAction(Intent.ACTION_DEFAULT)
                .putExtra("home", true)
                .putExtra("shortcut", true)

        val shortcut = ShortcutInfo.Builder(activity, "tag")
                .setShortLabel("Tags")
                .setLongLabel("Tags")
                .setIcon(Icon.createWithResource(activity, R.drawable.label))
                .setIntent(intent)
                .build()

        manager!!.addDynamicShortcuts(Arrays.asList(shortcut))
    }
}

fun createPriorityShortcut(activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
        val manager = activity.getSystemService(ShortcutManager::class.java)

        val intent = Intent(activity, PriorityActivity::class.java)
                .setAction(Intent.ACTION_DEFAULT)
                .putExtra("home", true)
                .putExtra("shortcut", true)

        val shortcut = ShortcutInfo.Builder(activity, "priority")
                .setShortLabel("Priority")
                .setLongLabel("Priority")
                .setIcon(Icon.createWithResource(activity, android.R.drawable.btn_star))
                .setIntent(intent)
                .build()

        manager!!.addDynamicShortcuts(Arrays.asList(shortcut))
    }
}

fun createStatusShortcut(activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
        val manager = activity.getSystemService(ShortcutManager::class.java)

        val intent = Intent(activity, StatusActivity::class.java)
                .setAction(Intent.ACTION_DEFAULT)
                .putExtra("home", true)
                .putExtra("shortcut", true)

        val shortcut = ShortcutInfo.Builder(activity, "status")
                .setShortLabel("Status")
                .setLongLabel("Status")
                .setIcon(Icon.createWithResource(activity, R.drawable.status))
                .setIntent(intent)
                .build()

        manager!!.addDynamicShortcuts(Arrays.asList(shortcut))
    }
}

fun createTodayShortcut(activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
        val manager = activity.getSystemService(ShortcutManager::class.java)

        val intent = Intent(activity, DayActivity::class.java)
                .setAction(Intent.ACTION_DEFAULT)
                .putExtra("home", true)
                .putExtra("shortcut", true)

        val shortcut = ShortcutInfo.Builder(activity, "today")
                .setShortLabel("Today")
                .setLongLabel("Today")
                .setIcon(Icon.createWithResource(activity, R.drawable.today))
                .setIntent(intent)
                .build()

        manager!!.addDynamicShortcuts(Arrays.asList(shortcut))
    }
}

fun createWeekShortcut(activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
        val manager = activity.getSystemService(ShortcutManager::class.java)

        val intent = Intent(activity, WeekActivity::class.java)
                .setAction(Intent.ACTION_DEFAULT)
                .putExtra("home", true)
                .putExtra("shortcut", true)

        val shortcut = ShortcutInfo.Builder(activity, "week")
                .setShortLabel("Week")
                .setLongLabel("Week")
                .setIcon(Icon.createWithResource(activity, R.drawable.week))
                .setIntent(intent)
                .build()

        manager!!.addDynamicShortcuts(Arrays.asList(shortcut))
    }
}

fun createMonthShortcut(activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
        val manager = activity.getSystemService(ShortcutManager::class.java)

        val intent = Intent(activity, MonthActivity::class.java)
                .setAction(Intent.ACTION_DEFAULT)
                .putExtra("home", true)
                .putExtra("shortcut", true)

        val shortcut = ShortcutInfo.Builder(activity, "month")
                .setShortLabel("Month")
                .setLongLabel("Month")
                .setIntent(intent)
                .build()

        manager!!.addDynamicShortcuts(Arrays.asList(shortcut))
    }
}

fun createTaskShortcut(activity: Activity, task: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
        val manager = activity.getSystemService(ShortcutManager::class.java)

        val taskObject = Task(task)

        val intent = Intent(activity, TaskActivity::class.java)
                .setAction(Intent.ACTION_DEFAULT)
                .putExtra("home", true)
                .putExtra("shortcut", true)
                .putExtra(TASK_NAME, task)

        val sT: String = if (taskObject.getTitle().length > 8) taskObject.getTitle().substring(0, 8)
        else taskObject.getTitle()

        val shortcut = ShortcutInfo.Builder(activity, task)
                .setShortLabel(sT)
                .setLongLabel(taskObject.getTitle())
                .setIcon(Icon.createWithResource(activity, android.R.drawable.ic_menu_agenda))
                .setIntent(intent)
                .build()

        manager!!.addDynamicShortcuts(Arrays.asList(shortcut))
    }
}

// Remove Shortcuts ------------------------------------------------------------------------
fun removeTagShortcut(activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
        val manager = activity.getSystemService(ShortcutManager::class.java)

        val s = ArrayList<String>()
        s.add("tag")
        manager!!.removeDynamicShortcuts(s)
    }
}

fun removePriorityShortcut(activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
        val manager = activity.getSystemService(ShortcutManager::class.java)

        val s = ArrayList<String>()
        s.add("priority")
        manager!!.removeDynamicShortcuts(s)
    }
}

fun removeStatusShortcut(activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
        val manager = activity.getSystemService(ShortcutManager::class.java)

        val s = ArrayList<String>()
        s.add("status")
        manager!!.removeDynamicShortcuts(s)
    }
}

fun removeTodayShortcut(activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
        val manager = activity.getSystemService(ShortcutManager::class.java)

        val s = ArrayList<String>()
        s.add("today")
        manager!!.removeDynamicShortcuts(s)
    }
}

fun removeWeekShortcut(activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
        val manager = activity.getSystemService(ShortcutManager::class.java)

        val s = ArrayList<String>()
        s.add("week")
        manager!!.removeDynamicShortcuts(s)
    }
}

fun removeMonthShortcut(activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
        val manager = activity.getSystemService(ShortcutManager::class.java)

        val s = ArrayList<String>()
        s.add("month")
        manager!!.removeDynamicShortcuts(s)
    }
}

fun removeTaskShortcut(activity: Activity, task: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
        val manager = activity.getSystemService(ShortcutManager::class.java)

        val s = ArrayList<String>()
        s.add(task)
        manager!!.removeDynamicShortcuts(s)
    }
}
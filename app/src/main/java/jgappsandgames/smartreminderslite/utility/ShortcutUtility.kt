package jgappsandgames.smartreminderslite.utility

// Java
import java.util.Arrays

// Android OS
import android.app.Activity
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderssave.tasks.getTaskFromPool

// Save
import jgappsandgames.smartreminderssave.utility.hasShortcuts
import org.jetbrains.anko.shortcutManager

/**
 * ShortcutUtility
 * Created by joshua on 1/5/2018.
 */
// Create Shortcuts ------------------------------------------------------------------------
fun createTagShortcut(activity: Activity) {
    if (hasShortcuts()) activity.shortcutManager.addDynamicShortcuts(
            Arrays.asList(ShortcutInfo.Builder(activity, "tag")
                    .setShortLabel("Tags")
                    .setLongLabel("Tags")
                    .setIcon(Icon.createWithResource(activity, R.drawable.label))
                    .setIntent(buildHomeIntent(activity, IntentOptions(shortcut = true)))
                    .build()))
}

fun createPriorityShortcut(activity: Activity) {
    if (hasShortcuts()) activity.shortcutManager.addDynamicShortcuts(
            Arrays.asList(ShortcutInfo.Builder(activity, "priority")
                    .setShortLabel("Priority")
                    .setLongLabel("Priority")
                    .setIcon(Icon.createWithResource(activity, android.R.drawable.btn_star))
                    .setIntent(buildPriorityIntent(activity, IntentOptions(shortcut = true)))
                    .build()))
}

fun createStatusShortcut(activity: Activity) {
    if (hasShortcuts()) activity.shortcutManager.addDynamicShortcuts(
            Arrays.asList(ShortcutInfo.Builder(activity, "status")
                    .setShortLabel("Status")
                    .setLongLabel("Status")
                    .setIcon(Icon.createWithResource(activity, R.drawable.status))
                    .setIntent(buildStatusIntent(activity, IntentOptions(shortcut = true)))
                    .build()))
}

fun createTodayShortcut(activity: Activity) {
    if (hasShortcuts()) activity.shortcutManager.addDynamicShortcuts(
            Arrays.asList(ShortcutInfo.Builder(activity, "today")
                    .setShortLabel("Today")
                    .setLongLabel("Today")
                    .setIcon(Icon.createWithResource(activity, R.drawable.today))
                    .setIntent(buildDayIntent(activity, IntentOptions(shortcut = true)))
                    .build()))
}

fun createWeekShortcut(activity: Activity) {
    if (hasShortcuts()) activity.shortcutManager.addDynamicShortcuts(
            Arrays.asList(ShortcutInfo.Builder(activity, "week")
                    .setShortLabel("Week")
                    .setLongLabel("Week")
                    .setIcon(Icon.createWithResource(activity, R.drawable.week))
                    .setIntent(buildWeekIntent(activity, IntentOptions(shortcut = true)))
                    .build()))
}

fun createMonthShortcut(activity: Activity) {
    if (hasShortcuts()) activity.shortcutManager.addDynamicShortcuts(
            Arrays.asList(ShortcutInfo.Builder(activity, "month")
                    .setShortLabel("Month")
                    .setLongLabel("Month")
                    .setIntent(buildMonthIntent(activity, IntentOptions(shortcut = true)))
                    .build()))
}

fun createTaskShortcut(activity: Activity, task: String) {
    if (hasShortcuts()) {
        val taskObject = getTaskFromPool().load(task)
        val sT: String = if (taskObject.getTitle().length > 8) taskObject.getTitle().substring(0, 8)
        else taskObject.getTitle()

        activity.shortcutManager.addDynamicShortcuts(Arrays.asList(ShortcutInfo.Builder(activity, task)
                .setShortLabel(sT)
                .setLongLabel(taskObject.getTitle())
                .setIcon(Icon.createWithResource(activity, android.R.drawable.ic_menu_agenda))
                .setIntent(buildTaskIntent(activity, IntentOptions(shortcut = true), TaskOptions(task = taskObject)))
                .build()))
    }
}

// Remove Shortcuts ------------------------------------------------------------------------
fun removeTagShortcut(activity: Activity) {
    if (hasShortcuts()) {
        val manager = activity.getSystemService(ShortcutManager::class.java)

        val s = ArrayList<String>()
        s.add("tag")
        manager!!.removeDynamicShortcuts(s)
    }
}

fun removePriorityShortcut(activity: Activity) {
    if (hasShortcuts()) {
        val manager = activity.getSystemService(ShortcutManager::class.java)

        val s = ArrayList<String>()
        s.add("priority")
        manager!!.removeDynamicShortcuts(s)
    }
}

fun removeStatusShortcut(activity: Activity) {
    if (hasShortcuts()) {
        val manager = activity.getSystemService(ShortcutManager::class.java)

        val s = ArrayList<String>()
        s.add("status")
        manager!!.removeDynamicShortcuts(s)
    }
}

fun removeTodayShortcut(activity: Activity) {
    if (hasShortcuts()) {
        val manager = activity.getSystemService(ShortcutManager::class.java)

        val s = ArrayList<String>()
        s.add("today")
        manager!!.removeDynamicShortcuts(s)
    }
}

fun removeWeekShortcut(activity: Activity) {
    if (hasShortcuts()) {
        val manager = activity.getSystemService(ShortcutManager::class.java)

        val s = ArrayList<String>()
        s.add("week")
        manager!!.removeDynamicShortcuts(s)
    }
}

fun removeMonthShortcut(activity: Activity) {
    if (hasShortcuts()) {
        val manager = activity.getSystemService(ShortcutManager::class.java)

        val s = ArrayList<String>()
        s.add("month")
        manager!!.removeDynamicShortcuts(s)
    }
}

fun removeTaskShortcut(activity: Activity, task: String) {
    if (hasShortcuts()) {
        val manager = activity.getSystemService(ShortcutManager::class.java)

        val s = ArrayList<String>()
        s.add(task)
        manager!!.removeDynamicShortcuts(s)
    }
}
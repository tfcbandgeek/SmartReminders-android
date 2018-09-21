package jgappsandgames.smartreminderslite.utility

// Android OS
import android.app.Activity
import android.content.Intent

// Application
import jgappsandgames.smartreminderslite.home.AboutActivity
import jgappsandgames.smartreminderslite.home.Settings2Activity
import jgappsandgames.smartreminderslite.sort.*
import jgappsandgames.smartreminderslite.tasks.TaskActivity
import jgappsandgames.smartreminderssave.tasks.Task

fun buildHomeIntent(activity: Activity, options: IntentOptions): Intent {
    val intent = Intent(activity, TagActivity::class.java).setAction(Intent.ACTION_DEFAULT).putExtra("home", true)

    if (options.shortcut) intent.putExtra("shortcut", true)
    if (options.option) intent.putExtra("option", true)
    if (options.clearStack) intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    return intent
}

fun buildTagsIntent(activity: Activity, options: IntentOptions): Intent {
    val intent = Intent(activity, TagActivity::class.java)

    if (options.shortcut) intent.putExtra("shortcut", true)
    if (options.option) intent.putExtra("option", true)
    if (options.clearStack) intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    return intent
}

fun buildStatusIntent(activity: Activity, options: IntentOptions): Intent {
    val intent = Intent(activity, StatusActivity::class.java)

    if (options.shortcut) intent.putExtra("shortcut", true)
    if (options.option) intent.putExtra("option", true)
    if (options.clearStack) intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    return intent
}

fun buildPriorityIntent(activity: Activity, options: IntentOptions): Intent {
    val intent = Intent(activity, PriorityActivity::class.java)

    if (options.shortcut) intent.putExtra("shortcut", true)
    if (options.option) intent.putExtra("option", true)
    if (options.clearStack) intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    return intent
}

fun buildDayIntent(activity: Activity, options: IntentOptions): Intent {
    val intent = Intent(activity, DayActivity::class.java)

    if (options.shortcut) intent.putExtra("shortcut", true)
    if (options.option) intent.putExtra("option", true)
    if (options.clearStack) intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    return intent
}

fun buildWeekIntent(activity: Activity, options: IntentOptions): Intent {
    val intent = Intent(activity, WeekActivity::class.java)

    if (options.shortcut) intent.putExtra("shortcut", true)
    if (options.option) intent.putExtra("option", true)
    if (options.clearStack) intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    return intent
}

fun buildMonthIntent(activity: Activity, options: IntentOptions): Intent {
    val intent = Intent(activity, MonthActivity::class.java)

    if (options.shortcut) intent.putExtra("shortcut", true)
    if (options.option) intent.putExtra("option", true)
    if (options.clearStack) intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    return intent
}

fun buildTaskIntent(activity: Activity, options: IntentOptions, task: TaskOptions): Intent {
    val intent = Intent(activity, TaskActivity::class.java)

    if (task.task != null) {
        intent.putExtra(TASK_TYPE, task.task.getType())
        intent.putExtra(TASK_NAME, task.task.getFilename())
    } else {
        intent.putExtra(TASK_TYPE, task.type)
        intent.putExtra(TASK_NAME, task.filename)
    }

    if (options.shortcut) intent.putExtra("shortcut", true)
    if (options.option) intent.putExtra("option", true)
    if (options.clearStack) intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    return intent
}

fun buildSettingsIntent(activity: Activity, options: IntentOptions): Intent {
    val intent = Intent(activity, Settings2Activity::class.java)

    if (options.shortcut) intent.putExtra("shortcut", true)
    if (options.option) intent.putExtra("option", true)
    if (options.clearStack) intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    return intent
}

fun buildAboutIntent(activity: Activity, options: IntentOptions): Intent {
    val intent = Intent(activity, AboutActivity::class.java)

    if (options.shortcut) intent.putExtra("shortcut", true)
    if (options.option) intent.putExtra("option", true)
    if (options.clearStack) intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    return intent
}

class IntentOptions(val shortcut: Boolean = false, val option: Boolean = false, val clearStack: Boolean = false)
class TaskOptions(val filename: String? = null, val task: Task? = null, val type: Int? = null)
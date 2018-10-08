package jgappsandgames.smartreminderslite.utility

// Android OS
import android.app.Activity
import android.content.Intent

// App
import jgappsandgames.smartreminderslite.home.Settings2Activity

// Save
import jgappsandgames.smartreminderssave.loadMaster
import jgappsandgames.smartreminderssave.utility.isFirstRun
import jgappsandgames.smartreminderssave.utility.loadFilePaths

/**
 * ActivityUtility
 * Created by joshua on 12/26/2017.
 *
 * Holds Intent Keys, Request Codes, Response Codes
 */
// Intent Keys -----------------------------------------------------------------------------
const val TASK_NAME = "task_name"
const val CHECKPOINT = "checkpoint"
const val TAG_LIST = "tags"
const val TASK_TYPE = "type"
const val FIRST_RUN = "first_run"

// Request Codes ---------------------------------------------------------------------------
const val REQUEST_CHECKPOINT = 1
const val REQUEST_MANAGEMENT = 2
const val REQUEST_TAGS = 3
const val REQUEST_EXTERNAL_STORAGE_PERMISSION = 4

// Response Codes --------------------------------------------------------------------------
const val RESPONSE_CANCEL = -1
const val RESPONSE_NONE = 0
const val RESPONSE_CHANGE = 1

fun loadClass(activity: Activity) {
    loadFilePaths(activity)
    if (isFirstRun()) activity.startActivity(Intent(activity, Settings2Activity::class.java).putExtra(FIRST_RUN, true))
    else loadMaster()
}
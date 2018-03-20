package jgappsandgames.smartreminderslite.utility

/**
 * ActivityUtility
 * Created by joshua on 12/26/2017.
 *
 * Holds Intent Keys, Request Codes, Response Codes
 */
class ActivityUtility {
    companion object {
        // Intent Keys -----------------------------------------------------------------------------
        const val TASK_NAME = "task_name"
        const val CHECKPOINT = "checkpoint"
        const val TAG_LIST = "tags"
        const val TASK_TYPE = "type"
        const val HOME_OPTION = "home_option"

        // Intent Values ---------------------------------------------------------------------------
        const val HOME_PLANNER: Int = 1
        const val HOME_TASK: Int = 2
        const val HOME_ALL: Int = 3
        const val HOME_DAY: Int = 4
        const val HOME_WEEK: Int = 5
        const val HOME_MONTH: Int = 6
        const val HOME_TAG: Int = 7
        const val HOME_STATUS: Int = 8
        const val HOME_PRIORITY: Int = 9

        // Request Codes ---------------------------------------------------------------------------
        const val REQUEST_CHECKPOINT = 1
        const val REQUEST_MANAGEMENT = 2
        const val REQUEST_TAGS = 3
        const val REQUEST_EXTERNAL_STORAGE_PERMISSION = 4

        // Response Codes --------------------------------------------------------------------------
        const val RESPONSE_CANCEL = -1
        const val RESPONSE_NONE = 0
        const val RESPONSE_CHANGE = 1
    }
}
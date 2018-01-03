package jgappsandgames.smartreminderslite.utility

/**
 * ActivityUtility
 * Created by joshua on 12/26/2017.
 *
 * Holds Intent Keys, Request Codes, Response Codes
 */
class ActivityUtility {
    companion object {
        // Intent Keys
        @JvmField
        val TASK_NAME = "task_name"
        @JvmField
        val CHECKPOINT = "checkpoint"
        @JvmField
        val TAG_LIST = "tags"
        @JvmField
        val TASK_TYPE = "type"

        // Request Codes
        @JvmField
        val REQUEST_CHECKPOINT = 1
        @JvmField
        val REQUEST_Management = 2
        @JvmField
        val REQUEST_TAGS = 3
        @JvmField
        val REQUEST_EXTERNAL_STORAGE_PERMISSION = 4

        // Response Codes
        @JvmField
        val RESPONSE_CANCEL = -1
        @JvmField
        val RESPONSE_NONE = 0
        @JvmField
        val RESPONSE_CHANGE = 1
    }
}
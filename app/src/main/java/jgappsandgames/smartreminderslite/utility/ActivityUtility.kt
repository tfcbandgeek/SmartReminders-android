package jgappsandgames.smartreminderslite.utility

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

// Request Codes ---------------------------------------------------------------------------
const val REQUEST_CHECKPOINT = 1
const val REQUEST_MANAGEMENT = 2
const val REQUEST_TAGS = 3
const val REQUEST_EXTERNAL_STORAGE_PERMISSION = 4

// Response Codes --------------------------------------------------------------------------
const val RESPONSE_CANCEL = -1
const val RESPONSE_NONE = 0
const val RESPONSE_CHANGE = 1
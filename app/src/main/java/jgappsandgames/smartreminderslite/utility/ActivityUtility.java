package jgappsandgames.smartreminderslite.utility;

/**
 * ActivityUtility
 * Created by joshua on 8/31/17.
 *
 * Holds Intent Keys, Request Codes, Response Codes
 */
public class ActivityUtility {
    // Intent Keys
    public static final String TASK_NAME = "task_name";
    public static final String CHECKPOINT = "checkpoint";
    public static final String TAG_LIST = "tags";

    // Request Codes
    public static final int REQUEST_CHECKPOINT = 1;
    public static final int REQUEST_Management = 2;
    public static final int REQUEST_TAGS = 3;
    public static final int REQUEST_EXTERNAL_STORAGE_PERMISSION = 4;

    // Response Codes
    public static final int RESPONSE_CANCEL = -1;
    public static final int RESPONSE_NONE = 0;
    public static final int RESPONSE_CHANGE = 1;
}
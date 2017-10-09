package jgappsandgames.smartreminderssave.settings;

// Java
import java.io.File;
import java.util.ArrayList;
import java.util.List;

// JSON
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// Android OS
import android.os.Build;

// Save
import jgappsandgames.smartreminderssave.json.JSONLoader;
import jgappsandgames.smartreminderssave.utility.API;
import jgappsandgames.smartreminderssave.utility.FileUtility;

/**
 * Settings
 * Created by joshua on 8/30/17.
 * Last Edited on 10/8/17 (169).
 * Edited On 10/5/17 (174)
 *
 * Current API Version: 10
 */
public class Settings {
    // Filepath
    private static final String FILENAME = "settings.srj";

    // Save Data
    private static final String VERSION = "version";
    private static final String USER_NAME = "user_name";
    private static final String DEVICE_NAME = "device_name";

    private static final String USE_EXTERNALFILE = "external_file";

    private static final String HAS_TAG_SHORTCUT = "has_tag_shortcut";
    private static final String HAS_STATUS_SHORTCUT = "has_status_shortcut";
    private static final String HAS_PRIORITY_SHORTCUT = "has_priority_shortcut";
    private static final String HAS_TODAY_SHORTCUT = "has_today_shortcut";
    private static final String HAS_WEEK_SHORTCUT = "has_week_shortcut";

    private static final String TASK_SHORTCUTS = "task_shortcuts";

    private static final String HAS_DONE_TUTORIAL = "has_done_tutorial";
    private static final String LAST_VERSION_SPLASH = "last_version_splash";

    // Data
    public static int version;

    public static String user_name;
    public static String device_name;

    public static boolean use_external_file;

    public static boolean has_tag_shortcut;
    public static boolean has_status_shortcut;
    public static boolean has_priority_shortcut;
    public static boolean has_today_shortcut;
    public static boolean has_week_shortcut;

    public static List<String> task_shortcuts;

    public static boolean has_done_tutorial;
    public static int last_version_splash;

    // Management Methods
    public static void create() {
        version = API.VERSION_TEST_E;

        user_name = "";
        device_name = Build.BRAND + " " + Build.MODEL + " " + Build.VERSION.SDK_INT;

        use_external_file = false;

        has_tag_shortcut = false;
        has_status_shortcut = false;
        has_priority_shortcut = false;
        has_today_shortcut = false;
        has_week_shortcut = false;

        task_shortcuts = new ArrayList<>();

        has_done_tutorial = false;
        last_version_splash = -1;
    }

    public static void load() {
        JSONObject data = JSONLoader.loadJSON(new File(FileUtility.getInternalFileDirectory(), FILENAME));

        if (data == null) {
            create();
            return;
        }

        version = data.optInt(VERSION, API.RELEASE);

        user_name = data.optString(USER_NAME);
        device_name = data.optString(DEVICE_NAME);

        use_external_file = data.optBoolean(USE_EXTERNALFILE);
        has_tag_shortcut = data.optBoolean(HAS_TAG_SHORTCUT);
        has_done_tutorial = data.optBoolean(HAS_DONE_TUTORIAL);
        last_version_splash = data.optInt(LAST_VERSION_SPLASH);

        if (version >= API.VERSION_TEST_E) {
            has_status_shortcut = data.optBoolean(HAS_STATUS_SHORTCUT);
        } else {
            has_status_shortcut = false;
        }

        if (version >= API.VERSION_C) {
            has_today_shortcut = data.optBoolean(HAS_TODAY_SHORTCUT, false);
            has_week_shortcut = data.optBoolean(HAS_WEEK_SHORTCUT, false);
        } else {
            has_today_shortcut = false;
            has_week_shortcut = false;
        }

        if (version >= API.VERSION_D) {
            has_priority_shortcut = data.optBoolean(HAS_PRIORITY_SHORTCUT, false);

            JSONArray array = data.optJSONArray(TASK_SHORTCUTS);
            task_shortcuts = new ArrayList<>(array.length());
            for (int i = 0; i < array.length(); i++) {
                task_shortcuts.add(array.optString(i));
            }
        } else {
            has_priority_shortcut = false;
            task_shortcuts = new ArrayList<>();
        }
    }

    public static void save() {
        JSONObject data = new JSONObject();

        // Write to JSON
        try {
            data.put(VERSION, API.VERSION_C);

            data.put(USER_NAME, user_name);
            data.put(DEVICE_NAME, device_name);

            data.put(USE_EXTERNALFILE, use_external_file);

            data.put(HAS_TAG_SHORTCUT, has_tag_shortcut);
            data.put(HAS_STATUS_SHORTCUT, has_status_shortcut);
            data.put(HAS_PRIORITY_SHORTCUT, has_priority_shortcut);
            data.put(HAS_TODAY_SHORTCUT, has_today_shortcut);
            data.put(HAS_WEEK_SHORTCUT, has_week_shortcut);

            data.put(HAS_DONE_TUTORIAL, has_done_tutorial);
            data.put(LAST_VERSION_SPLASH, last_version_splash);

            JSONArray array = new JSONArray();
            for (String s : task_shortcuts) {
                array.put(s);
            }
            data.put(TASK_SHORTCUTS, array);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONLoader.saveJSONObject(new File(FileUtility.getInternalFileDirectory(), FILENAME), data);
    }
}
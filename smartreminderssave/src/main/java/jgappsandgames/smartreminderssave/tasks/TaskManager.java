package jgappsandgames.smartreminderssave.tasks;

// Java
import java.io.File;
import java.util.ArrayList;
import java.util.List;

// JSON
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// Save
import jgappsandgames.smartreminderssave.json.JSONLoader;
import jgappsandgames.smartreminderssave.utility.API;
import jgappsandgames.smartreminderssave.utility.FileUtility;

/**
 * TaskManager
 * Created by joshua on 8/24/17.
 * Last Edited on 10/21/17 (141).
 * Edited on 9/21/17. (140)
 *
 * Currently on API: 10
 */
public class TaskManager {
    // Filepath Constant
    private static final String FILENAME = "taskmanager.srj";

    // Save Constants
    private static final String VERSION = "version";
    private static final String HOME = "home";
    private static final String TASKS = "tasks";
    private static final String ARCHIVED = "archived";
    private static final String DELETED = "deleted";

    // Data
    public static int version;
    public static List<String> home;
    public static List<String> tasks;
    public static List<String> archived;
    public static List<String> deleted;

    // Management Methods
    public static void create() {
        home = new ArrayList<>();
        tasks = new ArrayList<>();
        archived = new ArrayList<>();
        deleted = new ArrayList<>();
    }

    public static void load() {
        JSONObject data = JSONLoader.loadJSON(new File(FileUtility.getApplicationDataDirectory(), FILENAME));

        if (data == null) {
            create();
            return;
        }

        version = data.optInt(VERSION, API.RELEASE);

        home = new ArrayList<>();
        tasks = new ArrayList<>();
        archived = new ArrayList<>();
        deleted = new ArrayList<>();

        JSONArray h = data.optJSONArray(HOME);
        JSONArray t = data.optJSONArray(TASKS);
        JSONArray a = data.optJSONArray(ARCHIVED);
        JSONArray d = data.optJSONArray(DELETED);

        if (h != null && h.length() != 0) for (int i = 0; i < h.length(); i++) if (!home.contains(h.optString(i))) home.add(h.optString(i));
        if (t != null && t.length() != 0) for (int i = 0; i < t.length(); i++) if (!tasks.contains(t.optString(i))) tasks.add(t.optString(i));
        if (a != null && a.length() != 0) for (int i = 0; i < a.length(); i++) if (!archived.contains(a.optString(i))) archived.add(a.optString(i));
        if (d != null && d.length() != 0) for (int i = 0; i < d.length(); i++) if (!deleted.contains(d.optString(i))) deleted.add(d.optString(i));
    }

    public static void save() {
        JSONObject data = new JSONObject();

        try {
            data.put(VERSION, API.RELEASE);

            JSONArray h = new JSONArray();
            JSONArray t = new JSONArray();
            JSONArray a = new JSONArray();
            JSONArray d = new JSONArray();

            for (String task : home) h.put(task);
            for (String task : tasks) t.put(task);
            for (String task : archived) a.put(task);
            for (String task : deleted) d.put(task);

            data.put(HOME, h);
            data.put(TASKS, t);
            data.put(ARCHIVED, a);
            data.put(DELETED, d);
        } catch (JSONException j) {
            j.printStackTrace();
        }

        JSONLoader.saveJSONObject(new File(FileUtility.getApplicationDataDirectory(), FILENAME), data);
    }

    public static void clearTasks() {
        for (String task : deleted) new Task(task).delete();
        deleted = new ArrayList<>();
    }

    // Task Methods
    public static void archiveTask(Task task) {
        task.markArchived();
        task.save();

        if (task.getParent().equals("home")) {
            home.remove(task.getFilename());
        } else if (tasks.contains(task.getParent())) {
            Task parent = new Task(task.getParent());

            parent.removeChild(task.getFilename());

            parent.save();
        }

        tasks.remove(task.getFilename());
        archived.add(task.getFilename());
        TaskManager.save();
    }

    public static boolean deleteTask(Task task) {
        if (archived.contains(task.getFilename()))  {
            task.markDeleted();
            task.save();
            deleted.add(task.getFilename());
            archived.remove(task.getFilename());
            TaskManager.save();
            return true;
        }

        return false;
    }
}
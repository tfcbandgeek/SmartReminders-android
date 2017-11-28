package jgappsandgames.smartreminderssave.tasks;

// Java
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

// JSON
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// Save
import jgappsandgames.smartreminderssave.utility.JSONUtility;
import jgappsandgames.smartreminderssave.utility.API;
import jgappsandgames.smartreminderssave.utility.FileUtility;

/**
 * TaskManager
 * Created by joshua on 8/24/17.
 *
 * Currently on API: 10
 */
public class TaskManager {
    // Filepath Constant
    private static final String FILENAME = "taskmanager.srj";

    // Save Constants
    private static final String VERSION = "version";
    private static final String META = "meta";

    private static final String HOME = "home";
    private static final String TASKS = "tasks";
    private static final String ARCHIVED = "archived";
    private static final String DELETED = "deleted";

    // Data
    private static int version;
    public static JSONObject meta;
    public static ArrayList<String> home;
    public static ArrayList<String> tasks;
    public static ArrayList<String> archived;
    public static ArrayList<String> deleted;

    // Management Methods
    public static void create() {
        version = API.MANAGEMENT;

        home = new ArrayList<>();
        tasks = new ArrayList<>();
        archived = new ArrayList<>();
        deleted = new ArrayList<>();

        // Version 11
        meta = new JSONObject();
    }

    public static void load() {
        try {
            loadJSON(JSONUtility.loadJSON(new File(FileUtility.getApplicationDataDirectory(), FILENAME)));
        } catch (IOException i) {
            i.printStackTrace();

            create();
            save();
        }
        if (deleted.size() >= 50) deleted.remove(0);
    }

    public static void save() {
        JSONUtility.saveJSONObject(new File(FileUtility.getApplicationDataDirectory(), FILENAME), saveJSON());
    }

    public static void clearTasks() {
        for (String task : archived) deleteTask(new Task(task));
        archived = new ArrayList<>();
    }

    // JSONManagement Methods
    public static void loadJSON(JSONObject data) {
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

        if (h != null && h.length() != 0) for (int i = 0; i < h.length(); i++)
            if (!home.contains(h.optString(i))) home.add(h.optString(i));
        if (t != null && t.length() != 0) for (int i = 0; i < t.length(); i++)
            if (!tasks.contains(t.optString(i))) tasks.add(t.optString(i));
        if (a != null && a.length() != 0) for (int i = 0; i < a.length(); i++)
            if (!archived.contains(a.optString(i))) archived.add(a.optString(i));
        if (d != null && d.length() != 0) for (int i = 0; i < d.length(); i++)
            if (!deleted.contains(d.optString(i))) deleted.add(d.optString(i));

        // Version 11
        if (version >= API.MANAGEMENT) {
            meta = data.optJSONObject(META);
            if (meta == null) {
                meta = new JSONObject();
            }
        } else {
            meta = new JSONObject();
        }
    }

    public static JSONObject saveJSON() {
        JSONObject data = new JSONObject();

        try {
            data.put(VERSION, API.MANAGEMENT);

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

            // Version 11
            data.put(META, meta);
        } catch (JSONException j) {
            j.printStackTrace();
        }

        return data;
    }

    public static void updateTasks(JSONObject data) {
        ArrayList<String> ha = new ArrayList<>();
        ArrayList<String> ta = new ArrayList<>();
        ArrayList<String> aa = new ArrayList<>();
        ArrayList<String> da = new ArrayList<>();

        JSONArray h = data.optJSONArray(HOME);
        JSONArray t = data.optJSONArray(TASKS);
        JSONArray a = data.optJSONArray(ARCHIVED);
        JSONArray d = data.optJSONArray(DELETED);

        if (h != null && h.length() != 0) for (int i = 0; i < h.length(); i++)
            if (!ha.contains(h.optString(i))) ha.add(h.optString(i));
        if (t != null && t.length() != 0) for (int i = 0; i < t.length(); i++)
            if (!ta.contains(t.optString(i))) ta.add(t.optString(i));
        if (a != null && a.length() != 0) for (int i = 0; i < a.length(); i++)
            if (!aa.contains(a.optString(i))) aa.add(a.optString(i));
        if (d != null && d.length() != 0) for (int i = 0; i < d.length(); i++)
            if (!da.contains(d.optString(i))) da.add(d.optString(i));

        for (String task : ha) {
            if (home.contains(task)) continue;
            if (archived.contains(task) || deleted.contains(task)) continue;
            home.add(task);
            if (!tasks.contains(task)) tasks.add(task);
        }

        for (String task : ta) {
            if (tasks.contains(task)) continue;
            if (archived.contains(task) || deleted.contains(task)) continue;
            tasks.add(task);
        }

        for (String task : aa) {
            if (archived.contains(task)) continue;
            if (deleted.contains(task)) continue;
            if (tasks.contains(task)) tasks.remove(task);
            archived.add(task);
        }

        for (String task : da) {
            if (deleted.contains(task)) continue;
            if (archived.contains(task)) archived.remove(task);
            if (tasks.contains(task)) tasks.remove(task);
        }
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
        save();
    }

    public static boolean deleteTask(Task task) {
        if (archived.contains(task.getFilename()))  {
            task.delete();
            deleted.add(task.getFilename());
            archived.remove(task.getFilename());
            save();
            return true;
        }

        return false;
    }
}
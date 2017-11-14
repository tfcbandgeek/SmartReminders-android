package jgappsandgames.smartreminderssave.date;

// JSON
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// Java
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

// Save
import jgappsandgames.smartreminderssave.tasks.Task;

/**
 * Day
 * Created by joshua on 9/10/17.
 * Last Edited 10/8/17 (41).
 */
public class Day {
    // Log Constant
    private static  final String LOG = "Day";

    // Save Constants
    private static final String DAY = "day";
    private static final String TASKS = "tasks";

    // Data
    private final Calendar day;
    private final ArrayList<Task> tasks;

    public Day(Calendar day) {
        Log.d(LOG, "Day(Calendar) called");

        // Set the Day
        Log.v(LOG, "Set the Day");
        this.day = day;

        // Create an Empty Array to Hold The Tasks
        Log.v(LOG, "Create an Empty Array for Tasks");
        tasks = new ArrayList<>();

        Log.v(LOG, "Initializer Done");
    }

    public Day(JSONObject data) {
        Log.d(LOG, "Day(JSONObject) Called");

        // Load the Day
        Log.v(LOG, "Load the Day");
        JSONCalendar t = new JSONCalendar();
        day = t.loadCalendar(data.optJSONObject(DAY));

        // Load the Tasks
        Log.v(LOG, "Load The Tasks");
        JSONArray h = data.optJSONArray(TASKS);
        tasks = new ArrayList<>();
        if (h == null || h.length() == 0) return;
        for (int i = 0; i < h.length(); i++) tasks.add(new Task(h.optString(i)));

        Log.v(LOG, "Initiallizer Done");
    }

    public Day() {
        Log.e(LOG, "Error Initializer Called");
        // Date is Basically 404 over and over Again
        day = new GregorianCalendar(2404, 4, 0, 4, 40, 4);

        // Create an Array to hold the Tasks, Literally just here to prevent Errors
        tasks = new ArrayList<>();
    }

    public JSONObject toJSON() {
        Log.d(LOG, "toJSON Called");

        // Create the JSONObject
        Log.v(LOG, "Create JSONObjects");
        JSONObject data = new JSONObject();
        JSONArray a = new JSONArray();

        try {
            // Add the Day
            Log.v(LOG, "Add the Day");
            JSONCalendar t = new JSONCalendar();
            data.put(DAY, t.saveCalendar(day));

            // Add the Tasks
            Log.v(LOG, "Add the Tasks");
            for (Task task : tasks) a.put(task.getFilename());
            data.put(TASKS, a);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Return the data
        Log.v(LOG, "Return the data");
        return data;
    }

    public Calendar getDay() {
        return day;
    }

    public boolean addTask(Task task) {
        Log.d(LOG, "addTask Called");

        // Check to see if the Task is in th Right Day
        Log.v(LOG, "Check to see if the Task is in the Right Day");
        if (task.getDateDue() == null) return false;
        if (task.getDateDue().get(Calendar.YEAR) != day.get(Calendar.YEAR)) return false;
        if (task.getDateDue().get(Calendar.DAY_OF_YEAR) != day.get(Calendar.DAY_OF_YEAR)) return false;

        // Check to see if the Task is already here
        Log.v(LOG, "Check to see if the Task is already Here");
        for (Task temp : tasks) if (temp.getFilename().equals(task.getFilename())) return true;

        // Add the Task
        Log.v(LOG, "Add the Task");
        tasks.add(task);
        return true;
    }

    public boolean removeTask(Task task) {
        Log.d(LOG, "removeTask Called");

        if (tasks.contains(task)) {
            Log.v(LOG, "Task is Here, Removing It");
            tasks.remove(task);
            return true;
        }

        Log.v(LOG, "Task is not Here");
        return false;
    }

    public ArrayList<Task> getTasks() {
        Log.d(LOG, "getTasks");
        if (tasks == null) return new ArrayList<>();
        return tasks;
    }

    // Class to Convert A Calendar to JSONObject
    private class JSONCalendar {
        private static final String ACTIVE = "active";
        private static final String DATE = "date";

        Calendar loadCalendar(JSONObject data) {
            if (data.optBoolean(ACTIVE, false)) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(data.optLong(DATE, 0));
                return calendar;
            }

            return null;
        }

        JSONObject saveCalendar(Calendar calendar) {
            try {
                JSONObject data = new JSONObject();

                if (calendar == null) {
                    data.put(ACTIVE, false);
                } else {
                    data.put(ACTIVE, true);
                    data.put(DATE, calendar.getTimeInMillis());
                }
                return data;
            } catch (JSONException j) {
                j.printStackTrace();
            }

            return new JSONObject();
        }
    }
}
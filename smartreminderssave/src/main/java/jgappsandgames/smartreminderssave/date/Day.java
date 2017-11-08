package jgappsandgames.smartreminderssave.date;

// Java
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

// Save
import jgappsandgames.smartreminderssave.tasks.Task;

/**
 * Day
 * Created by joshua on 9/10/17.
 * Last Edited 10/8/17 (41).
 */
public class Day {
    // Save Constants
    private static final String DAY = "day";
    private static final String TASKS = "tasks";

    // Data
    private final Calendar day;
    private final ArrayList<Task> tasks;

    public Day(Calendar day) {
        this.day = day;

        tasks = new ArrayList<>();
    }

    public Day(JSONObject data) {
        // Load the Day
        JSONCalendar t = new JSONCalendar();
        day = t.loadCalendar(data.optJSONObject(DAY));

        // Load the Tasks
        JSONArray h = data.optJSONArray(TASKS);
        tasks = new ArrayList<>();
        if (h == null || h.length() == 0) return;
        for (int i = 0; i < h.length(); i++) tasks.add(new Task(h.optString(i)));
    }

    public Day() {
        // Date is Basically 404 over and over Again
        day = new GregorianCalendar(2404, 4, 0, 4, 40, 4);
        tasks = new ArrayList<>();
    }

    public JSONObject toJSON() {
        // Create the JSONObject
        JSONObject data = new JSONObject();
        JSONArray a = new JSONArray();

        try {
            // Add the Day
            JSONCalendar t = new JSONCalendar();
            data.put(DAY, t.saveCalendar(day));

            // Add the Task
            for (Task task : tasks) a.put(task.getFilename());
            data.put(TASKS, a);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Return the data
        return data;
    }

    public Calendar getDay() {
        return day;
    }

    public boolean addTask(Task task) {
        // Check to see if the Task is in th Right Day
        if (task.getDateDue() == null) return false;
        if (task.getDateDue().get(Calendar.YEAR) != day.get(Calendar.YEAR)) return false;
        if (task.getDateDue().get(Calendar.DAY_OF_YEAR) != day.get(Calendar.DAY_OF_YEAR)) return false;

        // Check to see if the Task is already here
        for (Task temp : tasks) if (temp.getFilename().equals(task.getFilename())) return true;

        // Add the Task
        tasks.add(task);
        return true;
    }

    public boolean removeTask(Task task) {
        if (tasks.contains(task)) {
            tasks.remove(task);
            return true;
        }

        return false;
    }

    public ArrayList<Task> getTasks() {
        if (tasks == null) return new ArrayList<>();
        return tasks;
    }

    // Class to Convert A Calendar to JSONObject
    private class JSONCalendar {
        private static final String ACTIVE = "active";
        private static final String DATE = "date";

        public Calendar loadCalendar(JSONObject data) {
            if (data.optBoolean(ACTIVE, false)) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(data.optLong(DATE, 0));
                return calendar;
            }

            return null;
        }

        public JSONObject saveCalendar(Calendar calendar) {
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
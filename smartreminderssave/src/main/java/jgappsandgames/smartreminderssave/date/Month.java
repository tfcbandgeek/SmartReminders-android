package jgappsandgames.smartreminderssave.date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import jgappsandgames.smartreminderssave.tasks.Task;

/**
 * Month
 * Created by joshua on 10/12/17.
 * Last Edited on 10/12/17 (112). <10.1>
 */
public class Month {
    // Save Constants
    private static final String START = "start";
    private static final String END = "end";

    private static final String STARTS_ON = "starts_on";
    private static final String DAYS_IN_MONTH = "days_in_month";

    private static final String DAYS = "days";

    // Data
    private Calendar start;
    private Calendar end;

    private int starts_on;
    private int days_in_month;

    private long update = 0;
    private ArrayList<Calendar> days;
    private ArrayList<Day> holders;

    // Initializers
    public Month(Calendar start) {
        start.set(Calendar.DAY_OF_MONTH, 1);
        this.start = (Calendar) start.clone();
        starts_on = start.get(Calendar.DAY_OF_WEEK);

        days_in_month = 0;
        days = new ArrayList<>();
        while (true) {
            days.add((Calendar) start.clone());
            start.add(Calendar.DAY_OF_MONTH, 1);
            if (start.get(Calendar.DAY_OF_WEEK) < days_in_month) break;
            days_in_month = start.get(Calendar.DAY_OF_MONTH);
        }
        start.add(Calendar.DAY_OF_MONTH, -1);
        end = start;

        holders = new ArrayList<>();
    }

    public Month(JSONObject data) {
        final JSONCalendar jc = new JSONCalendar();

        start = jc.loadCalendar(data.optJSONObject(START));
        end = jc.loadCalendar(data.optJSONObject(END));
        days_in_month = data.optInt(DAYS_IN_MONTH, 28);
        starts_on = data.optInt(STARTS_ON, 1);

        final JSONArray a = data.optJSONArray(DAYS);
        days = new ArrayList<>();
        holders = new ArrayList<>();
        for (int i = 0; i < a.length(); i++) days.add(jc.loadCalendar(a.optJSONObject(i)));
    }

    public Month() {
        start = new GregorianCalendar(2404, 4, 0, 4, 40, 4);
        end = new GregorianCalendar(2404, 4, 0, 4, 40, 4);
        starts_on = -1;
        days_in_month = -1;

        days = new ArrayList<>(1);
        holders = new ArrayList<>(1);
    }

    // Saving Method
    public JSONObject toJSON() {
        final JSONObject data = new JSONObject();
        final JSONArray d = new JSONArray();
        final JSONCalendar jc = new JSONCalendar();

        try {
            data.put(START, jc.saveCalendar(start));
            data.put(END, jc.saveCalendar(end));
            data.put(STARTS_ON, starts_on);
            data.put(DAYS_IN_MONTH, days_in_month);

            for (Calendar temp : days) d.put(jc.saveCalendar(temp));
            data.put(DAYS, d);
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }

        return data;
    }

    // Actually Load the days
    public void load() {
        update = System.currentTimeMillis();

        holders = new ArrayList<>(days.size());
        for (Calendar temp : days) holders.add(DateManagerKt.getDay(temp));
    }

    // Getters
    public Day getDay(Calendar day) {
        if (update < System.currentTimeMillis() - 300000) load();

        return DateManagerKt.getDay(day);
    }

    public Calendar getStart() {
        return start;
    }

    public Calendar getEnd() {
        return end;
    }

    public ArrayList<Task> getAllTasks() {
        if (update < System.currentTimeMillis() - 300000) load();

        final ArrayList<Task> all = new ArrayList<>();
        for (Day day : holders) all.addAll(day.getTasks());
        all.trimToSize();
        return all;
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
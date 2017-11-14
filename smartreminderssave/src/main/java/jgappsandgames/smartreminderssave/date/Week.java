package jgappsandgames.smartreminderssave.date;

// JSON
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

// Java
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

// Save
import jgappsandgames.smartreminderssave.tasks.Task;

/**
 * Week
 * Created by joshua on 9/10/17.
 * Last Edited on 10/5/17 (204).
 */
public class Week {
    // Log Constants
    private static final String LOG = "Week";

    // Save Constants
    private static final String START = "start";
    private static final String END = "end";

    private static final String SUNDAY = "sunday";
    private static final String MONDAY = "monday";
    private static final String TUESDAY = "tuesday";
    private static final String WEDNESDAY = "wednesday";
    private static final String THURSDAY = "thursday";
    private static final String FRIDAY = "friday";
    private static final String SATURDAY = "saturday";

    // Data
    private final Calendar start;
    private final Calendar end;

    private final Calendar sunday_c;
    private final Calendar monday_c;
    private final Calendar tuesday_c;
    private final Calendar wednesday_c;
    private final Calendar thursday_c;
    private final Calendar friday_c;
    private final Calendar saturday_c;

    private long load = 0;

    private Day sunday_d;
    private Day monday_d;
    private Day tuesday_d;
    private Day wednesday_d;
    private Day thursday_d;
    private Day friday_d;
    private Day saturday_d;

    // Initializers
    public Week(Calendar start) {
        Log.d(LOG, "Week(Calendar Called");

        Log.v(LOG, "Set Days");
        this.start = (Calendar) start.clone();

        sunday_c = this.start;
        start.add(Calendar.DAY_OF_WEEK, 1);

        monday_c = (Calendar) start.clone();
        start.add(Calendar.DAY_OF_WEEK, 1);

        tuesday_c = (Calendar) start.clone();
        start.add(Calendar.DAY_OF_WEEK, 1);

        wednesday_c = (Calendar) start.clone();
        start.add(Calendar.DAY_OF_WEEK, 1);

        thursday_c = (Calendar) start.clone();
        start.add(Calendar.DAY_OF_WEEK, 1);

        friday_c = (Calendar) start.clone();
        start.add(Calendar.DAY_OF_WEEK, 1);

        saturday_c = (Calendar) start.clone();
        end = saturday_c;

        Log.v(LOG, "Week Done");
    }

    public Week(JSONObject data) {
        Log.d(LOG, "Week(JSONObject)");
        JSONCalendar jc = new JSONCalendar();

        Log.v(LOG, "Set Calendar Days");
        start = jc.loadCalendar(data.optJSONObject(START));
        end = jc.loadCalendar(data.optJSONObject(END));

        sunday_c = jc.loadCalendar(data.optJSONObject(SUNDAY));
        monday_c = jc.loadCalendar(data.optJSONObject(MONDAY));
        tuesday_c = jc.loadCalendar(data.optJSONObject(TUESDAY));
        wednesday_c = jc.loadCalendar(data.optJSONObject(WEDNESDAY));
        thursday_c = jc.loadCalendar(data.optJSONObject(THURSDAY));
        friday_c = jc.loadCalendar(data.optJSONObject(FRIDAY));
        saturday_c = jc.loadCalendar(data.optJSONObject(SATURDAY));

        Log.v(LOG, "Week Done");
    }

    public Week() {
        Log.d(LOG, "Week Called");

        Log.v(LOG, "Set Calendar Days");
        start = new GregorianCalendar(2404, 4, 0, 4, 40, 4);
        end = start;

        sunday_c = start;
        monday_c = start;
        tuesday_c = start;
        wednesday_c = start;
        thursday_c = start;
        friday_c = start;
        saturday_c = start;

        Log.v(LOG, "Week Done");
    }

    // Saving Method
    public JSONObject toJSON() {
        Log.d(LOG, "toJSON");
        final JSONObject data = new JSONObject();
        final JSONCalendar jc = new JSONCalendar();

        try {
            Log.v(LOG, "Insert into JSON");
            data.put(START, jc.saveCalendar(start));
            data.put(END, end);

            data.put(SUNDAY, jc.saveCalendar(sunday_c));
            data.put(MONDAY, jc.saveCalendar(monday_c));
            data.put(TUESDAY, jc.saveCalendar(tuesday_c));
            data.put(WEDNESDAY, jc.saveCalendar(wednesday_c));
            data.put(THURSDAY, jc.saveCalendar(thursday_c));
            data.put(FRIDAY, jc.saveCalendar(friday_c));
            data.put(SATURDAY, jc.saveCalendar(saturday_c));
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }

        Log.v(LOG, "Return Data");
        return data;
    }

    // Actually load the Days
    public void load() {
        Log.d(LOG, "load Called");
        load = System.currentTimeMillis();

        Log.v(LOG, "Load Days");
        sunday_d = DateManagerKt.getDay(sunday_c);
        monday_d = DateManagerKt.getDay(monday_c);
        tuesday_d = DateManagerKt.getDay(tuesday_c);
        wednesday_d = DateManagerKt.getDay(wednesday_c);
        thursday_d = DateManagerKt.getDay(thursday_c);
        friday_d = DateManagerKt.getDay(friday_c);
        saturday_d = DateManagerKt.getDay(saturday_c);

        Log.v(LOG, "load Done");
    }

    // Getters
    public Day getDay(Calendar day_of_week) {
        if (load < System.currentTimeMillis() - 300000) load();

        switch (day_of_week.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                return sunday_d;

            case Calendar.MONDAY:
                return monday_d;

            case Calendar.TUESDAY:
                return tuesday_d;

            case Calendar.WEDNESDAY:
                return wednesday_d;

            case Calendar.THURSDAY:
                return thursday_d;

            case Calendar.FRIDAY:
                return friday_d;

            case Calendar.SATURDAY:
                return saturday_d;

            default:
                return new Day();
        }
    }

    public Calendar getStart() {
        return start;
    }

    public Calendar getEnd() {
        return end;
    }

    public ArrayList<Task> getAllTasks() {
        if (load < System.currentTimeMillis() + 300000) load();

        final ArrayList<Task> all = new ArrayList<>();

        all.addAll(sunday_d.getTasks());
        all.addAll(monday_d.getTasks());
        all.addAll(tuesday_d.getTasks());
        all.addAll(wednesday_d.getTasks());
        all.addAll(thursday_d.getTasks());
        all.addAll(friday_d.getTasks());
        all.addAll(saturday_d.getTasks());

        return all;
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
package jgappsandgames.smartreminderssave.date;

// JSON
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
    private Calendar start;
    private Calendar end;

    private Calendar sunday_c;
    private Calendar monday_c;
    private Calendar tuesday_c;
    private Calendar wednesday_c;
    private Calendar thursday_c;
    private Calendar friday_c;
    private Calendar saturday_c;

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
    }

    public Week(JSONObject data) {
        JSONCalendar jc = new JSONCalendar();

        start = jc.loadCalendar(data.optJSONObject(START));
        end = jc.loadCalendar(data.optJSONObject(END));

        sunday_c = jc.loadCalendar(data.optJSONObject(SUNDAY));
        monday_c = jc.loadCalendar(data.optJSONObject(MONDAY));
        tuesday_c = jc.loadCalendar(data.optJSONObject(TUESDAY));
        wednesday_c = jc.loadCalendar(data.optJSONObject(WEDNESDAY));
        thursday_c = jc.loadCalendar(data.optJSONObject(THURSDAY));
        friday_c = jc.loadCalendar(data.optJSONObject(FRIDAY));
        saturday_c = jc.loadCalendar(data.optJSONObject(SATURDAY));
    }

    public Week() {
        start = new GregorianCalendar(2404, 4, 0, 4, 40, 4);
        end = start;

        sunday_c = start;
        monday_c = start;
        tuesday_c = start;
        wednesday_c = start;
        thursday_c = start;
        friday_c = start;
        saturday_c = start;
    }

    // Saving Method
    public JSONObject toJSON() {
        final JSONObject data = new JSONObject();
        final JSONCalendar jc = new JSONCalendar();

        try {
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

        return data;
    }

    // Actually load the Days
    public void load() {
        load = System.currentTimeMillis();

        sunday_d = DateManagerKt.getDay(sunday_c);
        monday_d = DateManagerKt.getDay(monday_c);
        tuesday_d = DateManagerKt.getDay(tuesday_c);
        wednesday_d = DateManagerKt.getDay(wednesday_c);
        thursday_d = DateManagerKt.getDay(thursday_c);
        friday_d = DateManagerKt.getDay(friday_c);
        saturday_d = DateManagerKt.getDay(saturday_c);
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
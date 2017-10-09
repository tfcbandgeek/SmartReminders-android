package jgappsandgames.smartreminderssave.date;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jgappsandgames.smartreminderssave.tasks.Task;
import jgappsandgames.smartreminderssave.tasks.TaskManager;

/**
 * DateManager
 * Created by joshua on 9/10/17.
 * Last Edited on 10/5/17 (97)
 */
public class DateManager {
    // Data
    private static List<KeyValue> weeks;
    private static List<Task> tasks;

    // Management Methods
    public static void create() {
        if (TaskManager.tasks == null) throw new RuntimeException("TaskManager needs to be loaded before the DateManager can do anywork");

        tasks = new ArrayList<>();
        for (String t : TaskManager.tasks) {
            final Task task = new Task(t);
            if (task.getDate_due() != null) tasks.add(task);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);

        weeks = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            weeks.add(new KeyValue(i, new Week((Calendar) calendar.clone())));
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
        }

        for (Task task : tasks) {
            int i = 0;
            boolean b = true;
            if (!task.getDate_due().before(weeks.get(0).week.getStart())) {
                while (b) {
                    if (weeks.get(i).week.addTask(task)) {
                        b = false;
                    }

                    if (i > weeks.size() - 10) {
                        Calendar t = Calendar.getInstance();
                        t.add(Calendar.WEEK_OF_YEAR, weeks.size());
                        weeks.add(new KeyValue(weeks.size(), new Week(t)));
                    }

                    i++;
                }
            }
        }
    }

    // Getters
    public static int getWeekCount() {
        return weeks.size();
    }

    public static Week getWeek(int week) {
        for (KeyValue w : weeks) if (w.key == week) return w.week;
        return null;
    }

    public static Day getToday() {
        return getWeek(0).getDay(Calendar.getInstance());
    }

    public static List<Task> getDay(Calendar date_active) {
        if (date_active.before(getWeek(0).getStart())) return new ArrayList<>();

        for (KeyValue week : weeks) {
            if (week.week.getStart().before(date_active) && week.week.getEnd().after(date_active)) return week.week.getDay(date_active).getTasks();
        }

        return new ArrayList<>();
    }

    // Classes
    private static class KeyValue {
        public int key;
        public Week week;

        public KeyValue(int key, Week week) {
            this.key = key;
            this.week = week;
        }
    }
}
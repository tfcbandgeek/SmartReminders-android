package jgappsandgames.smartreminderssave.date;

import java.util.ArrayList;
import java.util.Calendar;

import jgappsandgames.smartreminderssave.tasks.Task;

/**
 * Month
 * Created by joshua on 10/12/17.
 */
public class Month {
    // Data
    private final int days_in_month;
    private final int month_starts_on;

    private final Calendar start;
    private final Calendar end;

    private final ArrayList<Day> days;

    public Month(Calendar start) {
        this.start = (Calendar) start.clone();
        days_in_month = start.getMaximum(Calendar.DAY_OF_MONTH);
        month_starts_on = start.get(Calendar.DAY_OF_WEEK);

        days = new ArrayList<>(days_in_month);
        for (int i = 0; i < days_in_month; i++) {
            days.add(new Day((Calendar) start.clone()));
            start.add(Calendar.DAY_OF_MONTH, 1);
        }
        end = days.get(days_in_month - 1).getDay();
        end.add(Calendar.DAY_OF_MONTH, 1);
    }

    // Task Management Methods
    public boolean addTask(Task task) {
        if (task.getDateDue().get(Calendar.YEAR) >= start.get(Calendar.YEAR)) {
            if (task.getDateDue().get(Calendar.DAY_OF_YEAR) >= start.get(Calendar.MONTH)) {
                if (task.getDateDue().get(Calendar.YEAR) <= end.get(Calendar.YEAR)) {
                    if (task.getDateDue().get(Calendar.MONTH) <= end.get(Calendar.MONTH)) {
                        for (Day day : days) {
                            if (day.getDay().get(Calendar.DAY_OF_MONTH) == task.getDateDue().get(Calendar.DAY_OF_MONTH)) {
                                day.addTask(task);
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean removeTask(Task task) {
        if (task.getDateDue().get(Calendar.YEAR) >= start.get(Calendar.YEAR)) {
            if (task.getDateDue().get(Calendar.DAY_OF_YEAR) >= start.get(Calendar.MONTH)) {
                if (task.getDateDue().get(Calendar.YEAR) <= end.get(Calendar.YEAR)) {
                    if (task.getDateDue().get(Calendar.MONTH) <= end.get(Calendar.MONTH)) {
                        for (Day day : days) {
                            if (day.getDay().get(Calendar.DAY_OF_MONTH) == task.getDateDue().get(Calendar.DAY_OF_MONTH)) {
                                day.removeTask(task);
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    // Getters
    public Day getDay(Calendar instance) {
        if (instance.get(Calendar.YEAR) >= start.get(Calendar.YEAR)) {
            if (instance.get(Calendar.DAY_OF_YEAR) >= start.get(Calendar.MONTH)) {
                if (instance.get(Calendar.YEAR) <= end.get(Calendar.YEAR)) {
                    if (instance.get(Calendar.MONTH) <= end.get(Calendar.MONTH)) {
                        for (Day day : days) {
                            if (day.getDay().get(Calendar.DAY_OF_MONTH) == instance.get(Calendar.DAY_OF_MONTH)) {
                                return day;
                            }
                        }
                    }
                }
            }
        }

        // Todo: Return Specialized Day Class
        return null;
    }

    public Calendar getStart() {
        return start;
    }

    public Calendar getEnd() {
        return end;
    }

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> tasks = new ArrayList<>();

        if (days == null || days.size() == 0) return tasks;
        for (Day day : days) tasks.addAll(day.getTasks());
        return tasks;
    }
}
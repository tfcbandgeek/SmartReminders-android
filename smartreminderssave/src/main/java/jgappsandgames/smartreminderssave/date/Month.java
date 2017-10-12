package jgappsandgames.smartreminderssave.date;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jgappsandgames.smartreminderssave.tasks.Task;

/**
 * Month
 * Created by joshua on 10/12/17.
 * Last Edited on 10/12/17 (112). <10.1>
 */
public class Month {
    // Data
    private int days_in_month;
    private int month_starts_on;

    private Calendar start;
    private Calendar end;

    private List<Day> days;

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
        if (task.getDate_due().get(Calendar.YEAR) >= start.get(Calendar.YEAR)) {
            if (task.getDate_due().get(Calendar.DAY_OF_YEAR) >= start.get(Calendar.MONTH)) {
                if (task.getDate_due().get(Calendar.YEAR) <= end.get(Calendar.YEAR)) {
                    if (task.getDate_due().get(Calendar.MONTH) <= end.get(Calendar.MONTH)) {
                        for (Day day : days) {
                            if (day.getDay().get(Calendar.DAY_OF_MONTH) == task.getDate_due().get(Calendar.DAY_OF_MONTH)) {
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
        if (task.getDate_due().get(Calendar.YEAR) >= start.get(Calendar.YEAR)) {
            if (task.getDate_due().get(Calendar.DAY_OF_YEAR) >= start.get(Calendar.MONTH)) {
                if (task.getDate_due().get(Calendar.YEAR) <= end.get(Calendar.YEAR)) {
                    if (task.getDate_due().get(Calendar.MONTH) <= end.get(Calendar.MONTH)) {
                        for (Day day : days) {
                            if (day.getDay().get(Calendar.DAY_OF_MONTH) == task.getDate_due().get(Calendar.DAY_OF_MONTH)) {
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

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();

        if (days == null || days.size() == 0) return tasks;
        for (Day day : days) tasks.addAll(day.getTasks());
        return tasks;
    }
}
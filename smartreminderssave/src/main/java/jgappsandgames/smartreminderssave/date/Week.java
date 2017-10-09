package jgappsandgames.smartreminderssave.date;

// Java
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

// Save
import jgappsandgames.smartreminderssave.tasks.Task;

/**
 * Week
 * Created by joshua on 9/10/17.
 * Last Edited on 10/5/17 (204).
 */
public class Week {
    // Data
    private Day sunday;
    private Day monday;
    private Day tuesday;
    private Day wednesday;
    private Day thursday;
    private Day friday;
    private Day saturday;

    public Week(Calendar start) {
        sunday = new Day((Calendar) start.clone());

        start.add(Calendar.DAY_OF_WEEK, 1);
        monday = new Day((Calendar) start.clone());

        start.add(Calendar.DAY_OF_WEEK, 1);
        tuesday = new Day((Calendar) start.clone());

        start.add(Calendar.DAY_OF_WEEK, 1);
        wednesday = new Day((Calendar) start.clone());

        start.add(Calendar.DAY_OF_WEEK, 1);
        thursday = new Day((Calendar) start.clone());

        start.add(Calendar.DAY_OF_WEEK, 1);
        friday = new Day((Calendar) start.clone());

        start.add(Calendar.DAY_OF_WEEK, 1);
        saturday = new Day((Calendar) start.clone());
    }

    public boolean addTask(Task task) {
        if (task.getDate_due().get(Calendar.YEAR) >= sunday.getDay().get(Calendar.YEAR)) {
            if (task.getDate_due().get(Calendar.DAY_OF_YEAR) >= sunday.getDay().get(Calendar.DAY_OF_YEAR)) {
                if (task.getDate_due().get(Calendar.YEAR) <= saturday.getDay().get(Calendar.YEAR)) {
                    if (task.getDate_due().get(Calendar.DAY_OF_YEAR) <= saturday.getDay().get(Calendar.DAY_OF_YEAR)) {
                        // Sunday
                        if (task.getDate_due().get(Calendar.DAY_OF_YEAR) == sunday.getDay().get(Calendar.DAY_OF_YEAR)) {
                            sunday.addTask(task);
                            return true;
                        }

                        // Monday
                        if (task.getDate_due().get(Calendar.DAY_OF_YEAR) == monday.getDay().get(Calendar.DAY_OF_YEAR)) {
                            monday.addTask(task);
                            return true;
                        }

                        // Tuesday
                        if (task.getDate_due().get(Calendar.DAY_OF_YEAR) == tuesday.getDay().get(Calendar.DAY_OF_YEAR)) {
                            tuesday.addTask(task);
                            return true;
                        }

                        // Wednesday
                        if (task.getDate_due().get(Calendar.DAY_OF_YEAR) == wednesday.getDay().get(Calendar.DAY_OF_YEAR)) {
                            wednesday.addTask(task);
                            return true;
                        }

                        // Thursday
                        if (task.getDate_due().get(Calendar.DAY_OF_YEAR) == thursday.getDay().get(Calendar.DAY_OF_YEAR)) {
                            thursday.addTask(task);
                            return true;
                        }

                        // Friday
                        if (task.getDate_due().get(Calendar.DAY_OF_YEAR) == friday.getDay().get(Calendar.DAY_OF_YEAR)) {
                            friday.addTask(task);
                            return true;
                        }

                        // Saturday
                        if (task.getDate_due().get(Calendar.DAY_OF_YEAR) == saturday.getDay().get(Calendar.DAY_OF_YEAR)) {
                            saturday.addTask(task);
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean removeTask(Task task) {
        if (task.getDate_due().get(Calendar.YEAR) >= sunday.getDay().get(Calendar.YEAR)) {
            if (task.getDate_due().get(Calendar.DAY_OF_YEAR) >= sunday.getDay().get(Calendar.DAY_OF_YEAR)) {
                if (task.getDate_due().get(Calendar.YEAR) <= saturday.getDay().get(Calendar.YEAR)) {
                    if (task.getDate_due().get(Calendar.DAY_OF_YEAR) <= saturday.getDay().get(Calendar.DAY_OF_YEAR)) {
                        // Sunday
                        if (task.getDate_due().get(Calendar.DAY_OF_YEAR) == sunday.getDay().get(Calendar.DAY_OF_YEAR)) {
                            sunday.removeTask(task);
                            return true;
                        }

                        // Monday
                        if (task.getDate_due().get(Calendar.DAY_OF_YEAR) == monday.getDay().get(Calendar.DAY_OF_YEAR)) {
                            monday.removeTask(task);
                            return true;
                        }

                        // Tuesday
                        if (task.getDate_due().get(Calendar.DAY_OF_YEAR) == tuesday.getDay().get(Calendar.DAY_OF_YEAR)) {
                            tuesday.removeTask(task);
                            return true;
                        }

                        // Wednesday
                        if (task.getDate_due().get(Calendar.DAY_OF_YEAR) == wednesday.getDay().get(Calendar.DAY_OF_YEAR)) {
                            wednesday.removeTask(task);
                            return true;
                        }

                        // Thursday
                        if (task.getDate_due().get(Calendar.DAY_OF_YEAR) == thursday.getDay().get(Calendar.DAY_OF_YEAR)) {
                            thursday.removeTask(task);
                            return true;
                        }

                        // Friday
                        if (task.getDate_due().get(Calendar.DAY_OF_YEAR) == friday.getDay().get(Calendar.DAY_OF_YEAR)) {
                            friday.removeTask(task);
                            return true;
                        }

                        // Saturday
                        if (task.getDate_due().get(Calendar.DAY_OF_YEAR) == saturday.getDay().get(Calendar.DAY_OF_YEAR)) {
                            saturday.removeTask(task);
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public Day getDay(Calendar instance) {
        switch (instance.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                return sunday;

            case Calendar.MONDAY:
                return monday;

            case Calendar.TUESDAY:
                return tuesday;

            case Calendar.WEDNESDAY:
                return wednesday;

            case Calendar.THURSDAY:
                return thursday;

            case Calendar.FRIDAY:
                return friday;

            case Calendar.SATURDAY:
                return saturday;
        }

        return null;
    }

    public Calendar getStart() {
        return sunday.getDay();
    }

    public List<Task> getAllTasks() {
        ArrayList<Task> tasks = new ArrayList<>();

        tasks.addAll(sunday.getTasks());
        tasks.addAll(monday.getTasks());
        tasks.addAll(tuesday.getTasks());
        tasks.addAll(wednesday.getTasks());
        tasks.addAll(thursday.getTasks());
        tasks.addAll(friday.getTasks());
        tasks.addAll(saturday.getTasks());

        return tasks;
    }

    public Calendar getEnd() {
        return saturday.getDay();
    }
}
package jgappsandgames.smartreminderssave.date;

// Java
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

// Save
import jgappsandgames.smartreminderssave.tasks.Task;

/**
 * Day
 * Created by joshua on 9/10/17.
 * Last Edited 10/8/17 (41).
 */
public class Day {
    private Calendar day;
    private List<Task> tasks;

    public Day(Calendar day) {
        this.day = day;

        tasks = new ArrayList<>();
    }

    public Calendar getDay() {
        return day;
    }

    public void addTask(Task task) {
        if (!tasks.contains(task)) tasks.add(task);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
    }

    public List<Task> getTasks() {
        return tasks;
    }
}
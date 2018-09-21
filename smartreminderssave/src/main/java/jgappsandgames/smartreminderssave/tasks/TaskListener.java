package jgappsandgames.smartreminderssave.tasks;

public interface TaskListener {
    void onTaskUpdate();
    void newListener(TaskListener listener);
}

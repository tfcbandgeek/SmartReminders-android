package me.jgappsandgames.openlog;

/**
 * Timer Class
 *
 * A generic Timer class for use with logging
 * (Please not that this class may not accurately show the programs performance)
 */
public class Timer {
    /**
     * The Name of the Timer
     */
    private final String name;

    /**
     * The Moment the Timer Starts
     */
    private long start;

    /**
     * The Moment the Timer Ends
     */
    private long end;

    /**
     * Initializer
     *
     * @param name The Timer's Name
     */
    public Timer(String name) {
        this.name = name;
    }

    /**
     * Start
     *
     * Called to Start the Timer
     */
    public void start() {
        if (Config.getInstance().getPrimaryWriter() != null) Config.getInstance().getPrimaryWriter().write(100, name, "Started");
        if (Config.getInstance().getSecondaryWriter() != null) Config.getInstance().getSecondaryWriter().write(100, name, "Started");
        end = 0;
        start = System.currentTimeMillis();
    }

    /**
     * Finish
     *
     * Called to finish the Timer
     * (Currently this Method does not Hold the Time, Each time the Method is Called it displays the current time from Start.
     *     This May CHANGE at any time
     */
    public void finish() {
        end = System.currentTimeMillis();
        long t = (end - start);
        if (Config.getInstance().getPrimaryWriter() != null) Config.getInstance().getPrimaryWriter().write(100, name, "Ran For " + String.valueOf(t));
        if (Config.getInstance().getSecondaryWriter() != null) Config.getInstance().getSecondaryWriter().write(100, name, "Ran For " + String.valueOf(t));
    }
}
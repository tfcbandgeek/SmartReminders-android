package me.jgappsandgames.openlog;

/**
 * Exception Class
 *
 * The Exception Class should be used to report errors within the app that do not lead to the app crashing.
 */
public class Exception {
    // ---- ---- ---- ---- ---- ---- ---- ---- ---- Exception Levels ---- ---- ---- ---- ---- ---- ---- ---- ----
    /**
     * The Expected Exception Level (Lowest)
     */
    public static final int EXPECTED = 11;

    /**
     * The Fix Exception Level
     */
    public static final int FIX = 12;

    /**
     * The Problem Exception Level (Highest)
     */
    public static final int PROBLEM = 13;

    // ---- ---- ---- ---- ---- ---- ---- ---- ---- Write Methods ---- ---- ---- ---- ---- ---- ---- ---- ----
    /**
     * Write Expected (Debug Level Only)
     *
     * @param key Identifier for the Message
     * @param data Message
     */
    public static void e(String key, String data) {
        if (Config.getInstance().isDebug()) {
            if (Config.getInstance().getPrimaryWriter() != null) Config.getInstance().getPrimaryWriter().write(EXPECTED, key, data);
            if (Config.getInstance().getSecondaryWriter() != null) Config.getInstance().getSecondaryWriter().write(EXPECTED, key, data);
        }
    }

    /**
     * Write Fix (Debug Level Only)
     *
     * @param key Identifier for the Message
     * @param data Message
     */
    public static void f(String key, String data) {
        if (Config.getInstance().isDebug()) {
            if (Config.getInstance().getPrimaryWriter() != null) Config.getInstance().getPrimaryWriter().write(FIX, key, data);
            if (Config.getInstance().getSecondaryWriter() != null) Config.getInstance().getSecondaryWriter().write(FIX, key, data);
        }
    }

    /**
     * Write Problem (Always)
     *
     * @param key Identifier for the Message
     * @param data Message
     */
    public static void p(String key, String data) {
        if (Config.getInstance().getPrimaryWriter() != null) Config.getInstance().getPrimaryWriter().write(PROBLEM, key, data);
        if (Config.getInstance().getSecondaryWriter() != null) Config.getInstance().getSecondaryWriter().write(PROBLEM, key, data);
    }
}
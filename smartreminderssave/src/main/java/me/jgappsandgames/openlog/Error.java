package me.jgappsandgames.openlog;

/**
 * Error Class
 *
 * The Error Class should be used to report when within the app that are not recoverable (The App Stops Execution Afterwards)
 */
public class Error {
    // ---- ---- ---- ---- ---- ---- ---- ---- ---- Error Levels ---- ---- ---- ---- ---- ---- ---- ---- ----
    /**
     * The Debug_Error Error Level (Lowest)
     */
    public static final int DEBUG_ERROR = 21;

    /**
     * The Error Error Level (Highest)
     */
    public static final int ERROR = 22;

    // ---- ---- ---- ---- ---- ---- ---- ---- ---- Write Methods ---- ---- ---- ---- ---- ---- ---- ---- ----

    /**
     * Write Debug (Debug Only)
     *
     * @param key Identifier for the Message
     * @param data Message
     */
    public static void d(String key, String data) {
        if (Config.getInstance().isDebug()) {
            if (Config.getInstance().getPrimaryWriter() != null) Config.getInstance().getPrimaryWriter().write(DEBUG_ERROR, key, data);
            if (Config.getInstance().getSecondaryWriter() != null) Config.getInstance().getSecondaryWriter().write(DEBUG_ERROR, key, data);
        }
    }

    /**
     * Write Error (Always)
     *
     * @param key Identifier for the Message
     * @param data Message
     */
    public static void e(String key, String data) {
        if (Config.getInstance().getPrimaryWriter() != null) Config.getInstance().getPrimaryWriter().write(ERROR, key, data);
        if (Config.getInstance().getSecondaryWriter() != null) Config.getInstance().getSecondaryWriter().write(ERROR, key, data);
    }
}
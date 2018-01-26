package me.jgappsandgames.openlog;

/**
 * Writer Interface
 *
 * Interface for Various Writer Classes to be Used with the Log, Exception, and Error Classes
 */
public interface Writer {
    /**
     * load()
     *
     * Called to load the Writer If Necessary
     */
    void load();

    /**
     * write(Int, String, String)
     *
     * Writes the incoming data
     *
     * @param code Log Level
     * @param key The Key
     * @param data The Actual problem/text
     */
    void write(int code, String key, String data);

    /**
     * clear()
     *
     * Called to Clear the Log If Necessary
     */
    void clear();
}
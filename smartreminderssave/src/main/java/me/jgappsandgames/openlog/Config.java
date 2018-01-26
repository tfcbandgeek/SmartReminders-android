package me.jgappsandgames.openlog;

// Java Imports
import java.io.File;

/**
 * Config
 *
 * Holds the Config File for Open Log
 */
public class Config {
    // ---- ---- ---- ---- ---- ---- ---- ---- ---- File Locations ---- ---- ---- ---- ---- ---- ---- ---- ----
    /**
     * Location for Saving the Master List for Logs, Exceptions, and Errors (Combined)
     *
     * DEFAULT: openlog/
     */
    private File all_file;

    /**
     * Location for Saving the Log File
     *
     * DEFAULT: openlog/
     */
    private File log_file;

    /**
     * Location for saving the Exception File
     *
     * DEFAULT: openlog/
     */
    private File except_file;

    /**
     * Location for saving the Error File
     *
     * DEFAULT: openlog/
     */
    private File error_file;

    // ---- ---- ---- ---- ---- ---- ---- ---- ---- Writers ---- ---- ---- ---- ---- ---- ---- ---- ----
    /**
     * The Primary Writer for The Library
     *
     * DEFAULT: ConsoleWriter
     */
    private Writer primary_writer;

    /**
     * The Secondary Writer for the Library
     *
     * DEFAULT: NULL
     */
    private Writer secondary_writer;

    // ---- ---- ---- ---- ---- ---- ---- ---- ---- Run Variables ---- ---- ---- ---- ---- ---- ---- ---- ----
    /**
     * Mark True if this run is a debug run
     * More Log, Exception, Error Levels are Ran
     *
     * DEFAULT: true
     */
    private boolean debug;

    /**
     * Holds the Prefix for the Libraries Writing
     *
     * DEFAULT: "OpenLog"
     */
    private String prefix;

    /**
     * Should the Date/Time Stamp be placed
     *
     * DEFAULT: false
     */
    private boolean time_stamp;

    /**
     * Holds the Lenght that the Key Object should be cut at
     *
     * DEFAULT: 10
     */
    private int key_legnth;

    // ---- ---- ---- ---- ---- ---- ---- ---- ---- Initializer ---- ---- ---- ---- ---- ---- ---- ---- ----

    /**
     * Initializer
     *
     * Sets the Save Locations, Creates the Directory (If necessary), and sets the default writer
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private Config() {
        // Set Locations
        all_file = new File("openlog");
        log_file = new File("openlog");
        except_file = new File("openlog");
        error_file = new File("openlog");

        // Create Directories if it does not exist yet
        if (!all_file.isDirectory()) all_file.mkdirs();
        if (!log_file.isDirectory()) log_file.mkdirs();
        if (!except_file.isDirectory()) except_file.mkdirs();
        if (!error_file.isDirectory()) error_file.mkdirs();

        primary_writer = ConsoleWriter.getInstance();

        debug = true;

        prefix = "OpenLog";

        time_stamp = false;

        key_legnth = 10;
    }

    // ---- ---- ---- ---- ---- ---- ---- ---- ---- Getters ---- ---- ---- ---- ---- ---- ---- ---- ---- ----

    /**
     * getAll()
     *
     * @return All File Location
     */
    public File getAll() {
        return all_file;
    }

    /**
     * getLog()
     *
     * @return Log File Location
     */
    public File getLog() {
        return log_file;
    }

    /**
     * getExcept()
     *
     * @return Except File Location
     */
    public File getExcept() {
        return except_file;
    }

    /**
     * getError()
     *
     * @return Error File Location
     */
    public File getError() {
        return error_file;
    }

    /**
     * getPrimaryWriter()
     *
     * @return The Primaray Writer or the secondary Wrier or a new Console Writer depending on there status
     */
    public Writer getPrimaryWriter() {
        if (primary_writer != null) return primary_writer;
        else if (secondary_writer != null) {
            primary_writer = secondary_writer;
            secondary_writer = null;
            return primary_writer;
        }
        else {
            primary_writer = ConsoleWriter.getInstance();
            return primary_writer;
        }
    }

    /**
     * getSecondaryWriter()
     *
     * @return The Secondary Writer
     */
    public Writer getSecondaryWriter() {
        return secondary_writer;
    }

    /**
     * isDebug()
     *
     * @return true if it is a debug run
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * getPrefix()
     *
     * @return the prefix for the App
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * getTimeStamp()
     *
     * @return true if the time stamp should be placed
     */
    public boolean getTimeStamp() {
        return time_stamp;
    }

    /**
     * getKeyLength
     *
     * @return the Length of the Key Object
     */
    public int getKeyLength() {
        return key_legnth;
    }

    // ---- ---- ---- ---- ---- ---- ---- ---- ---- Setters ---- ---- ---- ---- ---- ---- ---- ---- ----
    /**
     * setFiles(File)
     *
     * Sets A Universal File for the Logs
     * @param file The New File Location
     * @return The Config Instance, For Chaining Commands
     */
    public Config setFiles(File file) {
        if (!file.isDirectory()) file.mkdirs();

        all_file = file;
        log_file = file;
        except_file = file;
        error_file = file;
        return this;
    }

    /**
     * setAll(File)
     *
     * Sets the Combined File Location
     * @param file The New File Location
     * @return The Config Instance, For Chaining Commands
     */
    public Config setAll(File file) {
        if (!file.isDirectory()) file.mkdirs();

        all_file = file;
        return this;
    }

    /**
     * setLog(File)
     *
     * Sets the Log File Location
     * @param file The New File Location
     * @return The Config Instance, For Chaining Commands
     */
    public Config setLog(File file) {
        if (!file.isDirectory()) file.mkdirs();

        log_file = file;
        return this;
    }

    /**
     * setExcept(File)
     *
     * Sets the Exception File Location
     * @param file The New File Location
     * @return The Config Instance, For Chaining Commands
     */
    public Config setExcept(File file) {
        if (!file.isDirectory()) file.mkdirs();

        except_file = file;
        return this;
    }

    /**
     * setError(File)
     *
     * Sets the Error File Location
     * @param file The New File Location
     * @return The Config Instance, For Chaining Commands
     */
    public Config setError(File file) {
        if (!file.isDirectory()) file.mkdirs();

        error_file = file;
        return this;
    }

    /**
     * setPrimaryWriter(Writer)
     *
     * Sets the Writer for the Primary Writer
     * @param writer The New Writer
     * @return The Config Instance, For Chaining Commands
     */
    public Config setPrimaryWriter(Writer writer) {
        primary_writer = writer;
        return this;
    }

    /**
     * setSecondaryWriter(Writer)
     *
     * Sets the Writer for the Secondary Writer
     * @param writer The New Writer
     * @return The Config Instance, For Chaining Commands
     */
    public Config setSecondaryWriter(Writer writer) {
        secondary_writer = writer;
        return this;
    }

    /**
     * setDebug(Boolean)
     *
     * Called to set if this is a Debug Run
     * @param debug Debug Status
     * @return The Config Instance, For Chaining Commands
     */
    public Config setDebug(boolean debug) {
        this.debug = debug;
        return this;
    }

    /**
     * setPrefix(String)
     * @param prefix the new PreFix
     * @return this Config Instance, For chaining Commands
     */
    public Config setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    /**
     * setTimeStamp(boolean)
     * @param time_stamp the New Status
     * @return this Config Instance, For chaining commands
     */
    public Config setTimeStamp(boolean time_stamp) {
        this.time_stamp = time_stamp;
        return this;
    }

    /**
     * setKeyLegnth(int)
     * @param key_length the New Key Length
     * @return this Config Instance, For Chaining Commands
     */
    public Config setKeyLength(int key_length) {
        this.key_legnth = key_length;
        return this;
    }

    // ---- ---- ---- ---- ---- ---- ---- ---- ---- Singleton ---- ---- ---- ---- ---- ---- ---- ---- ----
    /**
     * The Config Instance
     */
    private static Config config;


    /**
     * getInstance()
     *
     * Get the Instance of the Config that is active
     * @return the Config Instance (Duh)
     */
    public static Config getInstance() {
        if (config == null) config = new Config();
        return config;
    }

    public static boolean isLoaded() {
        return config != null;
    }
}
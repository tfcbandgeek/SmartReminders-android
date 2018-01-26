package me.jgappsandgames.openlog;

// Java
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

/**
 * FileWriter Implements: Writer
 */
public class FileWriter implements Writer {
    // ---- ---- ---- ---- ---- ---- ---- ---- ---- Writers ---- ---- ---- ---- ---- ---- ---- ---- ----
    /**
     * The BufferedWriter Instance for the All Writer
     */
    private BufferedWriter all;

    /**
     * The BufferedWriter Instance for the Log Writer
     */
    private BufferedWriter log;

    /**
     * The BufferedWriter Instance for the Exception Writer
     */
    private BufferedWriter except;

    /**
     * The BufferedWriter Instance for the Error Writer
     */
    private BufferedWriter error;

    // ---- ---- ---- ---- ---- ---- ---- ---- ---- Visible Methods ---- ---- ---- ---- ---- ---- ---- ---- ----

    /**
     * load()
     *
     * Called to Load All the Writers
     */
    @Override
    public void load() {
        try {
            all = new BufferedWriter(new java.io.FileWriter(new File(Config.getInstance().getAll(), getDayString() + ".all")));
            log = new BufferedWriter(new java.io.FileWriter(new File(Config.getInstance().getLog(), getDayString() + ".log")));
            except = new BufferedWriter(new java.io.FileWriter(new File(Config.getInstance().getExcept(), getDayString() + ".except")));
            error = new BufferedWriter(new java.io.FileWriter(new File(Config.getInstance().getError(), getDayString() + ".error")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * writer(Int, String, String)
     *
     * Cakked to Write the Data to File(s)
     *
     * @param code Log Level
     * @param key The Key
     * @param data The Actual problem/text
     */
    @Override
    public void write(int code, String key, String data) {
        StringBuilder text = new StringBuilder();

        // Add The Prefix
        text.append("[").append(Config.getInstance().getPrefix()).append("] ");

        // Add The Date
        if (Config.getInstance().getTimeStamp()) text.append("<").append(Calendar.getInstance().getTime().toString()).append(Calendar.getInstance().get(Calendar.MILLISECOND)).append("> ");

        // Add the Code
        switch (code) {
            // Log IInformation
            case Log.VERBOSE:
                text.append("VERBOSE:  ");
                break;
            case Log.DEBUG:
                text.append("DEBUG:    ");
                break;
            case Log.TRACK:
                text.append("TRACK:    ");
                break;
            case Log.INFO:
                text.append("INFO:     ");
                break;
            case Log.WARN:
                text.append("WARN:     ");
                break;

            // Exception Information
            case Exception.EXPECTED:
                text.append("Expected: ");
                break;
            case Exception.FIX:
                text.append("Fix:      ");
                break;
            case Exception.PROBLEM:
                text.append("Problem:  ");
                break;

            // Error Information
            case Error.DEBUG_ERROR:
                text.append("D_Error:  ");
                break;

            case Error.ERROR:
                text.append("Error:    ");
                break;

            // Default
            default:
                text.append("NA:       ");
                break;
        }

        // Add the Key
        key = key + "                    ";
        key = key.substring(0, Config.getInstance().getKeyLength() - 1);
        key = "[" + key + "] ";
        text.append(key);

        // Add The Data
        text.append(data);

        try {
            if (code <= 10 ) {
                log.append(text);
                log.append(System.lineSeparator());
                log.flush();
            } else if (code <= 20) {
                except.append(text);
                except.append(System.lineSeparator());
                except.flush();
            } else if (code <= 30) {
                error.append(text);
                error.append(System.lineSeparator());
                error.flush();
            }

            all.append(text);
            all.append(System.lineSeparator());
            all.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * clear()
     *
     * Called to Clear the File(s)
     */
    @Override
    public void clear() {
        try {
            log.write("");
            log.flush();

            except.write("");
            except.flush();

            error.write("");
            error.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ---- ---- ---- ---- ---- ---- ---- ---- ---- Helper Methods ---- ---- ---- ---- ---- ---- ---- ---- ----
    /**
     * getDayString()
     *
     * Get the Day String for Filenames
     */
    private String getDayString() {
        return String.valueOf(Calendar.getInstance().get(Calendar.YEAR)) + "_" +
                String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) + "_" +
                String.valueOf(Calendar.getInstance().get(Calendar.HOUR)) + "_" +
                String.valueOf(Calendar.getInstance().get(Calendar.MINUTE));
    }

    // ---- ---- ---- ---- ---- ---- ---- ---- ---- Singleton Methods ---- ---- ---- ---- ---- ---- ---- ---- ----
    /**
     * The FileWriter Instance
     */
    private static FileWriter fileWriter;

    /**
     * getInstance()
     *
     * Called to get the FileWriter Instance
     * @return The Filewriter Instance
     */
    public static FileWriter getInstance() {
        if (fileWriter == null) fileWriter = new FileWriter();
        fileWriter.load();
        return fileWriter;
    }
}
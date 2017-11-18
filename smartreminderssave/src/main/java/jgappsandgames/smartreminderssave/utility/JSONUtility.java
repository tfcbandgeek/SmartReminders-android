package jgappsandgames.smartreminderssave.utility;

// Java
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

// JSON
import org.json.JSONException;
import org.json.JSONObject;

// Android
import android.os.Build;

/**
 * JSONUtility
 * Created by joshua on 10/4/17.
 */
public class JSONUtility {
    // JSON Calendar Constants
    private static final String ACTIVE = "active";
    private static final String DATE = "date";

    // Called to Load JSON From File
    public static JSONObject loadJSON(File file) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder builder = new StringBuilder();

            while (true) {
                final String t = reader.readLine();

                if (t == null) break;
                else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) builder.append(t).append(System.lineSeparator());
                    else builder.append(t).append(System.getProperty("line.separator"));
                }
            }
            return new JSONObject(builder.toString());
        } catch (NullPointerException | JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    // Called to Save JSON to File
    public static void saveJSONObject(File file, JSONObject data) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            writer.write(data.toString());
            writer.flush();
            writer.close();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    // Called to Create a Calendar from a JSONObject
    public static Calendar loadCalendar(JSONObject data) {
        if (data.optBoolean(ACTIVE, false)) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(data.optLong(DATE, 0));
            return calendar;
        }

        return null;
    }

    // Called to Create a JSONObject from A Calendar
    public static JSONObject saveCalendar(Calendar calendar) {
        try {
            JSONObject data = new JSONObject();

            if (calendar == null) {
                data.put(ACTIVE, false);
            } else {
                data.put(ACTIVE, true);
                data.put(DATE, calendar.getTimeInMillis());
            }
            return data;
        } catch (JSONException j) {
            j.printStackTrace();
        }

        return new JSONObject();
    }
}
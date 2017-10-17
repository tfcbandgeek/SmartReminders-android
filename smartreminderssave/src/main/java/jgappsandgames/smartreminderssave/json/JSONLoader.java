package jgappsandgames.smartreminderssave.json;

// Java
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

// JSON
import org.json.JSONException;
import org.json.JSONObject;

// Android
import android.os.Build;

/**
 * JSONLoader
 * Created by joshua on 10/4/17.
 * Last Edited on 10/12/17 (58).
 * Edited on 10/9/17 (58). <Support SDK16-26>
 */
public class JSONLoader {
    public static JSONObject loadJSON(File file) {
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
        } catch (IOException | NullPointerException | JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

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
}
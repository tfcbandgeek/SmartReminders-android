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

/**
 * JSONLoader
 * Created by joshua on 10/4/17.
 * Last Edited on 10/8/17 (52).
 * Edited on 10/4/17 (49).
 */
public class JSONLoader {
    public static JSONObject loadJSON(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder builder = new StringBuilder();

            while (true) {
                final String t = reader.readLine();

                if (t == null) break;
                else builder.append(t).append(System.lineSeparator());
            }

            return new JSONObject(builder.toString());
        } catch (IOException | NullPointerException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void saveJSONObject(File file, JSONObject jo) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            writer.write(jo.toString(4));
            writer.flush();
            writer.close();
        } catch (IOException | NullPointerException | JSONException e) {
            e.printStackTrace();
        }
    }
}
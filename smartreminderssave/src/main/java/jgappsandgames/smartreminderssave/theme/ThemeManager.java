package jgappsandgames.smartreminderssave.theme;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import jgappsandgames.smartreminderssave.json.JSONLoader;
import jgappsandgames.smartreminderssave.utility.API;
import jgappsandgames.smartreminderssave.utility.FileUtility;

/**
 * ThemeManager
 * Created by joshua on 10/16/17.
 */
public class ThemeManager {
    // File Constants
    private static final String FILENAME = "thememanager.srj";

    // JSON Constants
    private static final String VERSION = "version";
    private static final String COLOR = "color";
    private static final String LIGHT = "light";

    // Color Constants
    public static final int BLUE = 1;
    public static final int GREEN = 2;
    public static final int RED = 3;
    public static final int PURPLE = 4;

    // Light Constants
    public static final int DARK = 1;
    public static final int GREY = 2;
    public static final int WHITE = 3;

    // Data
    public static int version = 0;
    public static int color = 0;
    public static int light = 0;

    // Management Methods
    public static void load() {
        JSONObject data = JSONLoader.loadJSON(new File(FileUtility.getInternalFileDirectory(), FILENAME));

        version = data.optInt(VERSION, API.RELEASE);
        color = data.optInt(COLOR, 1);
        light = data.optInt(LIGHT, 1);
    }

    public static void create() {
        version = API.RELEASE;
        color = 1;
        light = 1;
    }

    public static void save() {
        JSONObject data = new JSONObject();

        try {
            data.put(VERSION, API.RELEASE);
            data.put(COLOR, color);
            data.put(LIGHT, light);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONLoader.saveJSONObject(new File(FileUtility.getInternalFileDirectory(), FILENAME), data);
    }
}
package jgappsandgames.smartreminderssave.tags;

// Java
import java.io.File;
import java.util.ArrayList;
import java.util.List;

// JSON
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// Program
import jgappsandgames.smartreminderssave.json.JSONLoader;
import jgappsandgames.smartreminderssave.utility.API;
import jgappsandgames.smartreminderssave.utility.FileUtility;

/**
 * TagManager
 * Created by joshua on 8/26/17.
 * Last Edited on 10/5/17 (116)
 *
 * Currently on API: 10
 **/
public class TagManager {
	// Constants
	private static final String FILENAME = "tagmanager.srj";
	
	public static final String VERSION = "version";
	public static final String TAGS = "tags";
	
	// Data
	public static int version;
	public static List<String> tags;
	
	// Management Methods
	public static void create() {
		version = API.RELEASE;
        tags = new ArrayList<>();
	}

	public static void load() {
        JSONObject data = JSONLoader.loadJSON(new File(FileUtility.getApplicationDataDirectory(), FILENAME));

        if (data == null) {
            create();
            return;
        }

        version = data.optInt(VERSION, 10);
        JSONArray t = data.optJSONArray(TAGS);
        tags = new ArrayList<>(t.length());
        for (int i = 0; i < t.length(); i++) tags.add(t.optString(i));
    }

    public static void save() {
        JSONObject data = new JSONObject();
        JSONArray t = new JSONArray();

        try {
            for (String tag : tags) t.put(tag);

            data.put(VERSION, version);
            data.put(TAGS, t);
        } catch (JSONException j) {
            j.printStackTrace();
        }

        JSONLoader.saveJSONObject(new File(FileUtility.getApplicationDataDirectory(), FILENAME), data);
    }
}
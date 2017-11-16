package jgappsandgames.smartreminderssave.tags;

// Java
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// JSON
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// Program
import jgappsandgames.smartreminderssave.utility.JSONUtility;
import jgappsandgames.smartreminderssave.utility.API;
import jgappsandgames.smartreminderssave.utility.FileUtility;

/**
 * TagManager
 * Created by joshua on 8/26/17.
 * Last Edited on 10/12/17 (85).
 * Edited on 10/5/17 (116)
 *
 * Currently on API: 10
 **/
public class TagManager {
	// Constants
	private static final String FILENAME = "tagmanager.srj";
	
	private static final String VERSION = "version";
	private static final String TAGS = "tags";
	
	// Data
	private static int version;
	public static List<String> tags;
	
	// Management Methods
	public static void create() {
		version = API.RELEASE;
        tags = new ArrayList<>();
	}

	public static void load() {
	    try {
            loadJSON(JSONUtility.loadJSON(new File(FileUtility.getApplicationDataDirectory(), FILENAME)));
        } catch (IOException i) {
	        i.printStackTrace();
	        create();
	        save();
        }
    }

    public static void save() {
        JSONUtility.saveJSONObject(new File(FileUtility.getApplicationDataDirectory(), FILENAME), toJSON());
    }

    // JSON Management Methods
    public static void loadJSON(JSONObject data) {
        if (data == null) {
            create();
            return;
        }

        version = data.optInt(VERSION, API.RELEASE);
        JSONArray t = data.optJSONArray(TAGS);
        tags = new ArrayList<>(t.length());
        for (int i = 0; i < t.length(); i++) tags.add(t.optString(i));
    }

    public static JSONObject toJSON() {
        JSONObject data = new JSONObject();
        JSONArray t = new JSONArray();

        try {
            for (String tag : tags) t.put(tag);

            data.put(VERSION, version);
            data.put(TAGS, t);
        } catch (JSONException j) {
            j.printStackTrace();
        }

        return data;
    }

    public static void addMissingTags(JSONObject data) {
        JSONArray t = data.optJSONArray(TAGS);
        tags = new ArrayList<>(t.length());
        for (int i = 0; i < t.length(); i++) if (!tags.contains(t.optString(i))) tags.add(t.optString(i));
    }
}
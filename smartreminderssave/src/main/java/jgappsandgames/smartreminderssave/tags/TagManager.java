package jgappsandgames.smartreminderssave.tags;

// Java
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
 *
 * Currently on API: 10
 **/
public class TagManager {
	// Constants
	private static final String FILENAME = "tagmanager.srj";
	
	private static final String VERSION = "version";
	private static final String META = "meta";

	private static final String TAGS = "tags";
	
	// Data
	private static int version;
	public static JSONObject meta;

	public static ArrayList<String> tags;
	
	// Management Methods
	public static void create() {
		version = API.MANAGEMENT;
        tags = new ArrayList<>();

        // VERSION 11
        meta = new JSONObject();
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

        // Version 11
        if (version >= API.MANAGEMENT) {
            meta = data.optJSONObject(META);
            if (meta == null) meta = new JSONObject();
        } else {
            meta = new JSONObject();
        }
    }

    public static JSONObject toJSON() {
        JSONObject data = new JSONObject();
        JSONArray t = new JSONArray();

        try {
            for (String tag : tags) t.put(tag);

            data.put(VERSION, API.MANAGEMENT);
            data.put(TAGS, t);
            data.put(META, meta);
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
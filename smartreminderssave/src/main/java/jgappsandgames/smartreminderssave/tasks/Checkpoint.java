package jgappsandgames.smartreminderssave.tasks;

// JSON
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Checkpoint
 * Created by joshua on 10/4/17.
 * Last Edited on 10/5/17 (53).
 */
public class Checkpoint {
    private static final String ID = "position";
    private static final String STATUS = "status";
    private static final String TEXT = "text";

    public int id;
    public boolean status;
    public String text;

    public Checkpoint(int id, String text) {
        this.id = id;
        this.text = text;
        status = false;
    }

    public Checkpoint(JSONObject object) {
        if (object == null) return;

        id = object.optInt(ID, 0);
        status = object.optBoolean(STATUS, false);
        text = object.optString(TEXT, "");
    }

    public JSONObject toJSON() {
        try {
            JSONObject data = new JSONObject();
            data.put(ID, id);
            data.put(STATUS, status);
            data.put(TEXT, text);

            return data;
        } catch (JSONException j) {
            j.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return toJSON().toString();
    }
}
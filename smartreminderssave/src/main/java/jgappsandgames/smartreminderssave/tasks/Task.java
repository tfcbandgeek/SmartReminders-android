package jgappsandgames.smartreminderssave.tasks;

// Java
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

// JSON
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// Save
import jgappsandgames.smartreminderssave.utility.JSONUtility;
import jgappsandgames.smartreminderssave.utility.API;

/**
 * Task
 * Created by joshua on 8/24/17.
 *
 * Current API: 10
 */
public class Task {
    // Save Constants
    private static final String PARENT = "parent";
    private static final String VERSION = "version";
    private static final String META = "meta";
    private static final String TYPE = "type";
    private static final String TASK_ID = "id";

    private static final String CAL_CREATE = "cal_a";
    private static final String CAL_DUE = "cal_b";
    private static final String CAL_UPDATE = "cal_c";
    private static final String CAL_ARCHIVED = "cal_d";
    private static final String CAL_DELETED = "cal_e";

    private static final String TITLE = "title";
    private static final String NOTE = "note";
    private static final String TAGS = "tags";
    private static final String CHILDREN = "children";
    private static final String CHECKPOINTS = "checkpoints";
    private static final String STATUS = "status";
    private static final String PRIORITY = "priority";

    private static final String COMPLETED_ON_TIME = "completed_on_time";
    private static final String COMPLETED_LATE = "completed_late";

    // Type Constants
    public static final int TYPE_NONE = 0;
    public static final int TYPE_FLDR = 1;
    public static final int TYPE_TASK = 2;

    // Checkpoint Constants
	public static final String CHECKPOINT_POSITION = "position";
	public static final String CHECKPOINT_STATUS = "status";
	public static final String CHECKPOINT_TEXT = "text";

    // Status Constants
    public static final int STATUS_DONE = 10;

    // Data
    private final String filename;
    private String parent;
    private int version;
    private JSONObject meta;
    private int type;
    private long task_id;

    private Calendar date_create;
    private Calendar date_due;
    private Calendar date_updated;
    private Calendar date_deleted;
    private Calendar date_archived;

    private String title;
    private String note;
    private ArrayList<String> tags;
    private ArrayList<String> children;
    private ArrayList<Checkpoint> checkpoints;
    private int status;
    private int priority;

    private boolean complete_on_time;
    private boolean complete_late;

    // Calendar
    private final JSONCalendar j_cal = new JSONCalendar();

    // Initializers
    public Task(String parent, int type) {
        Calendar calendar = Calendar.getInstance();

        filename = String.valueOf(calendar.getTimeInMillis()) + ".srj";

        this.parent = parent;
        version = API.MANAGEMENT;
        this.type = type;
        task_id = Calendar.getInstance().getTimeInMillis();

        date_create = (Calendar) calendar.clone();
        date_due = null;
        date_updated = (Calendar) calendar.clone();
        date_archived = null;
        date_deleted = null;

        title = "";
        note = "";
        tags = new ArrayList<>();
        children = new ArrayList<>();
        checkpoints = new ArrayList<>();
        status = 0;
        priority = 20;

        complete_on_time = false;
        complete_late = false;
    }

    public Task(String filename) {
        this.filename = filename;
        try {
            loadJSON(JSONUtility.loadJSON(new File(FileUtility.getApplicationDataDirectory(), filename)));
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    // Management Methods
    public void save() {
        JSONUtility.saveJSONObject(new File(FileUtility.getApplicationDataDirectory(), filename), toJSON());
    }

    public void delete() {
        File file = new File(FileUtility.getApplicationDataDirectory(), filename);
        file.delete();
    }

    // JSON Management Methods
    public void loadJSON(JSONObject data) {
        if (data == null) {
            title = "Null Task Load Error";
            note = "Error occurs when there is no Task to load";
            return;
        }

        version = data.optInt(VERSION, API.RELEASE);
        parent = data.optString(PARENT, "home");
        type = data.optInt(TYPE, TYPE_NONE);
        task_id = data.optLong(TASK_ID, Calendar.getInstance().getTimeInMillis());

        date_create = j_cal.loadCalendar(data.optJSONObject(CAL_CREATE));
        date_due = j_cal.loadCalendar(data.optJSONObject(CAL_DUE));
        date_updated = j_cal.loadCalendar(data.optJSONObject(CAL_UPDATE));
        date_archived = j_cal.loadCalendar(data.optJSONObject(CAL_ARCHIVED));
        date_deleted = j_cal.loadCalendar(data.optJSONObject(CAL_DELETED));

        title = data.optString(TITLE, "");
        note = data.optString(NOTE, "");
        status = data.optInt(STATUS, 0);
        priority = data.optInt(PRIORITY, 20);
        complete_on_time = data.optBoolean(COMPLETED_ON_TIME, false);
        complete_late = data.optBoolean(COMPLETED_LATE, false);

        JSONArray t = data.optJSONArray(TAGS);
        JSONArray c = data.optJSONArray(CHILDREN);
        JSONArray p = data.optJSONArray(CHECKPOINTS);

        tags = new ArrayList<>();
        children = new ArrayList<>();
        checkpoints = new ArrayList<>();

        if (t != null && t.length() != 0) for (int i = 0; i < t.length(); i++) tags.add(t.optString(i));
        if (c != null && c.length() != 0) for (int i = 0; i < c.length(); i++) children.add(c.optString(i));
        if (p != null && p.length() != 0) for (int i = 0; i < p.length(); i++) checkpoints.add(new Checkpoint(p.optJSONObject(i)));

        // Version 11
        if (version >= API.MANAGEMENT) {
            meta = data.optJSONObject(META);
            if (meta == null) meta = new JSONObject();
        } else {
            meta = new JSONObject();
        }
    }

    public JSONObject toJSON() {
        JSONObject data = new JSONObject();

        try {
            data.put(PARENT, parent);
            data.put(VERSION, API.MANAGEMENT);
            data.put(TYPE, type);
            data.put(TASK_ID, task_id);

            data.put(CAL_CREATE, j_cal.saveCalendar(date_create));
            data.put(CAL_DUE, j_cal.saveCalendar(date_due));
            data.put(CAL_UPDATE, j_cal.saveCalendar(date_updated));
            data.put(CAL_ARCHIVED, j_cal.saveCalendar(date_archived));
            data.put(CAL_DELETED, j_cal.saveCalendar(date_deleted));

            data.put(TITLE, title);
            data.put(NOTE, note);
            data.put(STATUS, status);
            data.put(PRIORITY, priority);
            data.put(COMPLETED_ON_TIME, complete_on_time);
            data.put(COMPLETED_LATE, complete_late);

            JSONArray t = new JSONArray();
            JSONArray c = new JSONArray();
            JSONArray p = new JSONArray();

            if (tags != null && tags.size() != 0) for (String tag : tags) t.put(tag);
            if (children != null && children.size() != 0) for (String child : children) c.put(child);
            if (checkpoints != null && checkpoints.size() != 0) for (Checkpoint checkpoint : checkpoints) p.put(checkpoint.toJSON());

            data.put(TAGS, t);
            data.put(CHILDREN, c);
            data.put(CHECKPOINTS, p);

            // Version 11
            data.put(META, meta);

        } catch (JSONException j) {
            j.printStackTrace();
        } catch (NullPointerException n) {
            n.printStackTrace();
            //throw new RuntimeException("Fix Me");
        }

        return data;
    }

    public void updateTask(JSONObject data) {
        if (j_cal.loadCalendar(data.optJSONObject(CAL_UPDATE)).after(date_updated)) loadJSON(data);
    }

    // Getters
    public String getFilename() {
        return filename;
    }

    public String getParent() {
        return parent;
    }

    public int getType() {
        return type;
    }

    public long getID() {
        return task_id;
    }

    public Calendar getDateCreated() {
        return date_create;
    }

    @Deprecated
    public Calendar getDate_due() {
        return date_due;
    }

    public Calendar getDateDue() {
        return date_due;
    }

    public String getDateDueString() {
        if (date_due == null) return "No Date";
        return String.valueOf(date_due.get(Calendar.MONTH) + 1) + "/" +
                String.valueOf(date_due.get(Calendar.DAY_OF_MONTH)) + "/" +
                String.valueOf(date_due.get(Calendar.YEAR));
    }

    public Calendar getDateUpdated() {
        return date_updated;
    }

    public Calendar getDateArchived() {
        return date_archived;
    }

    @Deprecated
    public Calendar getDate_deleted() {
        return date_deleted;
    }

    public Calendar getDateDeleted() {
        return date_deleted;
    }

    public String getTitle() {
        return title;
    }

    public String getNote() {
        return note;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public String getTagString() {
        if (tags == null || tags.size() == 0) return "No Tags Selected";

        StringBuilder builder = new StringBuilder();
        for (String tag : tags) builder.append(tag).append(", ");
        if (builder.length() >= 2) builder.setLength(builder.length() - 2);
        return builder.toString();
    }

    public ArrayList<String> getChildren() {
        return children;
    }

    public ArrayList<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    public int getStatus() {
        return status;
    }

    public boolean isCompleted() {
        return status == STATUS_DONE;
    }

    public String getStatusString() {
        if (isCompleted()) return "Completed";
        return "Incomplete";
    }

    public int getPriority() {
        return priority;
    }

    public boolean onTime() {
        return complete_on_time;
    }

    public boolean late() {
        return complete_late;
    }

    // Setters
    public Task setDateDue(Calendar calendar) {
        if (calendar == null) date_due = null;
        else date_due = (Calendar) calendar.clone();
        date_updated = Calendar.getInstance();
        return this;
    }

    public Task setTitle(String title) {
        this.title = title;
        date_updated = Calendar.getInstance();
        return this;
    }

    public Task setNote(String note) {
        this.note = note;
        date_updated = Calendar.getInstance();
        return this;
    }

    public Task setTags(ArrayList<String> tags) {
        this.tags = tags;
        date_updated = Calendar.getInstance();
        return this;
    }

    public Task setChildren(ArrayList<String> children) {
        this.children = children;
        date_updated = Calendar.getInstance();
        return this;
    }

    public Task setCheckpoints(ArrayList<Checkpoint> checkpoints) {
        this.checkpoints = checkpoints;
        date_updated = Calendar.getInstance();
        return this;
    }

    public Task setPriority(int priority) {
        this.priority = priority;
        date_updated = Calendar.getInstance();
        return this;
    }

    public Task markComplete(boolean mark) {
        if (mark) {
            status = STATUS_DONE;

            if (date_due == null) complete_on_time = true;
            else if (date_due.before(Calendar.getInstance())) complete_late = true;
            else complete_on_time = true;
        } else {
            status = 0;

            complete_late = false;
            complete_on_time = false;
        }

        date_updated = Calendar.getInstance();
        return this;
    }

    public Task markArchived() {
        date_archived = Calendar.getInstance();
        date_updated = Calendar.getInstance();
        return this;
    }

    public Task markDeleted() {
        date_deleted = Calendar.getInstance();
        date_updated = Calendar.getInstance();
        return this;
    }

    // Manipulaters
    public Task addTag(String tag) {
        if (!tags.contains(tag)) {
            tags.add(tag);
            date_updated = Calendar.getInstance();
            return this;
        }

        return this;
    }

    public Task removeTag(String tag) {
        if (tags.contains(tag)) {
            tags.remove(tag);
            date_updated = Calendar.getInstance();
            return this;
        }

        return this;
    }

    public Task addChild(String child) {
        if (!children.contains(child)) {
            children.add(child);
            date_updated = Calendar.getInstance();
            return this;
        }

        return this;
    }

    public Task removeChild(String child) {
        if (children.contains(child)) {
            children.remove(child);
            date_updated = Calendar.getInstance();
            return this;
        }

        return this;
    }

    public Task addCheckpoint(Checkpoint checkpoint) {
        for (Checkpoint c : checkpoints) if (c.id == checkpoint.id) return this;

        checkpoints.add(checkpoint);
        date_updated = Calendar.getInstance();
        return this;
    }

    public Task editCheckpoint(Checkpoint checkpoint) {
        for (Checkpoint c : checkpoints) {
            if (c.id == checkpoint.id) {
                c.status = checkpoint.status;
                c.text = checkpoint.text;
                date_updated = Calendar.getInstance();
                return this;
            }
        }

        return this;
    }

    public Task removeCheckpoint(Checkpoint checkpoint) {
        for (Checkpoint c : checkpoints) {
            if (c.id == checkpoint.id) {
                checkpoints.remove(c);
                date_updated = Calendar.getInstance();
                return this;
            }
        }

        return this;
    }

    private class JSONCalendar {
        private static final String ACTIVE = "active";
        private static final String DATE = "date";

        public Calendar loadCalendar(JSONObject data) {
            if (data.optBoolean(ACTIVE, false)) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(data.optLong(DATE, 0));
                return calendar;
            }

            return null;
        }

        public JSONObject saveCalendar(Calendar calendar) {
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
}
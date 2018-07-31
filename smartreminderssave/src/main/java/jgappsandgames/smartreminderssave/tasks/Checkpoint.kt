package jgappsandgames.smartreminderssave.tasks

// JSON
import org.json.JSONException
import org.json.JSONObject

/**
 * Checkpoint
 * Created by joshua on 12/12/2017.
 */
// TODO: Move into the Task File
class Checkpoint(var id: Int, var text: String, var status: Boolean) {
    companion object {
        // Constants -------------------------------------------------------------------------------
        private const val ID = "position"
        private const val STATUS = "status"
        private const val TEXT = "text"
    }

    // Constructors --------------------------------------------------------------------------------
    constructor(c_id: Int, c_text: String): this(c_id, c_text, false)
    constructor(data: JSONObject): this(data.optInt(ID, 0), data.optString(TEXT, ""), data.optBoolean(STATUS, false))

    // Json Methods --------------------------------------------------------------------------------
    fun toJSON(): JSONObject? {
        return try {
            val data = JSONObject()
            data.put(ID, id)
            data.put(STATUS, status)
            data.put(TEXT, text)

            data
        } catch (j: JSONException) {
            j.printStackTrace()
            null
        }

    }

    // String Method -------------------------------------------------------------------------------
    override fun toString(): String {
        return toJSON()!!.toString()
    }
}
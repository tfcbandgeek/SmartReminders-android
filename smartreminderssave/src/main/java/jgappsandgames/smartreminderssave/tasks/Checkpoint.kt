package jgappsandgames.smartreminderssave.tasks

// JSON
import org.json.JSONException
import org.json.JSONObject

/**
 * Checkpoint
 * Created by joshua on 12/12/2017.
 */
class Checkpoint(i_id: Int, i_text: String, i_status: Boolean) {
    companion object {
        // Constants -------------------------------------------------------------------------------
        private val ID = "position"
        private val STATUS = "status"
        private val TEXT = "text"
    }

    // Data ----------------------------------------------------------------------------------------
    var id: Int = 0
    var status: Boolean = false
    var text: String

    // Constructors --------------------------------------------------------------------------------
    init {
        id = i_id
        text = i_text
        status = i_status
    }

    constructor(): this(0, "", false)
    constructor(c_id: Int): this(c_id, "", false)
    constructor(c_id: Int, c_text: String): this(c_id, c_text, false)
    constructor(data: JSONObject): this(data.optInt(ID, 0), data.optString(TEXT, ""), data.optBoolean(STATUS, false))

    // Json Methods --------------------------------------------------------------------------------
    fun toJSON(): JSONObject? {
        try {
            val data = JSONObject()
            data.put(ID, id)
            data.put(STATUS, status)
            data.put(TEXT, text)

            return data
        } catch (j: JSONException) {
            j.printStackTrace()
            return null
        }

    }

    // String Method -------------------------------------------------------------------------------
    override fun toString(): String {
        return toJSON()!!.toString()
    }
}
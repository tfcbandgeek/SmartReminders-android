package jgappsandgames.smartreminderssave.tasks

import org.json.JSONException
import org.json.JSONObject

/**
 * Checkpoint
 * Created by joshua on 12/1/2017.
 */
class Checkpoint(i_id: Int, i_text: String) {
    private val ID = "position"
    private val STATUS = "status"
    private val TEXT = "text"

    @JvmField
    var id: Int = i_id
    @JvmField
    var status: Boolean = false
    @JvmField
    var text: String = i_text

    constructor(data: JSONObject?) : this(0, "") {
        if (data == null) return

        id = data.optInt(ID, 0)
        status = data.optBoolean(STATUS, false)
        text = data.optString(TEXT, "")
    }

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

    override fun toString(): String {
        return toJSON()!!.toString()
    }
}
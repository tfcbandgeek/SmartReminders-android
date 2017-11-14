package jgappsandgames.smartreminderssave.tasks

import org.json.JSONException
import org.json.JSONObject

/**
 * Checkpoint
 * Created by joshu on 11/13/2017.
 *
 * API: 11
 */

class Checkpoint {
    var id: Int = 0
    var status: Boolean = false
    var text: String = ""

    constructor(id: Int) {
        this.id = id
        status = false
    }

    constructor(id: Int, text: String) {
        this.id = id
        this.text = text
        status = false
    }

    constructor(`object`: JSONObject?) {
        if (`object` == null) return

        id = `object`.optInt(ID, 0)
        status = `object`.optBoolean(STATUS, false)
        text = `object`.optString(TEXT, "")
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

    companion object {
        private val ID = "position"
        private val STATUS = "status"
        private val TEXT = "text"
    }
}
package jgappsandgames.smartreminderssave.utility

import org.json.JSONObject

class Money(var store: String, var dollars: Int, var coins: Int) {
    private fun toJSON(): JSONObject {
        throw NotImplementedError()
    }
}
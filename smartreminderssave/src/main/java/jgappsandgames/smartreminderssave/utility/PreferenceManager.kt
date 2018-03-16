package jgappsandgames.smartreminderssave.utility

import android.content.Context
import android.content.SharedPreferences

/**
 * PreferenceManager
 * Created by joshua on 1/22/2018.
 */
class PreferenceManager {
    companion object {
        // Data
        private var preferences: SharedPreferences? = null
        private var editor: SharedPreferences.Editor? = null

        @JvmStatic
        fun initialize(context: Context) {
            preferences = context.getSharedPreferences("smartreminders", Context.MODE_PRIVATE)
        }

        @JvmStatic
        fun load() {
            if (preferences == null) throw RuntimeException("SharedPrefernces Should Be Initialized First")

            if (editor == null) editor = preferences!!.edit()
        }

        @JvmStatic
        fun put(key: String, data: String) {
            if (editor == null) load()

            editor!!.putString(key, data)
        }

        @JvmStatic
        fun delete(key: String) {
            if (editor == null) load()

            editor!!.remove(key)
        }

        @JvmStatic
        fun save() {
            if (editor != null) editor!!.commit()
        }
    }
}
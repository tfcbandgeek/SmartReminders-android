package jgappsandgames.smartreminderssave.planner

import jgappsandgames.smartreminderssave.utility.FileUtility
import jgappsandgames.smartreminderssave.utility.JSONUtility
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

/**
 * PlannerManager
 * Created by joshua on 2/24/2018.
 */
class PlannerManager {
    companion object {
        // Constants -------------------------------------------------------------------------------
        private const val FILENAME = "planner.srj"

        private const val VERSION = "version"
        private const val META = "b"
        private const val DAYS = "c"

        // Data ------------------------------------------------------------------------------------
        private var version: Int = 12
        var meta = JSONObject()
        private var days: ArrayList<String> = ArrayList()
        private var days_loaded: ArrayList<Day> = ArrayList()

        // Management Methods ----------------------------------------------------------------------
        fun load() {
            val file = File(FileUtility.getApplicationDataDirectory(), FILENAME)

            // Create
            if (!file.isFile) {
                version = 12
                meta = JSONObject()
                days = ArrayList()
                days_loaded = ArrayList()
            }

            // Load
            else {
                val data = JSONUtility.loadJSON(file)
                val d = data.optJSONArray(DAYS)

                version = data.optInt(VERSION, 12)
                meta = data.optJSONObject(META)
                days = ArrayList()
                days_loaded = ArrayList()

                for (i in 0 until d.length()) days.add(d.optString(i))
            }
        }

        fun save() {
            val data = JSONObject()
            val d = JSONArray()

            data.put(VERSION, 12)
            data.put(META, meta)

            for (day in days) d.put(day)
            data.put(DAYS, d)
        }

        // Day Methods -----------------------------------------------------------------------------
        fun getDay(day: Calendar): Day {
            for (d in days_loaded) {
                if (d.getDate() == day) return d
            }

            val day_filename = day.get(Calendar.YEAR).toString() + "_" + day.get(Calendar.MONTH).toString() + "_" + day.get(Calendar.DAY_OF_MONTH).toString() + ".planner.srj"
            for (d in days) {
                if (d == day_filename) {
                    days_loaded.add(Day(day))
                    return days_loaded[days_loaded.lastIndex]
                }
            }

            days.add(day_filename)
            days_loaded.add(Day(day))
            return days_loaded[days_loaded.lastIndex]
        }
    }
}
package jgappsandgames.smartreminderssave.utility

// Java
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.util.Calendar

// Android OS
import android.os.Build

// JSON
import org.json.JSONException
import org.json.JSONObject

/**
 * JSONUtility
 * Created by joshua on 12/6/2017.
 */
class JSONUtility {
    companion object {
        // JSON Calendar Constants
        private val ACTIVE = "active"
        private val DATE = "date"

        // Called to Load JSON From File
        @JvmStatic
        @Throws(IOException::class)
        fun loadJSON(file: File): JSONObject {
            try {
                val reader = BufferedReader(FileReader(file))
                val builder = StringBuilder()

                while (true) {
                    val t = reader.readLine()

                    if (t == null)
                        break
                    else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                            builder.append(t).append(System.lineSeparator())
                        else {
                            builder.append(t).append(System.getProperty("line.separator"))
                        }
                    }
                }
                return JSONObject(builder.toString())
            } catch (e: NullPointerException) {
                e.printStackTrace()
                return JSONObject()
            } catch (e: JSONException) {
                e.printStackTrace()
                return JSONObject()
            }
        }

        // Called to Save JSON to File
        @JvmStatic
        fun saveJSONObject(file: File, data: JSONObject) {
            try {
                val writer = BufferedWriter(FileWriter(file))

                writer.write(data.toString())
                writer.flush()
                writer.close()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }

        }

        // Called to Create a Calendar from a JSONObject
        @JvmStatic
        fun loadCalendar(data: JSONObject): Calendar? {
            if (data.optBoolean(ACTIVE, false)) {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = data.optLong(DATE, 0)
                return calendar
            }

            return null
        }

        // Called to Create a JSONObject from A Calendar
        @JvmStatic
        fun saveCalendar(calendar: Calendar?): JSONObject {
            try {
                val data = JSONObject()

                if (calendar == null) {
                    data.put(ACTIVE, false)
                } else {
                    data.put(ACTIVE, true)
                    data.put(DATE, calendar.timeInMillis)
                }
                return data
            } catch (j: JSONException) {
                j.printStackTrace()
            }

            return JSONObject()
        }
    }
}
package jgappsandgames.smartreminderssave.utility

// Java
import java.io.*

// JSON
import org.json.JSONObject

// Android OS
import android.os.Build
import android.util.Log

/**
 * JSONUtility
 * Created by joshuagarner on 11/1/17.
 */

// Log Constant
private val LOG = "JSONUtility"

// Load JSON
fun loadJSON(file: File, path: String): JSONObject {
    Log.d(LOG, "Loading: " + file.absolutePath + path)

    try {
        val reader = BufferedReader(FileReader(File(file, path)))
        val builder = StringBuilder()

        while (true) {
            val t = reader.readLine() ?: break

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) builder.append(t).append(System.lineSeparator())
            else builder.append(t).append(System.getProperty("line.separator"))
        }

        Log.v(LOG, builder.toString())
        return JSONObject(builder.toString())
    } catch (e: Exception) {
        e.printStackTrace()
        return JSONObject()
    }
}

// Save JSON
fun saveJSONObject(file: File, path: String, data: JSONObject) {
    Log.v(LOG, "Saving " + data.toString(4) + "\n" + file.absolutePath + path)

    val writer = BufferedWriter(FileWriter(File(file, path)))
    writer.write(data.toString())
    writer.flush()
    writer.close()

    Log.v(LOG, "Saved")
}
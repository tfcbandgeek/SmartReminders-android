package jgappsandgames.smartreminderssave.utility

import android.os.Build
import org.json.JSONObject
import java.io.*

/**
 * JSONUtility
 * Created by joshuagarner on 11/1/17.
 */

// Load JSON
fun loadJSON(file: File, path: String): JSONObject {
    val reader = BufferedReader(FileReader(File(file, path)))
    val builder = StringBuilder()

    while (true) {
        val t = reader.readLine() ?: break

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) builder.append(t).append(System.lineSeparator())
        else builder.append(t).append(System.getProperty("line.separator"))
    }

    return try {
        JSONObject(builder.toString())
    } catch (e: Exception) {
        e.printStackTrace()
        JSONObject()
    }
}

// Save JSON
fun saveJSONObject(file: File, path: String, data: JSONObject) {
    val writer = BufferedWriter(FileWriter(File(file, path)))
    writer.write(data.toString())
    writer.flush()
    writer.close()
}
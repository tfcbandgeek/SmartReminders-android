package jgappsandgames.smartreminderssave.utility

// Java
import java.io.*

// JSON
import org.json.JSONObject

// Android OS
import android.os.Build

/**
 * JSONUtility
 * Created by joshuagarner on 11/1/17.
 */

// Load JSON
fun loadJSON(file: File, path: String): JSONObject {
    try {
        val reader = BufferedReader(FileReader(File(file, path)))
        val builder = StringBuilder()

        while (true) {
            val t = reader.readLine() ?: break

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) builder.append(t).append(System.lineSeparator())
            else builder.append(t).append(System.getProperty("line.separator"))
        }

        return JSONObject(builder.toString())
    } catch (e: Exception) {
        e.printStackTrace()
        return JSONObject()
    }
}

// Save JSON
fun saveJSONObject(file: File, path: String, data: JSONObject) {
    val writer = BufferedWriter(FileWriter(File(file, path)))
    writer.write(data.toString())
    writer.flush()
    writer.close()
}
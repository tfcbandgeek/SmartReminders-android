package jgappsandgames.smartreminderslite.tasks

// Android OS
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import divideString

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.utility.TASK_NAME
import jgappsandgames.smartreminderssave.tasks.Task
import jgappsandgames.smartreminderssave.tasks.TaskManager
import jgappsandgames.smartreminderssave.utility.FileUtility
import jgappsandgames.smartreminderssave.utility.JSONUtility
import kotlinx.android.synthetic.main.activity_task_management.*
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream

/**
 * TaskManagementActivity
 * Created by joshua on 12/16/2017.\
 */
class TaskManagementActivity: Activity() {
    var view = 0
    var max = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_management)

        val t = TaskManager.taskPool.getPoolObject().load(intent.getStringExtra(TASK_NAME))

        val qrcode = generateQRCodes(t)
        max = qrcode.files.size
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.RGB_565

        previous.setOnClickListener {
            if (view != 0) view--
            location.text = "${view}/$max"
            qr.setImageBitmap(BitmapFactory.decodeFile(qrcode.files[view], options))
        }

        next.setOnClickListener {
            view++
            if (view >= max) view = max - 1
            location.text = "${view}/$max"
            qr.setImageBitmap(BitmapFactory.decodeFile(qrcode.files[view], options))
        }

        if (qrcode.files.size == 1) {
            (previous.parent as ViewGroup).removeView(previous)
            (next.parent as ViewGroup).removeView(next)
            qr.setImageBitmap(BitmapFactory.decodeFile(qrcode.files[0], options))
        } else {
            qr.setImageBitmap(BitmapFactory.decodeFile(qrcode.files[view], options))
            location.text = "${view}/$max"
        }
    }

    // QR Code Generation --------------------------------------------------------------------------
    private fun generateQRCode(data: JSONObject): Bitmap? {
        val hints = HashMap<EncodeHintType, ErrorCorrectionLevel>()
        hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.L
        val qrCodeWriter = QRCodeWriter()

        try {
            val bitMatrix = qrCodeWriter.encode(data.toString(), BarcodeFormat.QR_CODE, 1024, 1024, hints)
            val height = bitMatrix.height
            val width = bitMatrix.width

            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

            for (i in 0 until width) {
                for (j in 0 until height) {
                    bmp.setPixel(i, j, if (bitMatrix.get(i, j)) Color.BLACK else Color.WHITE)
                }
            }

            return bmp
        } catch (e: Exception) {
            e.printStackTrace()
            return Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565)
        }
    }

    private fun generateQRCodes(task: Task): QRCode {
        val json = task.toHeavyJSON()
        val filename = task.getFilename().dropLast(4)
        // TODO: Remove
        Log.e("Cache", filename)
        val timeStamp = task.getDateUpdated().timeInMillis
        val version = task.getVersion()

        if (File(FileUtility.getApplicationCacheDirectory(), ".$filename").exists()) {
            val meta = JSONUtility.loadJSONObject(File(FileUtility.getApplicationCacheDirectory(), ".$filename/meta.js"))
            if (meta.optLong("a", 0) == timeStamp) {
                return buildQRFromFile(filename)
            } else {
                File(FileUtility.getApplicationCacheDirectory(), ".$filename").deleteRecursively()
                return buildQRFromTask(json, filename, timeStamp, version)
            }
        } else {
            return buildQRFromTask(json, filename, timeStamp, version)
        }
    }

    private fun buildQRFromFile(filename: String): QRCode {
        val meta = JSONUtility.loadJSONObject(File(FileUtility.getApplicationCacheDirectory(), ".$filename/meta"))

        val timeStamp = meta.optLong("a", 0)
        val version = meta.optInt("b", 0)

        val c = meta.getJSONArray("c")
        val files = ArrayList<String>()
        for (i in 0 until c.length()) files.add(c.optString(i, ""))

        return QRCode(timeStamp, version, files)
    }

    private fun buildQRFromTask(json: JSONObject, filename: String, timeStamp: Long, version: Int): QRCode {
        Log.e("JSON.length", json.toString().length.toString())

        when {
            // Single QRCode (2500)
            json.toString().length < 2500 -> {
                // Build QRCodes
                val put = JSONObject().put("a", timeStamp).put("b", version).put("c", 1).put("d", 1).put("e", json.toString())
                Log.e("PUT.length", put.toString().length.toString())
                val code = generateQRCode(put) ?: throw IllegalArgumentException("Code should not be null")

                // Save QRCode
                val path = File(FileUtility.getApplicationCacheDirectory(), filename)
                if (!path.exists()) path.mkdirs()

                val stream = FileOutputStream(File(path, "1.png"))
                code.compress(Bitmap.CompressFormat.PNG, 100, stream)
                code.recycle()

                // Build and Meta Object
                val meta = JSONObject().put("a", timeStamp).put("b", version).put("c", 1)
                JSONUtility.saveJSONObject(File(path, "meta.js"), meta)

                // Build File List
                val array = ArrayList<String>()
                array.add("$path/1.png")

                return QRCode(timeStamp, version, array)
            }

            // Double QRCode (2400)
            json.toString().length <= 4800 -> {
                // Build and Save QRCodes
                val array = divideString(json.toString(), 2)

                val path = File(FileUtility.getApplicationCacheDirectory(), filename)
                if (!path.exists()) path.mkdirs()

                for (i in 0 until array.size) {
                    val string = array[i]
                    val put = JSONObject().put("a", timeStamp).put("b", version).put("c", 2).put("d", i).put("e", string)
                    Log.e("PUT.length", put.toString().length.toString())
                    val code = generateQRCode(put) ?: throw IllegalArgumentException("Code should not be null")

                    val stream = FileOutputStream(File(path, "$i.png"))
                    code.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    code.recycle()
                }

                // Build and Save Meta Object
                val meta = JSONObject().put("a", timeStamp).put("b", version).put("c", 2)
                JSONUtility.saveJSONObject(File(path, "meta.js"), meta)

                // Build File List
                val arrayA = ArrayList<String>()
                arrayA.add("$path/0.png")
                arrayA.add("$path/1.png")

                // Return QRCode
                return QRCode(timeStamp, version, arrayA)
            }

            // Triple QRCode (2334)
            json.toString().length <= 7000 -> {
                // Build and Save QRCodes
                val c = 3
                val array = divideString(json.toString(), c)
                val arrayName = ArrayList<String>(c)

                val path = File(FileUtility.getApplicationCacheDirectory(), filename)
                if (!path.exists()) path.mkdirs()

                for (i in 0 until array.size) {
                    val string = array[i]
                    val put = JSONObject().put("a", timeStamp).put("b", version).put("c", c).put("d", i).put("e", string)
                    Log.e("PUT.length", put.toString().length.toString())
                    val code = generateQRCode(put) ?: throw IllegalArgumentException("Code should not be null")

                    val stream = FileOutputStream(File(path, "$i.png"))
                    arrayName.add("$path/$i.png")
                    code.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    code.recycle()
                }

                // Build and Save Meta Object
                val meta = JSONObject().put("a", timeStamp).put("b", version).put("c", c)
                JSONUtility.saveJSONObject(File(path, "meta.js"), meta)

                // Return QRCode
                return QRCode(timeStamp, version, arrayName)
            }

            // Quadruple QRCode (2250)
            json.toString().length <= 9000 -> {
                // Build and Save QRCodes
                val c = 4
                val array = divideString(json.toString(), c)
                val arrayName = ArrayList<String>(c)

                val path = File(FileUtility.getApplicationCacheDirectory(), filename)
                if (!path.exists()) path.mkdirs()

                for (i in 0 until array.size) {
                    val string = array[i]
                    val put = JSONObject().put("a", timeStamp).put("b", version).put("c", c).put("d", i).put("e", string)
                    Log.e("PUT.length", put.toString().length.toString())
                    val code = generateQRCode(put) ?: throw IllegalArgumentException("Code should not be null")

                    val stream = FileOutputStream(File(path, "$i.png"))
                    arrayName.add("$path/$i.png")
                    code.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    code.recycle()
                }

                // Build and Save Meta Object
                val meta = JSONObject().put("a", timeStamp).put("b", version).put("c", c)
                JSONUtility.saveJSONObject(File(path, "meta.js"), meta)

                // Return QRCode
                return QRCode(timeStamp, version, arrayName)
            }

            // 5 QRCode (2100)
            json.toString().length <= 10500 -> {
                // Build and Save QRCodes
                val c = 5
                val array = divideString(json.toString(), c)
                val arrayName = ArrayList<String>(c)

                val path = File(FileUtility.getApplicationCacheDirectory(), filename)
                if (!path.exists()) path.mkdirs()

                for (i in 0 until array.size) {
                    val string = array[i]
                    val put = JSONObject().put("a", timeStamp).put("b", version).put("c", c).put("d", i).put("e", string)
                    Log.e("PUT.length", put.toString().length.toString())
                    val code = generateQRCode(put) ?: throw IllegalArgumentException("Code should not be null")

                    val stream = FileOutputStream(File(path, "$i.png"))
                    arrayName.add("$path/$i.png")
                    code.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    code.recycle()
                }

                // Build and Save Meta Object
                val meta = JSONObject().put("a", timeStamp).put("b", version).put("c", c)
                JSONUtility.saveJSONObject(File(path, "meta.js"), meta)

                // Return QRCode
                return QRCode(timeStamp, version, arrayName)
            }

            // 6 QRCode (2050)
            json.toString().length <= 12300 -> {
                // Build and Save QRCodes
                val c = 6
                val array = divideString(json.toString(), c)
                val arrayName = ArrayList<String>(c)

                val path = File(FileUtility.getApplicationCacheDirectory(), filename)
                if (!path.exists()) path.mkdirs()

                for (i in 0 until array.size) {
                    val string = array[i]
                    val put = JSONObject().put("a", timeStamp).put("b", version).put("c", c).put("d", i).put("e", string)
                    Log.e("PUT.length", put.toString().length.toString())
                    val code = generateQRCode(put) ?: throw IllegalArgumentException("Code should not be null")

                    val stream = FileOutputStream(File(path, "$i.png"))
                    arrayName.add("$path/$i.png")
                    code.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    code.recycle()
                }

                // Build and Save Meta Object
                val meta = JSONObject().put("a", timeStamp).put("b", version).put("c", c)
                JSONUtility.saveJSONObject(File(path, "meta.js"), meta)

                // Return QRCode
                return QRCode(timeStamp, version, arrayName)
            }

            // 7+ (2000)
            else -> {
                // Build and Save QRCodes
                val c = json.toString().length / 2000
                val array = divideString(json.toString(), c)
                val arrayName = ArrayList<String>(c)

                val path = File(FileUtility.getApplicationCacheDirectory(), filename)
                if (!path.exists()) path.mkdirs()

                for (i in 0 until array.size) {
                    val string = array[i]
                    val put = JSONObject().put("a", timeStamp).put("b", version).put("c", c).put("d", i).put("e", string)
                    Log.e("PUT.length", put.toString().length.toString())
                    val code = generateQRCode(put) ?: throw IllegalArgumentException("Code should not be null")

                    val stream = FileOutputStream(File(path, "$i.png"))
                    arrayName.add("$path/$i.png")
                    code.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    code.recycle()
                }

                // Build and Save Meta Object
                val meta = JSONObject().put("a", timeStamp).put("b", version).put("c", c)
                JSONUtility.saveJSONObject(File(path, "meta.js"), meta)

                // Return QRCode
                return QRCode(timeStamp, version, arrayName)
            }
        }
    }
}

data class QRCode(var timeStamp: Long, var version: Int, var files: ArrayList<String>)
package jgappsandgames.smartreminderslite.tasks

// Android OS
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.utility.TASK_NAME
import jgappsandgames.smartreminderssave.tasks.Task
import jgappsandgames.smartreminderssave.tasks.TaskManager
import kotlinx.android.synthetic.main.activity_task_management.*

/**
 * TaskManagementActivity
 * Created by joshua on 12/16/2017.\
 */
class TaskManagementActivity: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_management)

        val t = TaskManager.taskPool.getPoolObject().load(intent.getStringExtra(TASK_NAME))
        qr.setImageBitmap(generateQRCode(t))
        TaskManager.taskPool.returnPoolObject(t)
    }

    // QR Code Generation --------------------------------------------------------------------------
    private fun generateQRCode(task: Task): Bitmap? {
        val hints = HashMap<EncodeHintType, ErrorCorrectionLevel>()
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L)
        val qrCodeWriter = QRCodeWriter()

        try {
            val  bitMatrix = qrCodeWriter.encode(task.toHeavyJSON().toString(0), BarcodeFormat.QR_CODE, 1024, 1024, hints)
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
}
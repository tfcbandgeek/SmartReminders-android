package jgappsandgames.smartreminderslite.utility

// Android OS
import android.content.Context
import android.os.Build
import android.os.VibrationEffect

// Anko
import org.jetbrains.anko.vibrator

fun vibrate(context: Context) {
    if (context.vibrator.hasVibrator()) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            context.vibrator.vibrate(100)
        }
    }
}
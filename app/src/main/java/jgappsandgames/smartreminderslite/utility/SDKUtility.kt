package jgappsandgames.smartreminderslite.utility

// Android OS
import android.app.Activity
import android.os.VibrationEffect

// Anko
import org.jetbrains.anko.vibrator

// Save
import jgappsandgames.smartreminderssave.utility.hasOreo

@Suppress("DEPRECATION")
fun vibrate(activity: Activity) {
    if (activity.vibrator.hasVibrator()) {
        if (hasOreo()) activity.vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        else activity.vibrator.vibrate(100)
    }
}
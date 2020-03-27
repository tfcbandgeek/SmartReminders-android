package jgappsandgames.smartreminderssave.utility

import android.os.Build

fun hasKitKat(): Boolean = true
fun hasShortcuts(): Boolean = Build.VERSION_CODES.N_MR1 < Build.VERSION.SDK_INT
fun hasOreo(): Boolean = Build.VERSION_CODES.O < Build.VERSION.SDK_INT
fun hasPie(): Boolean = Build.VERSION_CODES.P < Build.VERSION.SDK_INT
fun hasQ(): Boolean = Build.VERSION_CODES.O < Build.VERSION.SDK_INT

fun getLineSeparator(): String = System.lineSeparator()

@Deprecated("Mispelled")
fun getLineSeperator(): String {
    return if (hasKitKat()) System.lineSeparator()
    else System.getProperty("line.separator")!!
}
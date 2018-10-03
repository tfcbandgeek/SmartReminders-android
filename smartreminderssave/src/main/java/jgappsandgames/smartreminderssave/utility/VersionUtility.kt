package jgappsandgames.smartreminderssave.utility

import android.os.Build

fun hasKitKat(): Boolean = Build.VERSION_CODES.KITKAT < Build.VERSION.SDK_INT
fun hasShortcuts(): Boolean = Build.VERSION_CODES.N_MR1 < Build.VERSION.SDK_INT
fun hasOreo(): Boolean = Build.VERSION_CODES.O < Build.VERSION.SDK_INT
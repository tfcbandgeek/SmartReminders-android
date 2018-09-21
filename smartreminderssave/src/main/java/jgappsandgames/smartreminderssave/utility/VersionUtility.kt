package jgappsandgames.smartreminderssave.utility

import android.os.Build

fun hasJellyBean(): Boolean = Build.VERSION_CODES.JELLY_BEAN < Build.VERSION.SDK_INT
fun hasKitKat(): Boolean = Build.VERSION_CODES.KITKAT < Build.VERSION.SDK_INT
fun hasLollipop(): Boolean = Build.VERSION_CODES.LOLLIPOP < Build.VERSION.SDK_INT
fun hasMarshmellow(): Boolean = Build.VERSION_CODES.M < Build.VERSION.SDK_INT
fun hasNougat(): Boolean = Build.VERSION_CODES.N < Build.VERSION.SDK_INT
fun hasShortcuts(): Boolean = Build.VERSION_CODES.N_MR1 < Build.VERSION.SDK_INT
fun hasOreo(): Boolean = Build.VERSION_CODES.O < Build.VERSION.SDK_INT
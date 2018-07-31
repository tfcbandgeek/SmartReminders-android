package jgappsandgames.smartreminderslite.utility

// Save
import jgappsandgames.smartreminderssave.utility.FileUtility

/**
 * MoveUtility
 * Created by joshua on 12/6/2017.
 */
fun moveToExternal() {
    FileUtility.copyFolder(FileUtility.getInternalFileDirectory(), FileUtility.getExternalFileDirectory())
}

fun moveToInternal() {
    FileUtility.copyFolder(FileUtility.getExternalFileDirectory(), FileUtility.getInternalFileDirectory())
}
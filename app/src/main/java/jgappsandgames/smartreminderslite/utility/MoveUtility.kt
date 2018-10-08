package jgappsandgames.smartreminderslite.utility

import jgappsandgames.smartreminderssave.utility.copyFolder
import jgappsandgames.smartreminderssave.utility.getExternalFileDirectory
import jgappsandgames.smartreminderssave.utility.getInternalFileDirectory

// Save

/**
 * MoveUtility
 * Created by joshua on 12/6/2017.
 */
fun moveToExternal() = copyFolder(getInternalFileDirectory(), getExternalFileDirectory())

fun moveToInternal() = copyFolder(getExternalFileDirectory(), getInternalFileDirectory())
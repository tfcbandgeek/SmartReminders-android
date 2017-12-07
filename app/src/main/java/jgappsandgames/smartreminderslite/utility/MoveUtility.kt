package jgappsandgames.smartreminderslite.utility

import jgappsandgames.smartreminderssave.utility.FileUtility

/**
 * MoveUtility
 * Created by joshua on 12/6/2017.
 */
class MoveUtility {
    companion object {
        @JvmStatic
        fun moveToExternal() {
            FileUtility.copyFolder(FileUtility.getInternalFileDirectory(), FileUtility.getExternalFileDirectory())
        }

        @JvmStatic
        fun moveToInternal() {
            FileUtility.copyFolder(FileUtility.getExternalFileDirectory(), FileUtility.getInternalFileDirectory())
        }
    }
}
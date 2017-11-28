package jgappsandgames.smartreminderssave.utility

/**
 * API
 * Created by joshu on 11/21/2017.
 */
class API {
    companion object {
        // Release (10/3/17)
        @JvmField
        val RELEASE = 10
        @JvmField
        val RELEASE_INFO = "List<> for JSONArray access Points"

        // Management (12/1/17*)
        @JvmField
        val MANAGEMENT = 11
        @JvmField
        val MANAGEMENT_INFO = "ThemeManager, Universal Manager, Task Metadata, File Metadata"

        // Shrinking (5/1/18*)
        @JvmField
        val SHRINKING = 12
        @JvmField
        val SHRINKING_INFO = "Single Character Keys for Save File, Attachments, Open File Notices"
    }
}
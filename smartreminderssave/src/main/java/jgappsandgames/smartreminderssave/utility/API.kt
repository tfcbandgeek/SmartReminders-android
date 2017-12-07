package jgappsandgames.smartreminderssave.utility

/**
 * API
 * Created by joshua on 12/6/2017.
 */
class API {
    companion object {
        // Release (10/3/17)
        @JvmField
        val RELEASE = 10
        @JvmField
        val RELEASE_INFO = "List<> for JSONArray access Points"

        // Management (1/1/18*)
        @JvmField
        val MANAGEMENT = 11
        @JvmField
        val MANAGEMENT_INFO = "ThemeManager, Universal Manager, Task Metadata, File Metadata"

        // Shrinking (6/1/18*)
        @JvmField
        val SHRINKING = 12
        @JvmField
        val SHRINKING_INFO = "Single Character Keys for Save File, Attachments, Open File Notices"

        // Encryption (1/1/19*)
        @JvmField
        val encryption = 13
        @JvmField
        val ENCRYPTION_INFO = "Encrypting data to make it secure"
    }
}
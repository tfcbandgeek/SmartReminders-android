package jgappsandgames.smartreminderssave

// Android
import android.util.Log

// Java
import java.io.FileNotFoundException

// JSON
import org.json.JSONException

// Save Library
import jgappsandgames.smartreminderssave.date.DateManager
import jgappsandgames.smartreminderssave.priority.PriorityManager
import jgappsandgames.smartreminderssave.settings.SettingsManager
import jgappsandgames.smartreminderssave.status.StatusManager
import jgappsandgames.smartreminderssave.tags.TagManager
import jgappsandgames.smartreminderssave.tasks.InvalidTaskTypeException
import jgappsandgames.smartreminderssave.tasks.TaskManager
import jgappsandgames.smartreminderssave.theme.ThemeManager
import jgappsandgames.smartreminderssave.utility.FileUtility

/**
 * MasterManager
 * Created by joshua on 12/10/2017.
 * Last Updated 4/30/2019.
 *
 * Manager Class For the Entire Save System
 */
class MasterManager {
    companion object {
        @JvmStatic fun create() {
            SettingsManager.create()
            TaskManager.create()
            TagManager.create()
            ThemeManager.create()

            save()
        }

        @JvmStatic fun load() {
            try {
                SettingsManager.load()
                TaskManager.load()
                TagManager.load()
                ThemeManager.load()
                DateManager.create()
                PriorityManager.create()
                StatusManager.create()
            } catch(e: FileNotFoundException) {
                Log.d("First Run?", "No Files Found")
                create()
            } catch (e: JSONException) {
                Log.d("First Run?", "JSON Error")
                create()
            } catch(e: InvalidTaskTypeException) {
                Log.d("First Run?", " Really Josh, This again?")
                e.printStackTrace()
            } /*catch (e: Exception) {
                Log.e("First Run?", e.message)
                create()
            }*/
        }

        @JvmStatic fun save() {
            SettingsManager.save()
            TaskManager.save()
            TagManager.save()
            ThemeManager.save()
        }

        @JvmStatic fun cleanSave() {}

        @JvmStatic fun cleanCache() = FileUtility.getApplicationCacheDirectory().deleteRecursively()
    }
}
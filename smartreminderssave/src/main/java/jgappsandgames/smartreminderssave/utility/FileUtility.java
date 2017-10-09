package jgappsandgames.smartreminderssave.utility;

// Java
import java.io.File;

// Android OS
import android.content.Context;
import android.os.Environment;

// Program
import jgappsandgames.smartreminderssave.settings.Settings;


/**
 * FileUtility
 * Created by joshua on 8/24/17.
 * Last Edited on 9/02/17. (72)
 *
 * Last Updated API Level: 5
 */
public class FileUtility {
    // File Paths
	private static File data;
	private static File cache;

    // Check to See if it is the Apps First Run
    public static boolean isFirstRun() {
        File file = new File(data, "firstrun");

        if (file.isDirectory()) return false;

        file.mkdirs();
        return true;
    }

    // Load File Paths
	public static void loadFilePaths(Context context) {
		data = context.getFilesDir();
		cache = context.getCacheDir();
	}

	// Get The Directory Where the Data should be stored
    public static File getApplicationDataDirectory() {
        // Create File Object
        File file;
        if (Settings.use_external_file) file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), ".smartreminders");
        else return data;

        // Create Directory
        if (!file.exists() || !file.isDirectory()) file.mkdirs();

        // Return the File
        return file;

    }

    // Get the Internal App Directory (Usefull for App Settings
    public static File getInternalFileDirectory() {
		return data;
	}

	// Get The Enternal Cache Directory
	public static File getApplicationCacheDirectory() {
		// Create File Object
		File file = new File(cache, ".smartreminders");
		
		// Create Directory
		if (!file.exists() || !file.isDirectory()) file.mkdirs();
		
		// Return the File
		return file;
	}
}
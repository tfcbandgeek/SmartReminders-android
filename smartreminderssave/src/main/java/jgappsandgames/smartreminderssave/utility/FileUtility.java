package jgappsandgames.smartreminderssave.utility;

// Java
import java.io.File;

// Android OS
import android.content.Context;
import android.os.Build;
import android.os.Environment;

// Program
import jgappsandgames.smartreminderssave.settings.Settings;

/**
 * FileUtility
 * Created by joshua on 8/24/17.
 *
 * Last Updated API Level: 10
 */
public class FileUtility {
    // File Paths
    private static String path = ".smartreminders";

	private static File data;
    private static File external;
	private static File cache;

    // Check to See if it is the Apps First Run
    public static boolean isFirstRun() {
        File file = new File(data, "firstrun");

        if (file.isDirectory()) return false;

        //noinspection ResultOfMethodCallIgnored
        data.mkdirs();
        //noinspection ResultOfMethodCallIgnored
        cache.mkdirs();
        //noinspection ResultOfMethodCallIgnored
        file.mkdirs();
        return true;
    }

    // Load File Paths
	public static void loadFilePaths(Context context) {
		data = new File(context.getFilesDir(), path);
		cache = new File(context.getCacheDir(), path);
	}

	// Get The Directory Where the Data should be stored
    public static File getApplicationDataDirectory() {
        // Create File Object
        if (Settings.use_external_file) {
            if (external != null) {
                return external;
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                external = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), path);
            else external = data;

            // Create Directory
            if (!external.exists() || !external.isDirectory()) //noinspection ResultOfMethodCallIgnored
                external.mkdirs();

            return external;
        }

        // Create Directory
        if (!data.exists() || !data.isDirectory()) //noinspection ResultOfMethodCallIgnored
            data.mkdirs();

        // Return the File
        return data;

    }

    // Get the Internal App Directory (Useful for App Settings
    public static File getInternalFileDirectory() {
        // Create Directory
        if (!data.exists() || !data.isDirectory()) //noinspection ResultOfMethodCallIgnored
            data.mkdirs();

        // Return the File
        return data;
	}

	// Get The Internal Cache Directory
	public static File getApplicationCacheDirectory() {
        // Create Directory
		if (!cache.exists() || !cache.isDirectory()) //noinspection ResultOfMethodCallIgnored
		    cache.mkdirs();
		
		// Return the File
		return cache;
	}
}
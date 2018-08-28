package jgappsandgames.me.save.utility;

import java.io.File;

public interface FileUtility {
    int INVALID_OUTPUT = -3;
    int NO_INPUT = -2;
    int FAILED = -1;
    int PASS = 0;

    // FileUtility Control Methods -----------------------------------------------------------------
    boolean isFirstRun();

    // Directory Getters ---------------------------------------------------------------------------
    File getInternalDataDirectory();
    File getApplicationDataDirectory();
    File getApplicationCacheDirectory();

    // Path Getters --------------------------------------------------------------------------------
    File getInternalPath(String file);
    File getApplicationPath(String file);
    File getApplicationCachePath(String file);

    // Move Methods --------------------------------------------------------------------------------
    int moveFolder(File in, File out);
    int copyFolder(File in, File out);
}

package jgappsandgames.smartreminderssave.utility;

/**
 * API
 * Created by joshua on 8/24/17.
 *
 * Holds Information about the various apis that may of been used
 */
public class API {
    // Version Test A
    // Initial Build
    public static final int VERSION_TEST_A = 0;
    public static final String VERSION_TEST_A_INFO = "Initial Build.";
	
	// Version Test B
	// Addition of TagManager, No Other Work
	public static final int VERSION_TEST_B = 1;
	public static final String VERSION_TEST_B_INFO = "Addition of the TagManager. No other Changes.";

    // Version Test C
    // Date handling Information added to Tasks
    public static final int VERSION_TEST_C = 2;
    public static final String VERSION_TEST_C_INFO = "Date Management data added to the Task Object.";

    // Version Test D
    // Addition of the Settings File
    public static final int VERSION_TEST_D = 3;
    public static final String VERSION_TEST_D_INFO = "Addition of the Save File.  No Other Changes";

    // Version Test E
    // Code Adjusting and Fixing.  Addition of More Shortcuts and loaded booleans
    public static final int VERSION_TEST_E = 4;
    public static final String VERSION_TEST_E_INFO = "Addition of Status and Date ShortCuts as well as loaded booleans.";

    // Version A
    // Credits, Removal of Cache Save,
    public static final int VERSION_A = 5;
    public static final String VERSION_A_INFO = "Addition of the Credit System and removal of the cache save. Task Priority and ID Were Also Added.";

    // Version B (9/9/17)
    // Priority
    public static final int VERSION_B = 6;
    public static final String VERSION_B_INFO = "Addition of DateManager";

    // Version C (9/13/17)
    // More Shortcuts
    public static final int VERSION_C = 7;
    public static final String VERSION_C_INFO = "Addition of Date Shortcuts";

    // Version D (9/16/17)
    public static final int VERSION_D = 8;
    public static final String VERSION_D_INFO = "Priority, Task Shortcuts added. Shrinking of save File.  Deleted Task Tracking.  Hiding of Task Variables.";

    // Version E (9/21/17)
    public static final int VERSION_E = 9;
    public static final String VERSION_E_INFO = "Finalized Task for API public release";

    // Release (10/3/17)
    public static final int RELEASE = 10;
    public static final String RELEASE_INFO = "List<> for JSONArray access Points";

    // Release (11/1/17) Planned
    public static final int RELEASE_A = 11;
    public static final String RELEASE_A_INFO = "Meta tag for tasks, Task Loaded hidden file, (Priority and Staus)Managers, Sort Managers Save, TaskChangeListeners";
}
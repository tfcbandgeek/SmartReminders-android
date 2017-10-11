package jgappsandgames.smartreminderslite.home;

// Android OS
import android.app.Activity;
import android.os.Bundle;

// Views
import android.widget.TextView;

// App
import jgappsandgames.smartreminderslite.BuildConfig;
import jgappsandgames.smartreminderslite.R;

/**
 * AboutActivity
 * Created by joshua on 10/2/17.
 * Last Edited on 10/11/17 (30).
 */
public class AboutActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Set Text
        ((TextView) findViewById(R.id.version)).setText(BuildConfig.VERSION_NAME);
        ((TextView) findViewById(R.id.build)).setText(String.valueOf(BuildConfig.VERSION_CODE));
        ((TextView) findViewById(R.id.api)).setText(jgappsandgames.smartreminderssave.BuildConfig.VERSION_NAME);
    }
}
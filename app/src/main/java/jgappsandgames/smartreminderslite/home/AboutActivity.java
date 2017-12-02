package jgappsandgames.smartreminderslite.home;

// Java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

// Android OS
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

// Views
import android.widget.TextView;
import android.widget.Toast;

// JSON
import org.json.JSONException;
import org.json.JSONObject;

// App
import jgappsandgames.smartreminderslite.BuildConfig;
import jgappsandgames.smartreminderslite.R;

/**
 * AboutActivity
 * Created by joshua on 10/2/17.
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

        // Check For Updates
        findViewById(R.id.update).setOnClickListener((view -> {
            Thread thread = new Thread(() -> {
                try {
                    final URL release = new URL("https://www.dropbox.com/s/a0i0ieed2dpskoo/release.json?dl=1");
                    final URLConnection release_connection = release.openConnection();
                    final BufferedReader reader = new BufferedReader(new InputStreamReader(release_connection.getInputStream()));

                    StringBuilder s = new StringBuilder();
                    while (true) {
                        final String t = reader.readLine();
                        if (t == null) break;
                        else s.append(t);
                    }

                    JSONObject data = new JSONObject(s.toString());
                    if (data.optInt("stable", 0) > BuildConfig.VERSION_CODE) {
                        runOnUiThread(() -> {
                            Toast.makeText(this, "Update Found", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/tfcbandgeek/SmartReminders-android/releases")));
                        });
                    } else {
                        runOnUiThread(() -> Toast.makeText(this, "No Update Found", Toast.LENGTH_LONG).show());
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException i) {
                    i.printStackTrace();
                } catch (JSONException j) {
                    j.printStackTrace();
                } catch (NullPointerException n) {
                    n.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            thread.start();
        }));

        // Release Candidate Updates
        findViewById(R.id.update_rc).setOnClickListener((view -> {
            Thread thread = new Thread(() -> {
                try {
                    final URL release = new URL("https://www.dropbox.com/s/a0i0ieed2dpskoo/release.json?dl=1");
                    final URLConnection release_connection = release.openConnection();
                    final BufferedReader reader = new BufferedReader(new InputStreamReader(release_connection.getInputStream()));

                    StringBuilder s = new StringBuilder();
                    while (true) {
                        final String t = reader.readLine();
                        if (t == null) break;
                        else s.append(t);
                    }

                    JSONObject data = new JSONObject(s.toString());
                    if (data.optInt("rc", 0) > BuildConfig.VERSION_CODE) {
                        runOnUiThread(() -> {
                            Toast.makeText(this, "Update Found", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/tfcbandgeek/SmartReminders-android/releases")));
                        });
                    } else {
                        runOnUiThread(() -> Toast.makeText(this, "No Update Found", Toast.LENGTH_LONG).show());
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException i) {
                    i.printStackTrace();
                } catch (JSONException j) {
                    j.printStackTrace();
                } catch (NullPointerException n) {
                    n.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            thread.start();
        }));

        // Beta Updates
        findViewById(R.id.update_beta).setOnClickListener((view -> {
            Thread thread = new Thread(() -> {
                try {
                    final URL release = new URL("https://www.dropbox.com/s/a0i0ieed2dpskoo/release.json?dl=1");
                    final URLConnection release_connection = release.openConnection();
                    final BufferedReader reader = new BufferedReader(new InputStreamReader(release_connection.getInputStream()));

                    StringBuilder s = new StringBuilder();
                    while (true) {
                        final String t = reader.readLine();
                        if (t == null) break;
                        else s.append(t);
                    }

                    JSONObject data = new JSONObject(s.toString());
                    if (data.optInt("beta", 0) > BuildConfig.VERSION_CODE) {
                        runOnUiThread(() -> {
                            Toast.makeText(this, "Update Found", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/tfcbandgeek/SmartReminders-android/releases")));
                        });
                    } else {
                        runOnUiThread(() -> Toast.makeText(this, "No Update Found", Toast.LENGTH_LONG).show());
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException i) {
                    i.printStackTrace();
                } catch (JSONException j) {
                    j.printStackTrace();
                } catch (NullPointerException n) {
                    n.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            thread.start();
        }));

        // Alpha Updates
        findViewById(R.id.update_alpha).setOnClickListener((view -> {
            Thread thread = new Thread(() -> {
                try {
                    final URL release = new URL("https://www.dropbox.com/s/a0i0ieed2dpskoo/release.json?dl=1");
                    final URLConnection release_connection = release.openConnection();
                    final BufferedReader reader = new BufferedReader(new InputStreamReader(release_connection.getInputStream()));

                    StringBuilder s = new StringBuilder();
                    while (true) {
                        final String t = reader.readLine();
                        if (t == null) break;
                        else s.append(t);
                    }

                    JSONObject data = new JSONObject(s.toString());
                    if (data.optInt("alpha", 0) > BuildConfig.VERSION_CODE) {
                        runOnUiThread(() -> {
                            Toast.makeText(this, "Update Found", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/tfcbandgeek/SmartReminders-android/releases")));
                        });
                    } else {
                        runOnUiThread(() -> Toast.makeText(this, "No Update Found", Toast.LENGTH_LONG).show());
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException i) {
                    i.printStackTrace();
                } catch (JSONException j) {
                    j.printStackTrace();
                } catch (NullPointerException n) {
                    n.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            thread.start();
        }));
    }
}
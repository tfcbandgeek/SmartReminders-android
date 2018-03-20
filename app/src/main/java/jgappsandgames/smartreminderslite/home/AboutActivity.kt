package jgappsandgames.smartreminderslite.home

// Java
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL

// Android OS
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle

// Views
import android.view.View
import android.widget.TextView
import android.widget.Toast

// JSON
import org.json.JSONException
import org.json.JSONObject

// App
import jgappsandgames.smartreminderslite.BuildConfig
import jgappsandgames.smartreminderslite.R

/**
 * AboutActivity
 * Created by joshua on 12/30/2017.
 */
class AboutActivity : Activity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        // Set Text
        (findViewById<View>(R.id.version) as TextView).text = BuildConfig.VERSION_NAME
        (findViewById<View>(R.id.build) as TextView).text = BuildConfig.VERSION_CODE.toString()
        (findViewById<View>(R.id.api) as TextView).text = jgappsandgames.smartreminderssave.BuildConfig.VERSION_NAME

        // Check For Updates
        findViewById<View>(R.id.update).setOnClickListener({ _ ->
            val thread = Thread {
                try {
                    val release = URL("https://www.dropbox.com/s/a0i0ieed2dpskoo/release.json?dl=1")
                    val releaseConnection = release.openConnection()
                    val reader = BufferedReader(InputStreamReader(releaseConnection.getInputStream()))

                    val s = StringBuilder()
                    while (true) {
                        val t = reader.readLine()
                        if (t == null) break
                        else s.append(t)
                    }

                    val data = JSONObject(s.toString())
                    if (data.optInt("stable", 0) > BuildConfig.VERSION_CODE) {
                        runOnUiThread {
                            Toast.makeText(this, "Update Found", Toast.LENGTH_LONG).show()
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/tfcbandgeek/SmartReminders-android/releases")))
                        }
                    } else {
                        runOnUiThread { Toast.makeText(this, "No Update Found", Toast.LENGTH_LONG).show() }
                    }
                } catch (e: MalformedURLException) {
                    e.printStackTrace()
                } catch (i: IOException) {
                    i.printStackTrace()
                } catch (j: JSONException) {
                    j.printStackTrace()
                } catch (n: NullPointerException) {
                    n.printStackTrace()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            thread.start()
        })
    }
}
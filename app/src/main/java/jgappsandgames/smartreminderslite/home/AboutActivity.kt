package jgappsandgames.smartreminderslite.home

// Java
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

// Android OS
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

// Anko
import org.jetbrains.anko.toast

// JSON
import org.json.JSONObject

// App
import jgappsandgames.smartreminderslite.BuildConfig
import jgappsandgames.smartreminderslite.R

// KotlinX
import kotlinx.android.synthetic.main.activity_about.*

/**
 * AboutActivity
 * Created by joshua on 12/30/2017.
 */
class AboutActivity: AppCompatActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        setTitle(R.string.app_name)

        // Set Text
        version.text = BuildConfig.VERSION_NAME
        build.text = BuildConfig.VERSION_CODE.toString()
        api.text = jgappsandgames.smartreminderssave.BuildConfig.VERSION_NAME

        // Check For Updates
        update.setOnClickListener {
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
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/tfcbandgeek/SmartReminders-android/releases")))
                        }
                    } else {
                        runOnUiThread { toast(R.string.no_update_found).show() }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            thread.start()
        }

        // External Libraries
        anko_button.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.anko_path))))
        }

        fab_button.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.fab_path))))
        }

        kotlin_button.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.kotlin_path))))
        }

        pool_button.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.pool_utility_path))))
        }
    }
}
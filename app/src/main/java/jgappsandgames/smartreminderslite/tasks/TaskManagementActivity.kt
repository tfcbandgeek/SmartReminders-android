package jgappsandgames.smartreminderslite.tasks

// Android OS
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import jgappsandgames.smartreminderslite.utility.IntentOptions
import jgappsandgames.smartreminderslite.utility.TASK_NAME
import jgappsandgames.smartreminderslite.utility.TaskOptions
import jgappsandgames.smartreminderslite.utility.buildTaskIntent
import jgappsandgames.smartreminderssave.tasks.Task
import jgappsandgames.smartreminderssave.tasks.TaskManager
import org.jetbrains.anko.activityManager
import org.jetbrains.anko.button
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout

/**
 * TaskManagementActivity
 * Created by joshua on 12/16/2017.\
 */
class TaskManagementActivity: AppCompatActivity() {
    var task: Task? = null
    var activity: AppCompatActivity? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        task = TaskManager.taskPool.getPoolObject().load(intent.getStringExtra(TASK_NAME))
        activity = this

        verticalLayout {
            textView {
                text = task?.getFilename()
            }

            button {
                text = "Duplicate"
                onClick {
                    context.startActivity(buildTaskIntent(activity!!, IntentOptions(), TaskOptions(task = task!!.copy())))
                }
            }
        }
    }
}
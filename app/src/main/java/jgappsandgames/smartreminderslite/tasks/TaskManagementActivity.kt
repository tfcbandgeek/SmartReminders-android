package jgappsandgames.smartreminderslite.tasks

// Android OS
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import jgappsandgames.smartreminderslite.utility.TASK_NAME
import jgappsandgames.smartreminderssave.tasks.Task
import jgappsandgames.smartreminderssave.tasks.TaskManager
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout

/**
 * TaskManagementActivity
 * Created by joshua on 12/16/2017.\
 */
class TaskManagementActivity: AppCompatActivity() {
    var task: Task? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        task = TaskManager.taskPool.getPoolObject().load(intent.getStringExtra(TASK_NAME) ?: "")

        verticalLayout {
            textView {
                text = task?.getFilename()
            }
        }
    }
}
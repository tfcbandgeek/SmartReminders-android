package jgappsandgames.smartreminderslite.tasks

// Android OS
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.utility.IntentOptions
import jgappsandgames.smartreminderslite.utility.TASK_NAME
import jgappsandgames.smartreminderslite.utility.TaskOptions
import jgappsandgames.smartreminderslite.utility.buildTaskIntent
import jgappsandgames.smartreminderssave.settings.SettingsManager
import jgappsandgames.smartreminderssave.tasks.Task
import jgappsandgames.smartreminderssave.tasks.TaskManager
import kotlinx.android.synthetic.main.activity_task_management.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.selector

/**
 * TaskManagementActivity
 * Created by joshua on 12/16/2017.\
 */
class TaskManagementActivity: AppCompatActivity() {
    var task: Task? = null
    var activity: AppCompatActivity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_management)
        task = TaskManager.taskPool.getPoolObject().load(intent.getStringExtra(TASK_NAME))

        filename.text = task!!.getFilename()

        type.text = when (task!!.getType()) {
            Task.TYPE_TASK -> "Task"
            Task.TYPE_FOLDER -> "Folder"
            Task.TYPE_NOTE -> "Note"
            Task.TYPE_SHOPPING_LIST -> "Shopping List"
            else -> "None"
        }

        duplicate.text = "Duplicate"
        duplicate.onClick {
            this@TaskManagementActivity.startActivity(buildTaskIntent(this@TaskManagementActivity, IntentOptions(), TaskOptions(task = task!!.copy())))
        }

        setViewTypeText()
        view_type.setOnClickListener {
            selector("Set the Default List View Type:", VIEW_TYPE_LIST) { di, i ->
                when (i) {
                    0 -> task!!.setListViewType(Task.LIST_PATH_NONE + task!!.getType()).save()
                    1 -> task!!.setListViewType(Task.LIST_TAG_NONE + task!!.getType()).save()
                    2 -> task!!.setListViewType(Task.LIST_CHILDREN_NONE + task!!.getType()).save()
                    3 -> task!!.setListViewType(Task.LIST_ALL_NONE + task!!.getType()).save()
                    4 -> task!!.setListViewType(Task.LIST_DEFAULT_NONE + task!!.getType()).save()
                    else -> task!!.setListViewType(Task.LIST_DEFAULT_NONE + task!!.getType()).save()
                }

                setViewTypeText()
            }
        }
    }

    companion object {
        private val VIEW_TYPE_LIST = listOf("Path", "Tag", "Children", "All", "Default")
    }

    private fun setViewTypeText() {
        view_type.text = when (task!!.getListViewType() - task!!.getType()) {
            Task.LIST_DEFAULT_NONE -> "None"
            Task.LIST_PATH_NONE -> "Path"
            Task.LIST_TAG_NONE -> "Tags"
            Task.LIST_CHILDREN_NONE -> "Children"
            Task.LIST_ALL_NONE -> "All"
            else -> "Error"
        }
    }
}
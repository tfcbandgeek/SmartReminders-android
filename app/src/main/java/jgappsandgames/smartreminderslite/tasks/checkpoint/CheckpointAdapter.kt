package jgappsandgames.smartreminderslite.tasks.checkpoint

// Views
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.holder.CheckpointHolder
import jgappsandgames.smartreminderslite.tasks.TaskActivity

// Save
import jgappsandgames.smartreminderssave.tasks.Checkpoint

/**
 * CheckpointAdapter
 * Created by joshua on 1/19/2018.
 */
class CheckpointAdapter(private val activity: TaskActivity, private val task: String, private val checkpoints: List<Checkpoint>):
        BaseAdapter() {
    // List Methods
    override fun getCount(): Int {
        return checkpoints.size
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    // Item Methods
    override fun getItem(position: Int): Checkpoint {
        return checkpoints[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convert_view: View?, parent: ViewGroup): View {
        var view = convert_view
        if (view == null) {
            view = LayoutInflater.from(activity).inflate(R.layout.list_checkpoint, parent, false)

            val holder = CheckpointHolder(activity, task, getItem(position), view!!)
            view.tag = holder
        } else {
            val holder = CheckpointHolder(activity, task, getItem(position), view)
            view.tag = holder
        }

        return view
    }
}
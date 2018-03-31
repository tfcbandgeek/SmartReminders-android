package jgappsandgames.smartreminderslite.tasks.tags

// Android
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle

// Views
import android.text.Editable
import android.text.TextWatcher
import android.view.View

// JSON
import org.json.JSONArray
import org.json.JSONException

// App
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.adapter.TagAdapterInterface
import jgappsandgames.smartreminderslite.holder.TagHolder
import jgappsandgames.smartreminderslite.utility.ActivityUtility

// Save
import jgappsandgames.smartreminderssave.tags.TagManager
import jgappsandgames.smartreminderssave.tasks.Task

/**
 * TagEditorActivity
 * Created by joshua on 1/19/2018.
 */
class TagEditorActivity: TagEditorActivityInterface(), TextWatcher, View.OnClickListener, View.OnLongClickListener, TagHolder.TagSwitcher {
    // Data ----------------------------------------------------------------------------------------
    private var task: Task? = null

    // LifeCycle Methods --------------------------------------------------------------------------
    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Load Data
        task = Task(intent.getStringExtra(ActivityUtility.TASK_NAME))

        // Set Listeners
        searchEnter!!.setOnClickListener(this)
        searchEnter!!.setOnLongClickListener(this)
        searchText!!.addTextChangedListener(this)

        // Set Adapters
        selected!!.adapter = TagAdapterInterface(this, this, task!!.getTags(), true)
        unselected!!.adapter = TagAdapterInterface(this, this, task!!.getTags(), false)
    }

    // Text Watcher Methods ------------------------------------------------------------------------
    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
        if (searchText!!.text.toString().length == 0) {
            selected!!.adapter = TagAdapterInterface(this, this, task!!.getTags(), true)
            unselected!!.adapter = TagAdapterInterface(this, this, task!!.getTags(), false)
        } else {
            selected!!.adapter = TagAdapterInterface(this, this, task!!.getTags(), true, searchText!!.text.toString())
            unselected!!.adapter = TagAdapterInterface(this, this, task!!.getTags(), false, searchText!!.text.toString())
        }

        if (TagManager.contains(searchText!!.text.toString())) searchEnter!!.setText(R.string.select)
        else searchEnter!!.setText(R.string.add)
    }
    override fun afterTextChanged(editable: Editable) {}

    // Click Listeners -----------------------------------------------------------------------------
    override fun onClick(view: View) {
        // Tag is Not in the List And is addable
        if (TagManager.addTag(searchText!!.text.toString())) {
            task!!.addTag(searchText!!.text.toString())
            searchText!!.setText("")

            selected!!.adapter = TagAdapterInterface(this, this, task!!.getTags(), true)
            unselected!!.adapter = TagAdapterInterface(this, this, task!!.getTags(), false)

            // Set new Result Intent
            try {
                val tags = JSONArray()
                for (tag in task!!.getTags()) tags.put(tag)
                setResult(ActivityUtility.RESPONSE_CHANGE, Intent().putExtra(ActivityUtility.TAG_LIST, tags.toString()))
            } catch (e: JSONException) {
                e.printStackTrace()
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }
        } else if (searchText!!.text.toString() != "") {
            task!!.addTag(searchText!!.text.toString())
            searchText!!.setText("")

            selected!!.adapter = TagAdapterInterface(this, this, task!!.getTags(), true)
            unselected!!.adapter = TagAdapterInterface(this, this, task!!.getTags(), false)

            // Set new Result Intent
            try {
                val tags = JSONArray()
                for (tag in task!!.getTags()) tags.put(tag)
                setResult(ActivityUtility.RESPONSE_CHANGE, Intent().putExtra(ActivityUtility.TAG_LIST, tags.toString()))
            } catch (e: JSONException) {
                e.printStackTrace()
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }
        }
    }

    override fun onLongClick(view: View): Boolean {
        return false
    }

    // TagSwitcher ---------------------------------------------------------------------------------
    override fun moveTag(tag: String?, selected: Boolean) {
        if (selected) task!!.addTag(tag!!)
        else task!!.removeTag(tag!!)

        // Set Adapters
        this.selected!!.adapter = TagAdapterInterface(this, this, task!!.getTags(), true)
        this.unselected!!.adapter = TagAdapterInterface(this, this, task!!.getTags(), false)

        // Set New Result Intent
        try {
            val tags = JSONArray()
            for (ta in task!!.getTags()) tags.put(ta)
            setResult(ActivityUtility.RESPONSE_CHANGE, Intent().putExtra(ActivityUtility.TAG_LIST, tags.toString()))
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }
}
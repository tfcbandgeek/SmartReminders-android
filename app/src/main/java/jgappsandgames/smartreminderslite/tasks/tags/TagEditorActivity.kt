package jgappsandgames.smartreminderslite.tasks.tags

// Android
import android.app.Activity
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
import jgappsandgames.smartreminderslite.adapter.TagAdapter
import jgappsandgames.smartreminderslite.utility.ActivityUtility

// KotlinX
import kotlinx.android.synthetic.main.activity_tag_editpr.tag_editor_search_enter
import kotlinx.android.synthetic.main.activity_tag_editpr.tag_editor_search_text
import kotlinx.android.synthetic.main.activity_tag_editpr.tag_editor_selected
import kotlinx.android.synthetic.main.activity_tag_editpr.tag_editor_unselected

// Save
import jgappsandgames.smartreminderssave.tags.TagManager
import jgappsandgames.smartreminderssave.tasks.Task

/**
 * TagEditorActivity
 * Created by joshua on 1/19/2018.
 */
class TagEditorActivity: Activity(), TextWatcher, View.OnClickListener, View.OnLongClickListener, TagAdapter.TagSwitcher {
    // Data ----------------------------------------------------------------------------------------
    private lateinit var task: Task

    // LifeCycle Methods --------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tag_editpr)

        // Set Result Intent
        setResult(ActivityUtility.RESPONSE_NONE)

        // Load Data
        task = Task(intent.getStringExtra(ActivityUtility.TASK_NAME))

        // Set Listeners
        tag_editor_search_enter.setOnClickListener(this)
        tag_editor_search_enter.setOnLongClickListener(this)
        tag_editor_search_text.addTextChangedListener(this)

        // Set Adapters
        tag_editor_selected.adapter = TagAdapter(this, this, task.getTags(), true)
        tag_editor_unselected.adapter = TagAdapter(this, this, task.getTags(), false)
    }

    // Text Watcher Methods ------------------------------------------------------------------------
    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
        if (tag_editor_search_text.text.toString().isEmpty()) {
            tag_editor_selected!!.adapter = TagAdapter(this, this, task.getTags(), true)
            tag_editor_unselected!!.adapter = TagAdapter(this, this, task.getTags(), false)
        } else {
            tag_editor_selected!!.adapter = TagAdapter(this, this, task.getTags(), true, tag_editor_search_text.text.toString())
            tag_editor_unselected!!.adapter = TagAdapter(this, this, task.getTags(), false, tag_editor_search_text.text.toString())
        }

        if (TagManager.contains(tag_editor_search_text.text.toString())) tag_editor_search_enter.setText(R.string.select)
        else tag_editor_search_enter.setText(R.string.add)
    }
    override fun afterTextChanged(editable: Editable) {}

    // Click Listeners -----------------------------------------------------------------------------
    override fun onClick(view: View) {
        // Tag is Not in the List And is addable
        if (TagManager.addTag(tag_editor_search_text.text.toString())) {
            task.addTag(tag_editor_search_text.text.toString())
            tag_editor_search_text.setText("")

            tag_editor_selected!!.adapter = TagAdapter(this, this, task.getTags(), true)
            tag_editor_unselected!!.adapter = TagAdapter(this, this, task.getTags(), false)

            // Set new Result Intent
            try {
                val tags = JSONArray()
                for (tag in task.getTags()) tags.put(tag)
                setResult(ActivityUtility.RESPONSE_CHANGE, Intent().putExtra(ActivityUtility.TAG_LIST, tags.toString()))
            } catch (e: JSONException) {
                e.printStackTrace()
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }
        } else if (tag_editor_search_text.text.toString() != "") {
            task.addTag(tag_editor_search_text.text.toString())
            tag_editor_search_text.setText("")

            tag_editor_selected!!.adapter = TagAdapter(this, this, task.getTags(), true)
            tag_editor_unselected!!.adapter = TagAdapter(this, this, task.getTags(), false)

            // Set new Result Intent
            try {
                val tags = JSONArray()
                for (tag in task.getTags()) tags.put(tag)
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
        if (selected) task.addTag(tag!!)
        else task.removeTag(tag!!)

        // Set Adapters
        this.tag_editor_selected!!.adapter = TagAdapter(this, this, task.getTags(), true)
        this.tag_editor_unselected!!.adapter = TagAdapter(this, this, task.getTags(), false)

        // Set New Result Intent
        try {
            val tags = JSONArray()
            for (ta in task.getTags()) tags.put(ta)
            setResult(ActivityUtility.RESPONSE_CHANGE, Intent().putExtra(ActivityUtility.TAG_LIST, tags.toString()))
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }
}
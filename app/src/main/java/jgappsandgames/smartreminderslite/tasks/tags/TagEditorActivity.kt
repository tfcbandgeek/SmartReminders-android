package jgappsandgames.smartreminderslite.tasks.tags

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import jgappsandgames.smartreminderslite.R
import jgappsandgames.smartreminderslite.holder.TagHolder
import jgappsandgames.smartreminderslite.utility.ActivityUtility
import jgappsandgames.smartreminderssave.tags.TagManager
import jgappsandgames.smartreminderssave.tasks.Task
import org.json.JSONArray
import org.json.JSONException

/**
 * Created by joshu on 1/19/2018.
 */
class TagEditorActivity: TagEditorActivityInterface(), TextWatcher, View.OnClickListener, View.OnLongClickListener, TagHolder.TagSwitcher {
    // Data
    private var task: Task? = null

    // LifeCycle Methods
    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Load Data
        task = Task(intent.getStringExtra(ActivityUtility.TASK_NAME))

        // Set Listeners
        search_enter!!.setOnClickListener(this)
        search_enter!!.setOnLongClickListener(this)
        search_text!!.addTextChangedListener(this)

        // Set Adapters
        selected!!.adapter = TagSelectedAdapter(this, task!!)
        unselected!!.adapter = TagUnselectedAdapter(this, task!!)
    }

    // Text Watcher Methods
    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
        // Not Needed, Only included because it is required by the TextWatcher
    }

    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
        selected!!.adapter = TagSelectedAdapter(this, task!!, search_text!!.getText().toString())
        unselected!!.adapter = TagUnselectedAdapter(this, task!!, search_text!!.getText().toString())

        if (TagManager.contains(search_text!!.getText().toString())) search_enter!!.setText(R.string.select)
        else search_enter!!.setText(R.string.add)
    }

    override fun afterTextChanged(editable: Editable) {
        // Not Needed, OnLy included because it is required by the TextWatcher
    }

    // Click Listeners
    override fun onClick(view: View) {
        // Tag is Not in the List And is addable
        if (TagManager.addTag(search_text!!.getText().toString())) {
            task!!.addTag(search_text!!.getText().toString())
            search_text!!.setText("")

            selected!!.adapter = TagSelectedAdapter(this, task!!)
            unselected!!.adapter = TagUnselectedAdapter(this, task!!)

            // Set new Result Intent
            try {
                val tags = JSONArray()
                for (tag in task!!.getTags()) tags.put(tag)
                setResult(ActivityUtility.RESPONSE_CHANGE, Intent().putExtra(ActivityUtility.TAG_LIST, tags.toString(4)))
            } catch (e: JSONException) {
                e.printStackTrace()
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }

        } else if (search_text!!.getText().toString() != "") {
            task!!.addTag(search_text!!.getText().toString())
            search_text!!.setText("")

            selected!!.adapter = TagSelectedAdapter(this, task!!)
            unselected!!.adapter = TagUnselectedAdapter(this, task!!)

            // Set new Result Intent
            try {
                val tags = JSONArray()
                for (tag in task!!.getTags()) tags.put(tag)
                setResult(ActivityUtility.RESPONSE_CHANGE, Intent().putExtra(ActivityUtility.TAG_LIST, tags.toString(4)))
            } catch (e: JSONException) {
                e.printStackTrace()
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }

        }// Tag is not addable
    }

    override fun onLongClick(view: View): Boolean {
        return false
    }

    // TagSwitcher
    override fun moveTag(tag: String?, selected: Boolean) {
        if (selected) task!!.addTag(tag!!)
        else task!!.removeTag(tag!!)

        // Set Adapters
        this.selected!!.adapter = TagSelectedAdapter(this, task!!)
        this.unselected!!.adapter = TagUnselectedAdapter(this, task!!)

        // Set New Result Intent
        try {
            val tags = JSONArray()
            for (ta in task!!.getTags()) tags.put(ta)
            setResult(ActivityUtility.RESPONSE_CHANGE, Intent().putExtra(ActivityUtility.TAG_LIST, tags.toString(4)))
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

    }
}
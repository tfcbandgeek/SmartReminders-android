package jgappsandgames.smartreminderslite.main

// Android OS
import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * PlannerHomeActivity
 * HomeActivity For the PlannerView.
 *
 * Created by joshua on 2/27/2018.
 */
class PlannerHomeActivity: Activity() {
    // Constants -----------------------------------------------------------------------------------
    // Data ----------------------------------------------------------------------------------------
    // LifeCycle Methods ---------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        linearLayout {
            orientation = LinearLayout.VERTICAL

            // MainView
            linearLayout {
                listView {
                    adapter = object: BaseAdapter() {
                        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun getItem(position: Int): Any {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun getItemId(position: Int): Long {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun getCount(): Int {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }
                    }
                }
            }.lparams(matchParent, matchParent, 1F)

            // Bottom Bar
            linearLayout {
                orientation = LinearLayout.HORIZONTAL

                button {
                    text = "Previous"
                    onClick {

                    }
                }

                textView {
                    text = "Friday, April 29, 1994"
                    onClick {

                    }
                }.lparams(matchParent, wrapContent, 1F)

                button {
                    text = "Next"
                    onClick {

                    }
                }
            }.lparams(matchParent, wrapContent)
        }
    }
}
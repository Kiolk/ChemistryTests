package com.github.kiolk.chemistrytests.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.ui.fragments.tests.OnTestCallback
import com.github.kiolk.chemistrytests.ui.fragments.tests.TestsPresenter
import com.github.kiolk.chemistrytests.data.adapters.AvailableTestRecyclerAdapter
import com.github.kiolk.chemistrytests.ui.fragments.bases.RecyclerTestBaseFragment
import com.github.kiolk.chemistrytests.data.listeners.OnItemClickListener
import com.github.kiolk.chemistrytests.data.models.TestParams
import com.github.kiolk.chemistrytests.ui.activities.TestingActivity
import com.github.kiolk.chemistrytests.utils.Constants.TEST_PARAM_INT

class LastModifiedFragment : RecyclerTestBaseFragment() {
    override val titleId: Int
        get() = R.string.CUSTOM_TEST

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_last_modified, null)
        val lastModifiedTests: MutableList<TestParams> = mutableListOf()
        val adapter = context?.let { AvailableTestRecyclerAdapter(it, lastModifiedTests) }
        val listener = object : OnItemClickListener {
            override fun onClick(view: View, position: Int) {
                val intent = Intent(context, TestingActivity::class.java)
                Log.d("MyLogs", "Start test by item $position")
                intent.putExtra(TEST_PARAM_INT, lastModifiedTests.get(position))
                startActivity(intent)
            }

            override fun onLongClick(view: View, position: Int) {

            }
        }

        attachRecyclerView(view, R.id.last_modified_tests_recycler_view, adapter, listener)

        context?.let {
            TestsPresenter.getLastModifiedTests(it, object : OnTestCallback {
                override fun <T> onSuccess(any: T?) {
                    val tests : MutableList<TestParams> = any as MutableList<TestParams>
                    tests.forEach { lastModifiedTests.add(it) }
                    adapter?.notifyDataSetChanged()
                    if(lastModifiedTests.size > 0) {
                        view.findViewById<TextView>(R.id.empty_list_message_recycler_view).visibility = View.GONE
                    }
                }
            })
        }
        return view //super.onCreateView(inflater, container, savedInstanceState)
    }
}
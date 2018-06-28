package com.github.kiolk.chemistrytests.data.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.fragments.tests.OnTestCallback
import com.github.kiolk.chemistrytests.data.fragments.tests.TestsPresenter
import com.github.kiolk.chemistrytests.data.adapters.AvailableTestRecyclerAdapter
import com.github.kiolk.chemistrytests.data.fragments.bases.RecyclerTestBaseFragment
import com.github.kiolk.chemistrytests.data.listeners.OnItemClickListener
import com.github.kiolk.chemistrytests.data.models.TestParams
import com.github.kiolk.chemistrytests.ui.activities.TEST_PARAM_INT
import com.github.kiolk.chemistrytests.ui.activities.TestingActivity

class FavaoriteTestFragment : RecyclerTestBaseFragment(){
    override val titleId: Int
        get() = R.string.CUSTOM_TEST

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_favorite_tests, null)
        val FavoriteTests: MutableList<TestParams> = mutableListOf()
        val adapter = context?.let { AvailableTestRecyclerAdapter(it, FavoriteTests) }
        val listener = object : OnItemClickListener {
            override fun onClick(view: View, position: Int) {
                val intent = Intent(context, TestingActivity::class.java)
                Log.d("MyLogs", "Start test by item $position")
                intent.putExtra(TEST_PARAM_INT, FavoriteTests.get(position))
                startActivity(intent)
            }

            override fun onLongClick(view: View, position: Int) {

            }
        }

        attachRecyclerView(view, R.id.favorite_tests_recycler_view, adapter, listener)

        context?.let {
            TestsPresenter.getFavoriteTests( object : OnTestCallback {
                override fun <T> onSuccess(any: T?) {
                    val tests : MutableList<TestParams> = any as MutableList<TestParams>
                    tests.forEach { FavoriteTests.add(it) }
                    adapter?.notifyDataSetChanged()
                    if(FavoriteTests.size > 0) {
                        view.findViewById<TextView>(R.id.empty_list_message_recycler_view).visibility = View.GONE
                    }
                }
            })
        }





        return view // super.onCreateView(inflater, container, savedInstanceState)
    }
}
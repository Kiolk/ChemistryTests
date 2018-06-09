package com.github.kiolk.chemistrytests.data.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.TestsPresenter
import com.github.kiolk.chemistrytests.data.adapters.AvailableTestRecyclerAdapter
import com.github.kiolk.chemistrytests.data.asynctasks.ResultCallback
import com.github.kiolk.chemistrytests.data.database.DBOperations
import com.github.kiolk.chemistrytests.data.listeners.OnItemClickListener
import com.github.kiolk.chemistrytests.data.listeners.RecyclerTouchListener
import com.github.kiolk.chemistrytests.data.models.TestParams
import com.github.kiolk.chemistrytests.ui.activities.TEST_PARAM_INT
import com.github.kiolk.chemistrytests.ui.activities.TestingActivity
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import reversSort

class LatestTestsFragment : Fragment() {

    var mTests: MutableList<TestParams> = mutableListOf()
    var mRecyclerAdapter: AvailableTestRecyclerAdapter? = null
    var mIsTestStart: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        addLastsTests()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_lasts_tests, null)
        addLastsTests(view)
        return view //super.onCreateView(inflater, container, savedInstanceState)
    }

    fun addLastsTests(view : View) {
//        mTests = DBOperations().getAllTestsParams()
//        mTests = reversSort(mTests)
        mRecyclerAdapter = context?.let { AvailableTestRecyclerAdapter(it, mTests) }
        val recycler = view.findViewById<RecyclerView>(R.id.lasts_tests_recycler_view)
        recycler?.layoutManager =LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycler?.adapter = mRecyclerAdapter
        recycler.isNestedScrollingEnabled = true
        recycler?.addOnItemTouchListener(RecyclerTouchListener(context, recycler, object : OnItemClickListener {
            override fun onClick(view: View, position: Int) {
                val intent = Intent(context, TestingActivity::class.java)
                Log.d("MyLogs", "Start test by item $position")
                intent.putExtra(TEST_PARAM_INT, mTests[position])
                startActivity(intent)
                mIsTestStart = true
            }

            override fun onLongClick(view: View, position: Int) {

            }
        }))

        TestsPresenter.setAvailableTests(object :ResultCallback{
            override fun <T> onSuccess(any: T?) {
               val tests = any as MutableList<TestParams>
                tests.forEach { mTests.add(it) }
                recycler?.adapter?.notifyDataSetChanged()
                view.findViewById<ProgressBar>(R.id.update_tests_progress_bar).visibility = View.GONE
            }

            override fun onError() {
            }
        })
    }
}

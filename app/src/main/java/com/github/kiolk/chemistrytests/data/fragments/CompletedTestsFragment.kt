package com.github.kiolk.chemistrytests.data.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.adapters.AvailableTestRecyclerAdapter
import com.github.kiolk.chemistrytests.data.listeners.OnItemClickListener
import com.github.kiolk.chemistrytests.data.listeners.RecyclerTouchListener
import com.github.kiolk.chemistrytests.data.models.ResultInformation

class CompletedTestsFragment : Fragment() {

    var mResults: MutableList<ResultInformation>? = null
    var mAdapter: AvailableTestRecyclerAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_last_user_tests, null)
        return view //super.onCreateView(inflater, container, savedInstanceState)
    }

    fun showResults(results: MutableList<ResultInformation>) {
        mResults = results
        val recyclerView: RecyclerView? = view?.findViewById<RecyclerView>(R.id.last_user_tests_recycler_view)
        recyclerView?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mAdapter = context?.let { AvailableTestRecyclerAdapter(it, null, mResults) }
        recyclerView?.adapter = mAdapter
        context?.let {
            val itemTouchListener = recyclerView?.let { it1 ->
                RecyclerTouchListener(it, it1, object : OnItemClickListener {
                    override fun onClick(view: View, position: Int) {
                        Toast.makeText(context, "Touch on item $position", Toast.LENGTH_SHORT).show()
                    }

                    override fun onLongClick(view: View, position: Int) {
                        Toast.makeText(context, "Long touch on item $position", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            recyclerView?.addOnItemTouchListener(itemTouchListener)
        }
    }
}

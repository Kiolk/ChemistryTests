package com.github.kiolk.chemistrytests.data.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import closeFragment
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.adapters.AvailableTestRecyclerAdapter
import com.github.kiolk.chemistrytests.data.fragments.dialogs.RepeatTestDialog
import com.github.kiolk.chemistrytests.data.listeners.OnItemClickListener
import com.github.kiolk.chemistrytests.data.listeners.RecyclerTouchListener
import com.github.kiolk.chemistrytests.data.models.ResultInformation
import com.github.kiolk.chemistrytests.ui.activities.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import showFragment

class CompletedTestsFragment : Fragment() {

    var mResults: MutableList<ResultInformation>? = null
    var mAdapter: AvailableTestRecyclerAdapter? = null
    var mFragment : ResultFragment = ResultFragment()
    var isShowResult : Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_last_user_tests, null)
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
                        val activityMain = activity as MainActivity
                        val manager = activityMain.supportFragmentManager
                        val container = activity?.main_frame_layout?.id
//                        val fragment = ResultFragment()
                        if (container != null && manager != null) {
                            showFragment(manager, container, mFragment)
//                            val result = Result(Test(params = (results[position].testParams!!)))
//                            result.mResultInfo = results[position]
                            mFragment.showResult(results[position])
                            isShowResult = true
                        }
                    }

                    override fun onLongClick(view: View, position: Int) {
                        Toast.makeText(context, "Long touch on item $position", Toast.LENGTH_SHORT).show()
                        RepeatTestDialog().fromInstance(results[position]).show(activity?.supportFragmentManager, position.toString())
                    }
                })
            }
            recyclerView?.addOnItemTouchListener(itemTouchListener)
        }
    }
    fun closeResult() : Boolean{
        if(isShowResult) {
            val activityMain = activity as MainActivity
            val manager = activityMain.supportFragmentManager
            closeFragment(manager, mFragment)
            mFragment = ResultFragment()
            isShowResult = false
            return true
        }else{
            return false
        }
    }
}

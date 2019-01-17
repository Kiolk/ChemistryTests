package com.github.kiolk.chemistrytests.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import closeFragment
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.adapters.AvailableTestRecyclerAdapter
import com.github.kiolk.chemistrytests.data.asynctasks.ResultCallback
import com.github.kiolk.chemistrytests.data.asynctasks.SingleAsyncTask
import com.github.kiolk.chemistrytests.data.asynctasks.SingleExecut
import com.github.kiolk.chemistrytests.data.executs.UpdateResultInDb
import com.github.kiolk.chemistrytests.ui.fragments.dialogs.RepeatTestDialog
import com.github.kiolk.chemistrytests.data.listeners.OnItemClickListener
import com.github.kiolk.chemistrytests.data.listeners.RecyclerTouchListener
import com.github.kiolk.chemistrytests.data.models.ResultInformation
import com.github.kiolk.chemistrytests.ui.activities.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import showFragment

class CompletedTestsFragment : com.github.kiolk.chemistrytests.ui.fragments.BaseFragment() {
    override val menuId: Int?
        get() = R.id.reset_history_menu_item
    override val titleId: Int
        get() = R.string.HISTORY

    companion object {
        val RESULT_TEST_TAG : String = "ResultTag"
    }

    var mResults: MutableList<ResultInformation>? = null
    var mAdapter: AvailableTestRecyclerAdapter? = null
    var mFragment : ResultFragment = ResultFragment()
    var isShowResult : Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setupToolBar()
        val view = inflater.inflate(R.layout.fragment_last_user_tests, null)
        if(savedInstanceState != null){
            val size = savedInstanceState?.getInt("cnt", 0)
            var cnt = 0
            if(size > 0){
                mResults = mutableListOf()
            }
            while(cnt <size ){
                mResults?.add(savedInstanceState.getSerializable("res" + cnt) as ResultInformation)
                ++cnt
            }
            mResults?.let { showResults(it, view) }
        }
        return view
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val size = savedInstanceState?.getInt("cnt", 0)
        var cnt = 0
        if(size ?: 0 > 0){
            mResults = mutableListOf()
        }
        while(cnt <size ?: 0){
            mResults?.add(savedInstanceState?.getSerializable("res" + cnt) as ResultInformation)
            ++cnt
        }
//        mResults?.let { showResults(it) }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
//        outState.putSerializable("results", mResults)
//        val recyclerView: RecyclerView? = view?.findViewById<RecyclerView>(R.id.last_user_tests_recycler_view)
//        recyclerView?.position
        var cnt = 0
        while(cnt < mResults?.size ?: 0){
            outState.putSerializable("res" + cnt, mResults?.get(cnt))
            ++cnt
        }
        outState.putInt("cnt", cnt)
    }

    fun showResults(results: MutableList<ResultInformation>, fragmentView : View? = null) {
        var view = view
        if(fragmentView != null){
            view = fragmentView
        }
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
                            mFragment = com.github.kiolk.chemistrytests.ui.fragments.ResultFragment()
                            showFragment(manager, container, mFragment, com.github.kiolk.chemistrytests.ui.fragments.CompletedTestsFragment.Companion.RESULT_TEST_TAG)
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

        val simpleItemTouchHalperCallBack : ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
            return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
                val position : Int = viewHolder?.adapterPosition ?: 0
                val resultingInformation = mResults!![position]
                SingleAsyncTask().execute(UpdateResultInDb(resultingInformation, object : ResultCallback{
                    override fun <T> onSuccess(any: T?) {
                    }

                    override fun onError() {
                    }
                }, true))
                mResults?.removeAt(position)
                mAdapter?.notifyDataSetChanged()
                Toast.makeText(context, "Remove item in position $position", Toast.LENGTH_SHORT).show()

            }
        }
        val itemTouchHelper : ItemTouchHelper = ItemTouchHelper(simpleItemTouchHalperCallBack)
        itemTouchHelper.attachToRecyclerView(recyclerView)

    }

    fun closeResult() : Boolean{
        if(isShowResult) {
            val activityMain = activity as MainActivity
            val manager = activityMain.supportFragmentManager
            closeFragment(manager, mFragment)
            mFragment = com.github.kiolk.chemistrytests.ui.fragments.ResultFragment()
            isShowResult = false
            return true
        }else{
            return false
        }
    }
}

package com.github.kiolk.chemistrytests.ui.fragments.statistic

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.adapters.recyclers.TopicsStatisitcRecyclerAdapter
import com.github.kiolk.chemistrytests.data.models.TopicStatisticModel

interface TopicStatisticView{
    fun showTopicStatistic(topicsStatistic : MutableList<TopicStatisticModel>)
}

class TopicStatisticFragment : Fragment(), TopicStatisticView {

    lateinit var mTopicStatistic :  MutableList<TopicStatisticModel>
    lateinit var mView : View

    override fun showTopicStatistic(topicsStatistic: MutableList<TopicStatisticModel>) {
        mTopicStatistic = topicsStatistic
        if(view != null){
            setupTopicsStatistic()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_topic_statistic, null)
//        setupTopicsStatistic()
        return mView
    }

    private fun setupTopicsStatistic() {
        val adapter = context?.let { TopicsStatisitcRecyclerAdapter(it, mTopicStatistic) }
        val recyclerView =  mView?.findViewById<RecyclerView>(R.id.topics_statistic_recycler_view)
        recyclerView?.visibility = View.VISIBLE
        mView?.findViewById<ProgressBar>(R.id.topic_statistic_progress_bar).visibility = View.GONE
        recyclerView?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView?.adapter = adapter
    }
}
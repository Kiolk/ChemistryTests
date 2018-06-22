package com.github.kiolk.chemistrytests.data.adapters.recyclers

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.models.TopicStatisticModel

class TopicsStatisitcRecyclerAdapter(var context : Context, var topicsStatistics : MutableList<TopicStatisticModel>) : RecyclerView.Adapter<TopicsStatisitcRecyclerAdapter.TopicViewHolder>() {

    override fun onBindViewHolder(holder: TopicViewHolder?, position: Int) {
        holder?.mTopicTitle?.text = topicsStatistics[position].mTopic
        holder?.mTopicProgressBar?.max = topicsStatistics[position].mAskedQuestions
        holder?.mTopicProgressBar?.progress = topicsStatistics[position].mCorrectAnswers
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TopicViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.card_topic_statistic, parent, false)
        return TopicViewHolder(view)
    }

    override fun getItemCount(): Int {
        return topicsStatistics.size
    }


    class TopicViewHolder internal constructor(item : View) : RecyclerView.ViewHolder(item){
        var mTopicTitle = item.findViewById<TextView>(R.id.topic_statistic_title_text_view)
        var mTopicProgressBar = item.findViewById<ProgressBar>(R.id.topic_statistic_progress_bar)
    }
}

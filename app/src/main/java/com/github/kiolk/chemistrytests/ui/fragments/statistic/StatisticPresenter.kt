package com.github.kiolk.chemistrytests.ui.fragments.statistic

import android.support.v4.app.Fragment
import com.github.kiolk.chemistrytests.data.asynctasks.ResultCallback
import com.github.kiolk.chemistrytests.data.asynctasks.SingleAsyncTask
import com.github.kiolk.chemistrytests.data.executs.PrepareStatisticExecutble
import com.github.kiolk.chemistrytests.ui.fragments.statistic.StatisticView
import com.github.kiolk.chemistrytests.ui.fragments.statistic.TopicStatisticView
import com.github.kiolk.chemistrytests.data.models.GeneralStatisticModel
import com.github.kiolk.chemistrytests.data.models.StatisticModel
import com.github.kiolk.chemistrytests.data.models.TopicStatisticModel

class StatisticPresenter(private var mGeneralStatistic: StatisticView, private var mTopicStatistic: TopicStatisticView){

    var mStatistic = StatisticModel()
    var mTopicsStatics:  MutableList<TopicStatisticModel> = mutableListOf()
    var isDataSetup : Boolean = false

    fun presentStatistic() : Fragment {
        mGeneralStatistic.setupGeneralStatistic(mStatistic)
        val fragment = mGeneralStatistic as Fragment
        return fragment
    }

   fun prepareStatistic(callback : ResultCallback){
        isDataSetup = true
        SingleAsyncTask().execute(PrepareStatisticExecutble(object : ResultCallback{
            override fun <T> onSuccess(any: T?) {
                val statistic : GeneralStatisticModel = any as GeneralStatisticModel
                mStatistic = statistic.mStatistic
                mTopicsStatics = statistic.mTopicsStatistic
                presentStatistic()
                presentTopicsStatistic()
                callback.onSuccess(statistic)
            }

            override fun onError() {
            }
        }))
    }

    fun presentTopicsStatistic() : Fragment{
        mTopicStatistic.showTopicStatistic(mTopicsStatics)
        return mTopicStatistic as Fragment
    }
}
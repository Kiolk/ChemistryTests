package com.github.kiolk.chemistrytests.data.adapters.pagers

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.github.kiolk.chemistrytests.data.asynctasks.ResultCallback
import com.github.kiolk.chemistrytests.data.fragments.statistic.GeneralStatisticFragment
import com.github.kiolk.chemistrytests.data.fragments.statistic.StatisticPresenter
import com.github.kiolk.chemistrytests.data.fragments.statistic.TopicStatisticFragment

class UserStatisticPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    var generalStatisticFragment: GeneralStatisticFragment = GeneralStatisticFragment()
    var mTopicStatisticFragment: TopicStatisticFragment = TopicStatisticFragment()
    var statisticPresenter: StatisticPresenter


    init {
        statisticPresenter = StatisticPresenter(generalStatisticFragment, mTopicStatisticFragment)
        statisticPresenter.prepareStatistic(object : ResultCallback{
            override fun <T> onSuccess(any: T?) {
                notifyDataSetChanged()
            }


            override fun onError() {
            }

        })
    }

    companion object {
        val GENERAL_STATISTIC: Int = 0
        val TOPICS_STATISTIC: Int = 1
        val NUMBER_VIEW_PAGER_ITEMS: Int = 2
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            GENERAL_STATISTIC -> {
                statisticPresenter.presentStatistic()
            }
            TOPICS_STATISTIC -> {
                statisticPresenter.presentTopicsStatistic()
            }
            else -> {
                return generalStatisticFragment
            }
        }
    }

    override fun getCount(): Int {
        return NUMBER_VIEW_PAGER_ITEMS
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            GENERAL_STATISTIC -> "General"
            TOPICS_STATISTIC -> "Topics"
            //TODO change naming of tab items from resource file
            else -> super.getPageTitle(position)
        }
    }
}
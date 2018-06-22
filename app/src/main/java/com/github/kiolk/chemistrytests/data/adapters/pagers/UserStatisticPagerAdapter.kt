package com.github.kiolk.chemistrytests.data.adapters.pagers

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.github.kiolk.chemistrytests.data.asynctasks.ResultCallback
import com.github.kiolk.chemistrytests.data.fragments.GeneralStatisticFragment
import com.github.kiolk.chemistrytests.data.fragments.TopicStatisticFragment
import com.github.kiolk.chemistrytests.data.presenters.StatisticPresenter

class UserStatisticPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    var generalStatisticFragment: GeneralStatisticFragment = GeneralStatisticFragment()
    var mTopicStatisticFragment: TopicStatisticFragment = TopicStatisticFragment()
    var statisticPresenter: StatisticPresenter


    init {
        statisticPresenter = StatisticPresenter(generalStatisticFragment, mTopicStatisticFragment)
        statisticPresenter.prepareStatistic(object : ResultCallback{
            override fun <T> onSuccess(any: T?) {
                notifyDataSetChanged()
//                statisticPresenter.presentStatistic()
//                statisticPresenter.presentTopicsStatistic()
            }


            override fun onError() {
            }

        })
//        statisticPresenter.presentStatistic()
//        statisticPresenter.presentTopicsStatistic()
    }

    companion object {
        val GENERAL_STATISTIC: Int = 0
        val TOPICS_STATISTIC: Int = 1
        val NUMBER_VIEW_PAGER_ITEMS: Int = 2
    }

//    override fun instantiateItem(container: View, position: Int): Any {
//       return when (position) {
//           GENERAL_STATISTIC -> {
//                statisticPresenter.presentStatistic()
//               generalStatisticFragment
//           }
//           TOPICS_STATISTIC -> {
//                statisticPresenter.presentTopicsStatistic()
//               mTopicStatisticFragment
//           }
//           else -> {
//               return generalStatisticFragment
//           }
//       }
//    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            GENERAL_STATISTIC -> {
                statisticPresenter.presentStatistic()
//                generalStatisticFragment
            }
            TOPICS_STATISTIC -> {
                statisticPresenter.presentTopicsStatistic()
//                mTopicStatisticFragment
            }
            else -> {
                return generalStatisticFragment
            }
        }
    }

    override fun getCount(): Int {
        return NUMBER_VIEW_PAGER_ITEMS
    }
}
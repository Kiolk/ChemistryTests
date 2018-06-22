package com.github.kiolk.chemistrytests.data.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.adapters.pagers.UserStatisticPagerAdapter
import com.github.kiolk.chemistrytests.data.presenters.StatisticPresenter

class UserStatisticFragment : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_user_statistic, null)
        val viewpager = view?.findViewById<ViewPager>(R.id.user_statistic_view_pager)
        val adapter = fragmentManager?.let { UserStatisticPagerAdapter(it) }
//        adapter?.statisticPresenter?.presentStatistic()
//        adapter?.statisticPresenter?.presentTopicsStatistic()
        viewpager?.adapter = adapter
//        adapter?.notifyDataSetChanged()
        return view
    }
}
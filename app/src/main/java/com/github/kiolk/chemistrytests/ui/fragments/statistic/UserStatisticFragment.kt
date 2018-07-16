package com.github.kiolk.chemistrytests.ui.fragments.statistic

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.adapters.pagers.UserStatisticPagerAdapter
import com.github.kiolk.chemistrytests.ui.fragments.bases.BaseViewPagerFragment

class UserStatisticFragment : BaseViewPagerFragment(), StatisticMvpView{

    override val titleId: Int
        get() = R.string.STATISTICS
    override val menuId: Int?
        get() = R.id.reset_history_menu_item

    override fun fragmentLayout(): Int? {
        return R.layout.tabbed_view_pager
    }

    override fun showStatistic() {
        getViewPager()?.adapter = fragmentManager?.let { UserStatisticPagerAdapter(it) }
        getTabLayout()?.setupWithViewPager(getViewPager())
        getProgerssBas()?.visibility = View.GONE
    }

//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        val view = inflater.inflate(R.layout.fragment_user_statistic, null)
//        val viewpager = view?.findViewById<ViewPager>(R.id.user_statistic_view_pager)
//        val adapter = fragmentManager?.let { UserStatisticPagerAdapter(it) }
//        viewpager?.adapter = adapter
//        return view
//    }
}
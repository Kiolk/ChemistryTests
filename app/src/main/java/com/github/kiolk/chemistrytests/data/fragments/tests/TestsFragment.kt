package com.github.kiolk.chemistrytests.data.fragments.tests

import android.view.View
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.adapters.TestsPageAdapter
import com.github.kiolk.chemistrytests.data.fragments.bases.BaseViewPagerFragment

class TestsFragment : BaseViewPagerFragment(), TestMvpView {

    var mAdapter: TestsPageAdapter? = null

    override fun showProgressBar(show: Boolean) {
        if (show) {
            getProgerssBas()?.visibility = View.VISIBLE
        } else {
            getProgerssBas()?.visibility = View.GONE
        }
    }

    override fun fragmentLayout(): Int? {
        return R.layout.tabbed_view_pager
    }


    override fun showTests() {
        mAdapter = context?.let { fragmentManager?.let { it1 -> TestsPageAdapter(it, TestsPresenter.getAvailableTestsFragments(it), it1) } }
        getViewPager()?.adapter = mAdapter
        getTabLayout()?.setupWithViewPager(getViewPager())
        showProgressBar(false)
    }
}
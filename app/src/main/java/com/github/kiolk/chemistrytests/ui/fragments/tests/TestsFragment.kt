package com.github.kiolk.chemistrytests.ui.fragments.tests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.adapters.TestsPageAdapter
import com.github.kiolk.chemistrytests.ui.fragments.bases.BaseViewPagerFragment

class TestsFragment : BaseViewPagerFragment(), TestMvpView {
    override val titleId: Int
        get() = R.string.AVAILABLE_TESTS
    override val menuId: Int?
        get() = R.id.search_menu_item

    var mAdapter: TestsPageAdapter? = null

    override fun showProgressBar(show: Boolean) {
        if (show) {
            getProgerssBas()?.visibility = View.VISIBLE
        } else {
            getProgerssBas()?.visibility = View.GONE
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setupToolBar()
        return super.onCreateView(inflater, container, savedInstanceState)
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
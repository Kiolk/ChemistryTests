package com.github.kiolk.chemistrytests.data.fragments.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.adapters.TestsPageAdapter
import com.github.kiolk.chemistrytests.data.fragments.bases.BaseViewPagerFragment
import com.github.kiolk.chemistrytests.data.models.TestFragmentModel

class HelpFragment : BaseViewPagerFragment(), HelpMvp{

    override val titleId: Int
        get() = R.string.HELP
    override val menuId: Int?
        get() = null

    var presenter : HelpPresenter

    init{
        presenter = HelpPresenter(this)
    }

    override fun showHelpInformation(listFragment : List<TestFragmentModel>) {
        val helpAdapter = context?.let { fragmentManager?.let { it1 -> TestsPageAdapter(it, listFragment, it1) } }
        getViewPager()?.adapter = helpAdapter
        getTabLayout()?.setupWithViewPager(getViewPager())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setupToolBar()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun fragmentLayout(): Int? {
        return R.layout.tabbed_view_pager
    }

}

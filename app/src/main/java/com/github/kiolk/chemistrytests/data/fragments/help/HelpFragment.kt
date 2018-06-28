package com.github.kiolk.chemistrytests.data.fragments.help

import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.adapters.TestsPageAdapter
import com.github.kiolk.chemistrytests.data.fragments.bases.BaseViewPagerFragment
import com.github.kiolk.chemistrytests.data.models.TestFragmentModel

class HelpFragment : BaseViewPagerFragment(), HelpMvp{

    var presenter : HelpPresenter

    init{
        presenter = HelpPresenter(this)
    }

    override fun showHelpInformation(listFragment : List<TestFragmentModel>) {
        val helpAdapter = context?.let { fragmentManager?.let { it1 -> TestsPageAdapter(it, listFragment, it1) } }
        getViewPager()?.adapter = helpAdapter
        getTabLayout()?.setupWithViewPager(getViewPager())
    }

    override fun fragmentLayout(): Int? {
        return R.layout.tabbed_view_pager
    }

}

package com.github.kiolk.chemistrytests.data.fragments.accounts

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.adapters.TestsPageAdapter
import com.github.kiolk.chemistrytests.data.adapters.pagers.BasePageAdapter
import com.github.kiolk.chemistrytests.data.fragments.bases.BaseViewPagerFragment
import com.github.kiolk.chemistrytests.data.models.AcountModel
import com.github.kiolk.chemistrytests.data.models.TestFragmentModel

class PremiumFragment : BaseViewPagerFragment(), AccountMvpView{

    val mPresenter = PremiumPresenter(this)

    override val titleId: Int
        get() = R.string.PREMIUM_ACCOUNT
    override val menuId: Int?
        get() = null

    override fun fragmentLayout(): Int? {
        return R.layout.tabbed_view_pager
    }

    override fun showAccounts(listFragment: List<AcountModel>) {
        val helpAdapter = context?.let { fragmentManager?.let { it1 -> BasePageAdapter(it, listFragment, it1) } }
        getViewPager()?.adapter = helpAdapter
        getTabLayout()?.setupWithViewPager(getViewPager())
        getProgerssBas()?.visibility = View.INVISIBLE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setupToolBar()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

}
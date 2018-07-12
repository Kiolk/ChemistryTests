package com.github.kiolk.chemistrytests.data.adapters.pagers

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.github.kiolk.chemistrytests.data.fragments.LatestTestsFragment
import com.github.kiolk.chemistrytests.data.fragments.accounts.AccountFeaturesFragment
import com.github.kiolk.chemistrytests.data.models.AcountModel
import com.github.kiolk.chemistrytests.data.models.TestFragmentModel

class BasePageAdapter(var context: Context, var listFragments : List<AcountModel>, fm : FragmentManager) : FragmentStatePagerAdapter(fm){

    var mList : MutableList<AccountFeaturesFragment>

    init {
        mList = mutableListOf()
        listFragments.forEach{
            mList.add(AccountFeaturesFragment().fromInstance(it))
        }
    }

    override fun getItem(position: Int): Fragment {
        return mList[position]
    }

    override fun getCount(): Int {
        return mList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return listFragments[position].accountTitle
    }
}
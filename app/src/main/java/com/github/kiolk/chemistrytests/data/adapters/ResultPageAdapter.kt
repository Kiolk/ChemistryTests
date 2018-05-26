package com.github.kiolk.chemistrytests.data.adapters

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.fragments.GenerralResultFragment
import com.github.kiolk.chemistrytests.data.fragments.ResultStatisticFragment
import com.github.kiolk.chemistrytests.data.fragments.TestInfoFragment
import com.github.kiolk.chemistrytests.data.models.Result
import com.github.kiolk.chemistrytests.data.models.ResultInformation

class ResultPageAdapter(var fm : FragmentManager, var result : ResultInformation, var context : Context) : FragmentStatePagerAdapter(fm){

    companion object {
        val GENERAL_TEST_INFORMATION = 0
        val GENERAL_TEST_RESULT = 1
        val RESULT_STATISTIC = 2
        val TAB_ITEMS = 3
    }


    override fun getItem(position: Int): Fragment {
       return when (position) {
            GENERAL_TEST_INFORMATION -> TestInfoFragment().fromInstance(result.testParams!!)
            GENERAL_TEST_RESULT ->  GenerralResultFragment().fromInstance(result)
            RESULT_STATISTIC ->  ResultStatisticFragment().fromInstance(result)
            else -> {
                TestInfoFragment()
            }
        }
    }

    override fun getCount(): Int {
        return TAB_ITEMS
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val titles : Array<String> = context.resources.getStringArray(R.array.RESULT_TAB_TITLES)
        return titles[position]
    }
}

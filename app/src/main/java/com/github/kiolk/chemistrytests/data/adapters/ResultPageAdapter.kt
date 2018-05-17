package com.github.kiolk.chemistrytests.data.adapters

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.fragments.TestInfoFragment
import com.github.kiolk.chemistrytests.data.models.Result

class ResultPageAdapter(var fm : FragmentManager, var result : Result, var context : Context) : FragmentStatePagerAdapter(fm){
    val GENERAL_TEST_INFORMATION = 0
    val GENERAL_TEST_RESULT = 1
    val RESULT_STATISTIC = 2
    val TAB_ITEMS = 3

    override fun getItem(position: Int): Fragment {
       return when (position) {
            GENERAL_TEST_INFORMATION -> TestInfoFragment().fromInstance(result.test)
            GENERAL_TEST_RESULT ->  TestInfoFragment().fromInstance(result.test)
            RESULT_STATISTIC ->  TestInfoFragment().fromInstance(result.test)
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

package com.github.kiolk.chemistrytests.data.adapters

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.github.kiolk.chemistrytests.data.fragments.LatestTestsFragment
import com.github.kiolk.chemistrytests.data.models.TestParams

class TestsPageAdapter(var context: Context, var sortedLists : List<MutableList<TestParams>?>, var fm : FragmentManager) : FragmentStatePagerAdapter(fm){

    var mLastsTestFragment = LatestTestsFragment()

    companion object {
        val LASTS_TEST : Int = 0
    }

    override fun getItem(position: Int): Fragment {
        return when (position){
            LASTS_TEST -> mLastsTestFragment
            else -> {
                LatestTestsFragment()
            }
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return "Lasts"
    }
}
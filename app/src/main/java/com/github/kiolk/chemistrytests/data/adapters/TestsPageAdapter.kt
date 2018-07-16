package com.github.kiolk.chemistrytests.data.adapters

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.github.kiolk.chemistrytests.ui.fragments.LatestTestsFragment
import com.github.kiolk.chemistrytests.data.models.TestFragmentModel
import com.github.kiolk.chemistrytests.data.models.TestParams

class TestsPageAdapter(var context: Context, var listFragments : List<TestFragmentModel>, var fm : FragmentManager) : FragmentStatePagerAdapter(fm){

    var mLastsTestFragment = LatestTestsFragment()

    companion object {
        val LASTS_TEST : Int = 0
        val CUSTOM_TESTS : Int = 1
        val UPDATED_TESTS : Int = 2
        val FAVORITE_TESTS : Int = 3
    }

    override fun getItem(position: Int): Fragment {
        return when (position){
            LASTS_TEST -> listFragments[position].fragment
            CUSTOM_TESTS -> listFragments[position].fragment
            UPDATED_TESTS -> listFragments[position].fragment
            FAVORITE_TESTS -> listFragments[position].fragment
            else -> {
                LatestTestsFragment()
            }
        }
    }

    override fun getCount(): Int {
        return listFragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return listFragments[position].title
    }
}
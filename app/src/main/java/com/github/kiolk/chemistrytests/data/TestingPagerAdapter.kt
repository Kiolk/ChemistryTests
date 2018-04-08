package com.github.kiolk.chemistrytests.data

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import com.github.kiolk.chemistrytests.data.fragments.QuestionFragment

class TestingPagerAdapter(fm : android.support.v4.app.FragmentManager, test : Test) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return QuestionFragment()
    }

    override fun getCount(): Int {
        return 50
    }

    override fun getPageTitle(position: Int): CharSequence {
        return position.toString()
    }
}

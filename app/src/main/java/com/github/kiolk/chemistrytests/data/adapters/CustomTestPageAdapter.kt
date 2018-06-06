package com.github.kiolk.chemistrytests.data.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.github.kiolk.chemistrytests.data.fragments.CustomTest
import com.github.kiolk.chemistrytests.data.fragments.QuestionsListFragment

class CustomTestPageAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    val mCustomFragment = CustomTest()
    val mQuestionsListFragment = QuestionsListFragment()

    companion object {
        val QUESTIONS_LIST: Int = 0
        val CUSTOM_TEST_PARAMS: Int = 1
        val TEST_INFORMATION: Int = 2
        val CUSTOM_TEST_TABS: Int = 3
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            QUESTIONS_LIST -> mQuestionsListFragment
            CUSTOM_TEST_PARAMS -> mCustomFragment
            TEST_INFORMATION -> CustomTest()
            else -> {
                CustomTest()
            }
        }
    }

    override fun getCount(): Int {
        return CUSTOM_TEST_TABS
    }
}
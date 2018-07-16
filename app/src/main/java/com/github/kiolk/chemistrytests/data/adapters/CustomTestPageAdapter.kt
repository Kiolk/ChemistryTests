package com.github.kiolk.chemistrytests.data.adapters

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.ui.fragments.CustomTest
import com.github.kiolk.chemistrytests.ui.fragments.QuestionsListFragment
import com.github.kiolk.chemistrytests.ui.fragments.TestDescriptionFragment

class CustomTestPageAdapter(val context: Context, fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    val mCustomFragment = CustomTest()
    val mQuestionsListFragment = QuestionsListFragment()
    val mTestDescriptionFragment = TestDescriptionFragment()

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
            TEST_INFORMATION -> mTestDescriptionFragment
            else -> {
                CustomTest()
            }
        }
    }

    override fun getCount(): Int {
        return CUSTOM_TEST_TABS
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            QUESTIONS_LIST -> context.resources.getString(R.string.QUESTIONS)
            CUSTOM_TEST_PARAMS -> context.resources.getString(R.string.TEST_PARAMS)
            TEST_INFORMATION -> context.resources.getString(R.string.TEST_DESCRIPTION)
            else -> {
                context.resources.getString(R.string.QUESTIONS)
            }
        }
    }
}
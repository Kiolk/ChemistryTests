package com.github.kiolk.chemistrytests.data

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import com.github.kiolk.chemistrytests.data.fragments.QuestionFragment
import com.github.kiolk.chemistrytests.data.models.Test

class TestingPagerAdapter(fm : android.support.v4.app.FragmentManager, test : Test) : FragmentStatePagerAdapter(fm) {

    lateinit var usingTest : Test

    init {
        usingTest = test
    }

    override fun getItem(position: Int): android.support.v4.app.Fragment {
        return QuestionFragment().fromInctance(usingTest.getQuestion(position))
    }

    override fun getCount(): Int {
        return usingTest.questions.size
    }

    override fun getPageTitle(position: Int): CharSequence {
       return position.toString()
    }
}

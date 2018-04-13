package com.github.kiolk.chemistrytests.data

import android.support.v4.app.FragmentStatePagerAdapter
import com.github.kiolk.chemistrytests.data.fragments.QuestionFragment
import com.github.kiolk.chemistrytests.data.models.Answer
import com.github.kiolk.chemistrytests.data.models.Test
import java.io.Serializable

interface CheckResultListener : Serializable {
    fun onResult(answer : Answer)
}

class TestingPagerAdapter(fm : android.support.v4.app.FragmentManager, test : Test) : FragmentStatePagerAdapter(fm) {

    lateinit var usingTest : Test
//    lateinit var mListener : CheckResultListener

    init {
        usingTest = test
//        mListener = listener
    }

    override fun getItem(position: Int): android.support.v4.app.Fragment {
        return QuestionFragment().fromInstance(usingTest.getQuestion(position))
    }

    override fun getCount(): Int {
        return usingTest.questions.size
    }

    override fun getPageTitle(position: Int): CharSequence {
       return position.toString()
    }
}

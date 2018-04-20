package com.github.kiolk.chemistrytests.data

import android.support.v4.app.FragmentStatePagerAdapter
import com.github.kiolk.chemistrytests.data.fragments.QuestionFragment
import com.github.kiolk.chemistrytests.data.models.ALL_QUESTION
import com.github.kiolk.chemistrytests.data.models.Answer
import com.github.kiolk.chemistrytests.data.models.Test
import java.io.Serializable

interface CheckResultListener : Serializable {
    fun onResult(answer : Answer)
}

class TestingPagerAdapter(fm : android.support.v4.app.FragmentManager,var test : Test) : FragmentStatePagerAdapter(fm) {

    init {

    }

    override fun getItem(position: Int): android.support.v4.app.Fragment {
        return QuestionFragment().fromInstance(test.getQuestion(position))
    }

    override fun getCount(): Int {
        if(test.params.numberOfQuestions == ALL_QUESTION) {
            return test.mSortedQuestions.size
        }else {

            return test.mSortedQuestions.size
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
       return position.toString()
    }
}

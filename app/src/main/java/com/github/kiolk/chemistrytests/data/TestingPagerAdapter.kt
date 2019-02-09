package com.github.kiolk.chemistrytests.data

import android.support.v4.app.FragmentStatePagerAdapter
import com.github.kiolk.chemistrytests.ui.fragments.QuestionAnsweredFragment
import com.github.kiolk.chemistrytests.ui.fragments.QuestionFragment
import com.github.kiolk.chemistrytests.data.models.ALL_QUESTION
import com.github.kiolk.chemistrytests.data.models.Answer
import com.github.kiolk.chemistrytests.data.models.CloseQuestion
import com.github.kiolk.chemistrytests.data.models.Test
import java.io.Serializable

interface CheckResultListener : Serializable {
    fun onResult(answer: Answer)
}

class TestingPagerAdapter(fm: android.support.v4.app.FragmentManager, var test: List<CloseQuestion>,
                          var isResult: Boolean = false,
                          var answers: MutableList<Answer>? = null) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): android.support.v4.app.Fragment {
      //  val isCheckAnswer = (test.contains(answers?.get(position)?.question?.questionId) && (answers?.get(position)?.userAnswers?.contains(-1) != true ||
        val isCheckAnswer =answers != null && (answers?.get(position)?.userAnswers?.contains(-1) != true ||
                answers?.get(position)?.userInput != null)
        return if (isCheckAnswer) {
            QuestionAnsweredFragment().fromInstance(answers!![position])
        } else            if (!isResult) {
                QuestionFragment().fromInstance(test[position])
            } else {
                QuestionAnsweredFragment().fromInstance(answers!![position])
            }
    }

    override fun getItemPosition(`object`: Any): Int {
        return super.getItemPosition(`object`)
    }

    override fun getCount(): Int {
        return test.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return (position + 1).toString()
    }
}

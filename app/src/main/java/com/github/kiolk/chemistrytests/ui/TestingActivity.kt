package com.github.kiolk.chemistrytests.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.CheckResultListener
import com.github.kiolk.chemistrytests.data.TestingPagerAdapter
import com.github.kiolk.chemistrytests.data.fragments.ResultFragment
import com.github.kiolk.chemistrytests.data.models.Answer
import com.github.kiolk.chemistrytests.data.models.OnEndTestListener
import com.github.kiolk.chemistrytests.data.models.Result
import getTrainingTets
import kotlinx.android.synthetic.main.activity_testing.*

class TestingActivity : AppCompatActivity() {

    lateinit var listener: CheckResultListener
    var mResultFragment = ResultFragment()
    lateinit var mResult : Result

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_testing)
        val test = getTrainingTets()
        mResult = Result(test, object : OnEndTestListener{
            override fun endedTest() {
                result_frame_layout.visibility = View.VISIBLE
                showFragment(R.id.result_frame_layout, mResultFragment)
                mResultFragment.showResult(mResult)
            }
        })
        val adapter = TestingPagerAdapter(supportFragmentManager, test)
        listener = object : CheckResultListener {

            override fun onResult(answer: Answer) {
                if (answer.question.checkAnswer(answer.userAnswer)) {
                    mResult.takeAnswer(answer)
                    Toast.makeText(baseContext, "Correct Answer! ${mResult.points.toString()}", Toast.LENGTH_LONG).show()

                } else {
                    mResult.takeAnswer(answer)
                    Toast.makeText(baseContext, "Wrong Answer! ${mResult.points.toString()}", Toast.LENGTH_LONG).show()
                }
            }
        }
        testing_view_pager.adapter = adapter
        questions_tab_layout.setupWithViewPager(testing_view_pager)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(result_frame_layout.visibility == View.VISIBLE){
            closeFragment(mResultFragment)
            result_frame_layout.visibility = View.GONE
        }
    }

    fun showFragment(container: Int, fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(container, fragment)
        transaction.commit()
        supportFragmentManager.executePendingTransactions()
    }

    fun closeFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.remove(fragment)
        transaction.commit()
    }
}

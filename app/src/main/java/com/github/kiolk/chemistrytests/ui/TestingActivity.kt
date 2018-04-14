package com.github.kiolk.chemistrytests.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.webkit.WebView
import android.widget.Toast
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.CheckResultListener
import com.github.kiolk.chemistrytests.data.TestingPagerAdapter
import com.github.kiolk.chemistrytests.data.fragments.ResultFragment
import com.github.kiolk.chemistrytests.data.models.Answer
import com.github.kiolk.chemistrytests.data.models.OnEndTestListener
import com.github.kiolk.chemistrytests.data.models.Result
import getTrainingTets
import kiolk.com.github.pen.utils.MD5Util
import kotlinx.android.synthetic.main.activity_testing.*

class TestingActivity : AppCompatActivity() {

    lateinit var listener: CheckResultListener
    var mResultFragment = ResultFragment()
    lateinit var mResult: Result
    lateinit var adapter: TestingPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_testing)
        val test = getTrainingTets()
        mResult = Result(test, object : OnEndTestListener {
            override fun endedTest() {
                result_frame_layout.visibility = View.VISIBLE
                showFragment(R.id.result_frame_layout, mResultFragment)
                mResultFragment.showResult(mResult)
            }
        })
        adapter = TestingPagerAdapter(supportFragmentManager, test)
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
        if (result_frame_layout.visibility == View.VISIBLE) {
            closeFragment(mResultFragment)
            result_frame_layout.visibility = View.GONE
            return
        }
        if (photo_web_view.visibility == View.VISIBLE) {
            photo_web_view.visibility = View.GONE
            //TODO find solution for jumping of pictures
            return
        }
        super.onBackPressed()
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
    fun showOptionPhoto(pictureUrl : String){
        val urlFolder = baseContext.cacheDir?.canonicalPath
        val url : String = "file://$urlFolder/ImageCache/"
        val tagPhoto = MD5Util.getHashString(pictureUrl)
        val data = "<body bgcolor=\"#000000\"><div class=\"centered-content\" align=\"middle\" ><img src=\"$tagPhoto.jpg\"/></div></body>"
        photo_web_view.loadDataWithBaseURL(url, data, "text/html", "UTF-8", null)
        photo_web_view.settings?.builtInZoomControls = true
        photo_web_view.settings?.displayZoomControls = false
        photo_web_view.settings?.useWideViewPort = true
        photo_web_view.settings?.loadWithOverviewMode = true
        photo_web_view.visibility = View.VISIBLE
    }
}

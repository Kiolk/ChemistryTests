package com.github.kiolk.chemistrytests.ui

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.CheckResultListener
import com.github.kiolk.chemistrytests.data.TestingPagerAdapter
import com.github.kiolk.chemistrytests.data.database.DBOperations
import com.github.kiolk.chemistrytests.data.fragments.HintFragment
import com.github.kiolk.chemistrytests.data.fragments.ResultFragment
import com.github.kiolk.chemistrytests.data.models.*
import com.google.firebase.database.*
import kiolk.com.github.pen.utils.MD5Util
import kotlinx.android.synthetic.main.activity_testing.*

val QUESTIONS_CHILDS : String = "Questions"

class TestingActivity : AppCompatActivity() {



    lateinit var listener: CheckResultListener
    var mResultFragment = ResultFragment()
    var mHintFragment = HintFragment()
    lateinit var mResult: Result
    lateinit var adapter: TestingPagerAdapter
    lateinit var mFirebaseDatabase : FirebaseDatabase
    lateinit var mDatabaseReference : DatabaseReference
    lateinit var mChaildEventListener : ChildEventListener
   lateinit var mParams : TestParams
    var mQuestions : MutableList<CloseQuestion> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_testing)
        mParams = intent.extras.get(TEST_PARAM_INT) as TestParams
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mFirebaseDatabase.getReference().child(QUESTIONS_CHILDS)
        mChaildEventListener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                val  question = p0?.getValue(CloseQuestion::class.java)
                var questionsList = mutableListOf<CloseQuestion>()
                question?.let { questionsList.add(it) }
//                question?.let { mQuestions.add(it) }
                Log.d("MyLogs", question?.questionId?.toString())
                if (question?.questionId == 30){
                    mQuestions = DBOperations().getAllQuestions()
                    questionsList = mQuestions
                    setupTestingViewPAger(questionsList)
                    setupBottomBar()
                }
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
        mDatabaseReference.addChildEventListener(mChaildEventListener)
        questions_app_bar_layout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener{
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                Log.d("MyLogs", "verticalOffset $verticalOffset")
                if(Math.abs(verticalOffset) > questions_app_bar_layout.totalScrollRange.div(2)){
                    questions_tool_bar.setBackgroundColor(Color.RED)
                    bottom_bar_linear_layout.visibility = View.GONE
                }else{
                    questions_tool_bar.setBackgroundColor(Color.GREEN)
                    bottom_bar_linear_layout.visibility = View.VISIBLE
                }
            }
        })
    }

    override fun onBackPressed() {
        if (result_frame_layout.visibility == View.VISIBLE) {
            closeFragment(mResultFragment)
            closeFragment(mHintFragment)
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

    override fun onResume() {
        super.onResume()
//        mDatabaseReference.addChildEventListener(mChaildEventListener)
    }

    override fun onPause() {
        super.onPause()
        mDatabaseReference.removeEventListener(mChaildEventListener)
    }

    private fun setupTestingViewPAger(questionsList: MutableList<CloseQuestion>) {

//        val test = getTrainingTets()
        val test = Test(questionsList, mParams)

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
                if(answer.userInput != null){
                    mResult.takeAnswer(answer)
                    Toast.makeText(baseContext, "Correct Answer! ${mResult.points.toString()}", Toast.LENGTH_LONG).show()
                    return
                }

                if (answer.question.checkCorrectAnswersByNumbers(answer.userAnswers)) {
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

    private fun setupBottomBar(){
        hint_button_image_view.setOnClickListener {
            val position = testing_view_pager.currentItem
            val hint : List<Hint>? = mResult.test.mSortedQuestions[position].hints
            if(hint != null) {
                result_frame_layout.setPadding(0, 0, 0, bottom_bar_linear_layout.height)
                result_frame_layout.visibility = View.VISIBLE
                showFragment(R.id.result_frame_layout, mHintFragment)
                mHintFragment.showHint(hint)
            }
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

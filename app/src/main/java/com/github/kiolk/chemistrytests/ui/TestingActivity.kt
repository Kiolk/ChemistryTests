package com.github.kiolk.chemistrytests.ui

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPager
import android.support.v4.view.ViewPropertyAnimatorListener
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import closeFragment
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.CheckResultListener
import com.github.kiolk.chemistrytests.data.TestingPagerAdapter
import com.github.kiolk.chemistrytests.data.database.DBOperations
import com.github.kiolk.chemistrytests.data.fragments.HintFragment
import com.github.kiolk.chemistrytests.data.fragments.LeaveTestDialog
import com.github.kiolk.chemistrytests.data.fragments.ResultFragment
import com.github.kiolk.chemistrytests.data.fragments.TestInfoFragment
import com.github.kiolk.chemistrytests.data.models.*
import com.github.kiolk.chemistrytests.utils.SlideAnimationUtil
import com.github.kiolk.chemistrytests.utils.SlideAnimationUtil.FASTER
import com.github.kiolk.chemistrytests.utils.SlideAnimationUtil.VERY_FASTER
import kiolk.com.github.pen.utils.MD5Util
import kotlinx.android.synthetic.main.activity_testing.*
import showFragment
import java.util.*

val QUESTIONS_CHILDS: String = "Questions"

class TestingActivity : AppCompatActivity() {



    lateinit var listener: CheckResultListener
    var mResultFragment = ResultFragment()
    var mHintFragment = HintFragment()
    lateinit var mResult: Result
    lateinit var adapter: TestingPagerAdapter
    //    lateinit var mFirebaseDatabase: FirebaseDatabase
//    lateinit var mDatabaseReference: DatabaseReference
//    lateinit var mChaildEventListener: ChildEventListener
    lateinit var mParams: TestParams
    lateinit var mViewPager: ControledViewPager
    var mTestInfo = TestInfoFragment()
    var isShowBottomBar: Boolean = false
    var isShowFAB: Boolean = false
    var isTestEnd: Boolean = false
    var isTestForeground : Boolean = false
    var isTimeEnd : Boolean = false
    var mQuestions: MutableList<CloseQuestion> = mutableListOf()
    var mTimer : Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_testing)
        mParams = intent.extras.get(TEST_PARAM_INT) as TestParams
//        mFirebaseDatabase = FirebaseDatabase.getInstance()
//        mDatabaseReference = mFirebaseDatabase.getReference().child(QUESTIONS_CHILDS)
        mViewPager = findViewById(R.id.testing_view_pager)
        mQuestions = DBOperations().getAllQuestions()
        animIn(end_test_fab)
        animOut(end_test_fab)
//        animIn(bottom_bar_linear_layout)
//        animOut(bottom_bar_linear_layout)
        setupTestingViewPAger(mQuestions)
        setupBottomBar()
//        mChaildEventListener = object : ChildEventListener {
//            override fun onCancelled(p0: DatabaseError?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//
//            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//
//            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//
//            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
////                val  question = p0?.getValue(CloseQuestion::class.java)
////                var questionsList = mutableListOf<CloseQuestion>()
////                question?.let { questionsList.add(it) }
//////                question?.let { mQuestions.add(it) }
////                Log.d("MyLogs", question?.questionId?.toString())
////                if (question?.questionId == 30){
////                    mQuestions = DBOperations().getAllQuestions()
////                    questionsList = mQuestions
////                    setupTestingViewPAger(DBOperations().getAllQuestions())
////                    setupBottomBar()
////                }
//            }
//
//            override fun onChildRemoved(p0: DataSnapshot?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//        }
//        mDatabaseReference.addChildEventListener(mChaildEventListener)
        questions_app_bar_layout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                Log.d("MyLogs", "verticalOffset $verticalOffset")
//                if (Math.abs(verticalOffset) >= questions_app_bar_layout.totalScrollRange.div(0.5F)) {
                if (Math.abs(verticalOffset) >= 55) {
//                    questions_tool_bar.setBackgroundColor(Color.RED)
                    if (isShowBottomBar) {
                        SlideAnimationUtil.slideOutToTop(baseContext, bottom_bar_linear_layout,
                                object : SlideAnimationUtil.SlideAnimationListener {
                                    override fun animationEnd() {
                                        bottom_bar_linear_layout.visibility = View.GONE
                                    }
                                }, FASTER)
                    }
                    isShowBottomBar = false
                } else {
//                    questions_tool_bar.setBackgroundColor(Color.GREEN)
                    bottom_bar_linear_layout.visibility = View.VISIBLE

                    if (!isShowBottomBar) {
                        bottom_bar_linear_layout.visibility = View.VISIBLE
                        SlideAnimationUtil.slideInToTop(baseContext, bottom_bar_linear_layout, null, VERY_FASTER)
                        checkHintPresent()
                    }
                    isShowBottomBar = true
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
        if (test_info_frame_layout.visibility == View.VISIBLE) {
            closeTestInfo()
            return
        }
        if (photo_web_view.visibility == View.VISIBLE) {
            photo_web_view.visibility = View.GONE
            //TODO find solution for jumping of pictures
            return
        }
        if (!isTestEnd) {
            LeaveTestDialog().show(supportFragmentManager, null)
            return
        }
        super.onBackPressed()
    }

    override fun onPause() {


//        if(isTestEnd){
//            closeFragment(mResultFragment)
//        }
        super.onPause()
        isTestForeground = false
//        mDatabaseReference.removeEventListener(mChaildEventListener)
    }



    private fun setupTestingViewPAger(questionsList: MutableList<CloseQuestion>) {

        val test = Test(questionsList, mParams)

        mResult = Result(test, object : OnEndTestListener {
            override fun endedTest() {
                if (!isShowFAB) {
                    end_test_fab.visibility = View.VISIBLE
                    animIn(end_test_fab)
                    animIn(end_test_fab)
                    isShowFAB = true
                    end_test_fab.setImageDrawable(resources.getDrawable(R.drawable.ic_results))
                    end_test_fab.setOnClickListener {
                        animOut(end_test_fab)
                        showResult()
                        isShowFAB = false
                    }
                }

//                result_frame_layout.visibility = View.VISIBLE
//                showFragment(R.id.result_frame_layout, mResultFragment)
//                mResultFragment.showResult(mResult)
//                val resultAdapter : TestingPagerAdapter = TestingPagerAdapter(supportFragmentManager, mResult.test.mSortedQuestions,
//                       true, mResult.userResultAnswers() )
////                testing_view_pager.removeAllViews()
//                testing_view_pager.adapter = resultAdapter
//                questions_tab_layout.setupWithViewPager(testing_view_pager)
//               val group : ViewGroup = questions_tab_layout.getChildAt(0) as ViewGroup
//                var cnt = 0
//                mResult.userResultAnswers().forEach{
//                    if(it.userInput == null) {
//                       if( it.question.checkCorrectAnswers(it.getAnsweredOptions())){
//                           group.getChildAt(cnt).background = resources.getDrawable(R.drawable.area_square_shape_correct)
//                       }else{
//                           group.getChildAt(cnt).background = resources.getDrawable(R.drawable.area_square_shape_wrong)
//                       }
//                    }else{
//                        if(it.question.checkOpenQuestionAnswers(it.userInput)){
//                            group.getChildAt(cnt).background = resources.getDrawable(R.drawable.area_square_shape_correct)
//                        }else{
//                            group.getChildAt(cnt).background = resources.getDrawable(R.drawable.area_square_shape_wrong)
//                        }
//                    }
//                    cnt = cnt + 1
//                }
            }
        })
        mTestInfo = TestInfoFragment().fromInstance(mResult.test.params)
        adapter = TestingPagerAdapter(supportFragmentManager, test.mSortedQuestions)
        listener = object : CheckResultListener {

            override fun onResult(answer: Answer) {
                if (mResult.test.params.testType == TRAINING_TEST) {
                    end_test_fab.setImageDrawable(resources.getDrawable(R.drawable.ic_select))
                    animIn(end_test_fab)
                    end_test_fab.setOnClickListener {
                        showSingleQuestionAnswer(answer)
                        updateViewPagerAdapter()
                        if (
                        mResult.test.params.direction == DIRECT_TEST
                                && end_test_fab.visibility == View.VISIBLE
                                && (testing_view_pager.currentItem < testing_view_pager.adapter?.count?.minus(1) ?: 0)) {
//                                && (mResult.askedQuestions.size != mResult.test.mSortedQuestions.size)) {
//                                ) {
                            end_test_fab.setImageDrawable(resources.getDrawable(R.drawable.ic_dot_right_arrow))
                            animIn(end_test_fab)
                            end_test_fab.setOnClickListener {
                                animOut(end_test_fab)
                                testing_view_pager.currentItem = testing_view_pager.currentItem + 1
                                updateIndicator(indicator_answered_progress_bar, mResult.askedQuestions.size, mResult.test.mSortedQuestions.size)
                            }
                        } else if (mResult.askedQuestions.size != mResult.test.mSortedQuestions.size) {
                            animOut(end_test_fab)
                            updateIndicator(indicator_answered_progress_bar, mResult.askedQuestions.size, mResult.test.mSortedQuestions.size)
                       updateTabLayout()
                        } else {
                            updateIndicator(indicator_answered_progress_bar, mResult.askedQuestions.size, mResult.test.mSortedQuestions.size)
                        updateTabLayout()
                        }
                    }
                } else if (mResult.test.params.direction == DIRECT_TEST && end_test_fab.visibility == View.GONE
                        && (testing_view_pager.currentItem < testing_view_pager.adapter?.count?.minus(1) ?: 0)) {
                    animIn(end_test_fab)
                    end_test_fab.setOnClickListener {
                        animOut(end_test_fab)
                        testing_view_pager.currentItem = testing_view_pager.currentItem + 1
                        updateIndicator(indicator_answered_progress_bar, mResult.askedQuestions.size, mResult.test.mSortedQuestions.size)
                    }
                } else {
                    showSingleQuestionAnswer(answer)
                }
                updateIndicator(indicator_answered_progress_bar, mResult.askedQuestions.size, mResult.test.mSortedQuestions.size)
                updateTabLayout()
            }
        }
        testing_view_pager.adapter = adapter
        if (mResult.test.params.direction == DIRECT_TEST) {
            testing_view_pager.setPagingEnabled(false)
            questions_tab_layout.visibility = View.GONE
        }
        attachViewPagerListener(testing_view_pager)
//        testing_view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
//            override fun onPageScrollStateChanged(state: Int) {
//            }
//
//            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//            }
//
//            override fun onPageSelected(position: Int) {
//                checkHintPresent()
//                if(end_test_fab.visibility == View.VISIBLE && !isTestEnd){
//                    end_test_fab.visibility == View.GONE
//                } else if (end_test_fab.visibility == View.INVISIBLE && !isTestEnd && test.params.testType == TRAINING_TEST){
//
//                }
//            }
//        })
        questions_tab_layout.setupWithViewPager(testing_view_pager)
        questions_tool_bar.title = mParams.testInfo.testTitle


        val naviagationListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (test_info_frame_layout.visibility == View.GONE) {

                    test_info_frame_layout.visibility = View.VISIBLE
                    SlideAnimationUtil.slideInFromLeft(baseContext, test_info_frame_layout, null, FASTER)
                    questions_tool_bar.setNavigationIcon(resources.getDrawable(R.drawable.ic_left_arrow))
                    showFragment(supportFragmentManager, R.id.test_info_frame_layout, mTestInfo)
                    mTestInfo.showTestInfo(mResult.test)
                } else {
                    closeTestInfo()
                }
            }
        }
        questions_tool_bar.setNavigationOnClickListener(naviagationListener)
        if(mParams.testTimer != null){
            mParams.testTimer?.let { startTestTimer(it) }
        }
    }

    private fun startTestTimer(time : Long){
        var timeLength = time
        test_timer_text_view.visibility = View.VISIBLE
        val handler = Handler()
        val task : TimerTask = object : TimerTask(){
            override fun run() {
                handler.post {
                    test_timer_text_view.text = timeLength.toString()
                    timeLength -= 100L
                    if(timeLength < 0 && isTestForeground){
                        showResult()
                        mTimer?.cancel()
                    }else if(timeLength < 0 && !isTestForeground){
                        isTimeEnd = true
                        mTimer?.cancel()
                    }
                }
            }
        }
        mTimer = Timer()
        mTimer?.scheduleAtFixedRate(task, 0, 100)
    }

    private fun updateViewPagerAdapter(){
        val position : Int = testing_view_pager.currentItem
        val updatedAdapter: TestingPagerAdapter = TestingPagerAdapter(supportFragmentManager, mResult.test.mSortedQuestions,
                false, mResult.userResultAnswers())
        attachViewPagerListener(testing_view_pager)
        testing_view_pager.adapter = updatedAdapter
        testing_view_pager.currentItem = position
    }

    private fun attachViewPagerListener(pager : ViewPager){
        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                if(mResult.test.params.testType == TRAINING_TEST && mResult.test.params.direction == DIRECT_ORDER){
                    return
                }
                checkHintPresent()
                if(end_test_fab.visibility == View.VISIBLE && !isTestEnd && mResult.test.mSortedQuestions.size != mResult.askedQuestions.size){
//                    end_test_fab.visibility = View.GONE
                    animOut(end_test_fab)
                } else if (end_test_fab.visibility == View.GONE && !isTestEnd && mResult.test.params.testType == TRAINING_TEST){
                    val group: ViewGroup = questions_tab_layout.getChildAt(0) as ViewGroup
                    if(group.getChildAt(position).background == resources.getDrawable(R.drawable.area_select_answer)){
                        animIn(end_test_fab)
                    }
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        mTimer?.cancel()
    }

    override fun onResume() {
        super.onResume()
        isTestForeground = true
        if(isTimeEnd){
            showResult()
        }
    }

    private fun closeTestInfo() {
//        test_info_frame_layout.visibility = View.GONE
        SlideAnimationUtil.slideOutToLeft(baseContext, test_info_frame_layout, object : SlideAnimationUtil.SlideAnimationListener {
            override fun animationEnd() {
                test_info_frame_layout.visibility = View.GONE
                questions_tool_bar.setNavigationIcon(resources.getDrawable(R.drawable.ic_menu))
                closeFragment(supportFragmentManager, mTestInfo)
            }
        }, FASTER)

    }

    private fun showSingleQuestionAnswer(answer: Answer) {
        if (answer.userInput != null) {
            mResult.takeAnswer(answer)
            Toast.makeText(baseContext, "Correct Answer! ${mResult.points.toString()}", Toast.LENGTH_LONG).show()
            return
        }

        if (answer.question.checkCorrectAnswersByNumbers(answer.userAnswers)) {
            mResult.takeAnswer(answer)
            Toast.makeText(baseContext, "Correct Answer! ${mResult.points.toString()}", Toast.LENGTH_LONG).show()
        } else if (answer.userAnswers.isEmpty()) {
            mResult.removeEmptyAnswer(answer)
        } else {
            mResult.takeAnswer(answer)
            Toast.makeText(baseContext, "Wrong Answer! ${mResult.points.toString()}", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateSelectTabLayout(){
        val position: Int = testing_view_pager.currentItem
        val group: ViewGroup = questions_tab_layout.getChildAt(0) as ViewGroup
        group.getChildAt(position).background = resources.getDrawable(R.drawable.area_select_answer)
    }

    private fun updateTabLayout() {
        val position: Int = testing_view_pager.currentItem
        if (mResult.askedQuestions.find { it.question == mResult.test.mSortedQuestions[position] } != null) {
            val group: ViewGroup = questions_tab_layout.getChildAt(0) as ViewGroup
            group.getChildAt(position).background = resources.getDrawable(R.drawable.area_take_answer)
        } else {
            val group: ViewGroup = questions_tab_layout.getChildAt(0) as ViewGroup
            group.getChildAt(position).background = resources.getDrawable(R.drawable.area_not_answer)
        }
//        testing_view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
//            override fun onPageScrollStateChanged(state: Int) {
//            }
//
//            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//                Log.d("MyLogs", "onPageScrolled $position")
//                if(mResult.askedQuestions.find { it.question ==  mResult.test.mSortedQuestions[position]} != null){
//                    val group: ViewGroup = questions_tab_layout.getChildAt(0) as ViewGroup
//                    group.getChildAt(position).background = resources.getDrawable(R.drawable.area_square_shape_correct)
//                }else{
//                    val group: ViewGroup = questions_tab_layout.getChildAt(0) as ViewGroup
//                    group.getChildAt(position).background = resources.getDrawable(R.drawable.area_square_shape_wrong)
//                }
//            }
//
//            override fun onPageSelected(position: Int) {
//                Log.d("MyLogs", "onPageSelected $position")
//            }
//        })
    }

    private fun setupBottomBar() {
        setupHintListener()
        periodical_table_image_view.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                showPeriodicTable("table")
            }
        })
        solubility_button_image_view.setOnClickListener {
            showPeriodicTable("solubility")
        }
    }

    private fun checkHintPresent() {
        val position = testing_view_pager.currentItem
        val hint: List<Hint>? = mResult.test.mSortedQuestions[position].hints
        if (hint == null) {
            hint_button_image_view.background = resources.getDrawable(R.drawable.ic_help_inactive)
            hint_button_image_view.setOnClickListener(null)
        } else {
            hint_button_image_view.background = resources.getDrawable(R.drawable.ic_help)
            setupHintListener()
        }
    }

    private fun setupHintListener(){
        hint_button_image_view.setOnClickListener {
            val position = testing_view_pager.currentItem
            val hint: List<Hint>? = mResult.test.mSortedQuestions[position].hints
            if (hint != null) {
                result_frame_layout.setPadding(0, 0, 0, bottom_bar_linear_layout.height)
                if(result_frame_layout.visibility != View.VISIBLE) {
                    result_frame_layout.visibility = View.VISIBLE
                    showFragment(R.id.result_frame_layout, mHintFragment)
                    mHintFragment.showHint(hint)
                }
            }
        }
    }

    fun showResult() {
        mTimer?.cancel()
        isTestEnd = true
        result_frame_layout.visibility = View.VISIBLE
        showFragment(R.id.result_frame_layout, mResultFragment)
        mResult.writeResultInformation()
        mResultFragment.showResult(mResult)
        val resultAdapter: TestingPagerAdapter = TestingPagerAdapter(supportFragmentManager, mResult.test.mSortedQuestions,
                true, mResult.userResultAnswers())
        testing_view_pager.adapter = resultAdapter
        testing_view_pager.setPagingEnabled(true)
        questions_tab_layout.visibility = View.VISIBLE
        questions_tab_layout.setupWithViewPager(testing_view_pager)
        val group: ViewGroup = questions_tab_layout.getChildAt(0) as ViewGroup
        var cnt = 0
        mResult.userResultAnswers().forEach {
            if (it.userInput == null) {
                if (it.question.checkCorrectAnswers(it.getAnsweredOptions())) {
                    group.getChildAt(cnt).background = resources.getDrawable(R.drawable.area_square_shape_correct)
                } else {
                    group.getChildAt(cnt).background = resources.getDrawable(R.drawable.area_square_shape_wrong)
                }
            } else {
                if (it.question.checkOpenQuestionAnswers(it.userInput)) {
                    group.getChildAt(cnt).background = resources.getDrawable(R.drawable.area_square_shape_correct)
                } else {
                    group.getChildAt(cnt).background = resources.getDrawable(R.drawable.area_square_shape_wrong)
                }
            }
            cnt = cnt + 1
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

    fun showOptionPhoto(pictureUrl: String) {
        val urlFolder = baseContext.cacheDir?.canonicalPath
        val url: String = "file://$urlFolder/ImageCache/"
        val tagPhoto = MD5Util.getHashString(pictureUrl)
        val data = "<body bgcolor=\"#000000\"><div class=\"centered-content\" align=\"middle\" ><img src=\"$tagPhoto.jpg\"/></div></body>"
        photo_web_view.loadDataWithBaseURL(url, data, "text/html", "UTF-8", null)
        photo_web_view.settings?.builtInZoomControls = true
        photo_web_view.settings?.displayZoomControls = false
        photo_web_view.settings?.useWideViewPort = true
        photo_web_view.settings?.loadWithOverviewMode = true
        photo_web_view.visibility = View.VISIBLE
    }

    fun showPeriodicTable(picture: String) {
        val urlFolder = baseContext.cacheDir?.canonicalPath
        val url: String = "file:///android_asset/"
        val data = "<body bgcolor=\"#000000\"><div class=\"centered-content\" align=\"middle\" ><img src=\"$picture.png\"/></div></body>"
        photo_web_view.loadDataWithBaseURL(url, data, "text/html", "UTF-8", null)
        photo_web_view.settings?.builtInZoomControls = true
        photo_web_view.settings?.displayZoomControls = false
        photo_web_view.settings?.useWideViewPort = true
        photo_web_view.settings?.loadWithOverviewMode = false
        photo_web_view.visibility = View.VISIBLE
    }

}

private fun updateIndicator(progressBar: ProgressBar?, size: Int, total: Int) {
    val percentAnswered: Int = size.times(100).div(total)
    progressBar?.progress = percentAnswered
}

fun animOut(view: View) {
    ViewCompat.animate(view)
            .scaleX(0.0F)
            .scaleY(0.0F)
            .alpha(0.0F)
            .setInterpolator(FastOutSlowInInterpolator())
            .withLayer()
            .setListener(object : ViewPropertyAnimatorListener {
                override fun onAnimationEnd(view: View?) {
                    view?.visibility = View.GONE
                }

                override fun onAnimationCancel(view: View?) {
                }

                override fun onAnimationStart(view: View?) {

                }
            })
            .start()
}

fun animIn(view: View) {
    view.visibility = View.VISIBLE
    ViewCompat.animate(view)
            .scaleX(1.0F)
            .scaleY(1.0F)
            .alpha(1.0F)
            .setInterpolator(FastOutSlowInInterpolator())
            .withLayer()
            .setListener(object : ViewPropertyAnimatorListener {
                override fun onAnimationEnd(view: View?) {
                }

                override fun onAnimationCancel(view: View?) {
                }

                override fun onAnimationStart(view: View?) {

                }
            })
            .start()
}

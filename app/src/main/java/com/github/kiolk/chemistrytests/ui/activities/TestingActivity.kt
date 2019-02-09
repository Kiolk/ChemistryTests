package com.github.kiolk.chemistrytests.ui.activities

import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import closeFragment
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.CheckResultListener
import com.github.kiolk.chemistrytests.data.TestingPagerAdapter
import com.github.kiolk.chemistrytests.data.asynctasks.ResultCallback
import com.github.kiolk.chemistrytests.data.asynctasks.SingleAsyncTask
import com.github.kiolk.chemistrytests.data.database.DBOperations
import com.github.kiolk.chemistrytests.data.executs.UpdateResultInDb
import com.github.kiolk.chemistrytests.data.models.*
import com.github.kiolk.chemistrytests.ui.activities.base.BaseActivity
import com.github.kiolk.chemistrytests.ui.customviews.ControledViewPager
import com.github.kiolk.chemistrytests.ui.fragments.ChemTheoryFragment
import com.github.kiolk.chemistrytests.ui.fragments.ChemTheoryFragment.Companion.THEORY_FRAGMENT_TAG
import com.github.kiolk.chemistrytests.ui.fragments.HintFragment
import com.github.kiolk.chemistrytests.ui.fragments.ResultFragment
import com.github.kiolk.chemistrytests.ui.fragments.TestInfoFragment
import com.github.kiolk.chemistrytests.ui.fragments.dialogs.LeaveTestDialog
import com.github.kiolk.chemistrytests.utils.CONSTANTS.TIMER_PATTERN
import com.github.kiolk.chemistrytests.utils.ChartHelper.PERIODIC_TABLE_NAME
import com.github.kiolk.chemistrytests.utils.ChartHelper.SOLUBILITY_CHART_NAME
import com.github.kiolk.chemistrytests.utils.ChartHelper.showWebView
import com.github.kiolk.chemistrytests.utils.Constants.MIN_PERCENT_FOR_SAVE_RESULT
import com.github.kiolk.chemistrytests.utils.Constants.TEST_PARAM_INT
import com.github.kiolk.chemistrytests.utils.SlideAnimationUtil
import com.github.kiolk.chemistrytests.utils.SlideAnimationUtil.FASTER
import com.github.kiolk.chemistrytests.utils.SlideAnimationUtil.VERY_FASTER
import com.github.kiolk.chemistrytests.utils.animIn
import com.github.kiolk.chemistrytests.utils.animOut
import com.github.kiolk.chemistrytests.utils.convertEpochTime
import kiolk.com.github.pen.utils.MD5Util
import kotlinx.android.synthetic.main.activity_testing.*
import kotlinx.android.synthetic.main.tabbed_view_pager.*
import showFragment
import java.util.*

class TestingActivity : BaseActivity() {

    companion object {
        val QUESTION_BNDL: String = "askedQuestion"
        val SORTED_QUESTION_BNDL: String = "sortedQuestion"
        val CURRENT_POSITION_BNDL: String = "currentPosition"
        val TEST_START_TIME_BNDL: String = "startTime"
        val IS_TEST_END_BNDL: String = "isTestEnd"
        val TIMER_VALUE_BNDL : String ="timer"
    }

    lateinit var listener: CheckResultListener
    var mResultFragment = ResultFragment()
    var mHintFragment = HintFragment()
    var mTheoryFragment = ChemTheoryFragment()
    lateinit var mResult: Result
    lateinit var adapter: TestingPagerAdapter
    lateinit var mParams: TestParams
    lateinit var mViewPager: ControledViewPager
    var mStartTest: Long = 0L
    var mTestInfo = TestInfoFragment()
    var isShowBottomBar: Boolean = false
    var isShowFAB: Boolean = false
    var isTestEnd: Boolean = false
    var isTestForeground: Boolean = false
    var isTimeEnd: Boolean = false
    var isTheoryShow: Boolean = false
    var mQuestions: MutableList<CloseQuestion> = mutableListOf()
    var mSvedSortedQuestion: MutableList<CloseQuestion>? = null
    var mSavedAskedQuestion: MutableList<Answer>? = null
    var mTimer: Timer? = null
    var pagelistener: ViewPager.OnPageChangeListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_testing)
        Log.d("MyLogs", "onCreate")
        mParams = intent.extras.get(TEST_PARAM_INT) as TestParams
        mStartTest = System.currentTimeMillis()
        mViewPager = findViewById(R.id.testing_view_pager)
        mQuestions = DBOperations().getAllQuestions()
        animIn(end_test_fab)
        animOut(end_test_fab)
    }

    private fun setupTabBar() {
        questions_app_bar_layout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                Log.d("MyLogs", "verticalOffset $verticalOffset")
                if (Math.abs(verticalOffset) >= 55) {
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
                    bottom_bar_linear_layout.visibility = View.VISIBLE

                    if (!isShowBottomBar) {
                        bottom_bar_linear_layout.visibility = View.VISIBLE
                        SlideAnimationUtil.slideInToTop(baseContext, bottom_bar_linear_layout, null, VERY_FASTER)
                        checkHintPresent()
                        checkTheoryPresent()
                        checkExplanationPresent()
                    }
                    isShowBottomBar = true
                }
            }
        })
    }


    override fun onStart() {
        super.onStart()
        Log.d("MyLogs", "onStart")
//        setupTestingViewPager(mQuestions)
//        setupBottomBar()
//        setupTabBar()
//        if (mSavedAskedQuestion != null) {
//            mResult.askedQuestions = mSavedAskedQuestion as MutableList<Answer>
//            updateViewPagerAdapter()
//        }
        questionSetup()
    }

    fun questionSetup() {
        Log.d("MyLogs", "onQuestionSetup")
        setupTestingViewPager(mQuestions)
        setupBottomBar()
        setupTabBar()
        if (mSavedAskedQuestion != null) {
            mResult.askedQuestions = mSavedAskedQuestion as MutableList<Answer>
            updateViewPagerAdapter()
        }

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d("MyLogs", "onRestoreSingle")
        val askedQuestions: MutableList<Answer> = mutableListOf()
        var cnt = 0
        var answer: Answer? = null
        mSavedAskedQuestion = mutableListOf()
        do {
            answer = savedInstanceState?.getSerializable(QUESTION_BNDL + cnt) as? Answer
            if (answer != null) {
//                askedQuestions.add(answer)
                answer.let { mSavedAskedQuestion?.add(it) }
            }
            ++cnt
        } while (answer != null)
//        mResult.askedQuestions = askedQuestions

        cnt = 0
        var question: CloseQuestion? = null
        mSvedSortedQuestion = mutableListOf()
        do {
            question = savedInstanceState?.getSerializable(SORTED_QUESTION_BNDL + cnt) as? CloseQuestion
            ++cnt
            question?.let { mSvedSortedQuestion?.add(it) }
        } while (question != null)
        isTestEnd = savedInstanceState?.getBoolean(IS_TEST_END_BNDL) ?: false
        questionSetup()
        reStoreTabLayout()
        testing_view_pager.currentItem = savedInstanceState?.getInt(CURRENT_POSITION_BNDL) as Int
        mResult.mStartTestTime = savedInstanceState.getLong(TEST_START_TIME_BNDL)
        mResult.writeResultInformation()
        if (isTestEnd) {
            showResult(false)
        }
        if(mResult.test.params.testTimer != null && !isTestEnd && !isTestEnd){
            startTestTimer(savedInstanceState.getLong(TIMER_VALUE_BNDL))
        }
    }

    private fun reStoreTabLayout() {
        var count = questions_tab_layout.tabCount
        val resultAnswer = mResult.userResultAnswers()
        while (count > 0) {
            --count
            if (resultAnswer[count].userInput != null || !resultAnswer[count].userAnswers.contains(-1)) {
                val group: ViewGroup = questions_tab_layout.getChildAt(0) as ViewGroup
                group.getChildAt(count).background = resources.getDrawable(R.drawable.area_take_answer)
            }
        }
    }

    private fun setubTabBackground(){
        var count = questions_tab_layout.tabCount
        --count
        while(count >= 0){
            val group: ViewGroup = questions_tab_layout.getChildAt(0) as ViewGroup
            group.getChildAt(count).background = resources.getDrawable(R.drawable.area_not_answer)
            --count
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        Log.d("MyLogs", "onSaveSingle")
        var cnt = 0
        mResult.askedQuestions.forEach {
            outState?.putSerializable(QUESTION_BNDL + cnt, mResult.askedQuestions[cnt])
            ++cnt
        }
        cnt = 0
        mResult.test.mSortedQuestions.forEach {
            outState?.putSerializable(SORTED_QUESTION_BNDL + cnt, it)
            ++cnt
        }
        outState?.putInt(CURRENT_POSITION_BNDL, testing_view_pager.currentItem)
        outState?.putLong(TEST_START_TIME_BNDL, mResult.mStartTestTime)
        outState?.putBoolean(IS_TEST_END_BNDL, isTestEnd)
        val timerTime : String? = test_timer_text_view.tag as? String
        if(timerTime != null) {
            outState?.putLong(TIMER_VALUE_BNDL, timerTime.toLong())
        }
    }

    override fun onBackPressed() {
        if (photo_web_view.visibility == View.VISIBLE) {
            photo_web_view.visibility = View.GONE
            //TODO find solution for jumping of pictures
            return
        }
        if (result_frame_layout.visibility == View.VISIBLE) {
            val theoryFragment = supportFragmentManager.findFragmentByTag(THEORY_FRAGMENT_TAG)
            if (theoryFragment != null && isTheoryShow) {
                closeFragment(theoryFragment)
                isTheoryShow = false
                return
            }
            closeFragment(mResultFragment)
            closeFragment(mHintFragment)
            closeFragment(mTheoryFragment)
            result_frame_layout.visibility = View.GONE
            return
        }
        if (test_info_frame_layout.visibility == View.VISIBLE) {
            closeTestInfo()
            return
        }
        if (!isTestEnd) {
            LeaveTestDialog().show(supportFragmentManager, null)
            return
        }
        super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        isTestForeground = false
    }


    private fun setupTestingViewPager(questionsList: MutableList<CloseQuestion>) {

        val test = Test(questionsList, mParams)

        if (mSvedSortedQuestion != null) {
            test.mSortedQuestions = mSvedSortedQuestion as MutableList<CloseQuestion>
        }

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
                        showResult(true)
                        isShowFAB = false
                    }
                }
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
        questions_tab_layout.setupWithViewPager(testing_view_pager)
        questions_tool_bar.title = mParams.testInfo.testTitle
        setubTabBackground()


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
        if (mParams.testTimer != null) {
            mParams.testTimer?.let { startTestTimer(it) }
        }
    }

    private fun startTestTimer(time: Long) {
        var timeLength = time
        test_timer_text_view.visibility = View.VISIBLE
        val handler = Handler()
        val task: TimerTask = object : TimerTask() {
            override fun run() {
                handler.post {
                    test_timer_text_view.text = convertEpochTime(timeLength, baseContext, TIMER_PATTERN,  true)
                    test_timer_text_view.tag = timeLength.toString()
                    timeLength -= 100L
                    if (timeLength < 0 && isTestForeground) {
                        showResult(true)
                        mTimer?.cancel()
                    } else if (timeLength < 0 && !isTestForeground) {
                        isTimeEnd = true
                        mTimer?.cancel()
                    }
                }
            }
        }
        mTimer?.cancel()
        mTimer = Timer()
        mTimer?.scheduleAtFixedRate(task, 0, 100)
    }

    private fun updateViewPagerAdapter() {
        val position: Int = testing_view_pager.currentItem
        val updatedAdapter: TestingPagerAdapter = TestingPagerAdapter(supportFragmentManager, mResult.test.mSortedQuestions,
                false, mResult.userResultAnswers())
        attachViewPagerListener(testing_view_pager)
        testing_view_pager.adapter = updatedAdapter
        testing_view_pager.currentItem = position
    }

    private fun attachViewPagerListener(pager: ViewPager) {
        pagelistener?.let { pager.removeOnPageChangeListener(it) }
        pagelistener = object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                if (mResult.test.params.testType == TRAINING_TEST && mResult.test.params.direction == DIRECT_ORDER) {
                    return
                }
                checkHintPresent()
                checkTheoryPresent()
                checkExplanationPresent()
                if (end_test_fab.visibility == View.VISIBLE && !isTestEnd && mResult.test.mSortedQuestions.size != mResult.askedQuestions.size) {
//                    end_test_fab.visibility = View.GONE
                    animOut(end_test_fab)
                } else if (end_test_fab.visibility == View.GONE && !isTestEnd && mResult.test.params.testType == TRAINING_TEST) {
                    val group: ViewGroup = questions_tab_layout.getChildAt(0) as ViewGroup
                    if (group.getChildAt(position).background == resources.getDrawable(R.drawable.area_select_answer)) {
                        animIn(end_test_fab)
                    }
                }
            }
        }
        pager.addOnPageChangeListener(pagelistener as ViewPager.OnPageChangeListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        mTimer?.cancel()
    }

    override fun onResume() {
        super.onResume()
        isTestForeground = true
        if (isTimeEnd) {
            showResult(true)
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
           // Toast.makeText(baseContext, "Correct Answer! ${mResult.points.toString()}", Toast.LENGTH_LONG).show()
            return
        }

        if (answer.question.checkCorrectAnswersByNumbers(answer.userAnswers)) {
            mResult.takeAnswer(answer)
            //Toast.makeText(baseContext, "Correct Answer! ${mResult.points.toString()}", Toast.LENGTH_LONG).show()
        } else if (answer.userAnswers.isEmpty()) {
            mResult.removeEmptyAnswer(answer)
        } else {
            mResult.takeAnswer(answer)
          //  Toast.makeText(baseContext, "Wrong Answer! ${mResult.points.toString()}", Toast.LENGTH_LONG).show()
        }
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
    }

    private fun setupBottomBar() {
        setupHintListener()
        periodical_table_image_view.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                showWebView(baseContext, PERIODIC_TABLE_NAME, photo_web_view)
            }
        })
        solubility_button_image_view.setOnClickListener {
            showWebView(baseContext, SOLUBILITY_CHART_NAME, photo_web_view)
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

    private fun checkTheoryPresent() {
        val position = testing_view_pager.currentItem
        val theoryListId: List<Long>? = mResult.test.mSortedQuestions[position].theoryListId
        if (theoryListId == null || theoryListId.isEmpty()) {
            theory_button_image_view.background = resources.getDrawable(R.drawable.ic_theory_gray)
            theory_button_image_view.setOnClickListener(null)
        } else {
            theory_button_image_view.background = resources.getDrawable(R.drawable.ic_theory)
            setupTheory()
        }
    }

    private fun checkExplanationPresent() {
        if (isTestEnd) {
            explanation_linear_layout.visibility = View.VISIBLE
            val position = testing_view_pager.currentItem
            val explanationList: List<Hint>? = mResult.test.mSortedQuestions[position].answerExplanations
            if (explanationList == null || explanationList.isEmpty()) {
                explanation_button_image_view.background = resources.getDrawable(R.drawable.ic_explanation_gray)
                explanation_button_image_view.setOnClickListener(null)
            } else {
                explanation_button_image_view.background = resources.getDrawable(R.drawable.ic_explanation)
                setupExplanation()
            }
        }
    }

    private fun setupExplanation() {
        explanation_button_image_view.setOnClickListener {
            val position = testing_view_pager.currentItem
            val explanationList: List<Hint>? = mResult.test.mSortedQuestions[position].answerExplanations
            if (explanationList != null && explanationList.isNotEmpty()) {
                result_frame_layout.setPadding(0, 0, 0, bottom_bar_linear_layout.height)
                if (result_frame_layout.visibility != View.VISIBLE) {
                    result_frame_layout.visibility = View.VISIBLE
                    showFragment(R.id.result_frame_layout, mHintFragment)
                    mHintFragment.showHint(explanationList)
                }
            }
        }
    }

    private fun setupTheory() {
        theory_button_image_view.setOnClickListener {
            val position = testing_view_pager.currentItem
            val theoryListId: List<Long>? = mResult.test.mSortedQuestions[position].theoryListId
            if (theoryListId != null && theoryListId.isNotEmpty()) {
                result_frame_layout.setPadding(0, 0, 0, bottom_bar_linear_layout.height)
                if (result_frame_layout.visibility != View.VISIBLE) {
                    result_frame_layout.visibility = View.VISIBLE
                    showFragment(R.id.result_frame_layout, mTheoryFragment)
                    mTheoryFragment.setTheory(theoryListId)
                }
            }
        }
    }

    private fun setupHintListener() {
        hint_button_image_view.setOnClickListener {
            val position = testing_view_pager.currentItem
            val hint: List<Hint>? = mResult.test.mSortedQuestions[position].hints
            if (hint != null) {
                result_frame_layout.setPadding(0, 0, 0, bottom_bar_linear_layout.height)
                if (result_frame_layout.visibility != View.VISIBLE) {
                    result_frame_layout.visibility = View.VISIBLE
                    showFragment(R.id.result_frame_layout, mHintFragment)
                    mHintFragment.showHint(hint)
                }
            }
        }
    }

    fun showResult(isFirstTime : Boolean) {
        mTimer?.cancel()
        isTestEnd = true
        mResult.writeResultInformation()
        indicator_line_linear_layout.visibility = View.GONE
//        mResult.mResultInfo.startTime = mStartTesindicator_answered_progress_bar.t
//        mResult.mResultInfo.endTime = System.currentTimeMillis()
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
        if(isFirstTime){
            result_frame_layout.visibility = View.VISIBLE
            showFragment(R.id.result_frame_layout, mResultFragment)
            mResultFragment.showResult(mResult.mResultInfo)
        }
        if (mResult.mResultInfo.percentAsked >= MIN_PERCENT_FOR_SAVE_RESULT && isFirstTime) {
            addResultForUserProfile()
        }
    }

    private fun addResultForUserProfile() {
        SingleAsyncTask().execute(UpdateResultInDb(mResult.mResultInfo, object : ResultCallback {
            override fun <T> onSuccess(any: T?) {
            }

            override fun onError() {
            }
        }))
    }

    fun showFragment(container: Int, fragment: Fragment, fragmentTag: String? = null) {
        val transaction = supportFragmentManager.beginTransaction()
        if (fragmentTag != null) {
            transaction.add(container, fragment, fragmentTag)
            transaction.addToBackStack(fragmentTag)
        } else {
            transaction.add(container, fragment)
        }
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

    private fun updateIndicator(progressBar: ProgressBar?, size: Int, total: Int) {
        indicator_line_linear_layout.visibility=View.VISIBLE
        val percentAnswered: Int = size.times(100).div(total)
        progressBar?.progress = percentAnswered
    }
}

//fun animOut(view: View) {
//    ViewCompat.animate(view)
//            .scaleX(0.0F)
//            .scaleY(0.0F)
//            .alpha(0.0F)
//            .setInterpolator(FastOutSlowInInterpolator())
//            .withLayer()
//            .setListener(object : ViewPropertyAnimatorListener {
//                override fun onAnimationEnd(view: View?) {
//                    view?.visibility = View.GONE
//                }
//
//                override fun onAnimationCancel(view: View?) {
//                }
//
//                override fun onAnimationStart(view: View?) {
//
//                }
//            })
//            .start()
//}
//
//fun animIn(view: View) {
//    view.visibility = View.VISIBLE
//    ViewCompat.animate(view)
//            .scaleX(1.0F)
//            .scaleY(1.0F)
//            .alpha(1.0F)
//            .setInterpolator(FastOutSlowInInterpolator())
//            .withLayer()
//            .setListener(object : ViewPropertyAnimatorListener {
//                override fun onAnimationEnd(view: View?) {
//                }
//
//                override fun onAnimationCancel(view: View?) {
//                }
//
//                override fun onAnimationStart(view: View?) {
//
//                }
//            })
//            .start()
//}

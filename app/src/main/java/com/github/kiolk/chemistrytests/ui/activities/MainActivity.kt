package com.github.kiolk.chemistrytests.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import closeFragment
import com.firebase.ui.auth.AuthUI
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.adapters.CoursesViewPagerAdapter
import com.github.kiolk.chemistrytests.data.asynctasks.ResultCallback
import com.github.kiolk.chemistrytests.data.asynctasks.SingleAsyncTask
import com.github.kiolk.chemistrytests.data.database.DBOperations
import com.github.kiolk.chemistrytests.data.executs.PrepareCoursesFromDb
import com.github.kiolk.chemistrytests.data.fragments.*
import com.github.kiolk.chemistrytests.data.fragments.CompletedTestsFragment.Companion.RESULT_TEST_TAG
import com.github.kiolk.chemistrytests.data.fragments.accounts.PremiumFragment
import com.github.kiolk.chemistrytests.data.fragments.configuration.ConfigurationFragment
import com.github.kiolk.chemistrytests.data.fragments.help.HelpFragment
import com.github.kiolk.chemistrytests.data.fragments.statistic.UserStatisticFragment
import com.github.kiolk.chemistrytests.data.fragments.tests.TestsFragment
import com.github.kiolk.chemistrytests.data.models.*
import com.github.kiolk.chemistrytests.data.models.CloseQuestion.Question.EASY_QUESTION
import com.github.kiolk.chemistrytests.data.models.CloseQuestion.Question.SINGLE_CHOICE
import com.github.kiolk.chemistrytests.ui.activities.base.BaseActivity
import com.github.kiolk.chemistrytests.ui.activities.main.MainMvp
import com.github.kiolk.chemistrytests.ui.activities.main.MainPresenter
import com.github.kiolk.chemistrytests.utils.ChartHelper.showWebView
import com.github.kiolk.chemistrytests.utils.Constants.ACCOUNTS_CHILD
import com.github.kiolk.chemistrytests.utils.Constants.PERIODIC_TABLE_NAME
import com.github.kiolk.chemistrytests.utils.Constants.SOLUBILITY_CHART_NAME
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kiolk.com.github.pen.Pen
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.tool_bar_main.*
import reversSort
import showFragment

class MainActivity : BaseActivity(), MainMvp {

    lateinit var mAuthentication: FirebaseAuth
    lateinit var mFirebaseDatabase: FirebaseDatabase
    lateinit var mAvaliableTests: AvaliableFragments
    lateinit var mAvailableTests: AvaliableTestFragment
    lateinit var mCompletedTsts: CompletedTestsFragment
    lateinit var mCustomTest: CustomTest
    lateinit var mCustomTestFragment: CustomTestFragment
    lateinit var mTestsFragment: TestsFragment
    var mPremiumAccounts: PremiumFragment = PremiumFragment()
    var mHelpFragment: HelpFragment = HelpFragment()
    var mConfigurationFragment: ConfigurationFragment = ConfigurationFragment()
    var mUserStatisticFragment: UserStatisticFragment = UserStatisticFragment()
    var mAppInformationViewFragment: AppInformationViewFragment = AppInformationViewFragment()
    var isTestFragmentShow: Boolean = false
    var mSelectedMenuItem: Int = R.id.test_item_menu

    var presenter = MainPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(main_tool_bar)
        main_drawer_layout.setStatusBarBackground(R.color.fui_transparent)
        mAvaliableTests = AvaliableFragments()
        mAvailableTests = AvaliableTestFragment()
        mCustomTest = CustomTest()
        mAuthentication = FirebaseAuth.getInstance()
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mCompletedTsts = CompletedTestsFragment()
        mCustomTestFragment = CustomTestFragment()
        mTestsFragment = TestsFragment()
        setupNavigationDrawer()
    }

    override fun onPostResume() {
        super.onPostResume()
        showTests()
    }

    private fun setupNavigationDrawer() {
        val view: TextView = navigation_relative_layout.getHeaderView(0).findViewById(R.id.user_login_text_view)
        view.text = mAuthentication.currentUser?.displayName
        if (mAuthentication.currentUser?.photoUrl != null) {
            val imageView: ImageView = navigation_relative_layout.getHeaderView(0).findViewById(R.id.user_profile_picture_image_view)
            Pen.getInstance().getImageFromUrl(mAuthentication.currentUser?.photoUrl?.toString()).inputTo(imageView)
        }
        navigation_relative_layout.setNavigationItemSelectedListener(object : NavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                if (item.itemId != R.id.courses_item_menu) {
                    courses_view_pager.visibility = View.GONE
                }
                if (item.itemId != R.id.periodic_table_item_menu && item.itemId != R.id.solubility_chart_item_menu && item.itemId != R.id.molar_calculator_item_menu) {
                    closeFragments()
                }

//                navigation_relative_layout.menu.findItem(mSelectedMenuItem).icon.setColorFilter(resources.getColor(R.color.GRAY_TEXT_COLOR), PorterDuff.Mode.ADD)
//                item.icon.setColorFilter(resources.getColor(R.color.BLACK_TEXT_COLOR), PorterDuff.Mode.ADD)
                main_drawer_layout.closeDrawer(navigation_relative_layout)
                mSelectedMenuItem = item.itemId
                main_tool_bar.visibility = View.VISIBLE
                when (item.itemId) {
                    R.id.test_item_menu -> {
                        showTests()
                        return true
                    }
                    R.id.courses_item_menu -> {
                        showCourses()
                        main_tool_bar.visibility = View.GONE
                        return true
                    }
                    R.id.custom_test_item_menu -> {
                        showCustomTest()
                        return true
                    }
                    R.id.tests_history_item_menu -> {
                        showHistory()
                        return true
                    }
                    R.id.statistic_item_menu -> {
                        showStatistic()
                        return true
                    }
                    R.id.periodic_table_item_menu -> {
                        showWebView(baseContext, PERIODIC_TABLE_NAME, main_web_view)
//                        if(resources.configuration.locale.language == "en"){
//                            showWebView(baseContext, PERIODIC_TABLE_ENGLISH, main_web_view)
//                        }else if(resources.configuration.locale.language == "be"){
//                            showWebView(baseContext, PERIODIC_TABLE_BELARUS, main_web_view)
//                        }else{
//                            showWebView(baseContext, PERIODIC_TABLE_NAME, main_web_view)
//                        }
                        return true
                    }
                    R.id.solubility_chart_item_menu -> {
                        showWebView(baseContext, SOLUBILITY_CHART_NAME, main_web_view)
                        return true
                    }
                    R.id.molar_calculator_item_menu -> {
                        return true
                    }
                    R.id.settings_item_menu -> {
                        showConfiguration()
                        return true
                    }
                    R.id.price_item_menu -> {
                        showPremiumAccounts()
                        return true
                    }
                    R.id.help_item_menu -> {
                        showHelpInformation()
                        return true
                    }
                    R.id.about_item_menu -> {
                        showInformation()
                        return true
                    }
                    R.id.add_item_menu -> {
                        addNewObject()
                        return true
                    }
                    R.id.log_out_item_menu -> {
                        Toast.makeText(baseContext, "Show third mGeneralStatistic", Toast.LENGTH_SHORT).show()
                        AuthUI.getInstance().signOut(baseContext)
                        val intent: Intent = Intent(baseContext, SplashActivity::class.java)
                        finish()
                        startActivity(intent)
                        return true
                    }
                }
                return false
            }
        })
    }

    private fun showConfiguration() {
        main_frame_layout.visibility = View.VISIBLE
        start_relative_layout.visibility = View.GONE
        showFragment(supportFragmentManager, R.id.main_frame_layout, mConfigurationFragment)
        mConfigurationFragment.mPresenter?.prepareSettings()
    }

    private fun showPremiumAccounts() {
        main_frame_layout.visibility = View.VISIBLE
        start_relative_layout.visibility = View.GONE
        mPremiumAccounts = PremiumFragment()
        showFragment(supportFragmentManager, R.id.main_frame_layout, mPremiumAccounts)
        mPremiumAccounts.mPresenter.requestAvelbleAccounts(resources.configuration.locale.language)
    }

    private fun showHelpInformation() {
        main_frame_layout.visibility = View.VISIBLE
        start_relative_layout.visibility = View.GONE
//        mUserStatisticFragment = UserStatisticFragment()
        showFragment(supportFragmentManager, R.id.main_frame_layout, mHelpFragment)
        mHelpFragment.presenter.executeHelpInformation()
    }

    private fun showInformation() {
        main_frame_layout.visibility = View.VISIBLE
        start_relative_layout.visibility = View.GONE
//        mUserStatisticFragment = UserStatisticFragment()
        showFragment(supportFragmentManager, R.id.main_frame_layout, mAppInformationViewFragment)
        mAppInformationViewFragment.presenter.getActualDataInformation()
    }

    private fun showStatistic() {
        main_frame_layout.visibility = View.VISIBLE
        start_relative_layout.visibility = View.GONE
        mUserStatisticFragment = UserStatisticFragment()
        showFragment(supportFragmentManager, R.id.main_frame_layout, mUserStatisticFragment)
        mUserStatisticFragment.showStatistic()
//        val presenter = StatisticPresenter(mStatisticFragment)
//        presenter.presentStatistic()
    }

    private fun showCustomTest() {
//        if (main_frame_layout.visibility != View.VISIBLE) {
        main_frame_layout.visibility = View.VISIBLE
        start_relative_layout.visibility = View.GONE
        mCustomTestFragment = CustomTestFragment()
        showFragment(supportFragmentManager, R.id.main_frame_layout, mCustomTestFragment)
//        }
    }

    private fun showTests() {
        if (!isTestFragmentShow) {
            if (main_frame_layout.visibility != View.VISIBLE) {
                courses_view_pager.visibility = View.GONE
                main_frame_layout.visibility = View.VISIBLE
//                start_relative_layout.visibility = View.GONE
            } else {
                closeFragments()
            }
            main_tool_bar.visibility = View.VISIBLE
            navigation_relative_layout.menu.findItem(R.id.test_item_menu)?.isChecked = true
            mTestsFragment = TestsFragment()
            showFragment(supportFragmentManager, R.id.main_frame_layout, mTestsFragment)
            mTestsFragment.showTests()
            isTestFragmentShow = true
        }
    }

    private fun showCourses() {
        main_frame_layout.visibility = View.GONE
        start_relative_layout.visibility = View.GONE
        courses_view_pager.visibility = View.VISIBLE
        open_courses_button.isEnabled = false
        SingleAsyncTask().execute(PrepareCoursesFromDb(object : ResultCallback {
            override fun <T> onSuccess(any: T?) {
                val courses: MutableList<Course> = any as MutableList<Course>
                val coursesAdapter = CoursesViewPagerAdapter(supportFragmentManager, courses)
                courses_view_pager.adapter = coursesAdapter
                open_courses_button.isEnabled = true
            }

            override fun onError() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }))
    }

    private fun showHistory() {
        main_frame_layout.visibility = View.VISIBLE
        start_relative_layout.visibility = View.GONE
        showFragment(supportFragmentManager, R.id.main_frame_layout, mCompletedTsts)
        var resultTests = DBOperations().getUser(mAuthentication.currentUser?.uid)?.completedTests
        resultTests = resultTests?.let { it1 -> reversSort(it1) }
        resultTests?.let { it1 -> mCompletedTsts.showResults(it1) }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        menu?.findItem(mSelectedMenuItem)?.isVisible = true
        return super.onPrepareOptionsMenu(menu)
    }

    override fun updateMenu(menuId: Int) {
        mSelectedMenuItem = menuId
        invalidateOptionsMenu()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        main_drawer_layout.closeDrawer(navigation_relative_layout)
        when (item?.itemId) {
            R.id.reset_history_menu_item -> {
                presenter.resetTestHistory()
                return true
            }
            R.id.search_menu_item -> {
                showMassageResult("Search message")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addNewObject() {
        val question = CloseQuestion(34, "Из предложенных вариантов, выберите символ химического элемента", null, SINGLE_CHOICE,
                listOf("Общая химия", "Химическая формула"),
                1.0F,
                "ru",
                listOf(Option("N__2_", null),
                        Option("C--60_", null),
                        Option("He", null),
                        Option("S__8_", null)),
                listOf(1), EASY_QUESTION, null, null, listOf(3), listOf("ЦТ2015", "ЧастьА"))
        val testParams: TestParams = getExampleTest()
        val account = AcountModel("Free", listOf("More than 100 question", "Theory"), "en", 1, 0.99F)
//        val res = mFirebaseDatabase.getReference().child(QUESTIONS_CHILDS)
        val res = mFirebaseDatabase.getReference().child(ACCOUNTS_CHILD)
//            val theory = ChemTheoryModel(1, "Valency", mutableListOf(Hint("Definition", listOf("http")), Hint("Element with constant valency", listOf("hhhtp"))))
//            val res = mFirebaseDatabase.getReference().child(DATA_THEORY_CHILD)
//            res.child(theory.theoryId.toString()).setValue(theory)
//            val courses = testCourses()
//            courses.forEach {
//                res.child(it.mCourseId.toString()).setValue(it)
//            }
        res.child("${account.descriptionLanguage}${account.accountId}").setValue(account)
//            val info = QuestionsDataBaseInfo(1, 3, 30)
//            res.child(question.questionId.toString()).setValue(question)
//            val intent = Intent(this, TestingActivity::class.java)
//            intent.putExtra(TEST_PARAM_INT, testParams)
//            startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(baseContext,
                        resources.getString(R.string.SUCCES_LOGGIN),
                        Toast.LENGTH_LONG)
                        .show()
            } else if (resultCode == Activity.RESULT_CANCELED) {
                finish()
            }
        }
    }

    override fun onBackPressed() {
        if (main_drawer_layout.isDrawerOpen(navigation_relative_layout)) {
            main_drawer_layout.closeDrawer(navigation_relative_layout)
            return
        }
        if (main_web_view.visibility == View.VISIBLE) {
            main_web_view.visibility = View.GONE
            navigation_relative_layout.menu.findItem(R.id.test_item_menu)?.isChecked = true
            return
        }
        if (main_frame_layout.visibility == View.VISIBLE) {
            if (!mCompletedTsts.isShowResult) {
                if (isTestFragmentShow) {
                    super.onBackPressed()
                    return
                }
                showTests()
//                main_tool_bar.visibility = View.VISIBLE
//                navigation_relative_layout.menu.findItem(R.id.test_item_menu)?.isChecked = true
                return
            } else {
                closeResultStatisticFragment()
                return
            }
        }
        if (courses_view_pager.visibility == View.VISIBLE) {
            showTests()
        } else {
            super.onBackPressed()
        }
    }

    private fun closeResultStatisticFragment() {
        closeFragment(supportFragmentManager, mCompletedTsts, RESULT_TEST_TAG)
        mCompletedTsts.isShowResult = false
    }

    private fun closeFragments() {
        if (mCompletedTsts.isShowResult) {
            closeResultStatisticFragment()
        }
        closeFragment(supportFragmentManager, mAvailableTests)
        closeFragment(supportFragmentManager, mCustomTestFragment)
        closeFragment(supportFragmentManager, mCompletedTsts)
        closeFragment(supportFragmentManager, mTestsFragment)
        closeFragment(supportFragmentManager, mUserStatisticFragment)
        closeFragment(supportFragmentManager, mAppInformationViewFragment)
        closeFragment(supportFragmentManager, mHelpFragment)
        closeFragment(supportFragmentManager, mConfigurationFragment)
        closeFragment(supportFragmentManager, mPremiumAccounts)
        isTestFragmentShow = false
    }

    override fun restart() {
        val intent = Intent(baseContext, MainActivity::class.java)
        startActivity(intent)
        finish()

    }

    override fun paymentProcess(paymentAccount: AcountModel) {
    }

    override fun showMassageResult(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}
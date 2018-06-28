package com.github.kiolk.chemistrytests.ui.activities

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import checkConnection
import closeFragment
import com.firebase.ui.auth.AuthUI
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.adapters.CoursesViewPagerAdapter
import com.github.kiolk.chemistrytests.data.adapters.MenuCustomArrayAdapter
import com.github.kiolk.chemistrytests.data.asynctasks.ResultCallback
import com.github.kiolk.chemistrytests.data.asynctasks.SingleAsyncTask
import com.github.kiolk.chemistrytests.data.database.DBOperations
import com.github.kiolk.chemistrytests.data.executs.PrepareCoursesFromDb
import com.github.kiolk.chemistrytests.data.fragments.*
import com.github.kiolk.chemistrytests.data.fragments.CompletedTestsFragment.Companion.RESULT_TEST_TAG
import com.github.kiolk.chemistrytests.data.fragments.help.HelpFragment
import com.github.kiolk.chemistrytests.data.models.*
import com.github.kiolk.chemistrytests.data.models.CloseQuestion.Question.EASY_QUESTION
import com.github.kiolk.chemistrytests.data.models.CloseQuestion.Question.SINGLE_CHOICE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kiolk.com.github.pen.Pen
import kotlinx.android.synthetic.main.activity_main.*
import reversSort
import showFragment

val TEST_PARAM_INT: String = "params"
val TESTS_CHILD: String = "tests"
val DATA_BASE_INFO_CHAILD: String = "DBInformation"
val DATA_BASE_USERS_CHAILD: String = "Users"
val DATA_COURSES_CHILD: String = "Courses"
val DATA_THEORY_CHILD : String = "Theory"

class MainActivity : AppCompatActivity() {
    //
    lateinit var mAuthentication: FirebaseAuth
    //    lateinit var mAuthenticationListener: FirebaseAuth.AuthStateListener
//    val mProviders = listOf<AuthUI.IdpConfig>(AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(), AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build())
    lateinit var mFirebaseDatabase: FirebaseDatabase
    lateinit var mFirebaseDatabase2: FirebaseDatabase
    lateinit var mDatabaseReference: DatabaseReference
    //    lateinit var mDatabaseReference2: DatabaseReference
    lateinit var mChaildEventListener: ChildEventListener
    lateinit var mAvaliableTests: AvaliableFragments
    lateinit var mAvailableTests: AvaliableTestFragment
    lateinit var mCompletedTsts: CompletedTestsFragment
    lateinit var mCustomTest: CustomTest
    lateinit var mCustomTestFragment: CustomTestFragment
    lateinit var mTestsFragment: TestsFragment
    lateinit var mTestDataBaseReference: DatabaseReference
    lateinit var mChildEventListener: ChildEventListener
    var mHelpFragment : HelpFragment = HelpFragment()
    var mStatisticFragment : GeneralStatisticFragment = GeneralStatisticFragment()
    var mUserStatisticFragment : UserStatisticFragment = UserStatisticFragment()
    var mAppInformationViewFragment : AppInformationViewFragment = AppInformationViewFragment()
    var isTestFragmentShow: Boolean = false
    var cnt = 0
    var cnt2 = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(main_tool_bar)
//        main_tool_bar.visibility = View.GONE
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
        testing_activity_button.setOnClickListener { view: View ->
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
            val res = mFirebaseDatabase.getReference().child(QUESTIONS_CHILDS)
//            val theory = ChemTheoryModel(1, "Valency", mutableListOf(Hint("Definition", listOf("http")), Hint("Element with constant valency", listOf("hhhtp"))))
//            val res = mFirebaseDatabase.getReference().child(DATA_THEORY_CHILD)
//            res.child(theory.theoryId.toString()).setValue(theory)
//            val courses = testCourses()
//            courses.forEach {
//                res.child(it.mCourseId.toString()).setValue(it)
//            }
            res.child(question.questionId.toString()).setValue(question)
//            val info = QuestionsDataBaseInfo(1, 3, 30)
//            res.child(question.questionId.toString()).setValue(question)
//            val intent = Intent(this, TestingActivity::class.java)
//            intent.putExtra(TEST_PARAM_INT, testParams)
//            startActivity(intent)
        }
        chose_ready_test_button.setOnClickListener { view: View ->
            if (main_frame_layout.visibility != View.VISIBLE) {
                main_frame_layout.visibility = View.VISIBLE
                start_relative_layout.visibility = View.GONE
//                showFragment(supportFragmentManager, R.id.main_frame_layout, mAvaliableTests)
                showFragment(supportFragmentManager, R.id.main_frame_layout, mAvailableTests)
            }
        }

        custom_test_button.setOnClickListener {
            if (main_frame_layout.visibility != View.VISIBLE) {
                main_frame_layout.visibility = View.VISIBLE
                start_relative_layout.visibility = View.GONE
                mCustomTestFragment = CustomTestFragment()
                showFragment(supportFragmentManager, R.id.main_frame_layout, mCustomTestFragment)
//                showFragment(supportFragmentManager, R.id.main_frame_layout, mCustomTest)
//                mCustomTest.combineCustomTest(DBOperations().getAllQuestions())
            }
        }
        open_courses_button.setOnClickListener {
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
        completed_test_button.setOnClickListener {
            main_frame_layout.visibility = View.VISIBLE
            start_relative_layout.visibility = View.GONE
            showFragment(supportFragmentManager, R.id.main_frame_layout, mCompletedTsts)
//            DBOperations().getUser(mAuthentication.currentUser?.uid)?.completedTests?.let { it1 -> mCompletedTsts.showResults(it1) }
            var resultTests = DBOperations().getUser(mAuthentication.currentUser?.uid)?.completedTests
            resultTests = resultTests?.let { it1 -> reversSort(it1) }
            resultTests?.let { it1 -> mCompletedTsts.showResults(it1) }
        }
//        mDatabaseReference.addChildEventListener(mChaildEventLisgettener)
    }

    private fun setupNavigationDrawer() {
        val view: TextView = navigation_relative_layout.getHeaderView(0).findViewById(R.id.user_login_text_view)
        view.text = mAuthentication.currentUser?.displayName
        if (mAuthentication.currentUser?.photoUrl != null) {
//            mAuthentication.currentUser?
            val imageView: ImageView = navigation_relative_layout.getHeaderView(0).findViewById(R.id.user_profile_picture_image_view)
            Pen.getInstance().getImageFromUrl(mAuthentication.currentUser?.photoUrl?.toString()).inputTo(imageView)
        }
        setupNavigationMenu()
    }

    private fun setupNavigationMenu() {
        val itemArray: List<MenuItemModel> = getMenuItems()
        val listAdapter: MenuCustomArrayAdapter = MenuCustomArrayAdapter(applicationContext, R.layout.item_navigation_menu, itemArray)
        navigation_menu_list_view.adapter = listAdapter
        navigation_menu_list_view.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            selectPosition(position)
        }
    }

    private fun selectPosition(position: Int) {
        if (position != 1) {
            courses_view_pager.visibility = View.GONE
        }
        closeFragments()
        when (position) {
            0 -> {
                showTests()
            }
            1 -> {
                showCourses()
            }
            2 -> {
                showCustomTest()
            }
            3 -> {
                showHistory()
            }
            4 ->{
                showStatistic()
            }
            6 -> showHelpInformation()
            7 -> {
                showInformation()
            }
            8 -> {
                Toast.makeText(baseContext, "Show third mGeneralStatistic", Toast.LENGTH_SHORT).show()
                AuthUI.getInstance().signOut(baseContext)
                val intent: Intent = Intent(baseContext, SplashActivity::class.java)
                finish()
                startActivity(intent)
            }
        }
        main_drawer_layout.closeDrawer(navigation_relative_layout)
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
                main_frame_layout.visibility = View.VISIBLE
                start_relative_layout.visibility = View.GONE
            } else {
                closeFragments()
            }
            mTestsFragment = TestsFragment()
            showFragment(supportFragmentManager, R.id.main_frame_layout, mTestsFragment)
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

    private fun getMenuItems(): List<MenuItemModel> {
        val titles: Array<String> = baseContext.resources.getStringArray(R.array.NAVIGATION_MENU_ITEMS)
        return listOf(
                MenuItemModel(R.drawable.ic_test, titles[0]),
                MenuItemModel(R.drawable.ic_study_notes, titles[1]),
                MenuItemModel(R.drawable.ic_puzzle, titles[2]),
                MenuItemModel(R.drawable.ic_history, titles[3]),
                MenuItemModel(R.drawable.ic_statistic, titles[4]),
                MenuItemModel(R.drawable.ic_settings_gears, titles[5]),
                MenuItemModel(R.drawable.ic_doubts_button, titles[6]),
                MenuItemModel(R.drawable.ic_info_sign, titles[7]),
                MenuItemModel(R.drawable.ic_sign_out_option, titles[8]))
    }

    private fun splashScreenSetup() {
        if (!checkConnection(baseContext)) {
//            upload_data_progress_bar.visibility = View.GONE
//            splash_frame_layout.visibility = View.GONE
            Log.d("MyLogs", "Continue work ofline")
            Toast.makeText(baseContext, "You continue off line", Toast.LENGTH_SHORT)
        } else {
            mFirebaseDatabase = FirebaseDatabase.getInstance()
            mDatabaseReference = mFirebaseDatabase.reference.child(QUESTIONS_CHILDS)
            mChaildEventListener = object : ChildEventListener {
                override fun onCancelled(p0: DatabaseError?) {
                }

                override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                }

                override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                }

                override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                    val question = p0?.getValue(CloseQuestion::class.java)
                    question?.let { DBOperations().insertQuestion(it) }
                    cnt = cnt + 1
//                    upload_data_progress_bar.max = 30
//                    upload_data_progress_bar.progress = cnt
                    Log.d("MyLogs", question?.questionId?.toString())
                    if (cnt == 30) {
//                        upload_data_progress_bar.visibility = View.GONE
//                        splash_frame_layout.visibility = View.GONE
                        mDatabaseReference.removeEventListener(mChaildEventListener)
                        Log.d("MyLogs", "Remove child Event Listener")
                    }
                }

                override fun onChildRemoved(p0: DataSnapshot?) {
                }
            }
            mDatabaseReference.addChildEventListener(mChaildEventListener)
            Log.d("MyLogs", "Add child event listener")
            mTestDataBaseReference = mFirebaseDatabase.reference.child(TESTS_CHILD)
            mChildEventListener = object : ChildEventListener {
                override fun onCancelled(p0: DatabaseError?) {
                }

                override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                }

                override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                }

                override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                    val testParams: TestParams? = p0?.getValue(TestParams::class.java)
                    Log.d("MyLogs", "add new test")
                    cnt2 = cnt2 + 1
                    testParams?.let { DBOperations().insertTest(testParams) }
                    if (cnt2 == 3) {
                        Log.d("MyLogs", "Complete tests")
                        mTestDataBaseReference.removeEventListener(mChildEventListener)
                    }
                }

                override fun onChildRemoved(p0: DataSnapshot?) {
                }
            }
            mTestDataBaseReference.addChildEventListener(mChildEventListener)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.sign_out_menu_item -> {
                AuthUI.getInstance().signOut(baseContext)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
//        mAuthentication.addAuthStateListener(mAuthenticationListener)
//        mDatabaseReference.addChildEventListener(mChaildEventListener)
    }

    override fun onPause() {
        super.onPause()
//        mAuthentication.removeAuthStateListener(mAuthenticationListener)
//        mDatabaseReference.removeEventListener(mChaildEventListener)
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
        if (main_frame_layout.visibility == View.VISIBLE) {
            if (!mCompletedTsts.isShowResult) {
                main_frame_layout.visibility = View.GONE
                start_relative_layout.visibility = View.VISIBLE
                closeFragments()
                return
            }else{
               closeResultStatisticFragment()
                return
            }
        }
        if (courses_view_pager.visibility == View.VISIBLE) {
            start_relative_layout.visibility = View.VISIBLE
            courses_view_pager.visibility = View.GONE
        } else {
            super.onBackPressed()
        }
    }

    private fun closeResultStatisticFragment() {
        closeFragment(supportFragmentManager, mCompletedTsts, RESULT_TEST_TAG)
        mCompletedTsts.isShowResult = false
    }

    private fun closeFragments() {
        if(mCompletedTsts.isShowResult){
            closeResultStatisticFragment()
        }
        closeFragment(supportFragmentManager, mAvailableTests)
        closeFragment(supportFragmentManager, mCustomTestFragment)
        closeFragment(supportFragmentManager, mCompletedTsts)
        closeFragment(supportFragmentManager, mTestsFragment)
        closeFragment(supportFragmentManager, mUserStatisticFragment)
        closeFragment(supportFragmentManager, mAppInformationViewFragment)
        closeFragment(supportFragmentManager, mHelpFragment)
        isTestFragmentShow = false
    }
}
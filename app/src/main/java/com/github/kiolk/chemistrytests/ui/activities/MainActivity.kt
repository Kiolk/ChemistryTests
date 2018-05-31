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
import com.github.kiolk.chemistrytests.data.fragments.AvaliableFragments
import com.github.kiolk.chemistrytests.data.fragments.AvaliableTestFragment
import com.github.kiolk.chemistrytests.data.fragments.CompletedTestsFragment
import com.github.kiolk.chemistrytests.data.fragments.CustomTest
import com.github.kiolk.chemistrytests.data.models.*
import com.github.kiolk.chemistrytests.data.models.CloseQuestion.Question.EASY_QUESTION
import com.github.kiolk.chemistrytests.data.models.CloseQuestion.Question.MULTIPLE_CHOICE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kiolk.com.github.pen.Pen
import kotlinx.android.synthetic.main.activity_main.*
import reversSort
import showFragment

//val RC_SIGN_IN: Int = 1
val TEST_PARAM_INT: String = "params"
val TESTS_CHILD: String = "tests"
val DATA_BASE_INFO_CHAILD: String = "DBInformation"
val DATA_BASE_USERS_CHAILD: String = "Users"
val DATA_COURSES_CHILD : String = "Courses"

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
    lateinit var mTestDataBaseReference: DatabaseReference
    lateinit var mChildEventListener: ChildEventListener
    var cnt = 0
    var cnt2 = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(main_tool_bar)
        main_tool_bar.visibility = View.GONE
        main_drawer_layout.setStatusBarBackground(R.color.fui_transparent)
        mAvaliableTests = AvaliableFragments()
        mAvailableTests = AvaliableTestFragment()
        mCustomTest = CustomTest()
        mAuthentication = FirebaseAuth.getInstance()
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mCompletedTsts = CompletedTestsFragment()
        setupNavigationDrawer()

//        splashScreenSetup()
//        upload_data_progress_bar.visibility = View.GONE
//        splash_frame_layout.visibility = View.GONE
//        mAuthenticationListener = object : FirebaseAuth.AuthStateListener {
//            override fun onAuthStateChanged(p0: FirebaseAuth) {
//                val firebaseUser: FirebaseUser? = p0.currentUser
//                if (firebaseUser != null) {
//                    Toast.makeText(baseContext,
//                            resources.getString(R.string.SUCCES_LOGGIN),
//                            Toast.LENGTH_LONG)
//                            .show()
//                } else {
//                    startActivityForResult(AuthUI.getInstance().
//                            createSignInIntentBuilder().
//                            setIsSmartLockEnabled(false).
//                            setAvailableProviders(mProviders).
//                            build(), RC_SIGN_IN)
//
//                }
//            }
//        }


//        text2.text = Html.fromHtml("H<sub>2</sub>SO<sup>4</sup> + Al = Al<sub>2</sub>(SO<sub>4</sub>)<sub>3</sub> + H<sub>2</sub>")
//        val sbb = SpannableStringBuilder("Hello")
//        val drawwable : Drawable = resources.getDrawable(R.drawable.sun)
//        val lineHeight = text2.lineHeight.times(3)
//        drawwable.setBounds(0, 0, lineHeight, lineHeight)
//        sbb.setSpan(ImageSpan(drawwable), 2, 3, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
//        sbb.append("World!")
//        text2.setText(sbb, TextView.BufferType.SPANNABLE)
//        Pen.getInstance().getImageFromUrl("https://i.stack.imgur.com/qoQ46.png").getBitmapDirect(object : GetBitmapCallback{
//            override fun getBitmap(pBitmapFromLoader: Bitmap?){
//                val drawable = BitmapDrawable(resources, pBitmapFromLoader)
//                val lineHeight = text2.lineHeight.times(5)
//                drawable.setBounds(0, 0, lineHeight, lineHeight)
//                sbb.setSpan(ImageSpan(drawable, ALIGN_BASELINE), 2, 3, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
//                sbb.append("World!")
//                sbb.append(Html.fromHtml("H<sub>2</sub>SO<sup>4</sup> + Al = Al<sub>2</sub>(SO<sub>4</sub>)<sub>3</sub> + H<sub>2</sub>"))
//
//                text2.setText(sbb, TextView.BufferType.SPANNABLE)
////                return pBitmapFromLoader ?: drawable.bitmap
//            }
//        })

//        setFormattedText(question_text_view, "X^2^^<br> drawable <br> H_2__SO_4__", "http://teacher-chem.ru/wp-content/uploads/2014/12/olimp-11.jpg")
        testing_activity_button.setOnClickListener { view: View ->
            val question = CloseQuestion(32, "Из указанных веществ выберите кислоты.", null, MULTIPLE_CHOICE,
                    listOf("Неорганическая химия", "Кислоты", "Химическая формула"),
                    1.0F,
                    "ru",
                    listOf(Option("H_2__SO_4__", null),
                            Option("HNO_3__", null),
                            Option("H_2__O", null),
                            Option("NaOH", null),
                            Option("Al_2__(SO_4__)_3__", null)),
                    listOf(1, 2), EASY_QUESTION, listOf(Hint("Как правило, формула кислоты начинается с водорода HClO_4__, " +
                    "H2S. Формула органических кислоты содержит карбоксильную функциональную группу -COOH." +
                    "<br> drawable", listOf("https://dic.academic.ru/pictures/wiki/files/76/Lipoic-acid-3D-vdW.png"))))
            val testParams: TestParams = getExampleTest()
            val res = mFirebaseDatabase.getReference().child(DATA_COURSES_CHILD)
            val courses = testCourses()
            courses.forEach {
                res.child(it.mCourseId.toString()).setValue(it)
            }
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
                showFragment(supportFragmentManager, R.id.main_frame_layout, mCustomTest)
                mCustomTest.combineCustomeTest(DBOperations().getAllQuestions())
            }
        }
        open_courses_button.setOnClickListener {
            main_frame_layout.visibility = View.GONE
            start_relative_layout.visibility = View.GONE
            courses_view_pager.visibility = View.VISIBLE
            open_courses_button.isEnabled = false
            SingleAsyncTask().execute(PrepareCoursesFromDb(object : ResultCallback{
                override fun <T> onSuccess(any: T?) {
                    val courses : MutableList<Course> = any as MutableList<Course>
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
        val listAdapter : MenuCustomArrayAdapter = MenuCustomArrayAdapter(applicationContext, R.layout.item_navigation_menu, itemArray)
        navigation_menu_list_view.adapter = listAdapter
        navigation_menu_list_view.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            selectPosition(position)
        }
    }

    private fun selectPosition(position: Int) {
        when(position){
            0 -> Toast.makeText(baseContext, "Show first fragment", Toast.LENGTH_SHORT).show()
            1 -> Toast.makeText(baseContext, "Show second fragment", Toast.LENGTH_SHORT).show()
            2 -> {
                Toast.makeText(baseContext, "Show third fragment", Toast.LENGTH_SHORT).show()
                AuthUI.getInstance().signOut(baseContext)
                val intent : Intent = Intent(baseContext, SplashActivity::class.java)
                finish()
                startActivity(intent)
            }
        }
    }



    private fun getMenuItems(): List<MenuItemModel> {
        val titles: Array<String> = baseContext.resources.getStringArray(R.array.NAVIGATION_MENU_ITEMS)
        val menuItems: List<MenuItemModel> = listOf(
                MenuItemModel(R.drawable.ic_statistic, titles[0]),
                MenuItemModel(R.drawable.ic_settings_gears, titles[1]),
                MenuItemModel(R.drawable.ic_sign_out_option, titles[2]))
        return menuItems
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
            if(!mCompletedTsts.closeResult()) {
                main_frame_layout.visibility = View.GONE
                start_relative_layout.visibility = View.VISIBLE
                closeFragment(supportFragmentManager, mAvailableTests)
                closeFragment(supportFragmentManager, mCustomTest)
                closeFragment(supportFragmentManager, mCompletedTsts)
            }
        } else if (courses_view_pager.visibility == View.VISIBLE) {
            start_relative_layout.visibility = View.VISIBLE
            courses_view_pager.visibility = View.GONE
        } else {
            super.onBackPressed()
        }
    }
//
//   private  fun showFragment(container: Int, fragment: Fragment) {
//        val transaction = supportFragmentManager.beginTransaction()
//        transaction.add(container, fragment)
//        transaction.commit()
//        supportFragmentManager.executePendingTransactions()
//    }
//
//    private fun closeFragment(fragment: Fragment) {
//        val transaction = supportFragmentManager.beginTransaction()
//        transaction.remove(fragment)
//        transaction.commit()
//    }
}
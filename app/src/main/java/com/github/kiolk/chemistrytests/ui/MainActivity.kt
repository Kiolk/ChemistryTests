package com.github.kiolk.chemistrytests.ui

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import checkConnection
import closeFragment
import com.firebase.ui.auth.AuthUI
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.database.DBConnector
import com.github.kiolk.chemistrytests.data.database.DBOperations
import com.github.kiolk.chemistrytests.data.fragments.AvaliableFragments
import com.github.kiolk.chemistrytests.data.fragments.AvaliableTestFragment
import com.github.kiolk.chemistrytests.data.models.*
import com.github.kiolk.chemistrytests.data.models.CloseQuestion.Question.EASY_QUESTION
import com.github.kiolk.chemistrytests.data.models.CloseQuestion.Question.INPUT_CHOICE
import com.github.kiolk.chemistrytests.data.models.CloseQuestion.Question.SINGLE_CHOICE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging
import kiolk.com.github.pen.Pen
import kiolk.com.github.pen.utils.PenConstantsUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.card_close_question.*
import showFragment

val RC_SIGN_IN: Int = 1
val TEST_PARAM_INT: String = "params"
val TESTS_CHILD : String = "tests"
val DATA_BASE_INFO_CHAILD : String = "DBInformation"

class MainActivity : AppCompatActivity() {

    lateinit var mAuthentication: FirebaseAuth
    lateinit var mAuthenticationListener: FirebaseAuth.AuthStateListener
    val mProviders = listOf<AuthUI.IdpConfig>(AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(), AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build())
    lateinit var mFirebaseDatabase: FirebaseDatabase
    lateinit var mFirebaseDatabase2: FirebaseDatabase
    lateinit var mDatabaseReference: DatabaseReference
//    lateinit var mDatabaseReference2: DatabaseReference
    lateinit var mChaildEventListener: ChildEventListener
    lateinit var mAvaliableTests: AvaliableFragments
    lateinit var mAvailableTests : AvaliableTestFragment
    lateinit var mTestDataBaseReference : DatabaseReference
    lateinit var mChildEventListener : ChildEventListener
    var cnt = 0
    var cnt2 = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initImageLoader()
        setContentView(R.layout.activity_main)
        setSupportActionBar(main_tool_bar)
        main_drawer_layout.setStatusBarBackground(R.color.fui_transparent)
        FirebaseMessaging.getInstance()
        mAvaliableTests = AvaliableFragments()
        mAvailableTests = AvaliableTestFragment()
        mAuthentication = FirebaseAuth.getInstance()
        mFirebaseDatabase = FirebaseDatabase.getInstance()
//        splashScreenSetup()
        upload_data_progress_bar.visibility = View.GONE
        splash_frame_layout.visibility = View.GONE
        mAuthenticationListener = object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                val firebaseUser: FirebaseUser? = p0.currentUser
                if (firebaseUser != null) {
                    Toast.makeText(baseContext,
                            resources.getString(R.string.SUCCES_LOGGIN),
                            Toast.LENGTH_LONG)
                            .show()
                } else {
                    startActivityForResult(AuthUI.getInstance().
                            createSignInIntentBuilder().
                            setIsSmartLockEnabled(false).
                            setAvailableProviders(mProviders).
                            build(), RC_SIGN_IN)

                }
            }

        }


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
            val question = CloseQuestion(31, "Чему равняется молярная масса ацетилена?", null, INPUT_CHOICE,
                    listOf("Органическая химия", "Тривиальные названия"),
                    1.0F,
                    "ru",
                    listOf(Option("26", null)),
                    listOf(1), EASY_QUESTION, listOf(Hint("Equal for sulfuric acid <br> drawable", listOf("https://upload.wikimedia.org/wikipedia/commons/8/8b/Sulfuric-acid-2D-dimensions.svg") )))
            val testParams: TestParams = getExampleTest()
            val res = mFirebaseDatabase.getReference().child(QUESTIONS_CHILDS)
            val info = QuestionsDataBaseInfo(1, 3, 30)
            res.child(question.questionId.toString()).setValue(question)
//            val intent = Intent(this, TestingActivity::class.java)
//            intent.putExtra(TEST_PARAM_INT, testParams)
//            startActivity(intent)
        }
        chose_ready_test_button.setOnClickListener { view: View ->
            if (main_frame_layout.visibility != View.VISIBLE) {
                main_frame_layout.visibility = View.VISIBLE
//                showFragment(supportFragmentManager, R.id.main_frame_layout, mAvaliableTests)
                showFragment(supportFragmentManager, R.id.main_frame_layout, mAvailableTests)
            }
        }
//        mDatabaseReference.addChildEventListener(mChaildEventListener)
    }

    private fun splashScreenSetup() {
        if(!checkConnection(baseContext)){
            upload_data_progress_bar.visibility = View.GONE
            splash_frame_layout.visibility = View.GONE
            Log.d("MyLogs", "Continue work ofline")
            Toast.makeText(baseContext, "You continue off line", Toast.LENGTH_SHORT)
        }else {
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
                    upload_data_progress_bar.max = 30
                    upload_data_progress_bar.progress = cnt
                    Log.d("MyLogs", question?.questionId?.toString())
                    if (cnt == 30) {
                        upload_data_progress_bar.visibility = View.GONE
                        splash_frame_layout.visibility = View.GONE
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
             mChildEventListener = object : ChildEventListener{
                override fun onCancelled(p0: DatabaseError?) {
                }

                override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                }

                override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                }

                override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                    val testParams : TestParams? = p0?.getValue(TestParams::class.java)
                    Log.d("MyLogs", "add new test")
                    cnt2 = cnt2 + 1
                    testParams?.let{DBOperations().insertTest(testParams)}
                    if(cnt2 == 3){
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
        mAuthentication.addAuthStateListener(mAuthenticationListener)
//        mDatabaseReference.addChildEventListener(mChaildEventListener)
    }

    override fun onPause() {
        super.onPause()
        mAuthentication.removeAuthStateListener(mAuthenticationListener)
//        mDatabaseReference.removeEventListener(mChaildEventListener)
    }

    private fun initImageLoader() {
        Pen.getInstance().setLoaderSettings().
                setSizeInnerFileCache(20L).
                setContext(baseContext).
                setTypeOfCache(PenConstantsUtil.INNER_FILE_CACHE).
                setSavingStrategy(PenConstantsUtil.SAVE_FULL_IMAGE_STRATEGY).
                setUp()

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
            main_frame_layout.visibility = View.GONE
            closeFragment(supportFragmentManager, mAvailableTests)
        }else {
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
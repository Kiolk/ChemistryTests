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
import closeFragment
import com.firebase.ui.auth.AuthUI
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.fragments.AvaliableFragments
import com.github.kiolk.chemistrytests.data.models.*
import com.github.kiolk.chemistrytests.data.models.CloseQuestion.Question.EASY_QUESTION
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

class MainActivity : AppCompatActivity() {

    lateinit var mAuthentication: FirebaseAuth
    lateinit var mAuthenticationListener: FirebaseAuth.AuthStateListener
    val mProviders = listOf<AuthUI.IdpConfig>(AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(), AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build())
    lateinit var mFirebaseDatabase: FirebaseDatabase
    lateinit var mDatabaseReference: DatabaseReference
    lateinit var mChaildEventListener: ChildEventListener
    lateinit var mAvaliableTests: AvaliableFragments

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initImageLoader()
        setContentView(R.layout.activity_main)
        setSupportActionBar(main_tool_bar)
        main_drawer_layout.setStatusBarBackground(R.color.fui_transparent)
        FirebaseMessaging.getInstance()
        mAvaliableTests = AvaliableFragments()
        mAuthentication = FirebaseAuth.getInstance()
        mFirebaseDatabase = FirebaseDatabase.getInstance()
//        mDatabaseReference = mFirebaseDatabase.getReference().child(TESTS_CHILD)

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
            val question = CloseQuestion(1, "Чему равен заряд иона, который содержит 10 электронов и 13 протонов?", null, SINGLE_CHOICE,
                    listOf("Строение атома", "Ионы"),
                    1.0F,
                    "кг",
                    listOf(Option("+3", null),
                            Option("+13", null),
                            Option("-10", null),
                            Option("-3", null)),
                    listOf(1), EASY_QUESTION)
            val testParams: TestParams = getExampleTest()
//            val res = mFirebaseDatabase.getReference().child(QUESTIONS_CHILDS)
//            res.child(question.questionId.toString()).setValue(question)
            val intent = Intent(this, TestingActivity::class.java)
            intent.putExtra(TEST_PARAM_INT, testParams)
            startActivity(intent)
        }
        chose_ready_test_button.setOnClickListener { view: View ->
            if (main_frame_layout.visibility != View.VISIBLE) {
                main_frame_layout.visibility = View.VISIBLE
                showFragment(supportFragmentManager, R.id.main_frame_layout, mAvaliableTests)
            }
        }
//        mDatabaseReference.addChildEventListener(mChaildEventListener)
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
            closeFragment(supportFragmentManager, mAvaliableTests)
        }
        super.onBackPressed()
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
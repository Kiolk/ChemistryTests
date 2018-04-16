package com.github.kiolk.chemistrytests.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.firebase.ui.auth.AuthUI
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.models.CloseQuestion
import com.github.kiolk.chemistrytests.data.models.CloseQuestion.Question.SINGLE_CHOICE
import com.github.kiolk.chemistrytests.data.models.Option
import com.github.kiolk.chemistrytests.data.models.Question
import com.github.kiolk.chemistrytests.data.models.setFormattedText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging
import kiolk.com.github.pen.Pen
import kiolk.com.github.pen.utils.PenConstantsUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.card_close_question.*

class MainActivity : AppCompatActivity() {

    lateinit var mAuthentication: FirebaseAuth
    lateinit var mAuthenticationListener : FirebaseAuth.AuthStateListener
    val mProviders = listOf<AuthUI.IdpConfig>(AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(), AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build())
    lateinit var mFirebaseDatabase : FirebaseDatabase
    lateinit var mDatabaseReference : DatabaseReference
    lateinit var mChaildEventListener : ChildEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initImageLoader()
        setContentView(R.layout.activity_main)
        FirebaseMessaging.getInstance()
        mAuthentication = FirebaseAuth.getInstance()
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mFirebaseDatabase.getReference().child("monts")
        mChaildEventListener = object : ChildEventListener{
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
//                val  question = p0?.getValue<CloseQuestion>(CloseQuestion::class.java)
//                Log.d("MyLogs", question.toString())
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
        mAuthenticationListener = object : FirebaseAuth.AuthStateListener{
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                val firebaseUser : FirebaseUser? = p0.currentUser
                if (firebaseUser != null){

                }else{
                    startActivityForResult(AuthUI.getInstance().
                    createSignInIntentBuilder().
                    setIsSmartLockEnabled(false).
                    setAvailableProviders(mProviders).
                            build(), 1)

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

        setFormattedText(question_text_view, "X^2^^<br> drawable <br> H_2__SO_4__", "http://teacher-chem.ru/wp-content/uploads/2014/12/olimp-11.jpg")
        testing_activity_button.setOnClickListener{view : View ->
            val question = CloseQuestion(1, "How many months in year?", null, 1, SINGLE_CHOICE,
                    listOf("Year", "Month", "Days"),
                    1.0F,
                    "en",
                    listOf(Option("5", null),
                            Option("8", null),
                            Option("9", null),
                            Option("11", null),
                            Option("12", null)),
                    listOf(1))
//            mDatabaseReference.child("4").setValue(question)
            val intent = Intent(this, TestingActivity::class.java)
            startActivity(intent)
        }
//        mDatabaseReference.addChildEventListener(mChaildEventListener)
    }

    override fun onResume() {
        super.onResume()
        mAuthentication.addAuthStateListener(mAuthenticationListener)
        mDatabaseReference.addChildEventListener(mChaildEventListener)
    }

    override fun onPause() {
        super.onPause()
        mAuthentication.removeAuthStateListener(mAuthenticationListener)
        mDatabaseReference.removeEventListener(mChaildEventListener)
    }

    private fun initImageLoader() {
        Pen.getInstance().setLoaderSettings().
                setSizeInnerFileCache(20L).
                setContext(baseContext).
                setTypeOfCache(PenConstantsUtil.INNER_FILE_CACHE).
                setSavingStrategy(PenConstantsUtil.SAVE_FULL_IMAGE_STRATEGY).
                setUp()

    }
}
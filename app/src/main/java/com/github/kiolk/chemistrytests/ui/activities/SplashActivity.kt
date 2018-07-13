package com.github.kiolk.chemistrytests.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import changeLocale
import checkConnection
import com.firebase.ui.auth.AuthUI
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.adapters.FeaturesPageAdapter
import com.github.kiolk.chemistrytests.data.adapters.InfinityPagerAdapter
import com.github.kiolk.chemistrytests.data.asynctasks.ResultCallback
import com.github.kiolk.chemistrytests.data.asynctasks.SingleAsyncTask
import com.github.kiolk.chemistrytests.data.executs.UpdateResultInFirebase
import com.github.kiolk.chemistrytests.data.executs.UploadDataInDb
import com.github.kiolk.chemistrytests.data.fragments.FeatureFragment
import com.github.kiolk.chemistrytests.data.fragments.FeatureFragment.Companion.IDE_SLIDE
import com.github.kiolk.chemistrytests.data.fragments.FeatureFragment.Companion.ROCKET_SLIDE
import com.github.kiolk.chemistrytests.data.fragments.FeatureFragment.Companion.STUDENT_SLIDE
import com.github.kiolk.chemistrytests.data.fragments.FeatureFragment.Companion.TARGET_SLIDE
import com.github.kiolk.chemistrytests.data.fragments.attachRoundIndicators
import com.github.kiolk.chemistrytests.data.managers.DataManager
import com.github.kiolk.chemistrytests.data.models.TestFragmentModel
import com.github.kiolk.chemistrytests.ui.activities.MainActivity.Companion.LANGUAGE_PREFERENCES
import com.github.kiolk.chemistrytests.ui.activities.MainActivity.Companion.LANGUAGE_PREFIX
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*

val SPEED_ROTATION: Long = 1000
val SPLASH_DURATION: Long = 2000
val PROVIDERS = listOf<AuthUI.IdpConfig>(AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(), AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build())
val RC_SIGN_IN: Int = 1

interface UserTouchListener{
    fun userTouch(int : Int)
}

class SplashActivity : AppCompatActivity() {

    companion object {
        val DURATION_SHOW_ONE_FEATURE : Long = 5000
    }

    lateinit var mHandler: Handler
    lateinit var mRunnable: Runnable
    lateinit var mResultCallback: ResultCallback
    lateinit var mAuthentication: FirebaseAuth
    lateinit var mAuthenticationListener: FirebaseAuth.AuthStateListener
    var isSetupAuth: Boolean = false
    var isUserStartLogin : Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setupTimer()
        loadData()
//        setupAuthentication()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(baseContext,
                        resources.getString(R.string.SUCCES_LOGGIN),
                        Toast.LENGTH_LONG)
                        .show()
                updateUserInformation()
//                startLoad()
            } else if (resultCode == Activity.RESULT_CANCELED) {
                finish()
            }
        }
    }

    private fun updateUserInformation() {
        val firbaseUser : FirebaseUser? = mAuthentication.currentUser
        if(firbaseUser != null){
            Log.d("MyLogs", "User uid when start ${firbaseUser.uid}")
            SingleAsyncTask().execute(UpdateResultInFirebase(firbaseUser.uid, object : ResultCallback{
                override fun <T> onSuccess(any: T?) {
                    val result : String = any as String
                    if(result == "Success"){
                        startLoad()
                    }
                }

                override fun onError() {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            }))
        }

    }

    private fun setupAuthentication() {
        isSetupAuth = true
        mAuthentication = FirebaseAuth.getInstance()
        mAuthenticationListener = object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                val firebaseUser: FirebaseUser? = p0.currentUser
                if (firebaseUser != null) {
                    Toast.makeText(baseContext,
                            resources.getString(R.string.SUCCES_LOGGIN),
                            Toast.LENGTH_LONG)
                            .show()
                    if(!isUserStartLogin) {
                        updateUserInformation()
//                        startLoad()
                    }
                } else {
                    showMainFeaturesPager()
                    isUserStartLogin = true
                }
            }
        }
        mAuthentication.addAuthStateListener(mAuthenticationListener)
    }

    private fun showMainFeaturesPager(){
        main_features_view_pager.visibility = View.VISIBLE
        dot_indicator_linear_layout.visibility = View.VISIBLE
        val mainFeatures = getMainFeatures()
        val adapter = FeaturesPageAdapter(supportFragmentManager, mainFeatures )
        val infinityPagerAdapter = InfinityPagerAdapter(adapter)
        main_features_view_pager.adapter = infinityPagerAdapter
        val dotCounter = mainFeatures.size
        login_button.visibility = View.VISIBLE
        login_button.setOnClickListener{
            startAuthenticationPage()
        }
//        setupFeatureTimer(dotCounter)

        val touchListener = object : UserTouchListener{
            override fun userTouch(int: Int) {
                changeButtonColor(int)
            }
        }
        attachRoundIndicators(baseContext, dot_indicator_linear_layout, main_features_view_pager, dotCounter, touchListener)
        val randomNumber = Random().nextInt(dotCounter)
        main_features_view_pager.currentItem = randomNumber
        changeButtonColor(randomNumber)
//
// mHandler = Handler()
//        var currentItem = 0
//        mRunnable = Runnable {
//            currentItem = main_features_view_pager.currentItem
//            if(currentItem < dotCounter - 1 ){
//                ++currentItem
//            }else {
//                currentItem = 0
//            }
//            main_features_view_pager.setCurrentItem(currentItem)
//            mHandler.postDelayed(mRunnable, DURATION_SHOW_ONE_FEATURE)
//        }
//        mHandler.postDelayed(mRunnable, DURATION_SHOW_ONE_FEATURE)
    }

    private fun changeButtonColor(int : Int){
        when(int){
            STUDENT_SLIDE -> login_button.setTextColor(resources.getColor(R.color.STUDENT_COLOR))
            ROCKET_SLIDE -> login_button.setTextColor(resources.getColor(R.color.ROCKET_COLOR))
            IDE_SLIDE -> login_button.setTextColor(resources.getColor(R.color.IDEA_COLOR))
            TARGET_SLIDE -> login_button.setTextColor(resources.getColor(R.color.TARGET_COLOR))
        }
    }

    private fun setupFeatureTimer(dotCounter: Int) {
        mHandler = Handler()
        var currentItem = 0
        mRunnable = Runnable {
            currentItem = main_features_view_pager.currentItem
            if(currentItem < dotCounter - 1 ){
                ++currentItem
                main_features_view_pager.setCurrentItem(currentItem)
                mHandler.postDelayed(mRunnable, DURATION_SHOW_ONE_FEATURE)
            } else {
                currentItem = 1
                main_features_view_pager.setCurrentItem(currentItem)
                mHandler.postDelayed(mRunnable, DURATION_SHOW_ONE_FEATURE)
            }
        }
        mHandler.postDelayed(mRunnable, DURATION_SHOW_ONE_FEATURE)
    }

    private fun closeMainFeaturesPager(){
        main_features_view_pager.visibility = View.GONE
        dot_indicator_linear_layout.visibility = View.GONE
        mHandler.removeCallbacks(mRunnable)
    }

    private fun getMainFeatures(): MutableList<TestFragmentModel> {
        return mutableListOf(TestFragmentModel("First", FeatureFragment()),
                TestFragmentModel("Second", FeatureFragment()),
                TestFragmentModel("Third", FeatureFragment()),
                TestFragmentModel("Fourth", FeatureFragment()))
    }

    fun startAuthenticationPage(){
        startActivityForResult(AuthUI.getInstance().
                createSignInIntentBuilder().
                setIsSmartLockEnabled(false).
                setAvailableProviders(PROVIDERS).
                build(), RC_SIGN_IN)
    }

    override fun onResume() {
        super.onResume()
//        val handler = Handler()
//        handler.postDelayed({if(!isSetupAuth){
//            setupAuthentication()
//        }
//        mAuthentication.addAuthStateListener(mAuthenticationListener)
//        setupTimer()
//        mHandler.postDelayed(mRunnable, DURATION_SHOW_ONE_FEATURE)
    }

    override fun onPostResume() {
        super.onPostResume()
    }

    override fun onPause() {
        super.onPause()
//        mHandler.removeCallbacks(mRunnable)
//        mAuthentication.removeAuthStateListener(mAuthenticationListener)

    }

    override fun onDestroy() {
        mAuthentication.removeAuthStateListener(mAuthenticationListener)
        mHandler.removeCallbacks(mRunnable)
        super.onDestroy()
    }

    private fun startLoad() {
        closeMainFeaturesPager()
        mAuthentication.removeAuthStateListener(mAuthenticationListener)
        logo_splash_image_view.visibility = View.VISIBLE
        my_progress_bar_image_view.visibility = View.GONE
        mResultCallback = object : ResultCallback {
            override fun <T> onSuccess(any: T?) {
                launchMainActivity()
            }

            override fun onError() {
            }
        }

        if (!checkConnection(baseContext)) {
            Log.d("MyLogs", "Continue work offline")
            Toast.makeText(baseContext, "You continue off line", Toast.LENGTH_SHORT).show()
            launchMainActivity()
        } else {
            val handler = Handler()
            handler.postDelayed({ SingleAsyncTask().execute(UploadDataInDb(mResultCallback)) }, SPLASH_DURATION)
        }
    }

    private fun setupTimer() {
        mHandler = Handler()
        mRunnable = Runnable {
            setupAuthentication()
//            setMyProgressBar()
//            mHandler.postDelayed(mRunnable, SPEED_ROTATION)
        }
        mHandler.postDelayed(mRunnable, 100)
    }

    private fun setMyProgressBar() {
        val string: String = my_progress_bar_image_view.tag.toString()
        val tag: Int = string.toInt()
        if (!listOf(2, 3, 4, 5, 6).contains(tag)) {
            my_progress_bar_image_view.setImageDrawable(resources.getDrawable(R.drawable.ic_benzolring1))
            my_progress_bar_image_view.tag = 2
            return
        }
        if (!listOf(1, 3, 4, 5, 6).contains(tag)) {
            my_progress_bar_image_view.setImageDrawable(resources.getDrawable(R.drawable.ic_benzolring2))
            my_progress_bar_image_view.tag = 3
            return
        }
        if (!listOf(2, 1, 4, 5, 6).contains(tag)) {
            my_progress_bar_image_view.setImageDrawable(resources.getDrawable(R.drawable.ic_benzolring3))
            my_progress_bar_image_view.tag = 4
            return
        }
        if (!listOf(2, 3, 1, 5, 6).contains(tag)) {
            my_progress_bar_image_view.setImageDrawable(resources.getDrawable(R.drawable.ic_benzolring4))
            my_progress_bar_image_view.tag = 5
            return
        }
        if (!listOf(2, 3, 4, 1, 6).contains(tag)) {
            my_progress_bar_image_view.setImageDrawable(resources.getDrawable(R.drawable.ic_benzolring5))
            my_progress_bar_image_view.tag = 6
            return
        }
        if (!listOf(2, 3, 4, 5, 1).contains(tag)) {
            my_progress_bar_image_view.setImageDrawable(resources.getDrawable(R.drawable.ic_benzolring))
            my_progress_bar_image_view.tag = 1
            return
        }
    }

    fun launchMainActivity() {
        val intent = Intent(baseContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun loadData() {
       val languageInterface = DataManager.instance.getInterfaceLanguage()
//        val preferences = getSharedPreferences(LANGUAGE_PREFERENCES, Activity.MODE_PRIVATE)
//        val lang = preferences.getString(LANGUAGE_PREFIX, "ru")
        val lang = languageInterface.languagePrefix
        if (lang != resources.configuration.locale.language && languageInterface.isUserSelection) {
            changeLocale(baseContext, lang)
        }
    }
}

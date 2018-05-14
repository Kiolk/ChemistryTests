package com.github.kiolk.chemistrytests.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import checkConnection
import com.firebase.ui.auth.AuthUI
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.asynctasks.ResultCallback
import com.github.kiolk.chemistrytests.data.asynctasks.SingleAsyncTask
import com.github.kiolk.chemistrytests.data.database.DBConnector
import com.github.kiolk.chemistrytests.data.executs.UploadDataInDb
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_splash.*

val SPEED_ROTATION : Long = 200
val SPLASH_DURATION : Long = 5000
val PROVIDERS = listOf<AuthUI.IdpConfig>(AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(), AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build())
val RC_SIGN_IN: Int = 1

class SplashActivity : AppCompatActivity() {

    lateinit var mHandler : Handler
    lateinit var mRunnable : Runnable
    lateinit var mResultCallback : ResultCallback
    lateinit var mAuthentication: FirebaseAuth
    lateinit var mAuthenticationListener: FirebaseAuth.AuthStateListener
    var isSetupAuth : Boolean = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setupAuthentication()
//        logo_splash_image_view.setOnSystemUiVisibilityChangeListener(object : View.OnSystemUiVisibilityChangeListener{
//            override fun onSystemUiVisibilityChange(visibility: Int) {
//                if (1==1){
//                    if(!isSetupAuth){
//                        setupAuthentication()
//                    }
//                    mAuthentication.addAuthStateListener(mAuthenticationListener)
//                }
//            }
//        })
//        val handler = Handler()
//        handler.postDelayed({logo_splash_image_view.visibility = View.VISIBLE}, 1000)
//        setupTimer()
//        mResultCallback = object : ResultCallback{
//            override fun onSuccess() {
//                launchMainActivity()
//            }
//
//            override fun onError() {
//            }
//        }
//
//        if (!checkConnection(baseContext)) {
//            Log.d("MyLogs", "Continue work offline")
//            Toast.makeText(baseContext, "You continue off line", Toast.LENGTH_SHORT).show()
//            launchMainActivity()
//        } else {
//            val handler = Handler()
//            handler.postDelayed({  SingleAsyncTask().execute(UploadDataInDb(mResultCallback)) }, SPLASH_DURATION)
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(baseContext,
                        resources.getString(R.string.SUCCES_LOGGIN),
                        Toast.LENGTH_LONG)
                        .show()
                startLoad()
            } else if (resultCode == Activity.RESULT_CANCELED) {
                finish()
            }
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
                    startLoad()

                } else {
                    startActivityForResult(AuthUI.getInstance().
                            createSignInIntentBuilder().
                            setIsSmartLockEnabled(false).
                            setAvailableProviders(PROVIDERS).
                            build(), RC_SIGN_IN)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
//        val handler = Handler()
//        handler.postDelayed({if(!isSetupAuth){
//            setupAuthentication()
//        }
            mAuthentication.addAuthStateListener(mAuthenticationListener)
    }

    override fun onPostResume() {
        super.onPostResume()
    }

    override fun onPause() {
        super.onPause()


    }

    override fun onDestroy() {
        mAuthentication.removeAuthStateListener(mAuthenticationListener)
        mHandler.removeCallbacks(mRunnable)
        super.onDestroy()
    }

    private fun startLoad(){
        logo_splash_image_view.visibility = View.GONE
        my_progress_bar_image_view.visibility = View.VISIBLE
        setupTimer()
        mResultCallback = object : ResultCallback{
            override fun onSuccess() {
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
            handler.postDelayed({  SingleAsyncTask().execute(UploadDataInDb(mResultCallback)) }, SPLASH_DURATION)
        }
    }

    private fun setupTimer() {
        mHandler = Handler()
        mRunnable = Runnable {
            setMyProgressBar()
//            mHandler.postDelayed(mRunnable, SPEED_ROTATION)
        }
        mHandler.postDelayed(mRunnable, SPEED_ROTATION)
    }

    private fun setMyProgressBar() {
        val string : String = my_progress_bar_image_view.tag.toString()
        val tag : Int = string.toInt()
        if(!listOf(2, 3, 4, 5, 6).contains(tag)){
            my_progress_bar_image_view.setImageDrawable(resources.getDrawable(R.drawable.ic_benzolring1))
            my_progress_bar_image_view.tag = 2
            return
        }
        if(!listOf(1, 3, 4, 5, 6).contains(tag)){
            my_progress_bar_image_view.setImageDrawable(resources.getDrawable(R.drawable.ic_benzolring2))
            my_progress_bar_image_view.tag = 3
            return
        }
        if(!listOf(2, 1, 4, 5, 6).contains(tag)){
            my_progress_bar_image_view.setImageDrawable(resources.getDrawable(R.drawable.ic_benzolring3))
            my_progress_bar_image_view.tag = 4
            return
        }
        if(!listOf(2, 3, 1, 5, 6).contains(tag)){
            my_progress_bar_image_view.setImageDrawable(resources.getDrawable(R.drawable.ic_benzolring4))
            my_progress_bar_image_view.tag = 5
            return
        }
        if(!listOf(2, 3, 4, 1, 6).contains(tag)){
            my_progress_bar_image_view.setImageDrawable(resources.getDrawable(R.drawable.ic_benzolring5))
            my_progress_bar_image_view.tag = 6
            return
        }
        if(!listOf(2, 3, 4, 5, 1).contains(tag)){
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
}

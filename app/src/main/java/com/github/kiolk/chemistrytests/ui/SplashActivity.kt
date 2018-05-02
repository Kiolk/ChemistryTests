package com.github.kiolk.chemistrytests.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import checkConnection
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.asynctasks.ResultCallback
import com.github.kiolk.chemistrytests.data.asynctasks.SingleAsyncTask
import com.github.kiolk.chemistrytests.data.database.DBConnector
import com.github.kiolk.chemistrytests.data.executs.UploadDataInDb
import kotlinx.android.synthetic.main.activity_splash.*

val SPEED_ROTATION : Long = 200
val SPLASH_DURATION : Long = 5000

class SplashActivity : AppCompatActivity() {

    lateinit var mHandler : Handler
    lateinit var mRunnable : Runnable
    lateinit var mResultCallback : ResultCallback



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        DBConnector.initInstance(baseContext)
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

    override fun onDestroy() {
        mHandler.removeCallbacks(mRunnable)
        super.onDestroy()
    }

    private fun setupTimer() {
        mHandler = Handler()
        mRunnable = Runnable {
            setMyProgressBar()
            mHandler.postDelayed(mRunnable, SPEED_ROTATION)
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

package com.github.kiolk.chemistrytests

import android.app.Application
import com.github.kiolk.chemistrytests.data.asynctasks.ResultCallback
import com.github.kiolk.chemistrytests.data.asynctasks.SingleAsyncTask
import com.github.kiolk.chemistrytests.data.database.DBConnector
import com.github.kiolk.chemistrytests.data.executs.AplicationInitialisationExecut
import com.google.firebase.messaging.FirebaseMessaging
import kiolk.com.github.pen.Pen
import kiolk.com.github.pen.utils.PenConstantsUtil

class ChemistryTestApp : Application(){
    override fun onCreate() {
        super.onCreate()
        SingleAsyncTask().execute(AplicationInitialisationExecut(baseContext, object : ResultCallback{
            override fun <T> onSuccess(any: T?) {
            }

            override fun onError() {
            }
        }))
//        initImageLoader()
//        DBConnector.initInstance(baseContext)
//
//        FirebaseMessaging.getInstance()
    }

    private fun initImageLoader() {
        Pen.getInstance().setLoaderSettings().
                setSizeInnerFileCache(20L).
                setContext(baseContext).
                setTypeOfCache(PenConstantsUtil.INNER_FILE_CACHE).
                setSavingStrategy(PenConstantsUtil.SAVE_FULL_IMAGE_STRATEGY).
//                setDefaultDrawable(baseContext.resources.getDrawable(R.drawable.area_neutral_shape)).
                setUp()
    }
}
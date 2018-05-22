package com.github.kiolk.chemistrytests

import android.app.Application
import com.github.kiolk.chemistrytests.data.database.DBConnector
import com.google.firebase.messaging.FirebaseMessaging
import kiolk.com.github.pen.Pen
import kiolk.com.github.pen.utils.PenConstantsUtil

class ChemistryTestApp : Application(){
    override fun onCreate() {
        super.onCreate()
        initImageLoader()
        DBConnector.initInstance(baseContext)

        FirebaseMessaging.getInstance()
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
package com.github.kiolk.chemistrytests.data.executs

import android.content.Context
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.asynctasks.ResultCallback
import com.github.kiolk.chemistrytests.data.asynctasks.ResultObject
import com.github.kiolk.chemistrytests.data.asynctasks.SingleExecut
import com.github.kiolk.chemistrytests.data.database.DBConnector
import com.github.kiolk.chemistrytests.data.managers.DataManager
import com.google.firebase.messaging.FirebaseMessaging
import kiolk.com.github.pen.Pen
import kiolk.com.github.pen.utils.PenConstantsUtil

class AplicationInitialisationExecut(var baseContext: Context, override var callback: ResultCallback) : SingleExecut {
    override fun perform(): ResultObject<*> {

        Pen.getInstance().setLoaderSettings().
                setSizeInnerFileCache(20L).
                setContext(baseContext).
                setTypeOfCache(PenConstantsUtil.INNER_FILE_CACHE).
                setSavingStrategy(PenConstantsUtil.SAVE_FULL_IMAGE_STRATEGY).
//                setDefaultDrawable(baseContext.resources.getDrawable(R.drawable.empty_background)).
                setUp()


        DBConnector.initInstance(baseContext)

        FirebaseMessaging.getInstance()

        DataManager.initInstance(baseContext)

        return ResultObject("Success", callback)
    }
}
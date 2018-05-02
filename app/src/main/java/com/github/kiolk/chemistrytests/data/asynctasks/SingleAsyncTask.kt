package com.github.kiolk.chemistrytests.data.asynctasks

import android.os.AsyncTask
import android.os.Handler

interface SingleExecut{
    var callback : ResultCallback
    fun perform()
}

interface ResultCallback{
    fun onSuccess()
    fun onError()
}

class SingleAsyncTask : AsyncTask<SingleExecut, Unit, ResultCallback?>(){

    override fun doInBackground(vararg params: SingleExecut?): ResultCallback? {
        val singleExecute : SingleExecut? = params[0]
        singleExecute?.perform()
        return singleExecute?.callback
    }
}
package com.github.kiolk.chemistrytests.data.asynctasks

import android.os.AsyncTask
import android.os.Handler

interface SingleExecut{
    var callback : ResultCallback
    fun perform(): ResultObject<*>
}

interface ResultCallback{
    fun <T> onSuccess(any : T? = null)
    fun onError()
}

//interface ResultObjedct(){
//
//    fun <T> add(any : T){
//        resultObject = any
//    }
//}

class ResultObject<T>(var resultObject : T, var callback: ResultCallback?){

}


class SingleAsyncTask : AsyncTask<SingleExecut, Unit, ResultObject<*>?>(){

    override fun doInBackground(vararg params: SingleExecut?): ResultObject<*>? {
        val singleExecute : SingleExecut? = params[0]
        return singleExecute?.perform()
    }

    override fun onPostExecute(result: ResultObject<*>?) {
        super.onPostExecute(result)
        result?.callback?.onSuccess(result.resultObject)
    }
}
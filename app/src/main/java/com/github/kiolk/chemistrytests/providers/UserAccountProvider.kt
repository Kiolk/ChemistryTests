package com.github.kiolk.chemistrytests.providers

import android.content.Context
import com.github.kiolk.chemistrytests.data.asynctasks.ResultCallback
import com.github.kiolk.chemistrytests.data.asynctasks.SingleAsyncTask
import com.github.kiolk.chemistrytests.data.database.DBOperations
import com.github.kiolk.chemistrytests.data.executs.UpdateResultInFirebase
import com.github.kiolk.chemistrytests.ui.fragments.getCurrentUser
import com.github.kiolk.chemistrytests.data.listeners.SimpleResultCallback
import com.github.kiolk.chemistrytests.data.managers.PrefGetter
import com.github.kiolk.chemistrytests.data.models.User

object UserAccountProvider {

    val ACCOUNT : String = "Account"

    fun changeTypeUserAccount(context : Context, typeAccount : Int, resultCallback : SimpleResultCallback){
      val user = getCurrentUser(context)
        user?.accountType = typeAccount
        user?.let { DBOperations().insertUser(it) }
        SingleAsyncTask().execute(user?.userId?.let { UpdateResultInFirebase(it, object : ResultCallback{
            override fun <T> onSuccess(any: T?) {
                resultCallback.successExecute()
            }

            override fun onError() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }) })
    }

    fun getAccountType(context : Context): Int {
        return PrefGetter.getInt(context, ACCOUNT)
    }

//    fun saveAccountType
//    fun getUser(){
//        val users: MutableList<User> = DBOperations().getAllUsers()
//        getCurrentUser()
//        var user: User? = users.find { it.userId == F.uid }
//    }
}

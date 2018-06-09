package com.github.kiolk.chemistrytests.data.executs

import com.github.kiolk.chemistrytests.data.asynctasks.ResultCallback
import com.github.kiolk.chemistrytests.data.asynctasks.ResultObject
import com.github.kiolk.chemistrytests.data.asynctasks.SingleExecut
import com.github.kiolk.chemistrytests.data.database.DBOperations
import com.github.kiolk.chemistrytests.data.models.TestParams
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AddCustomTestInUserDB(var customTestParams: TestParams, override var callback: ResultCallback) : SingleExecut {

    override fun perform(): ResultObject<*> {
        val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val users = DBOperations().getAllUsers()
            val currentUserObject = users.find { it.userId == currentUser.uid }
            if(currentUserObject != null){
                currentUserObject.userCustomTests.add(customTestParams)
                DBOperations().insertUser(currentUserObject)
            }
        }
        return ResultObject("Succes", callback)
    }
}

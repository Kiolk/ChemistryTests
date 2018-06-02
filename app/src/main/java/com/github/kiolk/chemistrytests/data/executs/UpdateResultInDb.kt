package com.github.kiolk.chemistrytests.data.executs

import android.util.Log
import com.github.kiolk.chemistrytests.data.asynctasks.ResultCallback
import com.github.kiolk.chemistrytests.data.asynctasks.ResultObject
import com.github.kiolk.chemistrytests.data.asynctasks.SingleExecut
import com.github.kiolk.chemistrytests.data.database.DBOperations
import com.github.kiolk.chemistrytests.data.models.ResultInformation
import com.github.kiolk.chemistrytests.data.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class UpdateResultInDb(var result: ResultInformation, override var callback: ResultCallback, val remove: Boolean = false) : SingleExecut {

    override fun perform(): ResultObject<*> {
        return updateResult()
    }

    private fun updateResult(): ResultObject<*> {
        val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            Log.d("MyLogs", "user features idToken${currentUser.uid}")
            val users: MutableList<User> = DBOperations().getAllUsers()
            var user: User? = users.find { it.userId == currentUser.uid }
            if (user != null && !remove) {
                user.completedTests.add(result)
                DBOperations().insertUser(user)
                Log.d("MyLogs", "user not exist ${user.userId}")
            } else if (user != null && remove){
                var position : Int = 0
                var cnt = 0
                        user.completedTests.forEach { if(it == result){
                            position = cnt
                        }
                        ++cnt}
                user.completedTests.removeAt(position)
                DBOperations().insertUser(user)
            } else {
                user = User(currentUser.uid)
                user.completedTests.add(result)
                Log.d("MyLogs", "user exist ${user.userId}")
                DBOperations().insertUser(user)
            }
        }
        return ResultObject("Success", callback)
    }
}
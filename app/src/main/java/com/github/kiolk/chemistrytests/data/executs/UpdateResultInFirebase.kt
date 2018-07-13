package com.github.kiolk.chemistrytests.data.executs

import com.github.kiolk.chemistrytests.data.asynctasks.ResultCallback
import com.github.kiolk.chemistrytests.data.asynctasks.ResultObject
import com.github.kiolk.chemistrytests.data.asynctasks.SingleExecut
import com.github.kiolk.chemistrytests.data.database.DBOperations
import com.github.kiolk.chemistrytests.data.models.User
import com.github.kiolk.chemistrytests.ui.activities.DATA_BASE_USERS_CHAILD
import com.google.firebase.database.*

class UpdateResultInFirebase(var userId: String, override var callback: ResultCallback) : SingleExecut {
    lateinit var mFirebaseDatabase: FirebaseDatabase
    lateinit var mDatabaseReference: DatabaseReference
    lateinit var mChildEventListener : ChildEventListener
    var user : User? = null

    override fun perform(): ResultObject<*> {
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mFirebaseDatabase.getReference().child(DATA_BASE_USERS_CHAILD)
        val users = DBOperations().getAllUsers()
        user = users.find { it.userId == userId }
        if(user != null){
            mDatabaseReference.child(userId).setValue(user)
            return ResultObject("Success", callback)
        }else{
            mChildEventListener = object : ChildEventListener{
                override fun onCancelled(p0: DatabaseError?) {
                }

                override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                }

                override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                }

                override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                    val user : User? = p0?.getValue(User::class.java)
                    if(user != null && user.userId == userId){
                        val res = DBOperations().insertUser(user)
                        mDatabaseReference.removeEventListener(mChildEventListener)
                        callback.onSuccess("Success")
                    }
                }

                override fun onChildRemoved(p0: DataSnapshot?) {
                }
            }
            mDatabaseReference.addChildEventListener(mChildEventListener)
        }
        return ResultObject("Wait", callback)
    }
}

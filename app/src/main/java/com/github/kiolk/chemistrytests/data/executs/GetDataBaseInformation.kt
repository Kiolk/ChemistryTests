package com.github.kiolk.chemistrytests.data.executs

import com.github.kiolk.chemistrytests.data.asynctasks.ResultCallback
import com.github.kiolk.chemistrytests.data.models.QuestionsDataBaseInfo
import com.github.kiolk.chemistrytests.utils.Constants.DATA_BASE_INFO_CHAILD
import com.google.firebase.database.*

class GetDataBaseInformation(var callback: ResultCallback) {

    lateinit var mChidInforListener: ChildEventListener
    lateinit var mFirebaseDatabase: FirebaseDatabase
    lateinit var mInfoDatabaseReference: DatabaseReference
    var dbInfo: QuestionsDataBaseInfo? = null

    init {
        getDBInformation()
    }

    private fun getDBInformation() {
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mInfoDatabaseReference = mFirebaseDatabase.reference.child(DATA_BASE_INFO_CHAILD)
        mChidInforListener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                dbInfo = p0?.getValue(QuestionsDataBaseInfo::class.java)
                mInfoDatabaseReference.removeEventListener(mChidInforListener)
                callback.onSuccess(dbInfo)
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
            }
        }
        mInfoDatabaseReference.addChildEventListener(mChidInforListener)
    }
}

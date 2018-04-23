package com.github.kiolk.chemistrytests.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import checkConnection
import com.firebase.ui.auth.AuthUI
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.database.DBConnector
import com.github.kiolk.chemistrytests.data.database.DBOperations
import com.github.kiolk.chemistrytests.data.fragments.AvaliableFragments
import com.github.kiolk.chemistrytests.data.fragments.AvaliableTestFragment
import com.github.kiolk.chemistrytests.data.models.CloseQuestion
import com.github.kiolk.chemistrytests.data.models.QuestionsDataBaseInfo
import com.github.kiolk.chemistrytests.data.models.TestParams
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    lateinit var mFirebaseDatabase: FirebaseDatabase
    lateinit var mDatabaseReference: DatabaseReference
    lateinit var mChaildEventListener: ChildEventListener
    lateinit var mTestDataBaseReference: DatabaseReference
    lateinit var mChildEventListener: ChildEventListener
    lateinit var mChidInforListener: ChildEventListener
    lateinit var mInfoDatabaseReference: DatabaseReference
    var dbInfo: QuestionsDataBaseInfo? = null
    var cnt = 0
    var cnt2 = 0
    var uploadTests: Boolean = false
    var uploadQuestions: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        DBConnector.initInstance(baseContext)
        if (!checkConnection(baseContext)) {
            Log.d("MyLogs", "Continue work offline")
            Toast.makeText(baseContext, "You continue off line", Toast.LENGTH_SHORT).show()
        } else {
            mFirebaseDatabase = FirebaseDatabase.getInstance()
            getDBInformation()
        }
    }

    private fun getDBInformation() {
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
                splashScreenSetup()
                val max = dbInfo?.availableQuestions
                if (max != null) splash_upload_progress_bar.max = max
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
            }
        }
        mInfoDatabaseReference.addChildEventListener(mChidInforListener)
    }


    private fun splashScreenSetup() {
        if (!checkConnection(baseContext)) {
            Log.d("MyLogs", "Continue work offline")
            Toast.makeText(baseContext, "You continue off line", Toast.LENGTH_SHORT).show()
        } else {
            mDatabaseReference = mFirebaseDatabase.reference.child(QUESTIONS_CHILDS)
            mChaildEventListener = object : ChildEventListener {
                override fun onCancelled(p0: DatabaseError?) {
                }

                override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                }

                override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                }

                override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                    val question = p0?.getValue(CloseQuestion::class.java)
                    question?.let { DBOperations().insertQuestion(it) }
                    cnt = cnt + 1
                    splash_upload_progress_bar.progress = cnt
                    Log.d("MyLogs", question?.questionId?.toString())
                    if (cnt == dbInfo?.availableQuestions) {
                        mDatabaseReference.removeEventListener(mChaildEventListener)
                        uploadQuestions = true
                        luanchMainActivity()
                        Log.d("MyLogs", "Remove child Event Listener")
                    }
                }

                override fun onChildRemoved(p0: DataSnapshot?) {
                }
            }
            mDatabaseReference.addChildEventListener(mChaildEventListener)
            Log.d("MyLogs", "Add child event listener")
            mTestDataBaseReference = mFirebaseDatabase.reference.child(TESTS_CHILD)
            mChildEventListener = object : ChildEventListener {
                override fun onCancelled(p0: DatabaseError?) {
                }

                override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                }

                override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                }

                override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                    val testParams: TestParams? = p0?.getValue(TestParams::class.java)
                    Log.d("MyLogs", "add new test")
                    cnt2 = cnt2 + 1
                    testParams?.let { DBOperations().insertTest(testParams) }
                    if (cnt2 == dbInfo?.avaIlableTests) {
                        Log.d("MyLogs", "Complete tests")
                        mTestDataBaseReference.removeEventListener(mChildEventListener)
                        uploadTests = true
                        luanchMainActivity()
                    }
                }

                override fun onChildRemoved(p0: DataSnapshot?) {
                }
            }
            mTestDataBaseReference.addChildEventListener(mChildEventListener)
        }
    }


    fun luanchMainActivity() {
        if (uploadQuestions && uploadTests) {
            val intent = Intent(baseContext, MainActivity::class.java)
            startActivity(intent)
        }
    }
}

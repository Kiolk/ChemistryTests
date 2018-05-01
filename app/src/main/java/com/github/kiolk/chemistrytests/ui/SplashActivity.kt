package com.github.kiolk.chemistrytests.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import checkConnection
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.database.DBConnector
import com.github.kiolk.chemistrytests.data.database.DBOperations
import com.github.kiolk.chemistrytests.data.models.CloseQuestion
import com.github.kiolk.chemistrytests.data.models.QuestionsDataBaseInfo
import com.github.kiolk.chemistrytests.data.models.TestParams
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*

class SplashActivity : AppCompatActivity() {

    lateinit var mFirebaseDatabase: FirebaseDatabase
    lateinit var mDatabaseReference: DatabaseReference
    lateinit var mChaildEventListener: ChildEventListener
    lateinit var mTestDataBaseReference: DatabaseReference
    lateinit var mChildEventListener: ChildEventListener
    lateinit var mChidInforListener: ChildEventListener
    lateinit var mInfoDatabaseReference: DatabaseReference
    lateinit var mTimer: Timer
    var dbInfo: QuestionsDataBaseInfo? = null
    var cnt = 0
    var cnt2 = 0
    var uploadTests: Boolean = false
    var uploadQuestions: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        DBConnector.initInstance(baseContext)
        setupTimer()
        if (!checkConnection(baseContext)) {
            Log.d("MyLogs", "Continue work offline")
            Toast.makeText(baseContext, "You continue off line", Toast.LENGTH_SHORT).show()
            uploadQuestions = true
            uploadTests = true
            launchMainActivity()
        } else {
            mFirebaseDatabase = FirebaseDatabase.getInstance()
            getDBInformation()
        }
    }

    private fun setupTimer() {
        mTimer = Timer()
        mTimer.schedule(object : TimerTask() {
            override fun run() {
                if(!listOf(2, 3, 4, 5, 6 ).contains(my_progress_bar_image_view.tag)) {
                    my_progress_bar_image_view.setImageDrawable(resources.getDrawable(R.drawable.ic_benzolring))
                    my_progress_bar_image_view.tag = 2
                    return
                }
                if(!listOf(1, 3, 4, 5, 6 ).contains(my_progress_bar_image_view.tag)) {
                    my_progress_bar_image_view.setImageDrawable(resources.getDrawable(R.drawable.ic_benzolring1))
                    my_progress_bar_image_view.tag = 3
                    return
                }
                if(!listOf(2, 1, 4, 5, 6 ).contains(my_progress_bar_image_view.tag)) {
                    my_progress_bar_image_view.setImageDrawable(resources.getDrawable(R.drawable.ic_benzolring2))
                    my_progress_bar_image_view.tag = 4
                    return
                }
                if(!listOf(2, 3, 1, 5, 6 ).contains(my_progress_bar_image_view.tag)) {
                    my_progress_bar_image_view.setImageDrawable(resources.getDrawable(R.drawable.ic_benzolring3))
                    my_progress_bar_image_view.tag = 5
                    return
                }
                if(!listOf(2, 3, 4, 1, 6 ).contains(my_progress_bar_image_view.tag)) {
                    my_progress_bar_image_view.setImageDrawable(resources.getDrawable(R.drawable.ic_benzolring4))
                    my_progress_bar_image_view.tag = 6
                    return
                }
                if(!listOf(2, 3, 4, 5, 1 ).contains(my_progress_bar_image_view.tag)) {
                    my_progress_bar_image_view.setImageDrawable(resources.getDrawable(R.drawable.ic_benzolring5))
                    my_progress_bar_image_view.tag = 1
                    return
                }
            }
        }, 100000, 1000)
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
                        launchMainActivity()
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
                        launchMainActivity()
                    }
                }

                override fun onChildRemoved(p0: DataSnapshot?) {
                }
            }
            mTestDataBaseReference.addChildEventListener(mChildEventListener)
        }
    }

    fun launchMainActivity() {
        if (uploadQuestions && uploadTests) {
            val intent = Intent(baseContext, MainActivity::class.java)
            startActivity(intent)
            mTimer.cancel()
            finish()
        }
    }
}

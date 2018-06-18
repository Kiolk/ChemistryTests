package com.github.kiolk.chemistrytests.data.executs

import android.util.Log
import com.github.kiolk.chemistrytests.data.asynctasks.ResultCallback
import com.github.kiolk.chemistrytests.data.asynctasks.ResultObject
import com.github.kiolk.chemistrytests.data.asynctasks.SingleExecut
import com.github.kiolk.chemistrytests.data.database.DBOperations
import com.github.kiolk.chemistrytests.data.models.*
import com.github.kiolk.chemistrytests.ui.activities.*
import com.google.firebase.database.*

class UploadDataInDb(override var callback: ResultCallback) : SingleExecut {

    lateinit var mFirebaseDatabase: FirebaseDatabase
    lateinit var mDatabaseReference: DatabaseReference
    lateinit var mChaildEventListener: ChildEventListener
    lateinit var mTestDataBaseReference: DatabaseReference
    lateinit var mCoursesDataBaseReference: DatabaseReference
    lateinit var mTheoryDataBaseReference: DatabaseReference
    lateinit var mChildEventListener: ChildEventListener
    lateinit var mChidInforListener: ChildEventListener
    lateinit var mChidCoursesListener: ChildEventListener
    lateinit var mChildTheoryListener: ChildEventListener
    lateinit var mInfoDatabaseReference: DatabaseReference
    var uploadTests: Boolean = false
    var uploadQuestions: Boolean = false
    var isUpdateCources : Boolean = false
    var isTheoryUpload : Boolean = false
    var cnt = 0
    var cnt2 = 0
    var cnt3 = 0
    var cntTheory = 0
    var dbInfo: QuestionsDataBaseInfo? = null

    override fun perform(): ResultObject<*> {
        return ResultObject("Succes", callback)
    }

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
                splashScreenSetup()
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
            }
        }
        mInfoDatabaseReference.addChildEventListener(mChidInforListener)
    }


    private fun splashScreenSetup() {
        mDatabaseReference = mFirebaseDatabase.reference.child(QUESTIONS_CHILDS)
        mChaildEventListener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                val question : CloseQuestion? = p0?.getValue(CloseQuestion::class.java)
                question?.let { DBOperations().insertQuestion(it) }
                cnt = cnt + 1
                Log.d("MyLogs", question?.questionId?.toString())
                if (cnt == dbInfo?.availableQuestions) {
                    mDatabaseReference.removeEventListener(mChaildEventListener)
                    uploadQuestions = true
                    setOnSuccess()
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
                    setOnSuccess()
                }
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
            }
        }
        mTestDataBaseReference.addChildEventListener(mChildEventListener)

        updateCourses()
        updateTheory()
    }

    private fun updateTheory(){
        mTheoryDataBaseReference = mFirebaseDatabase.reference.child(DATA_THEORY_CHILD)
        mChildTheoryListener = object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                val theory : ChemTheoryModel? = p0?.getValue(ChemTheoryModel::class.java)
                if(theory != null){
                    cntTheory = cntTheory +1
                    DBOperations().insertTheory(theory)
                    if(cntTheory == dbInfo?.availableTheory){
                        isTheoryUpload = true
                        mTheoryDataBaseReference.removeEventListener(mChildTheoryListener)
                        setOnSuccess()
                    }
                }
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
            }
        }
        mTheoryDataBaseReference.addChildEventListener(mChildTheoryListener)
    }

    private fun updateCourses() {
        mCoursesDataBaseReference = mFirebaseDatabase.reference.child(DATA_COURSES_CHILD)
        mChidCoursesListener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                val course: Course? = p0?.getValue(Course::class.java)
                Log.d("MyLogs", "add new test")
                cnt3 = cnt3 + 1
                course?.let { DBOperations().insertCourse(it) }
                if (cnt3 == dbInfo?.availableCourses) {
                    Log.d("MyLogs", "Complete tests")
                    mCoursesDataBaseReference.removeEventListener(mChidCoursesListener)
                    isUpdateCources = true
                     setOnSuccess()
                }
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
            }
        }
        mCoursesDataBaseReference.addChildEventListener(mChidCoursesListener)
    }

    private fun setOnSuccess(){
        if(uploadTests && uploadQuestions && (isUpdateCources && isTheoryUpload)){
//            callback.onSuccess("Success")
            perform()
        }
    }
}
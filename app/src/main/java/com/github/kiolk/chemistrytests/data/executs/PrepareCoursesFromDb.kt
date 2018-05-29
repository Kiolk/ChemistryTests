package com.github.kiolk.chemistrytests.data.executs

import com.github.kiolk.chemistrytests.data.asynctasks.ResultCallback
import com.github.kiolk.chemistrytests.data.asynctasks.ResultObject
import com.github.kiolk.chemistrytests.data.asynctasks.SingleExecut
import com.github.kiolk.chemistrytests.data.database.DBOperations
import com.github.kiolk.chemistrytests.data.models.Course
import com.github.kiolk.chemistrytests.data.models.ResultInformation
import com.github.kiolk.chemistrytests.data.models.TestParams
import com.google.firebase.auth.FirebaseAuth

class PrepareCoursesFromDb(override var callback: ResultCallback) : SingleExecut{
    override fun perform(): ResultObject<*> {
        val courses = DBOperations().getAllCources()
        val tests = DBOperations().getAllTestsParams()
        val userHistory : MutableList<ResultInformation>? = DBOperations().getUser(FirebaseAuth.getInstance().currentUser?.uid)?.completedTests
        courses.forEach {
            val tests = it.filterTestParams(tests)
        it.mCoursTestParams = tests}
        courses.forEach {
            val comletedTest : MutableList<Int> = mutableListOf()
            it.mCoursTestParams.forEach {
                val result : TestParams = it
                val testParams = userHistory?.find { it.testParams?.testId == result.testId }
                if(testParams != null){
                    testParams.testParams?.testId?.let { it1 -> comletedTest.add(it1) }
                }
            }
            it.mCompletedTests = comletedTest
        }
        return ResultObject(courses, callback)
    }

//    private fun getCourses() : MutableList<Course> {
//        val courses = DBOperations().getAllCources()
//        val tests = DBOperations().getAllTestsParams()
//        courses.forEach { it.filterTestParams(tests) }
////        callback.onSuccess(courses)
//        return ResultObject(courses, callback)
//    }
//
//    override fun getResultObject(): ResultObject<*> {
//        val courses = getCourses()
//        return ResultObject(courses, callback)
//    }
}
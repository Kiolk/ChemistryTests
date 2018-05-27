package com.github.kiolk.chemistrytests.data.executs

import com.github.kiolk.chemistrytests.data.asynctasks.ResultCallback
import com.github.kiolk.chemistrytests.data.asynctasks.ResultObject
import com.github.kiolk.chemistrytests.data.asynctasks.SingleExecut
import com.github.kiolk.chemistrytests.data.database.DBOperations
import com.github.kiolk.chemistrytests.data.models.Course

class PrepareCoursesFromDb(override var callback: ResultCallback) : SingleExecut{
    override fun perform(): ResultObject<*> {
        val courses = DBOperations().getAllCources()
        val tests = DBOperations().getAllTestsParams()
        courses.forEach {
            val tests = it.filterTestParams(tests)
        it.mCoursTestParams = tests}
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
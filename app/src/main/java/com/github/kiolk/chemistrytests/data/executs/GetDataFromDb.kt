package com.github.kiolk.chemistrytests.data.executs

import com.github.kiolk.chemistrytests.data.asynctasks.ResultCallback
import com.github.kiolk.chemistrytests.data.asynctasks.ResultObject
import com.github.kiolk.chemistrytests.data.asynctasks.SingleExecut
import com.github.kiolk.chemistrytests.data.database.DBOperations
import reversSort

class GetDataFromDb(override var callback: ResultCallback) : SingleExecut{
    override fun perform(): ResultObject<*> {
        var tests = DBOperations().getAllTestsParams()
        tests = reversSort(tests)
        return ResultObject(tests, callback)
    }
}
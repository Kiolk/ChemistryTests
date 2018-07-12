package com.github.kiolk.chemistrytests.data.executs

import com.github.kiolk.chemistrytests.data.asynctasks.ResultCallback
import com.github.kiolk.chemistrytests.data.asynctasks.ResultObject
import com.github.kiolk.chemistrytests.data.asynctasks.SingleExecut
import com.github.kiolk.chemistrytests.data.database.DBOperations
import com.github.kiolk.chemistrytests.data.models.AcountModel

class GetAccountsFromDb(override var callback: ResultCallback) : SingleExecut{
    override fun perform(): ResultObject<*> {
//        val account : AcountModel = AcountModel("Free", listOf("More then 1000 questions", "Explanation of question",
//             "Theory"), "en", 1, 0.99F)
//
//        val list : List<AcountModel> = listOf(account, account, account)
//        return ResultObject(list, callback)
        return ResultObject(DBOperations().getAllAccountInformation(), callback)
    }
}
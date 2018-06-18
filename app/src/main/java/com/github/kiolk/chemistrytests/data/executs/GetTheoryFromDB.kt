package com.github.kiolk.chemistrytests.data.executs

import com.github.kiolk.chemistrytests.data.asynctasks.ResultCallback
import com.github.kiolk.chemistrytests.data.asynctasks.ResultObject
import com.github.kiolk.chemistrytests.data.asynctasks.SingleExecut
import com.github.kiolk.chemistrytests.data.database.DBOperations
import com.github.kiolk.chemistrytests.data.models.ChemTheoryModel

class GetTheoryFromDB (val listTheoryId : List<Long>, override var callback: ResultCallback) : SingleExecut{
    override fun perform(): ResultObject<*> {
        val listChemTheory : MutableList<ChemTheoryModel> = DBOperations().getAllTheory()
        val tmp : MutableList<ChemTheoryModel> = mutableListOf()
        listTheoryId.forEach {
            val id : Long = it
            val theory = listChemTheory.find { it.theoryId == id }
            if(theory != null){
                tmp.add(theory)
            }
        }
        tmp.sortBy { it.theoryTitle }
        return ResultObject(tmp, callback)
    }
}
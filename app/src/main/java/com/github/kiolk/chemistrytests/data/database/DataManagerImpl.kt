package com.github.kiolk.chemistrytests.data.database

import com.github.kiolk.chemistrytests.data.asynctasks.ResultCallback
import com.github.kiolk.chemistrytests.data.executs.GetDataBaseInformation

class DataManagerImpl : DataManager{

    override fun executeActualInformation(callback: ResultCallback) {
        GetDataBaseInformation(callback)
    }
}
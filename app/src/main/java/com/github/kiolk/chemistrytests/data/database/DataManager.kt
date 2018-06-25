package com.github.kiolk.chemistrytests.data.database

import com.github.kiolk.chemistrytests.data.asynctasks.ResultCallback

interface DataManager{

    fun executeActualInformation(callback : ResultCallback)

}
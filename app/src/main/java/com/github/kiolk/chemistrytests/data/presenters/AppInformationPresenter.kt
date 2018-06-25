package com.github.kiolk.chemistrytests.data.presenters

import com.github.kiolk.chemistrytests.data.asynctasks.ResultCallback

interface AppInformationPresenter {

    val callback : ResultCallback

    fun getActualDataInformation()

}
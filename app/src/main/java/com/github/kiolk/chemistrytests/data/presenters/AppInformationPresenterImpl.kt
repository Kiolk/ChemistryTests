package com.github.kiolk.chemistrytests.data.presenters

import com.github.kiolk.chemistrytests.data.asynctasks.ResultCallback
import com.github.kiolk.chemistrytests.data.database.DataManagerImpl
import com.github.kiolk.chemistrytests.data.fragments.AppInformationView
import com.github.kiolk.chemistrytests.data.models.QuestionsDataBaseInfo

class AppInformationPresenterImpl(var informationView: AppInformationView) : AppInformationPresenter, ResultCallback {

    init {
        informationView.showProgressBar(true)
    }

    override fun <T> onSuccess(any: T?) {
        if(any == null) {
            return
        }
        informationView.showProgressBar(false)
        if (any is QuestionsDataBaseInfo) {
            informationView.setAppInfo(any as QuestionsDataBaseInfo)
        }
    }

    override fun onError() {
    }

    override fun getActualDataInformation() {
        informationView.showProgressBar(true)
        DataManagerImpl().executeActualInformation(this)
    }
}

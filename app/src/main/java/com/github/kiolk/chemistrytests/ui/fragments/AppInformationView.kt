package com.github.kiolk.chemistrytests.ui.fragments

import com.github.kiolk.chemistrytests.data.models.QuestionsDataBaseInfo

interface AppInformationView {
    fun setAppInfo(dataBaseInfo : QuestionsDataBaseInfo)

    fun showProgressBar(show : Boolean)
}
package com.github.kiolk.chemistrytests.data.fragments

import com.github.kiolk.chemistrytests.data.models.QuestionsDataBaseInfo

interface AppInformationView {
    fun setAppInfo(dataBaseInfo : QuestionsDataBaseInfo)

    fun showProgressBar(show : Boolean)
}
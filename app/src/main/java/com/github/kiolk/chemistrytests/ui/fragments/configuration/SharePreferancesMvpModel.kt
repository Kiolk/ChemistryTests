package com.github.kiolk.chemistrytests.ui.fragments.configuration

import com.github.kiolk.chemistrytests.data.models.LanguageModel

interface SharePreferancesMvpModel{
    fun getInterfaceLanguage() : LanguageModel
    fun saveInterfaceLanguage(language: LanguageModel)
}

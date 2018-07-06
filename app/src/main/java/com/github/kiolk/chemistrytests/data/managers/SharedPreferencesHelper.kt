package com.github.kiolk.chemistrytests.data.managers

import com.github.kiolk.chemistrytests.data.models.LanguageModel

interface SharedPreferencesHelper{
    fun getInterfaceLanguage() : LanguageModel
    fun saveInterfaceLanguage(language: LanguageModel)
}
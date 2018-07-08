package com.github.kiolk.chemistrytests.data.fragments.configuration

interface ConfigurationMvpPresenter{
    fun prepareSettings()
    fun saveLanguage(item : Int)
    fun prepareLanguageDialog()
    fun getLanguagePrefix(which: Int): String
    fun prepareAccentColorDialog()
}
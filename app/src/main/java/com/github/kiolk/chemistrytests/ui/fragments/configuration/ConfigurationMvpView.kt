package com.github.kiolk.chemistrytests.ui.fragments.configuration

import com.github.kiolk.chemistrytests.data.models.MenuItemModel

interface ConfigurationMvpView{
    fun setupListSettings(itemsArray: List<MenuItemModel>)
    fun showLanguageDialog(languageArray : Array<String>, checkedLanguage : Int)
    fun showAccentColorDialog()
    fun showDayNightModeDialog()
}
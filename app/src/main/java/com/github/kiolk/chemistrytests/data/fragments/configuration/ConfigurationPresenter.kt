package com.github.kiolk.chemistrytests.data.fragments.configuration

import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.models.MenuItemModel

class ConfigurationPresenter(var configurationView : ConfigurationMvpView) : ConfigurationMvpPresenter{

    override fun prepareSettings() {
        val itemsArray: List<MenuItemModel> = listOf(MenuItemModel(R.drawable.ic_test, "First"),
                MenuItemModel(R.drawable.ic_study_notes, "Second"),
                MenuItemModel(R.drawable.ic_puzzle, "Third"),
                MenuItemModel(R.drawable.ic_history, "Fourth"),
                MenuItemModel(R.drawable.ic_statistic, "Fifth"))
        configurationView.setupListSettings(itemsArray)
    }
}
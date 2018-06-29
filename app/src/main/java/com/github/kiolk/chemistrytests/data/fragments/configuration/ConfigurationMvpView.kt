package com.github.kiolk.chemistrytests.data.fragments.configuration

import com.github.kiolk.chemistrytests.data.models.MenuItemModel

interface ConfigurationMvpView{
    fun setupListSettings(itemsArray: List<MenuItemModel>)
}
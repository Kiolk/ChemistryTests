package com.github.kiolk.chemistrytests.data.managers

import com.github.kiolk.chemistrytests.data.fragments.accounts.AccountMvpPresenter
import com.github.kiolk.chemistrytests.data.models.AcountModel

interface DataBaseHelper{
    fun getAccountModels(presenter : AccountMvpPresenter)
}
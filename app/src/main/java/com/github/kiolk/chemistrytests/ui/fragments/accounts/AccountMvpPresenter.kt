package com.github.kiolk.chemistrytests.ui.fragments.accounts

import com.github.kiolk.chemistrytests.data.models.AcountModel

interface AccountMvpPresenter{
    fun preparAvaelbleAccounts(list : List<AcountModel>)
    fun requestAvelbleAccounts(language : String)
}
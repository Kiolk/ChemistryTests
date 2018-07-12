package com.github.kiolk.chemistrytests.data.fragments.accounts

import com.github.kiolk.chemistrytests.data.models.AcountModel
import com.github.kiolk.chemistrytests.data.models.TestFragmentModel

interface AccountMvpView{
    fun showAccounts(listFragment : List<AcountModel>)
}

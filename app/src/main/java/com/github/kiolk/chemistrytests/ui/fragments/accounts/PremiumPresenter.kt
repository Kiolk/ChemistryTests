package com.github.kiolk.chemistrytests.ui.fragments.accounts

import android.support.v4.app.Fragment
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.managers.DataManager
import com.github.kiolk.chemistrytests.data.models.AcountModel
import com.github.kiolk.chemistrytests.data.models.TestFragmentModel

class PremiumPresenter(var accountFragment : AccountMvpView) : AccountMvpPresenter{

    var mLanguage : String = "en"

    override fun requestAvelbleAccounts(language : String) {
        mLanguage = language
        DataManager.instance.getAccountModels(this)
    }

    override fun preparAvaelbleAccounts(list : List<AcountModel>) {
        val mutableList : MutableList<AcountModel> = mutableListOf()
        mutableList.addAll(list.filter { it.descriptionLanguage == mLanguage })
        accountFragment.showAccounts(mutableList)
    }
}

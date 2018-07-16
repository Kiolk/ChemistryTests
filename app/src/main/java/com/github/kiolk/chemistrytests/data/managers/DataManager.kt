package com.github.kiolk.chemistrytests.data.managers

import android.content.Context
import android.content.res.Resources
import com.github.kiolk.chemistrytests.data.asynctasks.ResultCallback
import com.github.kiolk.chemistrytests.data.asynctasks.SingleAsyncTask
import com.github.kiolk.chemistrytests.data.database.DBOperations
import com.github.kiolk.chemistrytests.data.executs.GetAccountsFromDb
import com.github.kiolk.chemistrytests.ui.fragments.accounts.AccountMvpPresenter
import com.github.kiolk.chemistrytests.data.models.AcountModel
import com.github.kiolk.chemistrytests.data.models.LanguageModel

class DataManager private constructor(context: Context) : SharedPreferencesHelper, ResourcesHelper, DataBaseHelper {
    override fun getResources(): Resources? {
        return resources.getResources()
    }

    private val languageProvider: LanguageProvider = LanguageProvider(context)
    private val resources: ResourcesImpl = ResourcesImpl(context)
    private val dataBase: DBOperations = DBOperations()

    override fun getInterfaceLanguage(): LanguageModel {
        return languageProvider.getInterfaceLanguage()
    }

    override fun saveInterfaceLanguage(language: LanguageModel) {
        languageProvider.saveInterfaceLanguage(language)
    }

    companion object {
        lateinit var instance: DataManager
        fun initInstance(context: Context) {
//            if(instance == null){
            instance = DataManager(context)
//            }
        }
    }

    override fun getAccountModels(presenter: AccountMvpPresenter) {
        val resultCallBack: ResultCallback = object : ResultCallback {
            override fun <T> onSuccess(any: T?) {
                val accounts = any as List<AcountModel>
                presenter.preparAvaelbleAccounts(accounts)
            }

            override fun onError() {
            }
        }
        SingleAsyncTask().execute(GetAccountsFromDb(resultCallBack))
    }
}



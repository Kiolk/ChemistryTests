package com.github.kiolk.chemistrytests.data.managers

import android.content.Context
import android.content.res.Resources
import com.github.kiolk.chemistrytests.data.models.LanguageModel

class DataManager private constructor (context : Context) : SharedPreferencesHelper, ResourcesHelper{
    override fun getResources(): Resources? {
        return resources.getResources()
    }

    private val sharedPreferences: SharedPreferences = SharedPreferences(context)
    private val resources : ResourcesImpl = ResourcesImpl(context)

    override fun getInterfaceLanguage(): LanguageModel {
        return sharedPreferences.getInterfaceLanguage()
    }

    override fun saveInterfaceLanguage(language: LanguageModel) {
        sharedPreferences.saveInterfaceLanguage(language)
    }

    companion object {
        lateinit var instance : DataManager
        fun initInstance(context : Context){
//            if(instance == null){
                instance = DataManager(context)
//            }
        }
    }

}



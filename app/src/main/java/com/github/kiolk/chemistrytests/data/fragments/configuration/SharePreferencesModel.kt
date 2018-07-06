package com.github.kiolk.chemistrytests.data.fragments.configuration

import android.app.Activity
import android.content.Context
import com.github.kiolk.chemistrytests.data.models.LanguageModel
import com.github.kiolk.chemistrytests.ui.activities.MainActivity
import com.github.kiolk.chemistrytests.ui.activities.MainActivity.Companion.LANGUAGE_SELECTED

class SharePreferencesModel(var contex : Context?) : SharePreferancesMvpModel {

    override fun getInterfaceLanguage(): LanguageModel {
        val preferences = contex?.getSharedPreferences(MainActivity.LANGUAGE_PREFERENCES, Activity.MODE_PRIVATE)
        if(preferences != null) {
            return LanguageModel(preferences.getString(MainActivity.LANGUAGE_PREFIX, "ru"),
                    preferences.getBoolean(LANGUAGE_SELECTED, false))
        } else{
            return LanguageModel()
        }
    }

    override fun saveInterfaceLanguage(language: LanguageModel) {
        val prefernces = contex?.getSharedPreferences(MainActivity.LANGUAGE_PREFERENCES, Activity.MODE_PRIVATE)
        if(prefernces != null) {
            val editor = prefernces?.edit()
            editor.putString(MainActivity.LANGUAGE_PREFIX, language.languagePrefix)
            editor.putBoolean(LANGUAGE_SELECTED, language.isUserSelection)
            editor.commit()
            return
        }
    }
}
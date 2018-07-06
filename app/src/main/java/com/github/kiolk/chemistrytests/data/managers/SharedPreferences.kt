package com.github.kiolk.chemistrytests.data.managers

import android.app.Activity
import android.content.Context
import com.github.kiolk.chemistrytests.data.models.LanguageModel
import com.github.kiolk.chemistrytests.ui.activities.MainActivity

class SharedPreferences(var context : Context) : SharedPreferencesHelper{

    override fun getInterfaceLanguage(): LanguageModel {
        val preferences = context.getSharedPreferences(MainActivity.LANGUAGE_PREFERENCES, Activity.MODE_PRIVATE)
        if(preferences != null) {
            return LanguageModel(preferences.getString(MainActivity.LANGUAGE_PREFIX, "ru"),
                    preferences.getBoolean(MainActivity.LANGUAGE_SELECTED, false))
        } else{
            return LanguageModel()
        }
    }

    override fun saveInterfaceLanguage(language: LanguageModel) {
        val prefernces = context.getSharedPreferences(MainActivity.LANGUAGE_PREFERENCES, Activity.MODE_PRIVATE)
        if(prefernces != null) {
            val editor = prefernces.edit()
            editor.putString(MainActivity.LANGUAGE_PREFIX, language.languagePrefix)
            editor.putBoolean(MainActivity.LANGUAGE_SELECTED, language.isUserSelection)
            editor.commit()
            return
        }
    }
}

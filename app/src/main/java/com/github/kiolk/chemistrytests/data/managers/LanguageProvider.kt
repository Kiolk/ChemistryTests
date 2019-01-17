package com.github.kiolk.chemistrytests.data.managers

import android.app.Activity
import android.content.Context
import com.github.kiolk.chemistrytests.data.models.LanguageModel
import com.github.kiolk.chemistrytests.ui.activities.MainActivity

class LanguageProvider(var context: Context) : SharedPreferencesHelper {

    companion object {

        val LANGUAGE_SELECTED = "Language_selected"
        val LANGUAGE_PREFIX = "Language"

//        fun getThemeAccent(context: Context): Int {
//            val themeColor = context?.let { PrefGetter.getString(it, THEME_COLOR) }
//            if (themeColor != null) {
//                return themeColor.toInt()
//            } else {
//                return -1
//            }
//        }
    }

    override fun getInterfaceLanguage(): LanguageModel {
        val language = PrefGetter.getString(context, LANGUAGE_PREFIX)
        return if (language != null) {
            LanguageModel(language, PrefGetter.getBoolean(context, LANGUAGE_SELECTED))
        } else {
            LanguageModel(isUserSelection = PrefGetter.getBoolean(context, LANGUAGE_SELECTED))
        }
//        return language != null ? LanguageModel( isUserSelection = PrefGetter.getBoolean(context, MainActivity.LANGUAGE_SELECTED)) :
//        LanguageModel(language, PrefGetter.getBoolean(context, MainActivity.LANGUAGE_SELECTED)
//        val preferences = context.getSharedPreferences(MainActivity.LANGUAGE_PREFERENCES, Activity.MODE_PRIVATE)
//        if(preferences != null) {
//            return LanguageModel(preferences.getString(MainActivity.LANGUAGE_PREFIX, "ru"),
//                    preferences.getBoolean(MainActivity.LANGUAGE_SELECTED, false))
//        } else{
//            return LanguageModel()
//        }
    }

    override fun saveInterfaceLanguage(language: LanguageModel) {
//        val prefernces = context.getSharedPreferences(MainActivity.LANGUAGE_PREFERENCES, Activity.MODE_PRIVATE)
//        if (prefernces != null) {
//            val editor = prefernces.edit()
//            editor.putString(MainActivity.LANGUAGE_PREFIX, language.languagePrefix)
//            editor.putBoolean(MainActivity.LANGUAGE_SELECTED, language.isUserSelection)
//            editor.commit()
            PrefSetter.putString(context, LANGUAGE_PREFIX, language.languagePrefix)
            PrefSetter.putBoolean(context, LANGUAGE_SELECTED, language.isUserSelection)
//            return
        }
    }


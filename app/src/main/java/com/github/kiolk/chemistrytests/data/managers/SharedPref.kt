package com.github.kiolk.chemistrytests.data.managers

import android.app.Activity
import android.content.Context
import com.github.kiolk.chemistrytests.data.models.LanguageModel
import com.github.kiolk.chemistrytests.ui.activities.MainActivity

class SharedPref(var context : Context) : SharedPreferencesHelper{

    companion object {

        val THEME_MODE : String = "ThemeMode"
        val THEME_COLOR : String = "ThemeColor"

//        fun getThemeMode(context : Context) : Int{
//            return themeMode(context)
//        }
//
//        private fun themeMode(context : Context?) : Int{
//            val themeMode = context?.let { PrefGetter.getString(it, THEME_MODE) }
//            if(themeMode != null){
//                return themeMode.toInt()
//            }else{
//                return -1
//            }
//        }

        fun getThemeAccent(context: Context): Int {
            val themeColor = context?.let { PrefGetter.getString(it, THEME_COLOR ) }
            if (themeColor != null){
                return themeColor.toInt()
            }else{
                return -1
            }
        }
    }

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

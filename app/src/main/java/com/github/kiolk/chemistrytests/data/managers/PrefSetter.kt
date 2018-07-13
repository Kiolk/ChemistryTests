package com.github.kiolk.chemistrytests.data.managers

import android.app.Activity
import android.content.Context
import com.github.kiolk.chemistrytests.ui.activities.MainActivity

object PrefSetter{
    fun putString(context : Context, key : String, value : String){
        val prefernces = context.getSharedPreferences(MainActivity.LANGUAGE_PREFERENCES, Activity.MODE_PRIVATE)
        val editor = prefernces.edit()
            editor.putString(key, value)
        editor.commit()
    }

    fun putInt(context : Context, key : String, value : Int){
        val prefernces = context.getSharedPreferences(MainActivity.LANGUAGE_PREFERENCES, Activity.MODE_PRIVATE)
        val editor = prefernces.edit()
        editor.putInt(key, value)
        editor.commit()
    }
}
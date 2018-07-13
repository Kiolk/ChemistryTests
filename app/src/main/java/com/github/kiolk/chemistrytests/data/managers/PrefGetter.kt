package com.github.kiolk.chemistrytests.data.managers

import android.app.Activity
import android.content.Context
import com.github.kiolk.chemistrytests.ui.activities.MainActivity

object PrefGetter {
    fun getString(context : Context, key : String) : String?{
        val prefernces = context.getSharedPreferences(MainActivity.LANGUAGE_PREFERENCES, Activity.MODE_PRIVATE)
        val value : String? = prefernces.getString(key, null)
        return value
    }

    fun getInt(context : Context, key : String) : Int {
        val preferences = context.getSharedPreferences(MainActivity.LANGUAGE_PREFERENCES, Activity.MODE_PRIVATE)
        return preferences.getInt(key, 0)
    }
}
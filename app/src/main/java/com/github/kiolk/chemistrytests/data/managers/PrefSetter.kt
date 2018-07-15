package com.github.kiolk.chemistrytests.data.managers

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE

object PrefSetter {
    @SuppressLint("ApplySharedPref")
    fun putString(context: Context, key: String, value: String) {
        val preferences = context.getSharedPreferences(PrefGetter.GENERAL_PREFERENCES, MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(key, value)
        editor.commit()
    }

    @SuppressLint("ApplySharedPref")
    fun putInt(context: Context, key: String, value: Int) {
        val preferences = context.getSharedPreferences(PrefGetter.GENERAL_PREFERENCES, MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putInt(key, value)
        editor.commit()
    }

    @SuppressLint("ApplySharedPref")
    fun putBoolean(context: Context, key: String, value: Boolean) {
        val preferences = context.getSharedPreferences(PrefGetter.GENERAL_PREFERENCES, MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean(key, value)
        editor.commit()
    }
}
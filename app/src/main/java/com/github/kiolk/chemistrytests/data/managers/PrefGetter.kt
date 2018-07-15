package com.github.kiolk.chemistrytests.data.managers

import android.content.Context
import android.content.Context.MODE_PRIVATE

object PrefGetter {

    val GENERAL_PREFERENCES = "Language_preferences"

    fun getString(context: Context, key: String): String? {
        val preferences = context.getSharedPreferences(GENERAL_PREFERENCES, MODE_PRIVATE)
        return preferences.getString(key, null)
    }

    fun getInt(context: Context, key: String): Int {
        val preferences = context.getSharedPreferences(GENERAL_PREFERENCES, MODE_PRIVATE)
        return preferences.getInt(key, 0)
    }

    fun getBoolean(context: Context, key: String): Boolean {
        val preferences = context.getSharedPreferences(GENERAL_PREFERENCES, MODE_PRIVATE)
        return preferences.getBoolean(key, false)
    }
}
package com.github.kiolk.chemistrytests.providers

import android.content.Context
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.managers.PrefGetter
import com.github.kiolk.chemistrytests.data.managers.PrefSetter
import com.github.kiolk.chemistrytests.ui.activities.base.BaseActivity

object ThemeProvider {

    private val THEME_MODE: String = "ThemeMode"
    private val THEME_COLOR: String = "ThemeColor"

    val DAY_MODE: Int = 0
    val NIGHT_MODE: Int = 1

    val RED: Int = 0
    val YELLOW: Int = 1
    val GREEN: Int = 2
    val PINK: Int = 3

    fun apply(activity: BaseActivity) {
        val themeMode = PrefGetter.getInt(activity, THEME_MODE)
        val themeAccent = PrefGetter.getInt(activity, THEME_COLOR)
//         val themeAccent = LanguageProvider.getThemeAccent(activity)
        activity.setTheme(getTheme(themeMode, themeAccent))
    }

    private fun getTheme(themeMode: Int, themeAccent: Int): Int {
        return when (themeMode) {
            DAY_MODE -> {
                when (themeAccent) {
                    YELLOW -> R.style.MyThemeYellowLight
                    RED -> R.style.MyThemeRedLight
                    GREEN -> R.style.MyThemeGreenLight
                    else -> R.style.MyThemeYellowLight
                }

            }
            NIGHT_MODE -> {
                when (themeAccent) {
                    PINK -> R.style.MyTheme_Dark
                    YELLOW -> R.style.MyThemeYellowDark
                    RED -> R.style.MyThemeRedDark
                    GREEN -> R.style.MyThemeGreenDark
                    else -> R.style.MyTheme_Dark
                }

            }
            else -> R.style.MyTheme_Dark
        }
    }

    fun setAccentColor(context: Context, color: Int) {
        PrefSetter.putInt(context, THEME_COLOR, color)
    }

    fun setNightMode(context: Context, dayMode: Int) {
        PrefSetter.putInt(context, THEME_MODE, dayMode)
    }

    fun getThemeMode(context: Context): Int {
        return PrefGetter.getInt(context, THEME_MODE)
    }

    fun getThemeAccent(context: Context): Int {
        return PrefGetter.getInt(context, THEME_COLOR)
    }
}

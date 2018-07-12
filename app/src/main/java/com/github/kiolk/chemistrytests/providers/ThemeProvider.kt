package com.github.kiolk.chemistrytests.providers

import android.content.Context
import android.content.SharedPreferences
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.managers.PrefSetter
import com.github.kiolk.chemistrytests.data.managers.SharedPref
import com.github.kiolk.chemistrytests.data.managers.SharedPref.Companion.THEME_COLOR
import com.github.kiolk.chemistrytests.ui.activities.base.BaseActivity

object ThemeProvider{

    val DAY_MODE : Int = 0
    val NIGHT_MODE : Int = 1

    val RED : Int = 0
    val YELLOW : Int = 1
    val GREEN : Int = 2
    val PINK : Int = 3

     fun apply(activity : BaseActivity){
         val themeMode = SharedPref.getThemeMode(activity)
         val themeAccent = SharedPref.getThemeAccent(activity)
         activity.setTheme(getTheme(themeMode, themeAccent))
     }

    private fun getTheme(themeMode: Int, themeAccent: Int): Int {
        return when(themeMode){
            DAY_MODE ->{when(themeAccent){
                PINK -> R.style.MyTheme_Dark
                YELLOW -> R.style.MyThemeYellowLight
                RED -> R.style.MyThemeRedLight
                GREEN -> R.style.MyThemeGreenLight
                else -> R.style.MyTheme_Dark
            }

            }
            NIGHT_MODE ->{when(themeAccent){
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

    fun setAccentColor(context : Context, color: Int) {
        PrefSetter.putString(context, THEME_COLOR, color.toString())
    }
}

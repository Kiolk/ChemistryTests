package com.github.kiolk.chemistrytests.utils

import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

object CONSTANTS{
    val DATE_PATTERN: String = "dd MMM yyyy - hh:mm:ss"
    val DURATION_TIME : String = "KK:mm:ss"
    val SHORT_DURATION_TIME : String = "KK:mm"
    val DAY_PATERN : String = "dd.MM.yyyy"
    val SLASH_DAY_PATERN : String = "dd/MM/yyyy"
    val TIMER_PATTERN : String = "hh:mm:ss"
}

fun convertEpochTime(date : Long, context : Context?, pattern : String, isAbsoluteTime: Boolean = false) : String {
    val day =  Date(date)
    val locale : Locale? = context?.resources?.configuration?.locale
    val formatter = SimpleDateFormat(pattern, locale)
    if(isAbsoluteTime) {
        formatter.timeZone = TimeZone.getTimeZone("Africa/Banjul")
    }
    return formatter.format(day)
}

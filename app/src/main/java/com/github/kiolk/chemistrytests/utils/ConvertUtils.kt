package com.github.kiolk.chemistrytests.utils

import android.content.Context
import com.github.kiolk.chemistrytests.utils.CONSTANTS.DATE_PATTERN
import java.text.SimpleDateFormat
import java.util.*

object CONSTANTS{
    val DATE_PATTERN: String = "dd MMM yyyy - hh:mm:ss"
    val DURATION_TIME : String = "hh:mm:ss"
    val DAY_PATERN : String = "dd.MM.yyyy"
    val SLASH_DAY_PATERN : String = "dd/MM/yyyy"
}

fun convertEpochTime(date : Long, context : Context?, pattern : String) : String {

    val day =  Date(date)
    val locale : Locale? = context?.resources?.configuration?.locale
    val formatter = SimpleDateFormat(pattern, locale)
    return formatter.format(day)
}

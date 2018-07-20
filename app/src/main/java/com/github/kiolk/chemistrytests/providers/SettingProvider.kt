package com.github.kiolk.chemistrytests.providers

import android.content.Context
import com.github.kiolk.chemistrytests.data.managers.PrefGetter
import com.github.kiolk.chemistrytests.data.managers.PrefSetter

object SettingProvider{

    private val USER_ID : String = "UserId"

    public fun stroeID(context: Context, userId : String){
        PrefSetter.putString(context, USER_ID, userId)
    }

    public fun getId(context: Context) : String?{
        return PrefGetter.getString(context, USER_ID)
    }
}

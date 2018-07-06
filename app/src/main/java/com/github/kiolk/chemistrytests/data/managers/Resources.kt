package com.github.kiolk.chemistrytests.data.managers

import android.content.Context
import android.content.res.Resources

class ResourcesImpl(var context : Context): ResourcesHelper{
    override fun getResources(): Resources? {
        return context.resources
    }
}

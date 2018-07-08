package com.github.kiolk.chemistrytests.ui.activities.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.kiolk.chemistrytests.providers.ThemeProvider

open class BaseActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        setupTheme()
        super.onCreate(savedInstanceState)
    }

    private fun setupTheme() {
        ThemeProvider.apply(this)
    }
}

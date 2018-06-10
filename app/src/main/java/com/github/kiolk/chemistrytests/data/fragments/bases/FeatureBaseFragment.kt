package com.github.kiolk.chemistrytests.data.fragments.bases

import android.support.v4.app.Fragment
import android.widget.Button
import com.github.kiolk.chemistrytests.ui.activities.SplashActivity

open class FeatureBaseFragment : Fragment() {

    fun attachLoginButton(button : Button){
        button.setOnClickListener {
            val splashActivity = activity as SplashActivity
            splashActivity.startAuthenticationPage()
        }
    }
}
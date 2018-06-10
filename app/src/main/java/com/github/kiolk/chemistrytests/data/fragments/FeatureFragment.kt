package com.github.kiolk.chemistrytests.data.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.fragments.bases.FeatureBaseFragment

class FeatureFragment : FeatureBaseFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_feature, null)
        attachLoginButton(view.findViewById(R.id.login_feature_fragment_button))
        return view //super.onCreateView(inflater, container, savedInstanceState)
    }
}
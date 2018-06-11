package com.github.kiolk.chemistrytests.data.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.github.kiolk.chemistrytests.data.fragments.FeatureFragment
import com.github.kiolk.chemistrytests.data.models.TestFragmentModel

class FeaturesPageAdapter(fm : FragmentManager, var listFeatures : MutableList<TestFragmentModel>) :  FragmentStatePagerAdapter(fm){

    override fun getItem(position: Int): Fragment {
        val fragment = listFeatures[position].fragment as FeatureFragment
           return fragment.fromInstance(position)
    }

    override fun getCount(): Int {
        return listFeatures.size
    }
}
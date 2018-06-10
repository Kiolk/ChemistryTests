package com.github.kiolk.chemistrytests.data.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.github.kiolk.chemistrytests.data.models.TestFragmentModel

class FeaturesPageAdapter(fm : FragmentManager, var listFeatures : MutableList<TestFragmentModel>) :  FragmentStatePagerAdapter(fm){

    override fun getItem(position: Int): Fragment {
           return listFeatures[position].fragment
    }

    override fun getCount(): Int {
        return listFeatures.size
    }
}
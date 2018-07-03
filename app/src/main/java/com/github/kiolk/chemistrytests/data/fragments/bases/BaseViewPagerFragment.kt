package com.github.kiolk.chemistrytests.data.fragments.bases

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.ProgressBar
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.fragments.BaseFragment
import kotlinx.android.synthetic.main.tabbed_view_pager.view.*

abstract class BaseViewPagerFragment : BaseFragment(){

    abstract fun fragmentLayout() : Int?

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if(fragmentLayout() != null){
            val view = fragmentLayout()?.let { inflater.inflate(it, container, false) }
            return view
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun getViewPager() : ViewPager? {
       return view?.findViewById(R.id.view_pager)
    }
    fun getTabLayout() : TabLayout?{
        return view?.findViewById(R.id.tab_layout)
    }

    fun getProgerssBas() = view?.findViewById<ProgressBar>(R.id.progress_bar)
}


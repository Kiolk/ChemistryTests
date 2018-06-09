package com.github.kiolk.chemistrytests.data.fragments

import android.support.v4.app.Fragment
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.View
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.ui.activities.MainActivity

open class BaseFragment : Fragment(){

    companion object {
        val DRAWER_LAYOUT_ID : Int = R.id.main_drawer_layout
    }


    fun setupToolBar(view : View, viewId : Int) {
        val toolBar = view.findViewById<Toolbar>(viewId)
        val navigation = View.OnClickListener {
            val mainActivity = activity as MainActivity
            mainActivity.findViewById<DrawerLayout>(DRAWER_LAYOUT_ID).openDrawer(Gravity.START)
        }
        toolBar?.setNavigationOnClickListener(navigation)
    }
}

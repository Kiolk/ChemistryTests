package com.github.kiolk.chemistrytests.data.fragments

import android.support.v4.app.Fragment
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.View
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.ui.activities.MainActivity

open class BaseFragment : Fragment(){


    fun setupToolBar(view : View) {
        val toolBar = view.findViewById<Toolbar>(R.id.custom_test_tool_bar)
        val navigation = object : View.OnClickListener {
            override fun onClick(v: View?) {
                val mainActivity = activity as MainActivity
                mainActivity.findViewById<DrawerLayout>(R.id.main_drawer_layout).openDrawer(Gravity.START)
            }
        }
        toolBar?.setNavigationOnClickListener(navigation)
    }
}

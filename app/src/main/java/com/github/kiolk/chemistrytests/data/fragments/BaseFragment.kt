package com.github.kiolk.chemistrytests.data.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.ui.activities.MainActivity
import kotlinx.android.synthetic.main.activity_main.*

abstract class BaseFragment : Fragment(){

    companion object {
        val DRAWER_LAYOUT_ID : Int = R.id.main_drawer_layout
    }

    abstract val titleId : Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setupToolBar()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun setupToolBar(view : View? = null, viewId : Int = 0) {
        val mainActivity = activity as MainActivity
        var toolBar = view?.findViewById<Toolbar>(viewId)
        if(toolBar == null){
            toolBar = mainActivity.main_tool_bar
        }
//        val navigation = View.OnClickListener {
//            val mainActivity = activity as MainActivity
//            mainActivity.findViewById<DrawerLayout>(DRAWER_LAYOUT_ID).openDrawer(Gravity.START)
//        }
        toolBar?.setNavigationOnClickListener{
            mainActivity.findViewById<DrawerLayout>(DRAWER_LAYOUT_ID).openDrawer(Gravity.START)
        }
        toolBar?.setTitle(titleId)
    }
}

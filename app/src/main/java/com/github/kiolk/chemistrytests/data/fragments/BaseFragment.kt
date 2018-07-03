package com.github.kiolk.chemistrytests.data.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.Toolbar
import android.view.*
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.ui.activities.MainActivity
import kotlinx.android.synthetic.main.tool_bar_main.*

abstract class BaseFragment : Fragment() {

    companion object {
        val DRAWER_LAYOUT_ID: Int = R.id.main_drawer_layout
    }

    abstract val titleId: Int
    abstract val menuId: Int?

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setupToolBar()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun setupToolBar(view: View? = null, viewId: Int = 0) {
        val mainActivity = activity as MainActivity
        var toolBar = view?.findViewById<Toolbar>(viewId)
        if (toolBar == null) {
            toolBar = mainActivity.main_tool_bar
        }
        toolBar?.setNavigationOnClickListener {
            mainActivity.findViewById<DrawerLayout>(DRAWER_LAYOUT_ID).openDrawer(Gravity.START)
        }
        toolBar?.setTitle(titleId)
        menuId?.let { toolBar?.menu?.findItem(it) }?.isVisible = true
        menuId?.let { mainActivity.updateMenu(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_main, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        menuId?.let { menu?.findItem(it) }?.isVisible = true
        super.onPrepareOptionsMenu(menu)
    }
}

package com.github.kiolk.chemistrytests.ui.fragments.bases

import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.github.kiolk.chemistrytests.data.adapters.AvailableTestRecyclerAdapter
import com.github.kiolk.chemistrytests.ui.fragments.BaseFragment
import com.github.kiolk.chemistrytests.data.listeners.OnItemClickListener
import com.github.kiolk.chemistrytests.data.listeners.RecyclerTouchListener

abstract class RecyclerTestBaseFragment : com.github.kiolk.chemistrytests.ui.fragments.BaseFragment(){
    override val menuId: Int?
        get() = null

    fun attachRecyclerView(view : View, recyclerId: Int, adapter : AvailableTestRecyclerAdapter?, itemTouchListener : OnItemClickListener){
        val recycler = view.findViewById<RecyclerView>(recyclerId)
        recycler?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycler?.adapter = adapter
        recycler?.isNestedScrollingEnabled = true
        recycler?.addOnItemTouchListener(RecyclerTouchListener(context, recycler, itemTouchListener))
    }
}
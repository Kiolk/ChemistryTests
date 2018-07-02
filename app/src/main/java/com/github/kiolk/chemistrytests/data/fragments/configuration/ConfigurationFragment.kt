package com.github.kiolk.chemistrytests.data.fragments.configuration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.adapters.MenuCustomArrayAdapter
import com.github.kiolk.chemistrytests.data.fragments.BaseFragment
import com.github.kiolk.chemistrytests.data.models.MenuItemModel

class ConfigurationFragment : BaseFragment(), ConfigurationMvpView{
    override val menuId: Int?
        get() = null


    var mPresenter = ConfigurationPresenter(this)

    override fun setupListSettings(itemsArray: List<MenuItemModel>) {
        setupToolBar()
        val listView = view?.findViewById<ListView>(R.id.stetting_list_view)
        val adapter = context?.let { MenuCustomArrayAdapter(it, R.layout.item_navigation_menu, itemsArray ) }
        listView?.adapter = adapter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       val view = inflater.inflate(R.layout.fragment_list_configuration, null)
        return view
    }

    override val titleId: Int
        get() = R.string.CONFIGURATION

}
package com.github.kiolk.chemistrytests.data.fragments.configuration

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import changeLocale
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.adapters.MenuCustomArrayAdapter
import com.github.kiolk.chemistrytests.data.fragments.BaseFragment
import com.github.kiolk.chemistrytests.data.models.MenuItemModel
import com.github.kiolk.chemistrytests.ui.activities.MainActivity

class ConfigurationFragment : BaseFragment(), ConfigurationMvpView {

    override val menuId: Int?
        get() = null


    var mPresenter = ConfigurationPresenter(this)

    override fun setupListSettings(itemsArray: List<MenuItemModel>) {
        setupToolBar()
        val listView = view?.findViewById<ListView>(R.id.stetting_list_view)
        val adapter = context?.let { MenuCustomArrayAdapter(it, R.layout.item_navigation_menu, itemsArray) }
        listView?.adapter = adapter
        listView?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            when (position) {
                0 -> {
                    Toast.makeText(context, "Change Language", Toast.LENGTH_SHORT).show()
                    mPresenter.prepareLanguageDialog()
                }
                else -> {

                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_list_configuration, null)
        return view
    }

    override val titleId: Int
        get() = R.string.CONFIGURATION


    override fun showLanguageDialog(languageArray: Array<String>, checkedLanguage: Int) {
        var position = 0
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
        dialogBuilder.setTitle(resources.getString(R.string.SELECT_LANGUAGE))
                .setSingleChoiceItems(languageArray, checkedLanguage) { dialog, which ->
                position = which}
        dialogBuilder.setPositiveButton(resources.getString(R.string.RESTART_APLICTION)) { dialog, which ->
            val activity = activity as MainActivity
            mPresenter.saveLanguage(position)
            context?.let { changeLocale(it, mPresenter.getLanguagePrefix(position)) }
            activity.restart()
        }
        val dialog = dialogBuilder.create()
        dialog.show()
    }
}
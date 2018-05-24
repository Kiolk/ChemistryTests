package com.github.kiolk.chemistrytests.data.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.models.MenuItemModel

class MenuCustomeArrayAdapter(var contexte: Context,
                              var layoutResourceId: Int,
                              var itemsArray: List<MenuItemModel>) : ArrayAdapter<MenuItemModel>(contexte, layoutResourceId, itemsArray) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//        val activity = contexte as Activity
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view =inflater.inflate(layoutResourceId, parent, false)
        val item = itemsArray[position]
        view?.findViewById<ImageView>(R.id.item_menu_icon_image_view)?.setImageResource(item.icon)
        view?.findViewById<TextView>(R.id.item_title_text_view)?.text = item.itemTitle
        return view //super.getView(position, convertView, parent)
    }
}
package com.github.kiolk.chemistrytests.data.adapters.recyclers

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.kiolk.chemistrytests.R

class FeaturesRecyclerAdapter(var context : Context, var featuresList: List<String>) : RecyclerView.Adapter<FeaturesRecyclerAdapter.FeaturesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): FeaturesViewHolder {
        val view = LayoutInflater.from(context).inflate( R.layout.item_feature,parent,  false)
        return FeaturesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return featuresList.size
    }

    override fun onBindViewHolder(holder: FeaturesViewHolder?, position: Int) {
        holder?.itemText?.text = featuresList[position]
    }


    class FeaturesViewHolder internal constructor(item: View) : RecyclerView.ViewHolder(item) {
        var itemText : TextView = item.findViewById(R.id.feature_item_text_view)
    }
}
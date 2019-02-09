package com.github.kiolk.chemistrytests.data.adapters.recyclers

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.models.ChemTheoryModel
import com.github.kiolk.chemistrytests.data.models.readingDuration
import kotlinx.android.synthetic.main.card_theory_item.view.*

class TheoryRecyclerAdapter(var context : Context, var listTheory : List<ChemTheoryModel>) : RecyclerView.Adapter<TheoryRecyclerAdapter.TheoryViewHolder>(){

    override fun getItemCount(): Int {
        return listTheory.size
    }

    override fun onBindViewHolder(holder: TheoryViewHolder?, position: Int) {
        holder?.title?.text = listTheory[position].theoryTitle
        holder?.readDuration?.text = listTheory[position].readingDuration().toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TheoryViewHolder {
        val view : View = LayoutInflater.from(context).inflate(R.layout.card_theory_item, parent, false)
        return TheoryViewHolder(view)
    }

    class TheoryViewHolder internal constructor(item : View) : RecyclerView.ViewHolder(item){
        val title : TextView = item.theory_item_title_text_view
        val readDuration : TextView = item.read_duration_text_view
    }

}

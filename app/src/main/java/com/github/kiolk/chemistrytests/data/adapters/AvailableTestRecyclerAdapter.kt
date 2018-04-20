package com.github.kiolk.chemistrytests.data.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.models.TestParams
import kotlinx.android.synthetic.main.card_test_params.view.*

class AvailableTestRecyclerAdapter(var context : Context, var avaliableTests : List<TestParams>) : RecyclerView.Adapter<AvailableTestRecyclerAdapter.AvaliableTestViewHolder>(){

    override fun onBindViewHolder(holder: AvaliableTestViewHolder?, position: Int) {
        val test = avaliableTests[position]
        holder?.title?.text = test.testInfo.testTitle
        holder?.author?.text = test.testInfo.testAuthor
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AvaliableTestViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.card_test_params, parent, false)
        return AvaliableTestViewHolder(view)
    }

    override fun getItemCount(): Int {
        return avaliableTests.size
    }

    class AvaliableTestViewHolder internal constructor(item : View) : RecyclerView.ViewHolder(item){
        var title : TextView = item.test_title_card_text_view
        var author : TextView = item.author_card_text_view
    }

}



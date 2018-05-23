package com.github.kiolk.chemistrytests.data.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.models.ResultInformation
import com.github.kiolk.chemistrytests.data.models.TestParams
import kiolk.com.github.pen.Pen
import kotlinx.android.synthetic.main.card_test_params.view.*
import reversSort

class AvailableTestRecyclerAdapter(var context : Context,
                                   var avaliableTests : MutableList<TestParams>? = null,
                                   var completedTests : MutableList<ResultInformation>? = null) : RecyclerView.Adapter<AvailableTestRecyclerAdapter.AvaliableTestViewHolder>(){


//    init {
//        if(avaliableTests != null){
//            avaliableTests = reversSort(avaliableTests!!)
//        }
//        if(completedTests != null){
//            completedTests = reversSort(completedTests!!)
//        }
//    }

    override fun onBindViewHolder(holder: AvaliableTestViewHolder?, position: Int) {
        var test : TestParams? = null
        val size = itemCount - 1
        if(avaliableTests != null){
//            val tmp = reversSort(avaliableTests!!)
//            avaliableTests = tmp
            test = avaliableTests!![position]
        }else if(completedTests != null){
//            val tmp = reversSort(completedTests!!)
//            completedTests = tmp
            test = completedTests!![position].testParams
        }
        setupTestInformation(holder, test)

    }

    private fun setupTestInformation(holder: AvaliableTestViewHolder?, test: TestParams?) {
        holder?.title?.text = test?.testInfo?.testTitle
        holder?.author?.text = test?.testInfo?.testAuthor
        Pen.getInstance().getImageFromUrl(test?.testInfo?.testIcon).inputTo(holder?.icon)
        holder?.lablLayer?.background = context.resources.getDrawable(R.drawable.area_square_shape_correct)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AvaliableTestViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.card_test_params, parent, false)
        return AvaliableTestViewHolder(view)
    }

    override fun getItemCount(): Int {
        if(avaliableTests != null){
            return avaliableTests!!.size
        }else{
           return completedTests?.size ?: 0
        }
    }

    class AvaliableTestViewHolder internal constructor(item : View) : RecyclerView.ViewHolder(item){
        var title : TextView = item.test_title_card_text_view
        var author : TextView = item.author_card_text_view
        var infoBlock : RelativeLayout = item.test_info_block_relative_layout
        var icon : ImageView = item.test_icon_image_view
        var lablLayer : LinearLayout = item.color_label_linear_layout

    }

}



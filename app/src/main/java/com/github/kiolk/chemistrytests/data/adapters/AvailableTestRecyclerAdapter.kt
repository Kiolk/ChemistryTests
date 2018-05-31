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
import com.github.kiolk.chemistrytests.data.fragments.setupIcons
import com.github.kiolk.chemistrytests.data.models.CloseQuestion.Question.COMPLEX_QUESTION
import com.github.kiolk.chemistrytests.data.models.CloseQuestion.Question.EASY_QUESTION
import com.github.kiolk.chemistrytests.data.models.CloseQuestion.Question.HARD_QUESTION
import com.github.kiolk.chemistrytests.data.models.CloseQuestion.Question.NORMAL_QUESTION
import com.github.kiolk.chemistrytests.data.models.CloseQuestion.Question.SIMPLE_QUESTION
import com.github.kiolk.chemistrytests.data.models.ResultInformation
import com.github.kiolk.chemistrytests.data.models.TestParams
import com.github.kiolk.chemistrytests.ui.customviews.RoundedImageView
import com.github.kiolk.chemistrytests.utils.CONSTANTS.SLASH_DAY_PATERN
import com.github.kiolk.chemistrytests.utils.convertEpochTime
import kiolk.com.github.pen.Pen
import kotlinx.android.synthetic.main.card_test_params.view.*
import reversSort

class AvailableTestRecyclerAdapter(var context : Context,
                                   var avaliableTests : MutableList<TestParams>? = null,
                                   var completedTests : MutableList<ResultInformation>? = null,
                                   var listCompletedTsts : List<Int>? = null) : RecyclerView.Adapter<AvailableTestRecyclerAdapter.AvaliableTestViewHolder>(){


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
        holder?.completedTestMark?.visibility = View.GONE
        if(completedTests != null && completedTests!![position].isCompleted){
            holder?.completedTestMark?.visibility = View.VISIBLE
        }
        setupTestInformation(holder, test)
    }

    private fun setupTestInformation(holder: AvaliableTestViewHolder?, test: TestParams?) {
        holder?.title?.text = test?.testInfo?.testTitle
        holder?.author?.text = test?.testInfo?.testAuthor
        holder?.createTime?.text = test?.testInfo?.testCreated?.let { convertEpochTime(it, context, SLASH_DAY_PATERN) }
        holder?.passedIcon?.visibility = View.GONE
        if(listCompletedTsts?.contains(test?.testId)==true){
            holder?.background?.background = context.resources.getDrawable(R.drawable.area_completed_test_shape)
            holder?.passedIcon?.visibility = View.VISIBLE
        }
        Pen.getInstance().getImageFromUrl(test?.testInfo?.testIcon).inputTo(holder?.icon)
        holder?.lablLayer?.background = when(test?.questionsStrength){
            EASY_QUESTION -> context.resources.getDrawable(R.drawable.area_easy_question)
            SIMPLE_QUESTION -> context.resources.getDrawable(R.drawable.area_simple_question)
            NORMAL_QUESTION -> context.resources.getDrawable(R.drawable.area_normal_question)
            COMPLEX_QUESTION -> context.resources.getDrawable(R.drawable.area_strong_question)
            HARD_QUESTION ->  context.resources.getDrawable(R.drawable.area_hard_question)
            else -> {
                context.resources.getDrawable(R.drawable.area_simple_question)
            }
        }
        holder?.itemView?.let { setupIcons(it, test, true) }
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
        var background : RelativeLayout = item.item_test_card_background_relative_layout
        var completedTestMark : ImageView = item.test_completed_mark_image_view
        var createTime : TextView = item.test_created_time_text_view
        var passedIcon : ImageView = item.passed_stamp_image_view
    }
}



package com.github.kiolk.chemistrytests.ui.customviews

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.Checkable
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.models.CloseQuestion

class QuestionView (var contextView : Context): LinearLayout(contextView), Checkable {



    var view : View
    var questionTitle : TextView
    var isCheckedItem : Boolean = false
    var itemLayout : RelativeLayout

    init {
        val layoutInflater : LayoutInflater = LayoutInflater.from(contextView)
        view = layoutInflater.inflate(R.layout.item_question_list, null)
        questionTitle = view.findViewById(R.id.question_title_text_view)
        itemLayout = view.findViewById(R.id.question_item_relative_layout)
    }

    fun setQuestion(question: CloseQuestion?){
       questionTitle.text = question?.questionEn
    }

    override fun isChecked(): Boolean {
        return isCheckedItem
    }

    override fun toggle() {
        isCheckedItem = !isCheckedItem
    }

    override fun setChecked(checked: Boolean) {
        isCheckedItem = checked
        if(isCheckedItem){
            itemLayout.setBackgroundColor(Color.BLUE)
        }else{
            itemLayout.setBackgroundColor(Color.WHITE)
        }
    }
}
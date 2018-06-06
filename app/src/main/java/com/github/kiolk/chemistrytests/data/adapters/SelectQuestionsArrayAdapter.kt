package com.github.kiolk.chemistrytests.data.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.github.kiolk.chemistrytests.data.models.CloseQuestion
import com.github.kiolk.chemistrytests.ui.customviews.QuestionView

class SelectQuestionsArrayAdapter(var contextAdapter: Context, resource: Int, var objects: MutableList<CloseQuestion>?) : ArrayAdapter<CloseQuestion>(contextAdapter, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val question: CloseQuestion? = getItem(position)
        if(convertView == null){

           val view = QuestionView(contextAdapter)
//            val title = view?.findViewById<QuestionView>(R.id.se)
//            title?.text = question?.questionEn
            view.setQuestion(question)
            return view
        }
//        val title = convertView.findViewById<TextView>(R.id.question_title_text_view)
//        title?.text = question?.questionEn
        convertView as QuestionView
        convertView.setQuestion(question)
        return convertView
    }

}

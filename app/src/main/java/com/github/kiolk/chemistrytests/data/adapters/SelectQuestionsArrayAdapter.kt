package com.github.kiolk.chemistrytests.data.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.models.CloseQuestion
import com.github.kiolk.chemistrytests.ui.customviews.QuestionView
import com.google.android.gms.plus.model.people.Person

class SelectQuestionsArrayAdapter(var contextAdapter: Context, resource: Int, var objects: MutableList<CloseQuestion>?) : ArrayAdapter<CloseQuestion>(contextAdapter, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val question: CloseQuestion? = getItem(position)
        if(convertView == null){

//           val view = LayoutInflater.from(contextAdapter).inflate(R.layout.item_question_list, parent, false)
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

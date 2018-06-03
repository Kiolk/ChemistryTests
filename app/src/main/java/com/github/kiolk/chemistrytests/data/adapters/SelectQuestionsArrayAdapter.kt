package com.github.kiolk.chemistrytests.data.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.github.kiolk.chemistrytests.data.models.CloseQuestion
import com.github.kiolk.chemistrytests.ui.customviews.QuestionView

class SelectQuestionsArrayAdapter(var contextAdapter: Context, resource: Int, var objects: MutableList<CloseQuestion>?) : ArrayAdapter<CloseQuestion>(contextAdapter, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: QuestionView? = null
        view = QuestionView(contextAdapter)
        val question: CloseQuestion? = objects?.get(position)
        view.setQuestion(question)
        return view ?: super.getView(position, convertView, parent)
    }
}

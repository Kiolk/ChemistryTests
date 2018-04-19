package com.github.kiolk.chemistrytests.data.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.models.TestParams

class TestsBaseAdapter(var context: Context, var testsParams: List<TestParams>) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var currentView = convertView ?: LayoutInflater.from(context).inflate(R.layout.card_test_params, parent, false)
        val currentTestParams: TestParams = getItem(position) as TestParams
        currentView?.findViewById<TextView>(R.id.test_title_card_text_view)?.text = currentTestParams.testInfo.testTitle
        currentView?.findViewById<TextView>(R.id.author_card_text_view)?.text = currentTestParams.testInfo.testAuthor
        return currentView
    }

    override fun getItem(position: Int): Any {
        return testsParams[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return testsParams.size
    }
}

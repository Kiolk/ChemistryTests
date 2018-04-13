package com.github.kiolk.chemistrytests.data.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.models.Result

class ResultFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_result, null)
        return view ?: super.onCreateView(inflater, container, savedInstanceState)
    }

    fun showResult(result: Result) {
        view?.findViewById<TextView>(R.id.result_test_score_text_view)?.text = result.getTestResult().toString()
    }
}

package com.github.kiolk.chemistrytests.data.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.models.StatisticModel

interface StatisticView{
    fun setupGeneralStatistic(statistic : StatisticModel)
}

class GeneralStatisticFragment : Fragment(), StatisticView {

    override fun setupGeneralStatistic(statistic: StatisticModel) {
        val progressBar = view?.findViewById<ProgressBar>(R.id.questions_statistic_progress_bar)
        progressBar?.isIndeterminate = false
        progressBar?.max = statistic.mTotalAskedQuestions
        progressBar?.progress = statistic.mCorrectAnswered
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_general_statistic, null)
        return view
    }



}
package com.github.kiolk.chemistrytests.ui.fragments.statistic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.ui.fragments.BaseFragment
import com.github.kiolk.chemistrytests.data.models.StatisticModel

interface StatisticView {
    fun setupGeneralStatistic(statistic: StatisticModel)
}

class GeneralStatisticFragment : com.github.kiolk.chemistrytests.ui.fragments.BaseFragment(), StatisticView {
    override val menuId: Int?
        get() = R.id.reset_history_menu_item

    override val titleId: Int
        get() = R.string.STATISTICS


    lateinit var mView: View
    var mStatistic: StatisticModel = StatisticModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_general_statistic, null)
        setupToolBar()
        return mView
    }

    private fun setupProgressBar() {
        val progressBar = mView?.findViewById<ProgressBar>(R.id.questions_statistic_progress_bar)
        mView?.findViewById<ProgressBar>(R.id.general_statistic_progress_bar).visibility = View.GONE
        progressBar?.visibility = View.VISIBLE
        progressBar?.isIndeterminate = false
        progressBar?.max = mStatistic.mTotalAskedQuestions
        progressBar?.progress = mStatistic.mCorrectAnswered
    }

    override fun setupGeneralStatistic(statistic: StatisticModel) {
        mStatistic = statistic
        if (view != null) {
            setupProgressBar()
        }
    }
}
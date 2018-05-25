package com.github.kiolk.chemistrytests.data.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.models.ResultInformation
import com.github.kiolk.chemistrytests.utils.CONSTANTS.DATE_PATTERN
import com.github.kiolk.chemistrytests.utils.convertEpochTime

class ResultStatisticFragment : Fragment(){
    val RESULT_BUNDLE: String = "resultInformation"
    lateinit var mResult : ResultInformation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mResult = arguments?.getSerializable(RESULT_BUNDLE) as ResultInformation
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_statistic_resalt, null)
        view.findViewById<TextView>(R.id.total_questions_result_test_view).text = mResult.totalQuestions.toString()
        view.findViewById<TextView>(R.id.asked_question_result_test_view).text = mResult.askedQuestions.toString()
        view.findViewById<TextView>(R.id.correct_answers_result_text_view).text = mResult.correctAnswered.toString()
        view.findViewById<TextView>(R.id.result_test_score_text_view).text = mResult.resultScore.toString()
        view.findViewById<TextView>(R.id.start_test_text_view).text = mResult.startTime?.let { convertEpochTime(it, inflater.context, DATE_PATTERN) }
        view.findViewById<TextView>(R.id.score_result_text_view).text = mResult.resultScore.toString()
//        view.findViewById<TextView>(R.id.test_mark_text_view).text = mResult.testMark
        return view// super.onCreateView(inflater, container, savedInstanceState)
    }

    fun fromInstance(result : ResultInformation) : ResultStatisticFragment{
        val fragment = ResultStatisticFragment()
        val bundle = Bundle()
        bundle.putSerializable(RESULT_BUNDLE, result)
        fragment.arguments = bundle
        return fragment
    }
}

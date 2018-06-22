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
import com.github.kiolk.chemistrytests.utils.CONSTANTS.DURATION_TIME
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
        view.findViewById<TextView>(R.id.start_test_text_view).text = mResult.startTime?.let { convertEpochTime(it, inflater.context, DATE_PATTERN) }
        view.findViewById<TextView>(R.id.score_result_text_view).text = mResult.resultScore.toString()
        view.findViewById<TextView>(R.id.end_test_text_view).text = mResult.endTime?.let{ convertEpochTime(it, inflater.context, DATE_PATTERN)}
        view.findViewById<TextView>(R.id.difficulty_text_view).text = mResult.difficulty?.toString()
        view.findViewById<TextView>(R.id.faster_correct_text_view).text = mResult.fasterCorrect?.let{convertEpochTime(it, inflater.context, DURATION_TIME, true)}
        view.findViewById<TextView>(R.id.faster_wrong_text_view).text = mResult.fasterWrong?.let{convertEpochTime(it, inflater.context, DURATION_TIME, true)}
        view.findViewById<TextView>(R.id.longer_correct_text_view).text = mResult.longerCorrect?.let{convertEpochTime(it, inflater.context, DURATION_TIME, true)}
        view.findViewById<TextView>(R.id.longer_wrong_text_view).text = mResult.longerWrong?.let{convertEpochTime(it, inflater.context, DURATION_TIME, true)}
        var durationTime : Long = 0
        mResult.listAnsweredQuestions?.forEach { durationTime += it.getDuration() }
//        view.findViewById<TextView>(R.id.duration_test_text_view).text = mResult.duration?.let{ convertEpochTime(it, inflater.context, DURATION_TIME, true)}
//        durationTime = durationTime.div(1000)
        view.findViewById<TextView>(R.id.duration_test_text_view).text = durationTime.let{ convertEpochTime(it, inflater.context, DURATION_TIME, true)}

        return view
    }

    fun fromInstance(result : ResultInformation) : ResultStatisticFragment{
        val fragment = ResultStatisticFragment()
        val bundle = Bundle()
        bundle.putSerializable(RESULT_BUNDLE, result)
        fragment.arguments = bundle
        return fragment
    }
}

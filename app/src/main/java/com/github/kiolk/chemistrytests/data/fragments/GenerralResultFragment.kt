package com.github.kiolk.chemistrytests.data.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.models.ResultInformation
import com.github.kiolk.chemistrytests.data.models.TestParams

class GenerralResultFragment : Fragment(){
    val PARAMS_BUNDLE : String = "params"

    lateinit var mTestInfo : ResultInformation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mTestInfo = arguments?.getSerializable(PARAMS_BUNDLE) as ResultInformation
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_general_score_reslt, null)
        view.findViewById<TextView>(R.id.user_score_fragment_text_view).text = mTestInfo.testMark
//        view.findViewById<TextView>(R.id.total_questions_result_test_view).text = mTestInfo.totalQuestions.toString()
//        view.findViewById<TextView>(R.id.asked_question_result_test_view).text = mTestInfo.askedQuestions.toString()
//        view.findViewById<TextView>(R.id.correct_answers_result_text_view).text = mTestInfo.correctAnswered.toString()
//        view.findViewById<TextView>(R.id.result_test_score_text_view).text = mTestInfo.resultScore.toString()
//        view.findViewById<TextView>(R.id.start_test_text_view).text = mTestInfo.startTime.toString()
//        view.findViewById<TextView>(R.id.score_result_text_view).text = mTestInfo.resultScore.toString()
//        view.findViewById<TextView>(R.id.test_mark_text_view).text = mTestInfo.testMark
        return view //super.onCreateView(inflater, container, savedInstanceState)
    }
    fun fromInstance(result: ResultInformation) : GenerralResultFragment{
        val fragment = GenerralResultFragment()
        val bundle = Bundle()
        bundle.putSerializable(PARAMS_BUNDLE, result)
        fragment.arguments = bundle
        return fragment
    }
}

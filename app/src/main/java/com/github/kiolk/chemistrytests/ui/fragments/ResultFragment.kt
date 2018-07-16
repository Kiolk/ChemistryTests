package com.github.kiolk.chemistrytests.ui.fragments

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.adapters.ResultPageAdapter
import com.github.kiolk.chemistrytests.data.adapters.ResultPageAdapter.Companion.GENERAL_TEST_RESULT
import com.github.kiolk.chemistrytests.data.models.Result
import com.github.kiolk.chemistrytests.data.models.ResultInformation

class ResultFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_view_pager_result, null)
        return view ?: super.onCreateView(inflater, container, savedInstanceState)
    }

    //    fun showResult(result: Result) {
    fun showResult(result: ResultInformation) {
//        view?.findViewById<TextView>(R.id.test_title_result_text_view)?.text = result.test.params.testInfo.testTitle
//        view?.findViewById<TextView>(R.id.total_questions_result_test_view)?.text = result.mResultInfo.totalQuestions.toString()
//        view?.findViewById<TextView>(R.id.asked_question_result_test_view)?.text = result.mResultInfo.askedQuestions.toString()
//        view?.findViewById<TextView>(R.id.correct_answers_result_text_view)?.text = result.mResultInfo.correctAnswered.toString()
//        view?.findViewById<TextView>(R.id.result_test_score_text_view)?.text = result.mResultInfo.resultScore.toString()
//        view?.findViewById<TextView>(R.id.start_test_text_view)?.text = result.mResultInfo.startTime.toString()
//        view?.findViewById<TextView>(R.id.score_result_text_view)?.text = result.mResultInfo.resultScore.toString()
        val adapter = fragmentManager?.let { context?.let { it1 -> ResultPageAdapter(it, result, it1) } }
        val viewPager = view?.findViewById<ViewPager>(R.id.result_view_pager)
        viewPager?.adapter = null
        viewPager?.adapter = adapter
        adapter?.notifyDataSetChanged()
        viewPager?.currentItem = GENERAL_TEST_RESULT
        view?.findViewById<TabLayout>(R.id.result_tab_layout)?.setupWithViewPager(viewPager)
    }
}

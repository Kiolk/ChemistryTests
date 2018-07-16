package com.github.kiolk.chemistrytests.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.adapters.SelectQuestionsArrayAdapter
import com.github.kiolk.chemistrytests.data.models.CloseQuestion

interface SelectedQuestions {
    fun setSelectedQuestions(): MutableList<Int>
}

class QuestionsListFragment : Fragment(), SelectedQuestions {

    var mQuestionsListAdapter: SelectQuestionsArrayAdapter? = null
    var mSelectedQuestions: MutableList<Int>? = null
    var mQuestions: MutableList<CloseQuestion> = mutableListOf()
    var mListView: ListView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_list_questions, null)
        mListView = view?.findViewById(R.id.available_list_question_list_view)
        return view //super.onCreateView(inflater, container, savedInstanceState)
    }

    fun setAvailableQuestions(list: MutableList<CloseQuestion>?) {
        mQuestions = list ?: mutableListOf()
        mQuestionsListAdapter = context?.let { SelectQuestionsArrayAdapter(it, R.layout.item_question_list, list) }
        mListView?.adapter = mQuestionsListAdapter
    }

    override fun setSelectedQuestions(): MutableList<Int> {
        mSelectedQuestions = mutableListOf()
        val checked = mListView?.checkedItemPositions
        var cnt = 0
        if (mListView?.choiceMode != ListView.CHOICE_MODE_NONE && checked?.size() != 0){
            mQuestions.forEach {
                if (checked?.get(cnt) == true) {
                    mSelectedQuestions?.add(it.questionId)
                }
                ++cnt
        }
        }
        return mSelectedQuestions ?: mutableListOf()
    }
}

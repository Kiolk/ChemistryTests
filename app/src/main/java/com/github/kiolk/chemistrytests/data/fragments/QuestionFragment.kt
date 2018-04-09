package com.github.kiolk.chemistrytests.data.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.models.CloseQuestion
import com.github.kiolk.chemistrytests.data.models.setFormattedText

class QuestionFragment : Fragment(){

    lateinit var mQuestion : CloseQuestion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mQuestion = arguments.getSerializable("question") as CloseQuestion
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_question, null)
        if(view!=null){
            setFormattedText(view.findViewById(R.id.question_text_view), mQuestion.questionEn, mQuestion.photoUrl)
            setFormattedText(view.findViewById(R.id.option_one_text_view), mQuestion.option1.text, mQuestion.option1.optionPhotoUtl)
        }

        return view ?:  super.onCreateView(inflater, container, savedInstanceState)
    }

    fun fromInctance(question : CloseQuestion) : QuestionFragment{
        val fragment = QuestionFragment()
        val bandle = Bundle()
        bandle.putSerializable("question", question)
        fragment.arguments = bandle
        return fragment
    }
}
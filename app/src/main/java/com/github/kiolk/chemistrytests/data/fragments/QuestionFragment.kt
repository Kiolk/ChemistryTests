package com.github.kiolk.chemistrytests.data.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.models.CloseQuestion
import com.github.kiolk.chemistrytests.data.models.setFormattedText
import kotlinx.android.synthetic.main.card_close_question.*

class QuestionFragment : Fragment() {

    lateinit var mQuestion: CloseQuestion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mQuestion = arguments.getSerializable("question") as CloseQuestion
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_question, null)
        if (view != null) {
            setFormattedText(view.findViewById(R.id.question_text_view), mQuestion.questionEn, mQuestion.photoUrl)

            if (mQuestion.questionOptions.size > 0) {
                setFormattedText(view.findViewById(R.id.option_one_text_view), mQuestion.questionOptions[0].text, mQuestion.questionOptions[0].optionPhotoUtl)
                view.findViewById<TextView>(R.id.option_one_text_view).visibility = View.VISIBLE
                view.findViewById<TextView>(R.id.question_one_label_text_view).visibility = View.VISIBLE
                view.findViewById<TextView>(R.id.option_one_text_view).tag = 0
            }

            if (mQuestion.questionOptions.size > 1) {
                setFormattedText(view.findViewById(R.id.option_two_text_view), mQuestion.questionOptions[1].text, mQuestion.questionOptions[1].optionPhotoUtl)
                view.findViewById<TextView>(R.id.option_two_text_view).visibility = View.VISIBLE
                view.findViewById<TextView>(R.id.question_two_label_text_view).visibility = View.VISIBLE
                view.findViewById<TextView>(R.id.option_two_text_view).tag = 1

            }

            if (mQuestion.questionOptions.size > 2) {
                setFormattedText(view.findViewById(R.id.option_three_text_view), mQuestion.questionOptions[2].text, mQuestion.questionOptions[2].optionPhotoUtl)
                view.findViewById<TextView>(R.id.option_three_text_view).visibility = View.VISIBLE
                view.findViewById<TextView>(R.id.question_three_label_text_view).visibility = View.VISIBLE
                view.findViewById<TextView>(R.id.option_three_text_view).tag = 2
            }

            if (mQuestion.questionOptions.size > 3) {
                setFormattedText(view.findViewById(R.id.option_four_text_view), mQuestion.questionOptions[3].text, mQuestion.questionOptions[3].optionPhotoUtl)
                view.findViewById<TextView>(R.id.option_four_text_view).visibility = View.VISIBLE
                view.findViewById<TextView>(R.id.question_four_label_text_view).visibility = View.VISIBLE
                view.findViewById<TextView>(R.id.option_four_text_view).tag = 3
            }

            if (mQuestion.questionOptions.size > 4) {
                setFormattedText(view.findViewById(R.id.option_five_text_view), mQuestion.questionOptions[4].text, mQuestion.questionOptions[4].optionPhotoUtl)
                view.findViewById<TextView>(R.id.option_five_text_view).visibility = View.VISIBLE
                view.findViewById<TextView>(R.id.question_five_label_text_view).visibility = View.VISIBLE
                view.findViewById<TextView>(R.id.option_five_text_view).tag = 4
            }

            val radioGroup = view.findViewById<RadioGroup>(R.id.answers_radio_group)
            radioGroup.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener{
                override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                    val answer = view.findViewById<RadioButton>(checkedId)
                    val answerNumber  : Int = answer.tag as Int
                    if (mQuestion.checkAnswer(mQuestion.questionOptions[answerNumber])){
                        Toast.makeText(view.context, "Correct Answer!", Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(view.context, "Wrong Answer!", Toast.LENGTH_LONG).show()
                    }
                }
            })
        }

        return view ?: super.onCreateView(inflater, container, savedInstanceState)
    }

    fun fromInctance(question: CloseQuestion): QuestionFragment {
        val fragment = QuestionFragment()
        val bundle = Bundle()
        bundle.putSerializable("question", question)
        fragment.arguments = bundle
        return fragment
    }
}
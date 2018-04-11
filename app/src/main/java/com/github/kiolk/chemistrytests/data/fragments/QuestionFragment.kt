package com.github.kiolk.chemistrytests.data.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.models.CloseQuestion
import com.github.kiolk.chemistrytests.data.models.setFormattedText
import kotlinx.android.synthetic.main.card_close_question.view.*

class QuestionFragment : Fragment() {

    lateinit var mQuestion: CloseQuestion
    lateinit var listener: View.OnClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mQuestion = arguments.getSerializable("question") as CloseQuestion
        setupCheckBocks()
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
                view.findViewById<CheckBox>(R.id.answer_one_check_box).setOnClickListener(listener)
            }

            if (mQuestion.questionOptions.size > 1) {
                setFormattedText(view.findViewById(R.id.option_two_text_view), mQuestion.questionOptions[1].text, mQuestion.questionOptions[1].optionPhotoUtl)
                view.findViewById<TextView>(R.id.option_two_text_view).visibility = View.VISIBLE
                view.findViewById<TextView>(R.id.question_two_label_text_view).visibility = View.VISIBLE
                view.findViewById<TextView>(R.id.option_two_text_view).tag = 1
                view.findViewById<CheckBox>(R.id.answer_two_check_box).setOnClickListener(listener)


            }

            if (mQuestion.questionOptions.size > 2) {
                setFormattedText(view.findViewById(R.id.option_three_text_view), mQuestion.questionOptions[2].text, mQuestion.questionOptions[2].optionPhotoUtl)
                view.findViewById<TextView>(R.id.option_three_text_view).visibility = View.VISIBLE
                view.findViewById<TextView>(R.id.question_three_label_text_view).visibility = View.VISIBLE
                view.findViewById<TextView>(R.id.option_three_text_view).tag = 2
                view.findViewById<CheckBox>(R.id.answer_three_check_box).setOnClickListener(listener)

            }

            if (mQuestion.questionOptions.size > 3) {
                setFormattedText(view.findViewById(R.id.option_four_text_view), mQuestion.questionOptions[3].text, mQuestion.questionOptions[3].optionPhotoUtl)
                view.findViewById<TextView>(R.id.option_four_text_view).visibility = View.VISIBLE
                view.findViewById<TextView>(R.id.question_four_label_text_view).visibility = View.VISIBLE
                view.findViewById<TextView>(R.id.option_four_text_view).tag = 3
                view.findViewById<CheckBox>(R.id.answer_four_check_box).setOnClickListener(listener)

            }

            if (mQuestion.questionOptions.size > 4) {
                setFormattedText(view.findViewById(R.id.option_five_text_view), mQuestion.questionOptions[4].text, mQuestion.questionOptions[4].optionPhotoUtl)
                view.findViewById<TextView>(R.id.option_five_text_view).visibility = View.VISIBLE
                view.findViewById<TextView>(R.id.question_five_label_text_view).visibility = View.VISIBLE
                view.findViewById<TextView>(R.id.option_five_text_view).tag = 4
                view.findViewById<CheckBox>(R.id.answer_five_check_box).setOnClickListener(listener)

            }

            val radioGroup = view.findViewById<RadioGroup>(R.id.answers_radio_group)
            radioGroup.setOnCheckedChangeListener { group, checkedId ->
                val answer = view.findViewById<RadioButton>(checkedId)
                val answerNumber: Int = answer.tag as Int
                if (mQuestion.checkAnswer(mQuestion.questionOptions[answerNumber])) {
                    Toast.makeText(view.context, "Correct Answer!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(view.context, "Wrong Answer!", Toast.LENGTH_LONG).show()
                }
            }
        }

        return view ?: super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setupCheckBocks() {
        fun clearCheckBox(answer: Int) {
            when (answer) {
                0 -> {
                    view?.answer_two_check_box?.isChecked = false
                    view?.answer_three_check_box?.isChecked = false
                    view?.answer_four_check_box?.isChecked = false
                    view?.answer_five_check_box?.isChecked = false
                }
                1 -> {
                    view?.answer_one_check_box?.isChecked = false
                    view?.answer_three_check_box?.isChecked = false
                    view?.answer_four_check_box?.isChecked = false
                    view?.answer_five_check_box?.isChecked = false
                }
                2 -> {
                    view?.answer_two_check_box?.isChecked = false
                    view?.answer_one_check_box?.isChecked = false
                    view?.answer_four_check_box?.isChecked = false
                    view?.answer_five_check_box?.isChecked = false
                }
                3 -> {
                    view?.answer_two_check_box?.isChecked = false
                    view?.answer_three_check_box?.isChecked = false
                    view?.answer_one_check_box?.isChecked = false
                    view?.answer_five_check_box?.isChecked = false
                }
                4 -> {
                    view?.answer_two_check_box?.isChecked = false
                    view?.answer_three_check_box?.isChecked = false
                    view?.answer_four_check_box?.isChecked = false
                    view?.answer_one_check_box?.isChecked = false
                }
                -1 -> {
                    view?.answer_two_check_box?.isChecked = false
                    view?.answer_three_check_box?.isChecked = false
                    view?.answer_four_check_box?.isChecked = false
                    view?.answer_one_check_box?.isChecked = false
                    view?.answer_five_check_box?.isChecked = false
                }
            }
        }

        fun checkCorrectAnswer(answer: Int): Int {
            return if (mQuestion.checkAnswer(mQuestion.questionOptions[answer])) {
                Toast.makeText(view?.context, "Correct Answer!", Toast.LENGTH_LONG).show()
                answer
            } else {
                Toast.makeText(view?.context, "Wrong Answer!", Toast.LENGTH_LONG).show()
                answer
            }
        }

        listener = View.OnClickListener { viewTarget: View ->
            val userAnswer: Int = when (viewTarget.id) {
                view?.answer_one_check_box?.id -> {
                    clearCheckBox(0)
                    checkCorrectAnswer(0)
                }
                view?.answer_two_check_box?.id -> {
                    clearCheckBox(1)
                    checkCorrectAnswer(1)
                }
                view?.answer_three_check_box?.id -> {
                    clearCheckBox(2)
                    checkCorrectAnswer(2)
                }
                view?.answer_four_check_box?.id -> {
                    clearCheckBox(3)

                    checkCorrectAnswer(3)
                }
                view?.answer_five_check_box?.id -> {
                    clearCheckBox(4)
                    checkCorrectAnswer(4)
                }
                else -> -1
            }
        }
    }

    fun fromInstance(question: CloseQuestion): QuestionFragment {
        val fragment = QuestionFragment()
        val bundle = Bundle()
        bundle.putSerializable("question", question)
        fragment.arguments = bundle
        return fragment
    }
}
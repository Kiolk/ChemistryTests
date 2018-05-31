package com.github.kiolk.chemistrytests.data.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.models.Answer
import com.github.kiolk.chemistrytests.data.models.CloseQuestion
import com.github.kiolk.chemistrytests.data.models.CloseQuestion.Question.INPUT_CHOICE
import com.github.kiolk.chemistrytests.data.models.CloseQuestion.Question.MULTIPLE_CHOICE
import com.github.kiolk.chemistrytests.data.models.CloseQuestion.Question.SINGLE_CHOICE
import com.github.kiolk.chemistrytests.data.models.setFormattedText
import com.github.kiolk.chemistrytests.ui.activities.TestingActivity
import kotlinx.android.synthetic.main.activity_testing.*
import kotlinx.android.synthetic.main.card_close_question.view.*

class QuestionAnsweredFragment : Fragment() {

    lateinit var mQuestion: CloseQuestion
    lateinit var mAnswer : Answer
    lateinit var listener: View.OnClickListener
    lateinit var photoListener: View.OnClickListener
    var userAnswers: MutableList<Int> = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAnswer = arguments?.getSerializable("answer") as Answer
        mQuestion = mAnswer.question
        setUpClickListener()
        setUpListener()
    }


    private fun setUpListener() {
        photoListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                val act = activity as TestingActivity
                if (v?.tag != null) {
                    val option: Int = v?.tag as Int
                    val pictureUrl = mQuestion.questionOptions[option].optionPhotoUtl
                    pictureUrl?.let { act?.showOptionPhoto(it) }
                } else {
                    mQuestion.photoUrl?.let { act.showOptionPhoto(it) }
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_question, null)
        var input : String = ""
        if (view != null) {
            val inputText =  view.findViewById<TextView>(R.id.question_text_view)
            setFormattedText(view.findViewById(R.id.question_text_view), mQuestion.questionEn, mQuestion.photoUrl)
            inputText.setOnClickListener(photoListener)


            if (mQuestion.questionType == INPUT_CHOICE) {
                val firstOption = view.findViewById<TextView>(R.id.option_one_text_view)
                view.findViewById<TextView>(R.id.question_one_label_text_view).visibility = View.VISIBLE
                view.findViewById<TextView>(R.id.question_one_label_text_view).text = resources.getString(R.string.CORRECT)
                firstOption.visibility = View.VISIBLE
                firstOption.background = resources.getDrawable(R.drawable.area_square_shape_correct)
                firstOption.text = mQuestion.correctAnswers[0].text
                if(mAnswer.userInput != null && mAnswer.userInput?.toUpperCase() != mQuestion.correctAnswers[0].text.toUpperCase()){
                   view.findViewById<TextView>(R.id.question_two_label_text_view).visibility = View.VISIBLE
                   view.findViewById<TextView>(R.id.question_two_label_text_view).text = resources.getString(R.string.YOU_ANSWER)
                    val secondOption = view.findViewById<TextView>(R.id.option_two_text_view)
                    secondOption.visibility = View.VISIBLE
                    secondOption.background = resources.getDrawable(R.drawable.area_square_shape_wrong)
                    secondOption.text = mAnswer.userInput
                }
                return view
            }

            if (mQuestion.questionOptions.size > 0) {
                setFormattedText(view.findViewById(R.id.option_one_text_view), mQuestion.questionOptions[0].text, mQuestion.questionOptions[0].optionPhotoUtl)
                if(mAnswer.getAnsweredOptions().contains(mQuestion.questionOptions[0])){
                    view.findViewById<TextView>(R.id.option_one_text_view).background = resources.getDrawable(R.drawable.area_square_shape_wrong)
                }
                if(mQuestion.correctAnswers.contains(mQuestion.questionOptions[0])){
                    view.findViewById<TextView>(R.id.option_one_text_view).background = resources.getDrawable(R.drawable.area_square_shape_correct)
                }
                view.findViewById<TextView>(R.id.option_one_text_view).visibility = View.VISIBLE
                view.findViewById<TextView>(R.id.question_one_label_text_view).visibility = View.VISIBLE
                view.findViewById<TextView>(R.id.option_one_text_view).tag = 0
                view.findViewById<CheckBox>(R.id.answer_one_check_box).visibility = View.VISIBLE
                view.findViewById<LinearLayout>(R.id.box_one_linear_layout).visibility = View.VISIBLE

                view.findViewById<CheckBox>(R.id.answer_one_check_box).setOnClickListener(listener)
                if (mQuestion.questionOptions[0].optionPhotoUtl != null) {
                    view.findViewById<TextView>(R.id.option_one_text_view).setOnClickListener(photoListener)
                }
            }

            if (mQuestion.questionOptions.size > 1) {
                setFormattedText(view.findViewById(R.id.option_two_text_view), mQuestion.questionOptions[1].text, mQuestion.questionOptions[1].optionPhotoUtl)
                if(mAnswer.getAnsweredOptions().contains(mQuestion.questionOptions[1])){
                    view.findViewById<TextView>(R.id.option_two_text_view).background = resources.getDrawable(R.drawable.area_square_shape_wrong)
                }
                if(mQuestion.correctAnswers.contains(mQuestion.questionOptions[1])){
                    view.findViewById<TextView>(R.id.option_two_text_view).background = resources.getDrawable(R.drawable.area_square_shape_correct)
                }
                view.findViewById<TextView>(R.id.option_two_text_view).visibility = View.VISIBLE
                view.findViewById<TextView>(R.id.question_two_label_text_view).visibility = View.VISIBLE
                view.findViewById<TextView>(R.id.option_two_text_view).tag = 1
                view.findViewById<CheckBox>(R.id.answer_two_check_box).visibility = View.VISIBLE
                view.findViewById<CheckBox>(R.id.answer_two_check_box).setOnClickListener(listener)
                view.findViewById<LinearLayout>(R.id.box_two_linear_layout).visibility = View.VISIBLE

                if (mQuestion.questionOptions[1].optionPhotoUtl != null) {
                    view.findViewById<TextView>(R.id.option_two_text_view).setOnClickListener(photoListener)
                }
            }

            if (mQuestion.questionOptions.size > 2) {
                setFormattedText(view.findViewById(R.id.option_three_text_view), mQuestion.questionOptions[2].text, mQuestion.questionOptions[2].optionPhotoUtl)
                if(mAnswer.getAnsweredOptions().contains(mQuestion.questionOptions[2])){
                    view.findViewById<TextView>(R.id.option_three_text_view).background = resources.getDrawable(R.drawable.area_square_shape_wrong)
                }
                if(mQuestion.correctAnswers.contains(mQuestion.questionOptions[2])){
                    view.findViewById<TextView>(R.id.option_three_text_view).background = resources.getDrawable(R.drawable.area_square_shape_correct)
                }
                view.findViewById<TextView>(R.id.option_three_text_view).visibility = View.VISIBLE
                view.findViewById<TextView>(R.id.question_three_label_text_view).visibility = View.VISIBLE
                view.findViewById<TextView>(R.id.option_three_text_view).tag = 2
                view.findViewById<CheckBox>(R.id.answer_three_check_box).visibility = View.VISIBLE
                view.findViewById<CheckBox>(R.id.answer_three_check_box).setOnClickListener(listener)
                view.findViewById<LinearLayout>(R.id.box_three_linear_layout).visibility = View.VISIBLE

                if (mQuestion.questionOptions[2].optionPhotoUtl != null) {
                    view.findViewById<TextView>(R.id.option_three_text_view).setOnClickListener(photoListener)
                }
            }

            if (mQuestion.questionOptions.size > 3) {
                setFormattedText(view.findViewById(R.id.option_four_text_view), mQuestion.questionOptions[3].text, mQuestion.questionOptions[3].optionPhotoUtl)
                if(mAnswer.getAnsweredOptions().contains(mQuestion.questionOptions[3])){
                    view.findViewById<TextView>(R.id.option_four_text_view).background = resources.getDrawable(R.drawable.area_square_shape_wrong)
                }
                if(mQuestion.correctAnswers.contains(mQuestion.questionOptions[3])){
                    view.findViewById<TextView>(R.id.option_four_text_view).background = resources.getDrawable(R.drawable.area_square_shape_correct)
                }
                view.findViewById<TextView>(R.id.option_four_text_view).visibility = View.VISIBLE
                view.findViewById<TextView>(R.id.question_four_label_text_view).visibility = View.VISIBLE
                view.findViewById<TextView>(R.id.option_four_text_view).tag = 3
                view.findViewById<CheckBox>(R.id.answer_four_check_box).visibility = View.VISIBLE
                view.findViewById<CheckBox>(R.id.answer_four_check_box).setOnClickListener(listener)
                view.findViewById<LinearLayout>(R.id.box_four_linear_layout).visibility = View.VISIBLE

                if (mQuestion.questionOptions[3].optionPhotoUtl != null) {
                    view.findViewById<TextView>(R.id.option_four_text_view).setOnClickListener(photoListener)
                }
            }

            if (mQuestion.questionOptions.size > 4) {
                setFormattedText(view.findViewById(R.id.option_five_text_view), mQuestion.questionOptions[4].text, mQuestion.questionOptions[4].optionPhotoUtl)
                if(mAnswer.getAnsweredOptions().contains(mQuestion.questionOptions[4])){
                    view.findViewById<TextView>(R.id.option_five_text_view).background = resources.getDrawable(R.drawable.area_square_shape_wrong)
                }
                if(mQuestion.correctAnswers.contains(mQuestion.questionOptions[4])){
                    view.findViewById<TextView>(R.id.option_four_text_view).background = resources.getDrawable(R.drawable.area_square_shape_correct)
                }
                view.findViewById<TextView>(R.id.option_five_text_view).visibility = View.VISIBLE
                view.findViewById<TextView>(R.id.question_five_label_text_view).visibility = View.VISIBLE
                view.findViewById<TextView>(R.id.option_five_text_view).tag = 4
                view.findViewById<CheckBox>(R.id.answer_five_check_box).visibility = View.VISIBLE
                view.findViewById<CheckBox>(R.id.answer_five_check_box).setOnClickListener(listener)
                view.findViewById<LinearLayout>(R.id.box_five_linear_layout).visibility = View.VISIBLE


                if (mQuestion.questionOptions[4].optionPhotoUtl != null) {
                    view.findViewById<TextView>(R.id.option_five_text_view).setOnClickListener(photoListener)
                }
            }
        }
        return view ?: inflater.let { super.onCreateView(it, container, savedInstanceState) }
    }

    //    private fun setupCheckBocks() {
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

    fun checkCorrectAnswer(answers: List<Int>) {
        val act = activity as TestingActivity
        act.listener.onResult(Answer(mQuestion, answers))
        updateIndicator(act.indicator_answered_progress_bar, act.mResult.askedQuestions.size,
                act.mResult.test.mSortedQuestions.size)
//            return if (mQuestion.checkAnswer(mQuestion.questionOptions[answer])) {
////                Toast.makeText(view?.context, "Correct Answer!", Toast.LENGTH_LONG).show()
//                act.listener.onResult(Answer(mQuestion, mQuestion.questionOptions[answer]))
//            } else {
////                Toast.makeText(view?.context, "Wrong Answer!", Toast.LENGTH_LONG).show()
//                act.listener.onResult(Answer(mQuestion, mQuestion.questionOptions[answer]))
//            }
    }

    private fun updateIndicator(progressBar: ProgressBar?, size: Int, total : Int) {
        val percentAnswered : Int = size.times(100).div(total)
        progressBar?.progress = percentAnswered
    }
    private fun setUpClickListener() {
        if (mQuestion.questionType == SINGLE_CHOICE) {
            listener = View.OnClickListener { viewTarget: View ->
                var userAnswers: MutableList<Int> = mutableListOf<Int>()
                when (viewTarget.id) {
                    view?.answer_one_check_box?.id -> {
                        clearCheckBox(0)
                        userAnswers.add(0)
                        checkCorrectAnswer(userAnswers)
                    }
                    view?.answer_two_check_box?.id -> {
                        clearCheckBox(1)
                        userAnswers.add(1)
                        checkCorrectAnswer(userAnswers)
                    }
                    view?.answer_three_check_box?.id -> {
                        clearCheckBox(2)
                        userAnswers.add(2)
                        checkCorrectAnswer(userAnswers)
                    }
                    view?.answer_four_check_box?.id -> {
                        clearCheckBox(3)
                        userAnswers.add(3)
                        checkCorrectAnswer(userAnswers)
                    }
                    view?.answer_five_check_box?.id -> {
                        clearCheckBox(4)
                        userAnswers.add(0)
                        checkCorrectAnswer(userAnswers)
                    }
                    else -> -1
                }
            }
        } else if (mQuestion.questionType == MULTIPLE_CHOICE) {
            listener = View.OnClickListener { viewTarget: View ->
                when (viewTarget.id) {
                    view?.answer_one_check_box?.id -> {
                        if (view?.answer_one_check_box?.isChecked != null
                                && view?.answer_one_check_box?.isChecked == true) {
                            userAnswers.add(0)
                            checkCorrectAnswer(userAnswers)

                        }
                        if (view?.answer_one_check_box?.isChecked != null
                                && view?.answer_one_check_box?.isChecked == false) {
                            userAnswers.remove(0)
                            checkCorrectAnswer(userAnswers)
                        }
                    }
                    view?.answer_two_check_box?.id -> {
                        if (view?.answer_two_check_box?.isChecked != null
                                && view?.answer_two_check_box?.isChecked == true) {
                            userAnswers.add(1)
                            checkCorrectAnswer(userAnswers)

                        }
                        if (view?.answer_two_check_box?.isChecked != null
                                && view?.answer_two_check_box?.isChecked == false) {
                            userAnswers.remove(1)
                            checkCorrectAnswer(userAnswers)
                        }
                    }
                    view?.answer_three_check_box?.id -> {
                        if (view?.answer_three_check_box?.isChecked != null
                                && view?.answer_three_check_box?.isChecked == true) {
                            userAnswers.add(2)
                            checkCorrectAnswer(userAnswers)

                        }
                        if (view?.answer_three_check_box?.isChecked != null
                                && view?.answer_three_check_box?.isChecked == false) {
                            userAnswers.remove(2)
                            checkCorrectAnswer(userAnswers)
                        }
                    }
                    view?.answer_four_check_box?.id -> {
                        if (view?.answer_four_check_box?.isChecked != null
                                && view?.answer_four_check_box?.isChecked == true) {
                            userAnswers.add(3)
                            checkCorrectAnswer(userAnswers)

                        }
                        if (view?.answer_four_check_box?.isChecked != null
                                && view?.answer_four_check_box?.isChecked == false) {
                            userAnswers.remove(3)
                            checkCorrectAnswer(userAnswers)
                        }
                    }
                    view?.answer_five_check_box?.id -> {
                        if (view?.answer_five_check_box?.isChecked != null
                                && view?.answer_five_check_box?.isChecked == true) {
                            userAnswers.add(4)
                            checkCorrectAnswer(userAnswers)

                        }
                        if (view?.answer_five_check_box?.isChecked != null
                                && view?.answer_five_check_box?.isChecked == false) {
                            userAnswers.remove(4)
                            checkCorrectAnswer(userAnswers)
                        }
                    }
                }
            }
        }
    }

    fun fromInstance(answer: Answer): QuestionAnsweredFragment {
        val fragment = QuestionAnsweredFragment()
        val bundle = Bundle()
        bundle.putSerializable("answer", answer)
        fragment.arguments = bundle
        return fragment
    }

    fun getInputAnswer() : String{
        return ""
    }
}
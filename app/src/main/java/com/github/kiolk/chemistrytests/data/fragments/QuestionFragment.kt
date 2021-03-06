package com.github.kiolk.chemistrytests.data.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
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
//import com.github.kiolk.chemistrytests.data.models.setFormattedText
import com.github.kiolk.chemistrytests.ui.activities.TestingActivity
import com.github.kiolk.chemistrytests.utils.setFormattedText
import kotlinx.android.synthetic.main.activity_testing.*
import kotlinx.android.synthetic.main.card_close_question.view.*

class QuestionFragment : Fragment() {

    lateinit var mQuestion: CloseQuestion
    lateinit var listener: View.OnClickListener
    lateinit var photoListener: View.OnClickListener
    var userAnswers: MutableList<Int> = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val que = arguments?.getSerializable("question") as CloseQuestion
        que?.let { mQuestion = que }
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
//        if (mQuestion.hints != null){
//            val activity = activity as TestingActivity
//            activity.hint_button_image_view.background = resources.getDrawable(R.drawable.ic_help_inactive)
//        }else{
//            val activity = activity as TestingActivity
//            activity.hint_button_image_view.background = resources.getDrawable(R.drawable.ic_help)
//        }
        val view = inflater?.inflate(R.layout.fragment_question, null)
        var input : String = ""
        if (view != null) {
            val inputText =  view.findViewById<TextView>(R.id.question_text_view)
            setFormattedText(view.findViewById(R.id.question_text_view), mQuestion.questionEn, mQuestion.photoUrl)
            inputText.setOnClickListener(photoListener)


            if (mQuestion.questionType == INPUT_CHOICE) {
                view.findViewById<LinearLayout>(R.id.open_input_linear_layout)?.visibility = View.VISIBLE
                view.findViewById<EditText>(R.id.input_answer_edit_text).addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        val act = activity as TestingActivity
                        if(inputText.text.toString() != "" && input != s?.toString()) {
                            s?.let{input = it.toString()}
                            act.listener.onResult(Answer(mQuestion, mutableListOf(), s?.toString()))
                            updateIndicator(act.indicator_answered_progress_bar, act.mResult.askedQuestions.size,
                                    act.mResult.test.mSortedQuestions.size)
                        }else{
                            act.listener.onResult(Answer(mQuestion, mutableListOf(), null))
                            updateIndicator(act.indicator_answered_progress_bar, act.mResult.askedQuestions.size,
                                    act.mResult.test.mSortedQuestions.size)
                        }
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                        if(inputText.text.toString() != "" && input == s?.toString()) {
//                            s?.let{input = it.toString()}
//                            val act = activity as TestingActivity
//                            act.listener.onResult(Answer(mQuestion, mutableListOf(), s?.toString()))
//                        }
                    }
                })
                return view
            }

            view.findViewById<LinearLayout>(R.id.answers_check_box).visibility = View.VISIBLE

            if (mQuestion.questionOptions.size > 0) {
                setFormattedText(view.findViewById(R.id.option_one_text_view), mQuestion.questionOptions[0].text, mQuestion.questionOptions[0].optionPhotoUtl)
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

        return view ?: inflater?.let { super.onCreateView(it, container, savedInstanceState) }
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
//        val percentAnswered : Int = size.times(100).div(total)
//        progressBar?.progress = percentAnswered
    }
//        setUpClickListener()
//        listener = View.OnClickListener { viewTarget: View ->
//            val userAnswer: Int = when (viewTarget.id) {
//                view?.answer_one_check_box?.id -> {
//                    clearCheckBox(0)
//                    checkCorrectAnswer(0)
//                }
//                view?.answer_two_check_box?.id -> {
//                    clearCheckBox(1)
//                    checkCorrectAnswer(1)
//                }
//                view?.answer_three_check_box?.id -> {
//                    clearCheckBox(2)
//                    checkCorrectAnswer(2)
//                }
//                view?.answer_four_check_box?.id -> {
//                    clearCheckBox(3)
//
//                    checkCorrectAnswer(3)
//                }
//                view?.answer_five_check_box?.id -> {
//                    clearCheckBox(4)
//                    checkCorrectAnswer(4)
//                }
//                else -> -1
//            }
//        }

    private fun setUpClickListener() {
        if (mQuestion.questionType == SINGLE_CHOICE) {
            listener = View.OnClickListener { viewTarget: View ->
                var userAnswers: MutableList<Int> = mutableListOf<Int>()
                when (viewTarget.id) {
                    view?.answer_one_check_box?.id -> {
                        clearCheckBox(0)
                        if(view?.answer_one_check_box?.isChecked == true) {
                            userAnswers.add(0)
                        }
                        checkCorrectAnswer(userAnswers)
                    }
                    view?.answer_two_check_box?.id -> {
                        clearCheckBox(1)
                        if(view?.answer_two_check_box?.isChecked == true) {
                            userAnswers.add(1)
                        }
                        checkCorrectAnswer(userAnswers)
                    }
                    view?.answer_three_check_box?.id -> {
                        clearCheckBox(2)
                        if(view?.answer_three_check_box?.isChecked == true) {
                            userAnswers.add(2)
                        }
                        checkCorrectAnswer(userAnswers)
                    }
                    view?.answer_four_check_box?.id -> {
                        clearCheckBox(3)
                        if(view?.answer_four_check_box?.isChecked == true) {
                            userAnswers.add(3)
                        }
                        checkCorrectAnswer(userAnswers)
                    }
                    view?.answer_five_check_box?.id -> {
                        clearCheckBox(4)
                        if(view?.answer_five_check_box?.isChecked == true) {
                            userAnswers.add(4)
                        }
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

    fun fromInstance(question: CloseQuestion): QuestionFragment {
        val fragment = QuestionFragment()
        val bundle = Bundle()
        bundle.putSerializable("question", question)
//        bundle.putSerializable("listener", listener)
        fragment.arguments = bundle
        return fragment
    }

    fun getInputAnswer() : String{
        return ""
    }

    fun getInputEditListener(before: EditText, current: EditText, isFirst : Boolean = false): View.OnFocusChangeListener {
        val listener = object : View.OnFocusChangeListener {
            var isTrueLostFocus = true
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    if(isFirst){
                        return
                    }
                    if (current.text.toString() == "" && before.text.toString() == "") {
                        current.isFocusable = false
                        before.isFocusable = true
                        isTrueLostFocus = false
                        return
                    }
                }
                if (!hasFocus && isTrueLostFocus) {
                    getInputAnswer()
                } else {
                    isTrueLostFocus = true
                }
            }
        }
        return listener
    }

    fun getTextChangeListener(before : EditText, current : EditText, after : EditText, isFirst : Boolean = false,
                              isLast : Boolean = false) : TextWatcher{
        return object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        }
    }
}
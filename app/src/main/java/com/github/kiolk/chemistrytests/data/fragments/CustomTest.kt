package com.github.kiolk.chemistrytests.data.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.SeekBar
import android.widget.TextView
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.models.*
import com.github.kiolk.chemistrytests.data.models.CloseQuestion.Question.INPUT_CHOICE
import com.github.kiolk.chemistrytests.data.models.CloseQuestion.Question.MULTIPLE_CHOICE
import com.github.kiolk.chemistrytests.data.models.CloseQuestion.Question.SINGLE_CHOICE
import com.github.kiolk.chemistrytests.ui.TEST_PARAM_INT
import com.github.kiolk.chemistrytests.ui.TestingActivity

class CustomTest() : Fragment() {

    lateinit var mSelectedTopics: MutableList<String>
    lateinit var mSortedQuestions: MutableList<CloseQuestion>
    lateinit var mSortedQuestionsTmp: MutableList<CloseQuestion>
    lateinit var mQuestions: MutableList<CloseQuestion>
    lateinit var mQuestionType: MutableList<Int>
    var isCheckedSingle: Boolean = false
    var isCheckedMultiple: Boolean = false
    var isCheckedInput: Boolean = false
    var mNumberAskedQuestions: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_custome_test, null)
        return view ?: super.onCreateView(inflater, container, savedInstanceState)
    }

    fun combineCustomeTest(questions: MutableList<CloseQuestion>) {
        mQuestions = questions
        view?.findViewById<TextView>(R.id.available_questions_indicator_text_view)?.text = mQuestions.size.toString()
        mSortedQuestionsTmp = mutableListOf()
        mSelectedTopics = mutableListOf()
        mSortedQuestions = mQuestions
        mSortedQuestionsTmp = mSortedQuestions
        mQuestionType = mutableListOf()
        view?.findViewById<CheckBox>(R.id.single_choice_check_box)?.setOnClickListener {
            isCheckedSingle = view?.findViewById<CheckBox>(R.id.single_choice_check_box)?.isChecked ?: false
            setAvailableQuestions()
        }
        view?.findViewById<CheckBox>(R.id.multiple_choice_check_box)?.setOnClickListener {
            isCheckedMultiple = view?.findViewById<CheckBox>(R.id.multiple_choice_check_box)?.isChecked ?: false
            setAvailableQuestions()
        }
        view?.findViewById<CheckBox>(R.id.input_choice_check_box)?.setOnClickListener {
            isCheckedInput = view?.findViewById<CheckBox>(R.id.input_choice_check_box)?.isChecked ?: false
            setAvailableQuestions()
        }
        view?.findViewById<TextView>(R.id.filtered_questions_indicator_text_view)?.text = mSortedQuestions.size.toString()
        var topics: MutableList<String> = mutableListOf()
        questions.forEach { it.tags?.forEach { topics.add(it) } }
        val arrayTopic = topics.toSet().toTypedArray()
        arrayTopic.sortBy { it }
        var isChackedArray: BooleanArray = kotlin.BooleanArray(arrayTopic.size)
        view?.findViewById<Button>(R.id.select_topic_button)?.setOnClickListener {
            val dialog: AlertDialog.Builder = AlertDialog.Builder(context)
            dialog.setTitle("Available topics").setMultiChoiceItems(arrayTopic, isChackedArray, object : DialogInterface.OnMultiChoiceClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int, isChecked: Boolean) {
                    if (isChecked) {
                        mSelectedTopics.add(arrayTopic[which])
                    } else if (mSelectedTopics.contains(arrayTopic[which])) {
                        mSelectedTopics.remove(arrayTopic[which])
                    }
                    setAvailableQuestions()
                }
            })
            val alertDialog = dialog.create()
            alertDialog.show()
        }
        view?.findViewById<CheckBox>(R.id.fixed_tags_check_box)?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                setAvailableQuestions()
            }
        })
        view?.findViewById<Button>(R.id.start_custom_test_button)?.setOnClickListener {
            startTest()
        }
    }

    private fun setAvailableQuestions() {
        val tmpSortedQuestions: MutableList<CloseQuestion> = mutableListOf()
        if (view?.findViewById<CheckBox>(R.id.fixed_tags_check_box)?.isChecked == false) {
            mSelectedTopics.forEach {
                val tag = it
                mSortedQuestionsTmp.forEach {
                    if (it.tags?.contains(tag) == true) {
                        if (!tmpSortedQuestions.contains(it)) {
                            tmpSortedQuestions.add(it)
                        }
                    }
                }
            }
            tmpSortedQuestions.toSet()
            if (mSelectedTopics.isNotEmpty()) {
                mSortedQuestions = tmpSortedQuestions
            } else {
                mSortedQuestions = mSortedQuestionsTmp
            }
        } else {
            mSortedQuestionsTmp.forEach {
                if (it.tags?.containsAll(mSelectedTopics) == true) {
                    if (!tmpSortedQuestions.contains(it)) {
                        tmpSortedQuestions.add(it)
                    }
                }
            }
            if (mSelectedTopics.isNotEmpty()) {
                mSortedQuestions = tmpSortedQuestions
            } else {
                mSortedQuestions = mSortedQuestionsTmp
            }
        }
        filteredByTypeQuestion()
        view?.findViewById<TextView>(R.id.filtered_questions_indicator_text_view)?.text = mSortedQuestions.size.toString()
        view?.findViewById<SeekBar>(R.id.number_asked_question_seek_bar)?.max = mSortedQuestions.size
        view?.findViewById<SeekBar>(R.id.number_asked_question_seek_bar)?.progress = mSortedQuestions.size
        mNumberAskedQuestions = view?.findViewById<SeekBar>(R.id.number_asked_question_seek_bar)?.progress ?: 0
        view?.findViewById<SeekBar>(R.id.number_asked_question_seek_bar)?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mNumberAskedQuestions = progress
                view?.findViewById<TextView>(R.id.will_ask_questions_indicator_text_view)?.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

    }

    private fun filteredByTypeQuestion() {
        val types = mutableListOf<Int>()
        if (view?.findViewById<CheckBox>(R.id.single_choice_check_box)?.isChecked == true) {
            types.add(SINGLE_CHOICE)
        }
        if (view?.findViewById<CheckBox>(R.id.multiple_choice_check_box)?.isChecked == true) {
            types.add(MULTIPLE_CHOICE)
        }
        if (view?.findViewById<CheckBox>(R.id.input_choice_check_box)?.isChecked == true) {
            types.add(INPUT_CHOICE)
        }
        val tmpSortedQuestions: MutableList<CloseQuestion> = mutableListOf()
        mSortedQuestions.forEach {
            if (types.contains(it.questionType)) {
                tmpSortedQuestions.add(it)
            }
        }
//        if(types.isNotEmpty()) {
        mQuestionType = types
        mSortedQuestions = tmpSortedQuestions
//        }

    }

    fun startTest() {
        val params: TestParams = TestParams(10, RANDOM_ORDER, TRAINING_TEST, mNumberAskedQuestions
                , true, FREE_TEST,
                TestInfo(), mSelectedTopics, mQuestionType)
        val intent: Intent = Intent(context, TestingActivity::class.java)
        intent.putExtra(TEST_PARAM_INT, params)
        startActivity(intent)
    }
}
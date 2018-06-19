package com.github.kiolk.chemistrytests.data.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.R.color.LEVEL_ONE
import com.github.kiolk.chemistrytests.data.TestsPresenter
import com.github.kiolk.chemistrytests.data.adapters.SelectQuestionsArrayAdapter
import com.github.kiolk.chemistrytests.data.database.DBOperations
import com.github.kiolk.chemistrytests.data.models.*
import com.github.kiolk.chemistrytests.data.models.CloseQuestion.Question.INPUT_CHOICE
import com.github.kiolk.chemistrytests.data.models.CloseQuestion.Question.MULTIPLE_CHOICE
import com.github.kiolk.chemistrytests.data.models.CloseQuestion.Question.SINGLE_CHOICE
import com.github.kiolk.chemistrytests.ui.activities.TEST_PARAM_INT
import com.github.kiolk.chemistrytests.ui.activities.TestingActivity
import com.google.firebase.auth.FirebaseAuth

class CustomTest : Fragment() {

    var mSelectedTopics: MutableList<String> = mutableListOf()
    lateinit var mSortedQuestions: MutableList<CloseQuestion>
    lateinit var mSortedQuestionsTmp: MutableList<CloseQuestion>
    lateinit var mQuestions: MutableList<CloseQuestion>
    lateinit var mQuestionType: MutableList<Int>
    lateinit var mCheckedArray : BooleanArray
    var mListQuestionId: MutableList<Int>? = null
    var mTestInfo: TestInfo = TestInfo()
    var mQuestionListAdapter: SelectQuestionsArrayAdapter? = null
    var isCheckedSingle: Boolean = false
    var isCheckedMultiple: Boolean = false
    var isCheckedInput: Boolean = false
    var isCheckedArrayInitialize : Boolean = false
    var isFirstTime : Boolean = true
    var mNumberAskedQuestions: Int = 0
    var mQuestionsStrength : Int = 1
    var isSaveTest: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_custome_test, null)
        return view ?: super.onCreateView(inflater, container, savedInstanceState)
    }

    fun combineCustomTest() {
        if(isFirstTime){
            initialisation()
            isFirstTime = false
        }
        view?.findViewById<TextView>(R.id.available_questions_indicator_text_view)?.text = mQuestions.size.toString()
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
        setupTagDialog()
//        var topics: MutableList<String> = mutableListOf()
//        questions.forEach { it.tags?.forEach { topics.add(it) } }
//        val arrayTopic = topics.toSet().toTypedArray()
//        arrayTopic.sortBy { it }
//        var isChackedArray: BooleanArray = kotlin.BooleanArray(arrayTopic.size)
//        view?.findViewById<Button>(R.id.select_topic_button)?.setOnClickListener {
//            val dialog: AlertDialog.Builder = AlertDialog.Builder(context)
//            dialog.setTitle("Available topics").setMultiChoiceItems(arrayTopic, isChackedArray, object : DialogInterface.OnMultiChoiceClickListener {
//                override fun onClick(dialog: DialogInterface?, which: Int, isChecked: Boolean) {
//                    if (isChecked) {
//                        mSelectedTopics.add(arrayTopic[which])
//                    } else if (mSelectedTopics.contains(arrayTopic[which])) {
//                        mSelectedTopics.remove(arrayTopic[which])
//                    }
//                    setAvailableQuestions()
//                }
//            })
//            val alertDialog = dialog.create()
//            alertDialog.show()
//        }
        view?.findViewById<CheckBox>(R.id.fixed_tags_check_box)?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                setAvailableQuestions()
            }
        })
        view?.findViewById<Button>(R.id.start_custom_test_button)?.setOnClickListener {
            startTest()
        }
        setupQuestionsStrengthBlock()
    }

    private fun initialisation() {
        mQuestions = DBOperations().getAllQuestions()
        mSortedQuestionsTmp = mutableListOf()
        mSortedQuestions = mQuestions
        mSortedQuestionsTmp = mSortedQuestions
        mQuestionType = mutableListOf()
    }

    private fun setupQuestionsStrengthBlock(){
        val strengthSeekBar = view?.findViewById<SeekBar>(R.id.strength_questionS_seek_bar)
        mQuestionsStrength = (strengthSeekBar?.progress ?: 0) + 1
        strengthSeekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mQuestionsStrength = progress + 1
                val indicator = view?.findViewById<ImageView>(R.id.strength_indicator_image_view)
                val strengthText = view?.findViewById<TextView>(R.id.strength_title_text_view)
                val titleArray = resources.getStringArray(R.array.QUESTIONS_STRENGTH_TYPE)
                when(progress){
                     0 -> {
                         indicator?.setImageDrawable(resources.getDrawable(R.drawable.ic_strength_one))
                         strengthText?.text = titleArray[progress]
                     }
                    1 -> {
                        indicator?.setImageDrawable(resources.getDrawable(R.drawable.ic_strength_two))
                        strengthText?.text = titleArray[progress]
                    }
                    2 -> {
                        indicator?.setImageDrawable(resources.getDrawable(R.drawable.ic_strength_three))
                        strengthText?.text = titleArray[progress]
                    }
                    3 -> {
                        indicator?.setImageDrawable(resources.getDrawable(R.drawable.ic_strength_four))
                        strengthText?.text = titleArray[progress]
                    }
                    4 -> {
                        indicator?.setImageDrawable(resources.getDrawable(R.drawable.ic_strength_five))
                        strengthText?.text = titleArray[progress]
                    }
                }
                setAvailableQuestions()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
        setAvailableQuestions()
    }

    private fun setupTagDialog() {
        var topics: MutableList<String> = mutableListOf()
        mQuestions.forEach { it.tags?.forEach { topics.add(it) } }
        val arrayTopic = topics.toSet().toTypedArray()
        arrayTopic.sortBy { it }
        if(!isCheckedArrayInitialize) {
            mCheckedArray = kotlin.BooleanArray(arrayTopic.size)
            isCheckedArrayInitialize = true
        }
        view?.findViewById<Button>(R.id.select_topic_button)?.setOnClickListener {
            val dialog: AlertDialog.Builder = AlertDialog.Builder(context)
            dialog.setTitle("Available topics").setMultiChoiceItems(arrayTopic, mCheckedArray, object : DialogInterface.OnMultiChoiceClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int, isChecked: Boolean) {
                    if (isChecked) {
                        mSelectedTopics.add(arrayTopic[which])
                        mCheckedArray[which]= true
                    } else if (mSelectedTopics.contains(arrayTopic[which])) {
                        mSelectedTopics.remove(arrayTopic[which])
                        mCheckedArray[which] = false
                    }
                    setAvailableQuestions()
                }
            })
            val alertDialog = dialog.create()
            alertDialog.show()
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
        filtererByStrengthQuestions()
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
//        setapQuestionListView()
//        mQuestionListAdapter = context?.let { SelectQuestionsArrayAdapter(it, R.layout.item_question_list, mSortedQuestions) }
//        mQuestionListAdapter?.notifyDataSetChanged()
    }

    private fun filtererByStrengthQuestions() {
        val tmpSortedQuestions: MutableList<CloseQuestion> = mutableListOf()
        mSortedQuestions.forEach {
            if (it.questionStrength == mQuestionsStrength) {
                tmpSortedQuestions.add(it)
            }
        }
        mSortedQuestions = tmpSortedQuestions
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
        updateQuestionList()
        updateTestInformation()
        val params: TestParams = TestParams(10, RANDOM_ORDER, TRAINING_TEST, mNumberAskedQuestions
                , true, FREE_TEST,
                mTestInfo, mSelectedTopics, mQuestionType, null, null, mListQuestionId, mQuestionsStrength)
        val intent: Intent = Intent(context, TestingActivity::class.java)
        if(isSaveTest){
            TestsPresenter.saveCustomTest(params)
        }
        intent.putExtra(TEST_PARAM_INT, params)
        startActivity(intent)
    }

    private fun updateQuestionList() {
        if (mListQuestionId?.size == 0) {
            mListQuestionId = null
        } else if (mListQuestionId != null) {
            if (mListQuestionId?.size ?: 0 > mNumberAskedQuestions) {
//                val tmp = mListQuestionId
//                mListQuestionId = tmp?.subList(0, mNumberAskedQuestions - 1)
            }
//                mListQuestionId = mListQuestionId?.subList(0, mNumberAskedQuestions - 1)
////            }
        }
    }

    private fun updateTestInformation() {
        mTestInfo.testCreated = System.currentTimeMillis()
        mTestInfo.lasModifed = System.currentTimeMillis()
        mTestInfo.testAuthor = FirebaseAuth.getInstance().currentUser?.displayName ?: "User"
    }

    fun getAvailableQuestions(): MutableList<CloseQuestion> {
        return mSortedQuestions //?: mutableListOf()
    }
}
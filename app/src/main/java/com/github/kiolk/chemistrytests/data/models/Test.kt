package com.github.kiolk.chemistrytests.data.models

import com.github.kiolk.chemistrytests.utils.CollectionUtil.randomSort
import java.io.Serializable


class Test(var questions: MutableList<CloseQuestion> = mutableListOf(),
           var params: TestParams = TestParams()) : Serializable {

    var resultingScore = 0F
    var mSortedQuestions: List<CloseQuestion>


    init {
        if (params.questionList != null && params.questionList?.size != 0) {
            val list = params.questionList
            val tmpList: MutableList<CloseQuestion> = mutableListOf()
            questions.forEach {
                if (list?.contains(it.questionId) == true) {
                    tmpList.add(it)
                }
            }
            questions = tmpList
        }
        mSortedQuestions = questions
        checkRandomisation()
        checkTags()
        checkKeyWords()
//        checkByListString(params.keyWords, false)
        checkStrength()
        checkQuestionType()
        filteredByAdditionalInformation()
        checkOrder()
    }

    private fun filteredByAdditionalInformation() {
        var tmpSortedQuestions: MutableList<CloseQuestion> = mutableListOf()
        mSortedQuestions.forEach {
            if (params.withHint) {
                if(it.hints != null){
                    tmpSortedQuestions.add(it)
                }
            }else{
                tmpSortedQuestions.add(it)
            }
        }
        mSortedQuestions = tmpSortedQuestions
        tmpSortedQuestions = mutableListOf()
        mSortedQuestions.forEach {
            if(params.withTheory){
                if(it.theoryListId?.isNotEmpty()==true){
                    tmpSortedQuestions.add(it)
                }
            }else{
                tmpSortedQuestions.add(it)
            }
        }
        mSortedQuestions = tmpSortedQuestions
        tmpSortedQuestions = mutableListOf()
        mSortedQuestions.forEach {
            if(params.withExplanation){
                if(it.answerExplanations != null){
                    tmpSortedQuestions.add(it)
                }
            }else{
                tmpSortedQuestions.add(it)
            }
        }
        mSortedQuestions = tmpSortedQuestions
    }

    private fun checkKeyWords() {
        if (params.keyWords != null && params.keyWords?.size != 0) {
            val tmpQuestionList = mutableListOf<CloseQuestion>()
            params.keyWords?.forEach {
                val keyWord = it
                val filtered = mSortedQuestions.filter { it.keyWords?.contains(keyWord) == true }
                filtered.forEach {
                    if (!tmpQuestionList.contains(it)) {
                        tmpQuestionList.add(it)
                    }
                }
            }
            mSortedQuestions = tmpQuestionList
        }
    }

    private fun checkQuestionsList() {
        if (params.questionList != null && params.questionList?.size ?: 0 > params.numberOfQuestions) {

        }
    }

    private fun checkTopics() {
        val tmpQuestionList = mutableListOf<CloseQuestion>()
        var isAdded: Boolean = false
        mSortedQuestions.forEach {
            val question = it
            it.tags?.forEach {
                if (params.tags.contains(it) && !isAdded) {
                    tmpQuestionList.add(question)
                    isAdded = true
                }
            }
        }
        mSortedQuestions = tmpQuestionList
    }

    private fun checkRandomisation() {
        if (params.isRandomOption) {
            mSortedQuestions.forEach {
                it.randomizedOptions()
            }
        }
    }

    private fun checkStrength() {
        val tmpQuestionList = mutableListOf<CloseQuestion>()
        if (params.strengthInRange) {
            mSortedQuestions.forEach {
                if (it.questionStrength <= params.questionsStrength) {
                    tmpQuestionList.add(it)
                }
            }
        } else {
            mSortedQuestions.forEach {
                if (it.questionStrength == params.questionsStrength) {
                    tmpQuestionList.add(it)
                }
            }
        }
        mSortedQuestions = tmpQuestionList
    }

    private fun checkOrder() {
        if (params.order == RANDOM_ORDER) {
            if (params.numberOfQuestions != ALL_QUESTION && mSortedQuestions.size >= params.numberOfQuestions) {
                mSortedQuestions = randomSort(mSortedQuestions).subList(0, params.numberOfQuestions)
            } else {
                mSortedQuestions = randomSort(mSortedQuestions)
            }
        } else {
            if (params.numberOfQuestions != ALL_QUESTION && mSortedQuestions.size >= params.numberOfQuestions) {
                mSortedQuestions = mSortedQuestions.subList(0, params.numberOfQuestions)
            }
        }
    }

    private fun checkTags() {
        if (params.tags.isNotEmpty()) {
            val tmpQuestionList = mutableListOf<CloseQuestion>()
            params.tags.forEach {
                val tag = it
                val filtered = mSortedQuestions.filter { it.tags?.contains(tag) == true }
                filtered.forEach {
                    if (!tmpQuestionList.contains(it)) {
                        tmpQuestionList.add(it)
                    }
                }
            }
            mSortedQuestions = tmpQuestionList
        }
    }

    private fun checkByListString(list: List<String>?, isTags: Boolean) {
        if (list != null) {
            val tmpQuestionList = mutableListOf<CloseQuestion>()
            list.forEach {
                val item = it
                var filtered = listOf<CloseQuestion>()
                if (isTags) {
                    filtered = mSortedQuestions.filter { it.tags?.contains(item) == true }
                } else {
                    filtered = mSortedQuestions.filter { it.keyWords?.contains(item) == true }
                }
                filtered.forEach {
                    if (!tmpQuestionList.contains(it)) {
                        tmpQuestionList.add(it)
                    }
                }
            }
            mSortedQuestions = tmpQuestionList
        }
    }

    private fun checkQuestionType() {
        if (params.questionTypes.isNotEmpty()) {
            val tmpQuestionList = mutableListOf<CloseQuestion>()
            params.questionTypes.forEach {
                val type = it
                tmpQuestionList.addAll(mSortedQuestions.filter { it.questionType == type })
            }
            mSortedQuestions = tmpQuestionList
        }
    }

    fun getQuestion(questionNumber: Int) = mSortedQuestions[questionNumber]
}

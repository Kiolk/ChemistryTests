package com.github.kiolk.chemistrytests.data.models

import android.graphics.Path


class Test(var questions: List<CloseQuestion>,
           var params: TestParams) {

    //    var questionNumber = -1
    var resultingScore = 0F
    //    var allQuestions: List<CloseQuestion>
    var mSortedQuestions: List<CloseQuestion>
//    var mSortedQuestions2: List<CloseQuestion>
//    var filteredQuestions : Int


    init {
//        allQuestions = questions
//        mSortedQuestions2 = mutableListOf()
        mSortedQuestions = questions
        checkRandomisation()
        checkQuestionType()
        checkTags()
        checkOrder()
//        filteredQuestions =mSortedQuestions.size
    }

    private fun checkRandomisation() {
        if (params.isRandomOption) {
            mSortedQuestions.forEach {
                it.randomizedOptions()
            }
        }
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
//                tmpQuestionList.addAll(mSortedQuestions.filter { it.tags?.contains(tag) == true })
//                tmpQuestionList.toList()
            }
            mSortedQuestions = tmpQuestionList
//            mSortedQuestions2 = tmpQuestionList
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

//    fun getNext(): Question {
//        if (!isStartTest) {
//            isStartTest = true
//            questionNumber = -1
//        }
//        questionNumber++
//        val question = questions[questionNumber]
//        return question
//    }
//
//    fun checkAnswer(answer: Option): Boolean {
//        if (questions[questionNumber].checkAnswer(answer)) {
//            resultingScore = resultingScore.plus(questions[questionNumber].questionCost)
//            return true
//        }
//        return false
//    }
//
//    fun getCorrectAnswer(): Option {
//        return questions[questionNumber].getAnswer()
//    }


//    fun hasNext(): Boolean = questionNumber < questions.size - 1
}

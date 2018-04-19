package com.github.kiolk.chemistrytests.data.models

import android.graphics.Path


class Test(var questions: List<CloseQuestion>,
           var params: TestParams) {

    //    var questionNumber = -1
    var resultingScore = 0F
    var allQuestions: List<CloseQuestion>

    init {
        allQuestions = questions
        checkRandomisation()
        checkOrder()
        checkQuestionType()
        checkTags()
    }

    private fun checkRandomisation() {
        if (params.isRandomOption) {
            questions.forEach {
                it.randomizedOptions()
            }
        }
    }

    private fun checkOrder() {
        if (params.order == RANDOM_ORDER) {
            if (params.numberOfQuestions != ALL_QUESTION && questions.size >= params.numberOfQuestions) {
                questions = randomSort(questions).subList(0, params.numberOfQuestions)
            } else {
                questions = randomSort(questions)
            }
        } else {
            if (params.numberOfQuestions != ALL_QUESTION && questions.size >= params.numberOfQuestions) {
                questions = questions.subList(0, params.numberOfQuestions)
            }
        }
    }

    private fun checkTags() {
        if(params.tags.isNotEmpty()){
            val tmpQuestionList = mutableListOf<CloseQuestion>()
            params.tags.forEach {
                val tag= it
                tmpQuestionList.addAll(questions.filter { it.tags?.contains(tag) == true })
            }
            questions = tmpQuestionList
        }
    }

    private fun checkQuestionType() {
        if (params.questionTypes.isNotEmpty()) {
            val tmpQuestionList = mutableListOf<CloseQuestion>()
            params.questionTypes.forEach {
                val type = it
                tmpQuestionList.addAll(questions.filter { it.questionType == type })
            }
            questions = tmpQuestionList
        }
    }

    fun getQuestion(questionNumber: Int) = questions[questionNumber]

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

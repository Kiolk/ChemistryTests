package com.github.kiolk.chemistrytests.data.models

import android.graphics.Path


class Test(var questions: List<CloseQuestion>,
           var params: TestParams) {


    var isStartTest = false
    var questionNumber = -1
    var resultingScore = 0F
    lateinit var allQuestions : List<CloseQuestion>

    init {
        if (params.order == RANDOM_ORDER) {
            allQuestions = questions
            if(params.isRandomOption) {
                questions.forEach { it.options = randomSort(it.options) }
            }
            if (params.numberOfQuestions != ALL_QUESTION && questions.size >= params.numberOfQuestions){
                questions = randomSort(questions).subList(0, params.numberOfQuestions)
            }
        }
    }

    fun getQuestion(questionNumber : Int) = questions[questionNumber]

    fun getNext(): Question {
        if (!isStartTest) {
            isStartTest = true
            questionNumber = -1
        }
        questionNumber++
        val question = questions[questionNumber]
        return question
    }

    fun checkAnswer(answer: Option): Boolean {
        if (questions[questionNumber].checkAnswer(answer)) {
            resultingScore = resultingScore.plus(questions[questionNumber].questionCost)
            return true
        }
        return false
    }

    fun getCorrectAnswer(): Option {
        return questions[questionNumber].getAnswer()
    }


    fun hasNext(): Boolean = questionNumber < questions.size - 1
}
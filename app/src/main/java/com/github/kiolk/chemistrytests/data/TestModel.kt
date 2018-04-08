package com.github.kiolk.chemistrytests.data

import com.github.kiolk.chemistrytests.data.Test.TestParams.RANDOM_ORDER

class Test(var questions: List<CloseQuestion>,
           var order: Int = TestParams.DIRECT_ORDER
) {
    object TestParams {
        val RANDOM_ORDER: Int = 0
        val DIRECT_ORDER: Int = 1
    }

    var isStartTest = false
    var questionNumber = -1
    var resultingScore = 0F

    init {
        if (order == RANDOM_ORDER) {
            questions = randomSort(questions)
        }
    }

    fun getNext(): Question {
        if (!isStartTest) {
            isStartTest = true
            questionNumber = -1
        }
        questionNumber++
        val question = questions[questionNumber]
        return question
    }

    fun checkAnswer(answer : String) : Boolean{
        if(questions[questionNumber].checkAnswer(answer)){
            resultingScore = resultingScore.plus(questions[questionNumber].questionCost)
            return true
        }
        return false
    }

    fun getCorrectAnswer() : Option{
        return questions[questionNumber].getAnswer()
    }




    fun hasNext(): Boolean = questionNumber < questions.size - 1
}
package com.github.kiolk.chemistrytests.data.models

interface OnEndTestListener {
    fun endedTest()
}

class Result(var test: Test, var endListener: OnEndTestListener) {

    var askedQuestions: MutableList<Answer> = mutableListOf()
    var points: Float = 0.0F


    fun takeAnswer(answer: Answer) {

        if (askedQuestions.find { it.question == answer.question } != null) {
            if (answer.userInput != null) {
                askedQuestions.find { it.question == answer.question }?.userInput = answer.userInput
            } else {
                askedQuestions.find { it.question == answer.question }?.userAnswers = answer.userAnswers
            }
        } else {
            askedQuestions.add(answer)
        }
        points = getTestResult()
        if (isLastTestQuestion()) endListener.endedTest()
    }

    fun getTestResult(): Float {
        var result: Float = 0.0F
        askedQuestions.filter { it.userAnswers.isNotEmpty() && it.question.checkCorrectAnswersByNumbers(it.userAnswers) }.forEach {
            result += it.question.questionCost
        }
        askedQuestions.filter { !it.userAnswers.isNotEmpty() && it.question.checkOpenQuestionAnswers(it.userInput) }.forEach {
            result += it.question.questionCost
        }
        return result
    }

    fun isLastTestQuestion() = askedQuestions.size == test.mSortedQuestions.size

    fun userResultAnswers(): MutableList<Answer> {
        val listAskedQuestion: MutableList<Answer> = mutableListOf()
        test.mSortedQuestions.forEach {
            val question: CloseQuestion = it
            val userAnswer: Answer? = askedQuestions.find { it.question == question }
            if (userAnswer != null) {
                listAskedQuestion.add(userAnswer)
            } else {
                listAskedQuestion.add(Answer(question, listOf(-1), null))
            }
        }
        return listAskedQuestion
    }

    fun removeEmptyAnswer(answer: Answer) {
        val tmp: MutableList<Answer> = mutableListOf()
        askedQuestions.forEach {
            if (it.question != answer.question) {
                tmp.add(it)
            }
        }
        askedQuestions = tmp
    }
}


package com.github.kiolk.chemistrytests.data.models

import java.io.Serializable

interface OnEndTestListener {
    fun endedTest()
}

class Result(var test: Test = Test(), var endListener: OnEndTestListener? = null) : Serializable {

    var askedQuestions: MutableList<Answer> = mutableListOf()
    var points: Float = 0.0F
    var correctAnswers = 0
    lateinit var mResultInfo : ResultInformation

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
        if (isLastTestQuestion()) endListener?.endedTest()
    }

    fun getTestResult(): Float {
        var result: Float = 0.0F
        correctAnswers = 0
        askedQuestions.filter { it.userAnswers.isNotEmpty() && it.question.checkCorrectAnswersByNumbers(it.userAnswers) }.forEach {
            result += it.question.questionCost
            correctAnswers += 1
        }
        askedQuestions.filter { !it.userAnswers.isNotEmpty() && it.question.checkOpenQuestionAnswers(it.userInput) }.forEach {
            result += it.question.questionCost
            correctAnswers += 1
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

    fun writeResultInformation(){
        mResultInfo = ResultInformation()
        mResultInfo.totalQuestions = test.mSortedQuestions.size
        mResultInfo.askedQuestions = askedQuestions.size
        mResultInfo.correctAnswered = correctAnswers
        mResultInfo.wrongAnswered = mResultInfo.askedQuestions!! - correctAnswers
        mResultInfo.startTime = 1111111111111L
        mResultInfo.endTime = 2222222222222L
        mResultInfo.duration = mResultInfo.endTime!! - mResultInfo.startTime!!
        mResultInfo.resultScore = getTestResult()
        mResultInfo.percentCorrect = (mResultInfo.correctAnswered!! / mResultInfo.totalQuestions!!)*100F
        mResultInfo.resultScore = getTestResult()
        mResultInfo.testMark = getTestMark()
        mResultInfo.testParams = test.params
        mResultInfo.listAskedQuestionsId = mutableListOf()
        askedQuestions.forEach { mResultInfo.listAskedQuestionsId?.add(it.question.questionId)  }

//        mResultInfo.sortedQuestions = test.mSortedQuestions //as MutableList<CloseQuestion>
//        mResultInfo.anweredQuestions = askedQuestions
    }

    private fun getTestMark(): String {
        val resultScore = getTestResult()
        val maxScore : Float = getMaxScore()
        val system : ScoredSystem = ScoredSystem(test.params.scoredSystem)
        return system.getMark(resultScore, maxScore) ?: "No mark"
    }

    private fun getMaxScore(): Float {
        var maxResult = 0F
        test.mSortedQuestions.forEach{maxResult += it.questionCost}
        return maxResult
    }
}


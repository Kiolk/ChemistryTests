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
    var mStartTestTime : Long
    var mStartNewQuestion : Long

    init {
        mStartTestTime = System.currentTimeMillis()
        mStartNewQuestion = mStartTestTime
    }

    fun takeAnswer(answer: Answer) {
        if (askedQuestions.find { it.question == answer.question } != null) {
            val additionalThinkingTime : Long = System.currentTimeMillis() - mStartNewQuestion
            if (answer.userInput != null) {
                val findAnswer = askedQuestions.find { it.question == answer.question }
                findAnswer?.userInput = answer.userInput
                findAnswer?.endTime = (findAnswer?.endTime ?: 0) + additionalThinkingTime
            } else {
                val findAnswer = askedQuestions.find { it.question == answer.question }
                findAnswer?.userAnswers = answer.userAnswers
                findAnswer?.endTime = (findAnswer?.endTime ?: 0) + additionalThinkingTime
            }
        } else {
            answer.startTime = mStartNewQuestion
            answer.endTime = System.currentTimeMillis()
            askedQuestions.add(answer)
        }
        mStartNewQuestion = System.currentTimeMillis()
        points = getTestResult()
        if (isLastTestQuestion()) endListener?.endedTest()
    }

    fun getTestResult(): Float {
        var result: Float = 0.0F
        correctAnswers = 0
        askedQuestions.filter { it.userAnswers.isNotEmpty() && it.question.checkCorrectAnswersByNumbers(it.userAnswers) }.forEach {
            result += it.question.questionCost
            it.isCorrectAnswered = true
            correctAnswers += 1
        }
        askedQuestions.filter { !it.userAnswers.isNotEmpty() && it.question.checkOpenQuestionAnswers(it.userInput) }.forEach {
            result += it.question.questionCost
            it.isCorrectAnswered = true
            correctAnswers += 1
        }
        return result
    }

    fun isLastTestQuestion() = askedQuestions.size == test.mSortedQuestions.size

    fun userResultAnswers(): MutableList<Answer> {
        val listAskedQuestion: MutableList<Answer> = mutableListOf()
        test.mSortedQuestions.forEach {
            val question: CloseQuestion = it
            val userAnswer: Answer? = askedQuestions.find { it.question.questionId == question.questionId }
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

    fun writeResultInformation() : ResultInformation{
        mResultInfo = ResultInformation()
        mResultInfo.startTime = mStartTestTime
        mResultInfo.endTime = System.currentTimeMillis()
        mResultInfo.totalQuestions = test.mSortedQuestions.size
        mResultInfo.askedQuestions = askedQuestions.size
        mResultInfo.correctAnswered = correctAnswers
        mResultInfo.wrongAnswered = mResultInfo.askedQuestions!! - correctAnswers
        mResultInfo.percentAsked = (mResultInfo.askedQuestions?.div(mResultInfo.totalQuestions?.toFloat() ?: 1F)) ?: 0F
//        mResultInfo.startTime = mStartTestTime
//        mResultInfo.endTime = System.currentTimeMillis()
        mResultInfo.duration = mResultInfo.endTime!! - mResultInfo.startTime!!
        mResultInfo.resultScore = getTestResult()
        mResultInfo.percentCorrect = (mResultInfo.correctAnswered!! / mResultInfo.totalQuestions!!)*100F
        val percent : Float = mResultInfo.percentCorrect ?: 0F
        mResultInfo.isCompleted = percent >= test.params.minPercentForComplete
        mResultInfo.resultScore = getTestResult()
        mResultInfo.testMark = getTestMark()
        var totalDifficulty : Int = 0
        askedQuestions.forEach { totalDifficulty += it.question.questionStrength }
        mResultInfo.difficulty = totalDifficulty.div(askedQuestions.size.toFloat())
        mResultInfo.fasterCorrect = askedQuestions.filter { it.isCorrectAnswered }.minBy{it.getDuration()}?.getDuration()
        mResultInfo.longerCorrect = askedQuestions.filter { it.isCorrectAnswered }.maxBy{it.getDuration()}?.getDuration()
        mResultInfo.fasterWrong = askedQuestions.filter { !it.isCorrectAnswered }.minBy{it.getDuration()}?.getDuration()
        mResultInfo.longerWrong = askedQuestions.filter { !it.isCorrectAnswered }.maxBy{it.getDuration()}?.getDuration()
        mResultInfo.testParams = test.params
        mResultInfo.listAskedQuestionsId = mutableListOf()
        mResultInfo.listQuestionsIdByResult = mutableListOf()
        askedQuestions.forEach {
            val questionId = it.question.questionId
            val result : Boolean = if(it.userAnswers.isNotEmpty()){
                it.question.checkCorrectAnswersByNumbers(it.userAnswers)
            }else{
                it.question.checkOpenQuestionAnswers(it.userInput)
            }
            mResultInfo.listAskedQuestionsId?.add(questionId)
            mResultInfo.listQuestionsIdByResult?.add(QuestionResultPair(result, questionId))
        }
        mResultInfo.listAnsweredQuestions = askedQuestions

//        mResultInfo.sortedQuestions = test.mSortedQuestions //as MutableList<CloseQuestion>
//        mResultInfo.anweredQuestions = askedQuestions
        return mResultInfo
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


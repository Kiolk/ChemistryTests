package com.github.kiolk.chemistrytests.data.models

import java.io.Serializable

class ResultInformation : Serializable{
    var totalQuestions : Int? = null
    var askedQuestions : Int? = null
    var correctAnswered : Int? = null
    var wrongAnswered : Int? = null
    var percentCorrect : Float? = null
    var percentAsked : Float = 0F
    var resultScore : Float? = null
    var startTime : Long? = null
    var endTime : Long? = null
    var duration : Long? = null
    var testMark : String? = null
    var difficulty : Float? = null
    var testParams : TestParams? = null
    var listAskedQuestionsId : MutableList<Int>? = null
    var isCompleted: Boolean = false
    var fasterCorrect : Long? = null
    var fasterWrong : Long? = null
    var longerCorrect : Long? = null
    var longerWrong : Long? = null
    var listQuestionsIdByResult : MutableList<Pair<Boolean, Int>>? = null
    var listAnsweredQuestions : MutableList<Answer>? = null
//    var sortedQuestions :List<CloseQuestion> = mutableListOf()
//    var anweredQuestions : MutableList<Answer> = mutableListOf()
}
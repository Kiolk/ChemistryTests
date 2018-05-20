package com.github.kiolk.chemistrytests.data.models

import java.io.Serializable

class ResultInformation : Serializable{
    var totalQuestions : Int? = null
    var askedQuestions : Int? = null
    var correctAnswered : Int? = null
    var wrongAnswered : Int? = null
    var percentCorrect : Float? = null
    var resultScore : Float? = null
    var startTime : Long? = null
    var endTime : Long? = null
    var duration : Long? = null
}
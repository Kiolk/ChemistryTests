package com.github.kiolk.chemistrytests.data.models

interface OnAnswerListener{
    fun takeAnswer(question : CloseQuestion, points : Float)
}

abstract class Result(var params: TestParams){

    val askedQuestions : MutableList<CloseQuestion> = mutableListOf()
    val points : Float = 0.0F


    fun takeAnswer(question : CloseQuestion, point: Float){
        askedQuestions.add(question)
        points.plus(point)
    }
}


package com.github.kiolk.chemistrytests.data.models

interface OnEndTestListener{
    fun endedTest()
}

 class Result(var test : Test, var endListener : OnEndTestListener){

    val askedQuestions : MutableList<Answer> = mutableListOf()
    var points : Float = 0.0F


    fun takeAnswer(answer : Answer){
        if(askedQuestions.find { it.question == answer.question } != null ){
            askedQuestions.find { it.question == answer.question}?.userAnswer = answer.userAnswer
        }else{
            askedQuestions.add(answer)
        }
        points = getTestResult()
        if(isLastTestQuestion()) endListener.endedTest()
    }

    fun getTestResult() : Float{
        var result : Float = 0.0F
        askedQuestions.filter { it.question.checkAnswer(it.userAnswer) == true}.forEach{
            result += it.question.questionCost
        }
        return result
    }
     fun isLastTestQuestion()  = askedQuestions.size == test.questions.size

}


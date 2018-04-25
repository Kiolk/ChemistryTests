package com.github.kiolk.chemistrytests.data.models

import java.io.Serializable

class Answer(var question: CloseQuestion, var userAnswers: List<Int>, var userInput: String? = null) : Serializable {
    fun getAnsweredOptions(): MutableList<Option> {
        val resultList: MutableList<Option> = mutableListOf()
        userAnswers.forEach {
            if (it >= 0) {
                resultList.add(question.questionOptions[it])
            }
        }
        return resultList
    }
}
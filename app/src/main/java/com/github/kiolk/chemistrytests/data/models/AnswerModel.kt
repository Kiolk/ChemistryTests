package com.github.kiolk.chemistrytests.data.models

import java.io.Serializable

class Answer(var question : CloseQuestion, var userAnswers : List<Int>, var userInput : String? = null) :Serializable
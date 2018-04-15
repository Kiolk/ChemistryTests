package com.github.kiolk.chemistrytests.data.models

val RANDOM_ORDER: Int = 0
val DIRECT_ORDER: Int = 1
val EXAM_TEST : Int = 0
val TRAINING_TEST : Int = 1
val ALL_QUESTION : Int = 0
val FREE_TEST = 0
val DIRECT_TEST =1

class TestParams(var order : Int = DIRECT_ORDER,
                 var testType : Int = TRAINING_TEST,
                 var numberOfQuestions : Int = ALL_QUESTION,
                 var isRandomOption : Boolean = false,
                 var direction : Int = FREE_TEST)

package com.github.kiolk.chemistrytests.data.models

import com.github.kiolk.chemistrytests.data.models.CloseQuestion.Question.SINGLE_CHOICE
import java.io.Serializable

val RANDOM_ORDER: Int = 0
val DIRECT_ORDER: Int = 1
val EXAM_TEST : Int = 0
val TRAINING_TEST : Int = 1
val ALL_QUESTION : Int = 0
val FREE_TEST = 0
val DIRECT_TEST =1

class TestParams(var testId : Int = 1,
                 var order : Int = DIRECT_ORDER,
                 var testType : Int = TRAINING_TEST,
                 var numberOfQuestions : Int = ALL_QUESTION,
                 var isRandomOption : Boolean = false,
                 var direction : Int = FREE_TEST,
                 var testInfo : TestInfo = TestInfo(),
                 var tags : List<String> = mutableListOf(),
                 var questionTypes : List<Int> = mutableListOf(),
                 var testTimer : Long? = null) : Serializable

class TestInfo(var testTitle : String = "Test",
               var testDescription : String = "Description",
               var testAuthor : String = "Author",
               var testCreated : Long = 0L,
               var lasModifed : Long = 0L,
               var testIcon : String? = null) : Serializable

fun getExampleTest() : TestParams{
    return TestParams(3, RANDOM_ORDER, TRAINING_TEST, 5, true,
            FREE_TEST, TestInfo("My first Chemistry test",
            "This test contain simple in chemistry",
            "Yauheni Slizh", 1111111111111, 1111111111111),
            listOf("Valency", "Chemical formula", "Acids"),
            listOf(SINGLE_CHOICE))
}
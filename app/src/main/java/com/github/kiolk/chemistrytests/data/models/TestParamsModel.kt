package com.github.kiolk.chemistrytests.data.models

import com.github.kiolk.chemistrytests.data.models.CloseQuestion.Question.SINGLE_CHOICE
import java.io.Serializable

val RANDOM_ORDER: Int = 0
val DIRECT_ORDER: Int = 1
val EXAM_TEST: Int = 0
val TRAINING_TEST: Int = 1
val ALL_QUESTION: Int = 0
val FREE_TEST = 0
val DIRECT_TEST = 1
val DEFAULT_URL_ICON : String = "https://firebasestorage.googleapis.com/v0/b/myjson-182914.appspot.com/o/gears.png?alt=media&token=3dee1870-9c2c-4c12-b1df-aa9a24be5b5c"

class TestParams(var testId: Int = 1,
                 var order: Int = DIRECT_ORDER,
                 var testType: Int = TRAINING_TEST,
                 var numberOfQuestions: Int = ALL_QUESTION,
                 var isRandomOption: Boolean = false,
                 var direction: Int = FREE_TEST,
                 var testInfo: TestInfo = TestInfo(),
                 var tags: List<String> = mutableListOf(),
                 var questionTypes: List<Int> = mutableListOf(),
                 var testTimer: Long? = null,
                 var scoredSystem: String? = null,
                 var questionList: MutableList<Int>? = null,
                 var questionsStrength : Int = 1) : Serializable

class TestInfo(var testTitle: String = "Test",
               var testDescription: String = "Description",
               var testAuthor: String = "Author",
               var testCreated: Long = 0L,
               var lasModifed: Long = 0L,
               var testIcon: String? = DEFAULT_URL_ICON) : Serializable

class ScoredSystem(var scoredTytle: String? = "Scored system") : Serializable {

    companion object {
        val TEN_POINT_SYSTEM: String = "Ten point system"
        val ALPHABET_SCORE_SYSTEM : String = "Alphabet score system"
    }

    object TenPointSystem {
        val description : String = "Description of ten point system"
    }


    fun getMark(userScore: Float, maxScore: Float): String {
        if (scoredTytle == TEN_POINT_SYSTEM) {
            val percentScore = userScore.div(maxScore)
            return when {
                percentScore > 0.95 -> "10"
                percentScore > 0.85 && percentScore < 0.95 ->"9"
                percentScore > 0.75 && percentScore <0.85 -> "8"
                percentScore > 0.65 && percentScore <0.75 -> "7"
                percentScore > 0.55 && percentScore <0.65 -> "6"
                percentScore > 0.45 && percentScore <0.55 -> "5"
                percentScore > 0.35 && percentScore <0.45 -> "4"
                percentScore > 0.25 && percentScore <0.35 -> "3"
                percentScore > 0.15 && percentScore <0.25 -> "2"
                percentScore > 0.05 && percentScore <0.15 -> "1"
                percentScore < 0.5 -> "0"
                else -> {
                    "0"
                }
            }
        }else if (scoredTytle == ALPHABET_SCORE_SYSTEM){
            val percentScore = userScore.div(maxScore)
            return when {
                percentScore > 0.98 -> "A+"
                percentScore > 0.94 && percentScore < 0.97 ->"A"
                percentScore > 0.90 && percentScore <0.93 -> "A-"
                percentScore > 0.85 && percentScore <0.89 -> "B+"
                percentScore > 0.80 && percentScore <0.84 -> "B"
                percentScore > 0.75 && percentScore <0.79 -> "B-"
                percentScore > 0.70 && percentScore <0.74 -> "C+"
                percentScore > 0.65 && percentScore <0.69 -> "C"
                percentScore > 0.60 && percentScore <0.64 -> "C-"
                percentScore > 0.50 && percentScore <0.59 -> "D"
                percentScore > 0.00 && percentScore <0.49 -> "F"
                else -> {
                    "0"
                }
            }
        }
        return "No mark"
    }

    fun getDescription() : String{
        return when(scoredTytle){
                TEN_POINT_SYSTEM -> TenPointSystem.description
            else -> {
                "No description"
            }
        }
    }
}

fun getExampleTest(): TestParams {
    return TestParams(3, RANDOM_ORDER, TRAINING_TEST, 5, true,
            FREE_TEST, TestInfo("My first Chemistry test",
            "This test contain simple in chemistry",
            "Yauheni Slizh", 1111111111111, 1111111111111),
            listOf("Valency", "Chemical formula", "Acids"),
            listOf(SINGLE_CHOICE))
}
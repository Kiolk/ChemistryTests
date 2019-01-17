package com.github.kiolk.chemistrytests.data.models

import java.io.Serializable

class AcountModel(var accountTitle : String = "Title",
                  var accountFeatures : List<String> = listOf(),
                  var descriptionLanguage : String = "en",
                  var accountId : Int = 1,
                  var accountCost : Float = 1.0F) : Serializable

object Account{
    val FREE_ACCOUNT : Int = 1
    val BASIC_ACCOUNT : Int = 2
    val PREMIUM_ACCOUNT : Int = 3
    val NUMBER_OF_QUESTIONS_FREE_ACCOUNT : Int = 15
    val NUMBER_OF_QUESTIONS_BASIC_ACCOUNT : Int = 20
    val NUMBER_OF_QUESTIONS_PREMIUM_ACCOUNT : Int = 36
}
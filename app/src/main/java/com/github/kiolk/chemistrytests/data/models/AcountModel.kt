package com.github.kiolk.chemistrytests.data.models

import java.io.Serializable

class AcountModel(var accountTitle : String = "Title",
                  var accountFeatures : List<String> = listOf(),
                  var descriptionLanguage : String = "en",
                  var accountId : Int = 1,
                  var accountCost : Float = 1.0F) : Serializable

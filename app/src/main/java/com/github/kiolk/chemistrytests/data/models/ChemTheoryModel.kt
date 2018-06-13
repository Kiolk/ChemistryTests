package com.github.kiolk.chemistrytests.data.models

import java.io.Serializable

class ChemTheoryModel(var theoryId : Long = 0,
                      var theoryTitle: String = "Title",
                      var chemTheoryList : MutableList<Hint> = mutableListOf()) : Serializable

package com.github.kiolk.chemistrytests.data.models

import java.io.Serializable

fun ChemTheoryModel.readingDuration() : Int {
    var totalNumberChar = 0
    this.chemTheoryList.forEach { totalNumberChar += it.length() }
    return totalNumberChar / 900
}

class ChemTheoryModel(var theoryId : Long = 0,
                      var theoryTitle: String = "Title",
                      var chemTheoryList : MutableList<Hint> = mutableListOf()) : Serializable

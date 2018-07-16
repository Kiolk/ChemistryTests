package com.github.kiolk.chemistrytests.utils

import java.util.*

object CollectionUtil {

    fun <T> randomSort(collection: List<T>): List<T> {
        val listOfNumbers: MutableList<Int> = mutableListOf()
        while (listOfNumbers.size != collection.size) {
            val random = Random().nextInt(collection.size)
            if (!listOfNumbers.contains(random)) {
                listOfNumbers.add(random)
            }
        }

        val resultListOption = mutableListOf<T>()
        listOfNumbers.forEach({ resultListOption.add(collection[it]) })

        return resultListOption
    }
}
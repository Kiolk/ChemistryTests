package com.github.kiolk.chemistrytests.ui.activities.main

interface MainMvp {

    fun updateMenu(itemId : Int)
    fun showMassageResult(message : String)
    fun restart()
}

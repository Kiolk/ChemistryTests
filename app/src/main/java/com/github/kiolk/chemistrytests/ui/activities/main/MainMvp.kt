package com.github.kiolk.chemistrytests.ui.activities.main

import com.github.kiolk.chemistrytests.data.models.AcountModel

interface MainMvp {

    fun updateMenu(itemId : Int)
    fun showMassageResult(message : String)
    fun restart()
    fun paymentProcess(paymentAccount : AcountModel)
}

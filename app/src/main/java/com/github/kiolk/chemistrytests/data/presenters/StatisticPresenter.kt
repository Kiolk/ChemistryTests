package com.github.kiolk.chemistrytests.data.presenters

import com.github.kiolk.chemistrytests.data.database.DBOperations
import com.github.kiolk.chemistrytests.data.fragments.GeneralStatisticFragment
import com.github.kiolk.chemistrytests.data.models.StatisticModel
import com.google.firebase.auth.FirebaseAuth

class StatisticPresenter(var fragment : GeneralStatisticFragment){

    var mStatistic = StatisticModel()

    fun presentStatistic(){
        prepareStatistic()
        fragment.setupGeneralStatistic(mStatistic)
    }

    private fun prepareStatistic(){
        var user = DBOperations().getUser(FirebaseAuth.getInstance().currentUser?.uid)
        var result = user?.completedTests
            result?.forEach {
                mStatistic.mTotalAskedQuestions += it.askedQuestions ?: 0
                mStatistic.mCorrectAnswered += it.correctAnswered ?: 0
                mStatistic.mWrongAnswered += it.wrongAnswered ?: 0
            }
    }
}
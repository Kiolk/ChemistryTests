package com.github.kiolk.chemistrytests.data.presenters

import com.github.kiolk.chemistrytests.data.database.DBOperations
import com.github.kiolk.chemistrytests.data.fragments.GeneralStatisticFragment
import com.github.kiolk.chemistrytests.data.models.CloseQuestion
import com.github.kiolk.chemistrytests.data.models.StatisticModel
import com.github.kiolk.chemistrytests.data.models.TopicStatisticModel
import com.google.firebase.auth.FirebaseAuth

class StatisticPresenter(var fragment : GeneralStatisticFragment){

    var mStatistic = StatisticModel()
    var mTopicsStatic :  MutableList<TopicStatisticModel> = mutableListOf()

    fun presentStatistic(){
        fragment.setupGeneralStatistic(mStatistic)
        var fragment : GeneralStatisticFragment
    }

    private fun prepareStatistic(){
        val user = DBOperations().getUser(FirebaseAuth.getInstance().currentUser?.uid)
        val result = user?.completedTests
        val questionResultInformation : MutableList<Pair<Boolean, Int>> = mutableListOf()
            result?.forEach {
                mStatistic.mTotalAskedQuestions += it.askedQuestions ?: 0
                mStatistic.mCorrectAnswered += it.correctAnswered ?: 0
                mStatistic.mWrongAnswered += it.wrongAnswered ?: 0
                it.listQuestionsIdByResult?.let { it1 -> questionResultInformation.addAll(it1) }
            }
        val listAskedQuestions : MutableList<Int> = mutableListOf()
        questionResultInformation.forEach { listAskedQuestions.add(it.second) }
        val setOfQuestionsId = listAskedQuestions.toSet()
        val questionsList : MutableList<CloseQuestion> = DBOperations().getQuestionsById(setOfQuestionsId)
        val tmp = questionsList.map { it.tags }
        val tags : MutableList<String> = mutableListOf()
        tmp.forEach{it?.forEach{
            tags.add(it)
        }}
        val setOfTags : Set<String> = tags.toSet()
        setOfTags.forEach{
            val topicStatistic : TopicStatisticModel = TopicStatisticModel(it)
            val topic = it
            val topicQuestionsId : List<Int> = questionsList.filter {  it.tags?.contains(topic) == true }.map { it.questionId }
            topicQuestionsId.forEach{
                val id = it
                topicStatistic.mAskedQuestions += questionResultInformation.filter { it.second == id }.count()
                topicStatistic.mCorrectAnswers += questionResultInformation.filter { it.second == id && it.first }.count()
            }
            mTopicsStatic.add(topicStatistic)
        }
        val dack = 3
    }
}
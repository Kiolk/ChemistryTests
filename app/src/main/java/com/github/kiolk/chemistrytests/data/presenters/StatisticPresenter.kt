package com.github.kiolk.chemistrytests.data.presenters

import android.support.v4.app.Fragment
import com.github.kiolk.chemistrytests.data.asynctasks.ResultCallback
import com.github.kiolk.chemistrytests.data.asynctasks.SingleAsyncTask
import com.github.kiolk.chemistrytests.data.executs.PrepareStatisticExecutble
import com.github.kiolk.chemistrytests.data.fragments.StatisticView
import com.github.kiolk.chemistrytests.data.fragments.TopicStatisticView
import com.github.kiolk.chemistrytests.data.models.GeneralStatisticModel
import com.github.kiolk.chemistrytests.data.models.StatisticModel
import com.github.kiolk.chemistrytests.data.models.TopicStatisticModel

class StatisticPresenter(private var mGeneralStatistic: StatisticView, private var mTopicStatistic: TopicStatisticView){

    var mStatistic = StatisticModel()
    var mTopicsStatics:  MutableList<TopicStatisticModel> = mutableListOf()
    var isDataSetup : Boolean = false

    fun presentStatistic() : Fragment {
//        if(!isDataSetup){
//            prepareStatistic()
//        }
        mGeneralStatistic.setupGeneralStatistic(mStatistic)
        val fragment = mGeneralStatistic as Fragment
        return fragment
    }

   fun prepareStatistic(callback : ResultCallback){
        isDataSetup = true
        SingleAsyncTask().execute(PrepareStatisticExecutble(object : ResultCallback{
            override fun <T> onSuccess(any: T?) {
                val statistic : GeneralStatisticModel = any as GeneralStatisticModel
                mStatistic = statistic.mStatistic
                mTopicsStatics = statistic.mTopicsStatistic
                presentStatistic()
                presentTopicsStatistic()
                callback.onSuccess(statistic)
            }

            override fun onError() {
            }
        }))

//        val user = DBOperations().getUser(FirebaseAuth.getInstance().currentUser?.uid)
//        val result = user?.completedTests
//        val questionResultInformation : MutableList<Pair<Boolean, Int>> = mutableListOf()
//            result?.forEach {
//                mStatistic.mTotalAskedQuestions += it.askedQuestions ?: 0
//                mStatistic.mCorrectAnswered += it.correctAnswered ?: 0
//                mStatistic.mWrongAnswered += it.wrongAnswered ?: 0
//                it.listQuestionsIdByResult?.let { it1 -> questionResultInformation.addAll(it1) }
//            }
//        val listAskedQuestions : MutableList<Int> = mutableListOf()
//        questionResultInformation.forEach { listAskedQuestions.add(it.second) }
//        val setOfQuestionsId = listAskedQuestions.toSet()
//        val questionsList : MutableList<CloseQuestion> = DBOperations().getQuestionsById(setOfQuestionsId)
//        val tmp = questionsList.map { it.tags }
//        val tags : MutableList<String> = mutableListOf()
//        tmp.forEach{it?.forEach{
//            tags.add(it)
//        }}
//        val setOfTags : Set<String> = tags.toSet()
//        setOfTags.forEach{
//            val topicStatistic : TopicStatisticModel = TopicStatisticModel(it)
//            val topic = it
//            val topicQuestionsId : List<Int> = questionsList.filter {  it.tags?.contains(topic) == true }.map { it.questionId }
//            topicQuestionsId.forEach{
//                val id = it
//                topicStatistic.mAskedQuestions += questionResultInformation.filter { it.second == id }.count()
//                topicStatistic.mCorrectAnswers += questionResultInformation.filter { it.second == id && it.first }.count()
//            }
//            mTopicsStatics.add(topicStatistic)
//        }
//        val dack = 3
    }

    fun presentTopicsStatistic() : Fragment{
//        if(!isDataSetup){
//            prepareStatistic()
//        }
        mTopicStatistic.showTopicStatistic(mTopicsStatics)
        return mTopicStatistic as Fragment
    }
}
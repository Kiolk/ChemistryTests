package com.github.kiolk.chemistrytests.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.models.QuestionsDataBaseInfo
import com.github.kiolk.chemistrytests.data.presenters.AppInformationPresenterImpl

class AppInformationViewFragment : Fragment(), com.github.kiolk.chemistrytests.ui.fragments.AppInformationView {

    lateinit var presenter: AppInformationPresenterImpl

    override fun setAppInfo(dataBaseInfo: QuestionsDataBaseInfo) {
        view?.findViewById<TextView>(R.id.data_base_version_text_view)?.text = dataBaseInfo.version.toString()
    }

    override fun showProgressBar(show: Boolean) {
        val progressBar = view?.findViewById<ProgressBar>(R.id.db_information_progress_bar)
        val cardView = view?.findViewById<CardView>(R.id.db_information_card_view)
        if (show) {
            cardView?.visibility = View.GONE
            progressBar?.visibility = View.VISIBLE
        } else {
            progressBar?.visibility = View.GONE
            cardView?.visibility = View.VISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = AppInformationPresenterImpl(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_database_information, null)
        return view
    }

}

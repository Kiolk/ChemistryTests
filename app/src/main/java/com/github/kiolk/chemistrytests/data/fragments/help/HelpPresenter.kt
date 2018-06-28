package com.github.kiolk.chemistrytests.data.fragments.help

import android.support.v4.app.Fragment
import com.github.kiolk.chemistrytests.data.asynctasks.ResultCallback
import com.github.kiolk.chemistrytests.data.models.TestFragmentModel

class HelpPresenter(var helpView : HelpMvp) : HelpMvpPresenter, ResultCallback{

    override fun <T> onSuccess(any: T?) {
        helpView.showHelpInformation(listOf(TestFragmentModel("First", Fragment()), TestFragmentModel("Second", Fragment()),
                TestFragmentModel("Third", Fragment())))
    }

    override fun onError() {
    }

    override fun executeHelpInformation() {
        onSuccess("Success")
    }
}
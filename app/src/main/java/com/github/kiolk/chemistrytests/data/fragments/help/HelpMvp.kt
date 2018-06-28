package com.github.kiolk.chemistrytests.data.fragments.help

import com.github.kiolk.chemistrytests.data.models.TestFragmentModel

interface HelpMvp {

    fun showHelpInformation(listFragment : List<TestFragmentModel>)

}
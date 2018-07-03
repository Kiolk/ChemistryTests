package com.github.kiolk.chemistrytests.ui.activities.main

class MainPresenter(var view : MainMvp) : MainPrasenterMvp{

    override fun resetTestHistory() {
        view.showMassageResult("Reset tests history")
    }
}
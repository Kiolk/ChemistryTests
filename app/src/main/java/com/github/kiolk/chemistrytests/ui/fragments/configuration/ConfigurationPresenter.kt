package com.github.kiolk.chemistrytests.ui.fragments.configuration

import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.managers.DataManager
import com.github.kiolk.chemistrytests.data.managers.PrefGetter
import com.github.kiolk.chemistrytests.data.models.LanguageModel
import com.github.kiolk.chemistrytests.data.models.MenuItemModel

class ConfigurationPresenter(private var configurationView : ConfigurationMvpView) : ConfigurationMvpPresenter{

    override fun prepareAccentColorDialog() {
      configurationView.showAccentColorDialog()
    }

    override fun getLanguagePrefix(which: Int): String {
        return when(which){
            0 -> "en"
            1 -> "ru"
            2 -> "be"
            else -> "en"
        }
    }


    override fun prepareLanguageDialog() {
        val languageArray = DataManager.instance.getResources()?.getStringArray(R.array.LANGUAGE_ARRAY)
        val checkedLanguage : Int = when(DataManager.instance.getInterfaceLanguage().languagePrefix){
            "en" -> 0
            "ru" -> 1
            "be" -> 2
            else -> 0
        }
        languageArray?.let { configurationView.showLanguageDialog(it, checkedLanguage) }
    }

    override fun saveLanguage(item : Int) {
        val selectLanguage : String = when(item){
            0 -> "en"
            1 -> "ru"
            2 -> "be"
            else -> "en"
        }
       DataManager.instance.saveInterfaceLanguage(LanguageModel(selectLanguage, true))
    }

    override fun
            prepareSettings() {
        val resourseDrawableid  = DataManager.instance.getResources()?.obtainTypedArray(R.array.settings_items)
        val titles = DataManager.instance.getResources()?.getStringArray(R.array.SETTINGS_TITLE_ARRAY)
        val items : MutableList<MenuItemModel> = mutableListOf()
        var cnt : Int = 0
        titles?.forEach { items.add(MenuItemModel(resourseDrawableid?.getDrawable(cnt), titles!![cnt]))
        cnt++}
//        val itemsArray: List<MenuItemModel> = List(titles?.size, init -> )
//                listOf(MenuItemModel(R.drawable.ic_translation, "Language"),
//                MenuItemModel(R.drawable.ic_study_notes, "Second"),
//                MenuItemModel(R.drawable.ic_puzzle, "Third"),
//                MenuItemModel(R.drawable.ic_history, "Fourth"),
//                MenuItemModel(R.drawable.ic_statistic, "Fifth"))
        configurationView.setupListSettings(items)
    }

    override fun prepareDayNightModeDialog() {
        configurationView.showDayNightModeDialog()
    }
}
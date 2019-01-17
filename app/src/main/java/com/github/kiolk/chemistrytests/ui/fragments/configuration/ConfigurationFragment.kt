package com.github.kiolk.chemistrytests.ui.fragments.configuration

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import changeLocale
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.adapters.MenuCustomArrayAdapter
import com.github.kiolk.chemistrytests.ui.fragments.BaseFragment
import com.github.kiolk.chemistrytests.data.models.MenuItemModel
import com.github.kiolk.chemistrytests.providers.ThemeProvider
import com.github.kiolk.chemistrytests.providers.ThemeProvider.DAY_MODE
import com.github.kiolk.chemistrytests.providers.ThemeProvider.GREEN
import com.github.kiolk.chemistrytests.providers.ThemeProvider.NIGHT_MODE
import com.github.kiolk.chemistrytests.providers.ThemeProvider.RED
import com.github.kiolk.chemistrytests.providers.ThemeProvider.YELLOW
import com.github.kiolk.chemistrytests.ui.activities.MainActivity
import com.github.kiolk.chemistrytests.utils.Constants.EMPTY_STRING
import com.github.kiolk.chemistrytests.utils.Constants.SELECT_SYMBOL

class ConfigurationFragment : BaseFragment(), ConfigurationMvpView {

    override val menuId: Int?
        get() = null


    var mPresenter = ConfigurationPresenter(this)

    override fun setupListSettings(itemsArray: List<MenuItemModel>) {
        setupToolBar()
        val listView = view?.findViewById<ListView>(R.id.stetting_list_view)
        val adapter = context?.let { MenuCustomArrayAdapter(it, R.layout.item_navigation_menu, itemsArray) }
        listView?.adapter = adapter
        listView?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            when (position) {
                0 -> {
                    Toast.makeText(context, "Change Language", Toast.LENGTH_SHORT).show()
                    mPresenter.prepareLanguageDialog()
                }
                1 -> {
                    mPresenter.prepareAccentColorDialog()
                }
                2 -> {
                    mPresenter.prepareDayNightModeDialog()
//                    context?.let { PrefSetter.putString(it, THEME_MODE, NIGHT_MODE.toString()) }
                }
                else -> {

                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_list_configuration, null)
        return view
    }

    override val titleId: Int
        get() = R.string.CONFIGURATION


    override fun showLanguageDialog(languageArray: Array<String>, checkedLanguage: Int) {
        var position = 0
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
        dialogBuilder.setTitle(resources.getString(R.string.SELECT_LANGUAGE))
                .setSingleChoiceItems(languageArray, checkedLanguage) { dialog, which ->
                    position = which
                }
        dialogBuilder.setPositiveButton(resources.getString(R.string.RESTART_APLICTION)) { dialog, which ->
            val activity = activity as MainActivity
            mPresenter.saveLanguage(position)
            context?.let { changeLocale(it, mPresenter.getLanguagePrefix(position)) }
            activity.restart()
        }
        val dialog = dialogBuilder.create()
        dialog.show()
    }

    override fun showAccentColorDialog() {
//        val savedColor = context?.let { LanguageProvider.getThemeAccent(it) }
        val savedColor = context?.let { ThemeProvider.getThemeAccent(it) }
        val inflater = layoutInflater
        val selectColorView = inflater.inflate(R.layout.dialog_accent_color, null)
        val redButton = selectColorView.findViewById<Button>(R.id.red_button)
        val yellowButton = selectColorView.findViewById<Button>(R.id.yellow_button)
        val greenButton = selectColorView.findViewById<Button>(R.id.green_button)
        var color = RED

        when (savedColor) {
            RED -> redButton.text = SELECT_SYMBOL
            YELLOW -> yellowButton.text = SELECT_SYMBOL
            GREEN -> greenButton.text = SELECT_SYMBOL
        }

        val listener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                cleanButtons()
                when (v?.id) {
                    R.id.red_button -> {
                        color = RED
                        redButton.text = SELECT_SYMBOL
//                        context?.let { ThemeProvider.setAccentColor(it, RED) }
                    }
                    R.id.yellow_button -> {
//                        yellowButton.setTag(1, YELLOW)
                        color = YELLOW
//                        cleanButtons()
                        yellowButton.text = SELECT_SYMBOL
//                        context?.let { ThemeProvider.setAccentColor(it, YELLOW) }
                    }
                    R.id.green_button -> {
                        color = GREEN
                        greenButton.text = SELECT_SYMBOL
                    }
                }
            }

            private fun cleanButtons() {
                redButton.text = EMPTY_STRING
                yellowButton.text = EMPTY_STRING
                greenButton.text = EMPTY_STRING
            }
        }
        yellowButton.setOnClickListener(listener)
        redButton.setOnClickListener(listener)
        greenButton.setOnClickListener(listener)
        val alertBuilder = AlertDialog.Builder(context)
        alertBuilder.setTitle(context?.resources?.getString(R.string.SELECT_ACCENT_COLOR))
        alertBuilder.setView(selectColorView)
        alertBuilder.setPositiveButton(context?.resources?.getString(R.string.ACCEPT_COLOR), object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
//                val color
                context?.let { ThemeProvider.setAccentColor(it, color) }
                val activity = activity as MainActivity
                activity.restart()
            }
        })
        val dialog = alertBuilder.create()
        dialog.show()
    }

    override fun showDayNightModeDialog() {
        val modeView = layoutInflater.inflate(R.layout.dialog_day_night_mode, null)
        val dayButton: ImageView = modeView.findViewById(R.id.day_mode_button)
        val nightButton: ImageView = modeView.findViewById(R.id.night_mode_button)
        val actualMode: Int = context?.let { ThemeProvider.getThemeMode(it) } ?: DAY_MODE
        var dayNightModeValue: Int = actualMode

        fun setButtonElevation(selectedView: View, deselectedView: View) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                deselectedView.elevation = resources.getDimension(R.dimen.ZERO_ELEVATION)
                selectedView.elevation = resources.getDimension(R.dimen.BLOCK_PADDING)
            }
        }

        when (actualMode) {
            DAY_MODE -> {
                setButtonElevation(dayButton, nightButton)
            }
            NIGHT_MODE -> {
                setButtonElevation(nightButton, dayButton)
            }
        }


        val listener: View.OnClickListener = View.OnClickListener { v ->
            when (v?.id) {
                R.id.day_mode_button -> {
                    dayNightModeValue = DAY_MODE
                    setButtonElevation(dayButton, nightButton)
                }
                R.id.night_mode_button -> {
                    dayNightModeValue = NIGHT_MODE
                    setButtonElevation(nightButton, dayButton)
                }
            }
        }

        dayButton.setOnClickListener(listener)
        nightButton.setOnClickListener(listener)

        val alertBuilder = AlertDialog.Builder(context)
        alertBuilder.setTitle(context?.resources?.getString(R.string.SELECT_DAY_NIGHT_MODE))
        alertBuilder.setView(modeView)
        alertBuilder.setPositiveButton(context?.resources?.getString(R.string.ACCEPT_MODE)) { dialog, which ->
            context?.let { ThemeProvider.setNightMode(it, dayNightModeValue) }
            val activity = activity as MainActivity
            activity.restart()
        }
        val dialog = alertBuilder.create()
        dialog.show()
    }
}
package com.github.kiolk.chemistrytests.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.models.Account.BASIC_ACCOUNT
import com.github.kiolk.chemistrytests.data.models.TestInfo
import com.github.kiolk.chemistrytests.providers.UserAccountProvider

class TestDescriptionFragment : Fragment(){

    var mTestInfo = TestInfo()
    var isSaveTest : Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_test_description, null)
        val checkBox =  view.findViewById<CheckBox>(R.id.save_test_check_box)
        checkBox.setOnClickListener { isSaveTest = checkBox.isChecked }
        if(context?.let { UserAccountProvider.getAccountType(it) } ?: 0 >= BASIC_ACCOUNT){
            checkBox.visibility = View.INVISIBLE
        }
        return view
    }

    fun setDescription(testInfo: TestInfo) {
        view?.findViewById<EditText>(R.id.custom_test_title_edit_text)?.setText(testInfo.testTitle)
        view?.findViewById<EditText>(R.id.custom_test_description_edit_text)?.setText(testInfo.testDescription)
        view?.findViewById<CheckBox>(R.id.save_test_check_box)?.isChecked = isSaveTest
    }

    fun getTestInfo(): TestInfo {
        mTestInfo.testTitle = view?.findViewById<EditText>(R.id.custom_test_title_edit_text)?.text?.toString() ?: "TestTest"
        mTestInfo.testDescription = view?.findViewById<EditText>(R.id.custom_test_description_edit_text)?.text?.toString() ?: "DescriptionDescription"
        return mTestInfo
    }
}
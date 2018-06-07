package com.github.kiolk.chemistrytests.data.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.models.TestInfo

class TestDescriptionFragment : Fragment(){

    var mTestInfo = TestInfo()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_test_description, null)
        return view //super.onCreateView(inflater, container, savedInstanceState)
    }

    fun setDescription(testInfo: TestInfo) {
        view?.findViewById<EditText>(R.id.custom_test_title_edit_text)?.setText(testInfo.testTitle)
        view?.findViewById<EditText>(R.id.custom_test_description_edit_text)?.setText(testInfo.testDescription)
    }

    fun getTestInfo(): TestInfo {
        mTestInfo.testTitle = view?.findViewById<EditText>(R.id.custom_test_title_edit_text)?.text?.toString() ?: "TestTest"
        mTestInfo.testDescription = view?.findViewById<EditText>(R.id.custom_test_description_edit_text)?.text?.toString() ?: "DescriptionDescription"
        return mTestInfo
    }
}
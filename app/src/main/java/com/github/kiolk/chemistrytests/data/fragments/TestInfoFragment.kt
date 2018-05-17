package com.github.kiolk.chemistrytests.data.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.models.Test

class TestInfoFragment : Fragment() {

    val TEST_BUNDLE = "testBundle"
    var mTest: Test? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            mTest = arguments?.getSerializable(TEST_BUNDLE) as? Test
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_test_info, null)
        view?.findViewById<TextView>(R.id.test_title_info_fragment_text_view)?.text = mTest?.params?.testInfo?.testTitle
        view?.findViewById<TextView>(R.id.test_description_text_view)?.text = mTest?.params?.testInfo?.testDescription
        return view ?: super.onCreateView(inflater, container, savedInstanceState)
    }

    fun showTestInfo(test: Test) {
//        view?.findViewById<TextView>(R.id.test_title_info_fragment_text_view)?.text = test.params.testInfo.testTitle
//        view?.findViewById<TextView>(R.id.test_description_text_view)?.text =test.params.testInfo.testDescription
    }

    fun fromInstance(test: Test): TestInfoFragment {
        val bundle: Bundle = Bundle()
        val fragment = TestInfoFragment()
        bundle.putSerializable(TEST_BUNDLE, test)
        fragment.arguments = bundle
        return fragment
    }
}
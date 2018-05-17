package com.github.kiolk.chemistrytests.data.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.models.*
import com.github.kiolk.chemistrytests.data.models.CloseQuestion.Question.INPUT_CHOICE
import com.github.kiolk.chemistrytests.data.models.CloseQuestion.Question.MULTIPLE_CHOICE
import com.github.kiolk.chemistrytests.data.models.CloseQuestion.Question.SINGLE_CHOICE

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
        setupIcons(view)
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

    private fun setupIcons(view : View){
        val params : LinearLayout.LayoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0F)
//
//                view.findViewById<LinearLayout>(R.id.training_test_icon_linear_layout).layoutParams as LinearLayout.LayoutParams
//        params.weight = 1.0F
        if(mTest?.params?.testType == TRAINING_TEST){
            view.findViewById<LinearLayout>(R.id.training_test_icon_linear_layout).layoutParams = params
        }else if(mTest?.params?.testType == EXAM_TEST){
            view.findViewById<LinearLayout>(R.id.exam_test_type_linear_layout).layoutParams = params
        }
        if (mTest?.params?.direction == FREE_TEST){
            view.findViewById<LinearLayout>(R.id.free_direction_icon_linear_layout).layoutParams = params
        }else if(mTest?.params?.direction == DIRECT_TEST){
            view.findViewById<LinearLayout>(R.id.direct_order_icon_linear_layout).layoutParams = params
        }
        if(mTest?.params?.questionTypes?.contains(SINGLE_CHOICE) == true){
            view.findViewById<LinearLayout>(R.id.single_choice_icon_linear_layout).layoutParams = params
        }
        if(mTest?.params?.questionTypes?.contains(MULTIPLE_CHOICE) == true){
            view.findViewById<LinearLayout>(R.id.multiple_choice_icon_linear_layout).layoutParams = params
        }
        if(mTest?.params?.questionTypes?.contains(INPUT_CHOICE) == true){
            view.findViewById<LinearLayout>(R.id.input_choice_icon_linear_layout).layoutParams = params
        }
    }
}
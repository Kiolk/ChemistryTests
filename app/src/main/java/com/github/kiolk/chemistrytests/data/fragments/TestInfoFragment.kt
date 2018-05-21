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
    var mParams: TestParams? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            mParams = arguments?.getSerializable(TEST_BUNDLE) as? TestParams
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_test_info, null)
        view?.findViewById<TextView>(R.id.test_title_info_fragment_text_view)?.text = mParams?.testInfo?.testTitle
        view?.findViewById<TextView>(R.id.test_description_text_view)?.text = mParams?.testInfo?.testDescription
        setupIcons(view)
        return view ?: super.onCreateView(inflater, container, savedInstanceState)
    }

    fun showTestInfo(test: Test) {
//        view?.findViewById<TextView>(R.id.test_title_info_fragment_text_view)?.text = test.params.testInfo.testTitle
//        view?.findViewById<TextView>(R.id.test_description_text_view)?.text =test.params.testInfo.testDescription
    }

    fun fromInstance(params: TestParams): TestInfoFragment {
        val bundle: Bundle = Bundle()
        val fragment = TestInfoFragment()
        bundle.putSerializable(TEST_BUNDLE, params)
        fragment.arguments = bundle
        return fragment
    }

    private fun setupIcons(view : View){
        val params : LinearLayout.LayoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0F)
//
//                view.findViewById<LinearLayout>(R.id.training_test_icon_linear_layout).layoutParams as LinearLayout.LayoutParams
//        params.weight = 1.0F
        if(mParams?.testType == TRAINING_TEST){
            val iconLayout = view.findViewById<LinearLayout>(R.id.training_test_icon_linear_layout)
            iconLayout.visibility = View.VISIBLE
            iconLayout.layoutParams = params
        }else if(mParams?.testType == EXAM_TEST){
            val iconLayout = view.findViewById<LinearLayout>(R.id.exam_test_type_linear_layout)
            iconLayout.visibility = View.VISIBLE
            iconLayout.layoutParams = params
        }
        if (mParams?.direction == FREE_TEST){
            val iconLayout = view.findViewById<LinearLayout>(R.id.free_direction_icon_linear_layout)
            iconLayout.visibility = View.VISIBLE
            iconLayout.layoutParams = params
        }else if(mParams?.direction == DIRECT_TEST){
            val iconLayout = view.findViewById<LinearLayout>(R.id.direct_order_icon_linear_layout)
            iconLayout.visibility = View.VISIBLE
            iconLayout.layoutParams = params
        }
        if(mParams?.testTimer!=null){
            val iconLayout = view.findViewById<LinearLayout>(R.id.timer_control_icon_linear_layout)
            iconLayout.visibility = View.VISIBLE
            iconLayout.layoutParams = params
        }
        if(mParams?.questionTypes?.contains(SINGLE_CHOICE) == true){
            val iconLayout = view.findViewById<LinearLayout>(R.id.single_choice_icon_linear_layout)
            iconLayout.visibility = View.VISIBLE
            iconLayout.layoutParams = params
        }
        if(mParams?.questionTypes?.contains(MULTIPLE_CHOICE) == true){
            val iconLayout = view.findViewById<LinearLayout>(R.id.multiple_choice_icon_linear_layout)
            iconLayout.visibility = View.VISIBLE
            iconLayout.layoutParams = params
        }
        if(mParams?.questionTypes?.contains(INPUT_CHOICE) == true){
            val iconLayout = view.findViewById<LinearLayout>(R.id.input_choice_icon_linear_layout)
            iconLayout.visibility = View.VISIBLE
            iconLayout.layoutParams = params
        }
    }
}
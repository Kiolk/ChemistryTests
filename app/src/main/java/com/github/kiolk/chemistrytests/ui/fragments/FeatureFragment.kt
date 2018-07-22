package com.github.kiolk.chemistrytests.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.ui.fragments.bases.FeatureBaseFragment

class FeatureFragment : FeatureBaseFragment(){

    companion object {
        val SLIDE_NUMBER : String = "SlideNumber"
        val STUDENT_SLIDE : Int = 0
        val ROCKET_SLIDE : Int = 1
        val IDE_SLIDE : Int = 2
        val TARGET_SLIDE : Int = 3
    }

    var mPosition : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPosition = arguments?.getInt(SLIDE_NUMBER)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_feature, null)
        attachLoginButton(view.findViewById<Button>(R.id.login_feature_fragment_button))
        val description = view?.findViewById<TextView>(R.id.feature_description_text_view)
        description?.visibility = View.VISIBLE
        val drawable = when(mPosition){
            STUDENT_SLIDE -> {
                description?.text = resources.getString(R.string.STUDY_EVRY_WHARE)
                context?.resources?.getDrawable(R.drawable.student)
            }
            ROCKET_SLIDE -> {
                description?.text = resources.getString(R.string.LEAVE_UP_KNOLIDHE)
                context?.resources?.getDrawable(R.drawable.rocket)
            }
            IDE_SLIDE -> {
                description?.text = resources.getString(R.string.TRAINING_YOUR_BRAIN)
                context?.resources?.getDrawable(R.drawable.idea)
            }
            TARGET_SLIDE -> {
                description?.text = resources.getString(R.string.TARGET_FEATURE)
                context?.resources?.getDrawable(R.drawable.target)
            }
            else -> {
                context?.resources?.getDrawable(R.drawable.student)
            }
        }
        view?.findViewById<ImageView>(R.id.feature_image_view)?.setImageDrawable(drawable)
        return view
    }

    fun fromInstance(slidePosition : Int) : Fragment{
        val fragment = FeatureFragment()
        val bundle = Bundle()
        bundle.putInt(SLIDE_NUMBER, slidePosition)
        fragment.arguments = bundle
        return fragment
    }
}
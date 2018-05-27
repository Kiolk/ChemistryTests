package com.github.kiolk.chemistrytests.data.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.models.Course
import kiolk.com.github.pen.Pen

val COURSE_BND : String = "course"

class CoursePageFragment : Fragment() {
    lateinit var mCourse : Course

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCourse = arguments?.getSerializable(COURSE_BND) as Course
//        setupCourseInformation()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_curse_page, null)
        view?.findViewById<Toolbar>(R.id.course_tool_bar)?.title = mCourse.mCourseTitle
        Pen.getInstance().getImageFromUrl(mCourse.mCourseIcon).inputTo(view?.findViewById<ImageView>(R.id.course_icon_image_view))
        view?.findViewById<TextView>(R.id.description_course_text_view)?.text = mCourse.mCourseDescription
        return view ?: super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setupCourseInformation() {

    }

    fun fromeInstance(course: Course): CoursePageFragment {
        val fragment = CoursePageFragment()
        val bundle = Bundle(       )
        bundle.putSerializable(COURSE_BND, course)
        fragment.arguments = bundle
        return fragment
    }
}

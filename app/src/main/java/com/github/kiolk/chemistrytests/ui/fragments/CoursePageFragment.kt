package com.github.kiolk.chemistrytests.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.adapters.AvailableTestRecyclerAdapter
import com.github.kiolk.chemistrytests.data.listeners.OnItemClickListener
import com.github.kiolk.chemistrytests.data.listeners.RecyclerTouchListener
import com.github.kiolk.chemistrytests.data.models.Course
import com.github.kiolk.chemistrytests.ui.activities.TestingActivity
import com.github.kiolk.chemistrytests.utils.Constants.TEST_PARAM_INT
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
        val adapter = context?.let { AvailableTestRecyclerAdapter(it, mCourse.mCoursTestParams, null, mCourse.mCompletedTests) }
        val recyclerView = view?.findViewById<RecyclerView>(R.id.courses_tests_recycler_view)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.adapter = adapter
        context?.let {
            val itemTouchListener = recyclerView?.let { it1 ->
                RecyclerTouchListener(it, it1, object : OnItemClickListener {
                    override fun onClick(view: View, position: Int) {
                        val intent = Intent(context, TestingActivity::class.java)
                        Log.d("MyLogs", "Start test by item $position")
                        intent.putExtra(TEST_PARAM_INT, mCourse.mCoursTestParams[position])
                        startActivity(intent)
                    }

                    override fun onLongClick(view: View, position: Int) {
                    }

                    })
                }
            recyclerView?.addOnItemTouchListener(itemTouchListener)
            }
        return view ?: super.onCreateView(inflater, container, savedInstanceState)
    }


    fun fromInstance(course: Course): CoursePageFragment {
        val fragment = CoursePageFragment()
        val bundle = Bundle(       )
        bundle.putSerializable(COURSE_BND, course)
        fragment.arguments = bundle
        return fragment
    }
}

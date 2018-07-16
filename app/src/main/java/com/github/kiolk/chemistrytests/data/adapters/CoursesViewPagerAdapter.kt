package com.github.kiolk.chemistrytests.data.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import com.github.kiolk.chemistrytests.ui.fragments.CoursePageFragment
import com.github.kiolk.chemistrytests.data.models.Course

class CoursesViewPagerAdapter (var fm : android.support.v4.app.FragmentManager,
                               var courses : MutableList<Course>) : FragmentStatePagerAdapter(fm){
    override fun getItem(position: Int): Fragment {
        return CoursePageFragment().fromInstance(courses[position])
    }

    override fun getCount(): Int {
       return courses.size
    }
}
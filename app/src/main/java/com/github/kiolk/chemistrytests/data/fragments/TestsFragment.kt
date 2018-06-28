package com.github.kiolk.chemistrytests.data.fragments

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.TestsPresenter
import com.github.kiolk.chemistrytests.data.adapters.TestsPageAdapter
import com.github.kiolk.chemistrytests.data.database.DBOperations
import com.github.kiolk.chemistrytests.data.models.TestFragmentModel
import com.github.kiolk.chemistrytests.data.models.TestParams

class TestsFragment : BaseFragment(){

    override val titleId: Int
        get() = R.string.CUSTOM_TEST

    //    lateinit var mListener : ViewPager.OnPageChangeListener
     var mAdapter : TestsPageAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       val view = inflater.inflate(R.layout.fragment_tests, null)
        setupViewPager(view)
        return view //super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setupViewPager(view: View) {
//        val params = DBOperations().getAllTestsParams()
//        params.sortBy { it.testInfo.lasModifed }
//        val lastsTestFragments = LatestTestsFragment()
//        val customUserTestsFragment = CustomUserTestsFragment()
//        val list = listOf(TestFragmentModel("Lasts", lastsTestFragments),
//                TestFragmentModel("Custom", customUserTestsFragment))
//        mListener = object  : ViewPager.OnPageChangeListener{
//            override fun onPageScrollStateChanged(state: Int) {
//            }
//
//            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//            }
//
//            override fun onPageSelected(position: Int) {
////                mAdapter?.mLastsTestFragment?.addLastsTests(list[0])
//            }
//        }
        mAdapter = context?.let { fragmentManager?.let { it1 -> TestsPageAdapter(it, TestsPresenter.getAvailableTestsFragments(it), it1) } }
        val viewPager = view.findViewById<ViewPager>(R.id.available_test_view_pager)
        viewPager?.adapter = mAdapter
//        viewPager?.addOnPageChangeListener(mListener)
        view.findViewById<TabLayout>(R.id.available_test_tab_layout)?.setupWithViewPager(viewPager)
//        setupToolBar(view,  R.id.custom_test_tool_bar)
    }
}
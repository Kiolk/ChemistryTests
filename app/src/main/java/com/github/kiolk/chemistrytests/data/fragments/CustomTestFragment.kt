package com.github.kiolk.chemistrytests.data.fragments

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.adapters.CustomTestPageAdapter
import com.github.kiolk.chemistrytests.data.adapters.CustomTestPageAdapter.Companion.CUSTOM_TEST_PARAMS
import com.github.kiolk.chemistrytests.data.adapters.CustomTestPageAdapter.Companion.QUESTIONS_LIST
import com.github.kiolk.chemistrytests.data.adapters.CustomTestPageAdapter.Companion.TEST_INFORMATION
import com.github.kiolk.chemistrytests.data.database.DBOperations
import com.github.kiolk.chemistrytests.data.models.CloseQuestion
import com.github.kiolk.chemistrytests.data.models.TestInfo
import com.github.kiolk.chemistrytests.ui.activities.MainActivity
import kotlinx.android.synthetic.main.activity_main.*

class CustomTestFragment : BaseFragment() {

    lateinit var mQuestions: MutableList<CloseQuestion>
    lateinit var changeStateListener: ViewPager.OnPageChangeListener
    var customTestAdapter: CustomTestPageAdapter? = null
    var mSelectedQuestions: MutableList<Int>? = null
    var mFromTestInfo: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        mQuestions = DBOperations().getAllQuestions()
        customTestAdapter = fragmentManager?.let { context?.let { it1 -> CustomTestPageAdapter(it1, it) } }
        customTestAdapter?.mCustomFragment?.combineCustomTest(mQuestions)
        view?.findViewById<ViewPager>(R.id.custom_test_view_pager)?.currentItem = 1
        changeStateListener = object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    QUESTIONS_LIST -> customTestAdapter?.mQuestionsListFragment?.setAvailableQuestions(customTestAdapter?.mCustomFragment?.getAvailableQuestions())
                    CUSTOM_TEST_PARAMS -> {
                        customTestAdapter?.mCustomFragment?.combineCustomTest(mQuestions)
                        mSelectedQuestions = customTestAdapter?.mQuestionsListFragment?.setSelectedQuestions()
                        customTestAdapter?.mCustomFragment?.mListQuestionId = mSelectedQuestions ?: mutableListOf(3, 4)
                        if (mFromTestInfo) {
                            customTestAdapter?.mCustomFragment?.mTestInfo = customTestAdapter?.mTestDescriptionFragment?.getTestInfo() ?: TestInfo()
                            mFromTestInfo = false
                        }
                    }
                    TEST_INFORMATION -> {
                        customTestAdapter?.mCustomFragment?.mTestInfo?.let { customTestAdapter?.mTestDescriptionFragment?.setDescription(it) }
                        mFromTestInfo = true
                    }
                }
            }
        }
        super.onCreate(savedInstanceState)
    }

//    private fun setupToolBar(view : View) {
//        val toolBar = view.findViewById<Toolbar>(R.id.custom_test_tool_bar)
//        val navigation = object : View.OnClickListener {
//            override fun onClick(v: View?) {
//                val mainActivity = activity as MainActivity
//                mainActivity.findViewById<DrawerLayout>(R.id.main_drawer_layout).openDrawer(Gravity.START)
//            }
//        }
//        toolBar?.setNavigationOnClickListener(navigation)
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_custom_test, null)
        val viewPager: ViewPager = view.findViewById(R.id.custom_test_view_pager)
        val tabLayout: TabLayout = view.findViewById(R.id.custom_test_tab_layout)
        viewPager.adapter = customTestAdapter
        viewPager.currentItem = 2
//        viewPager.currentItem = 1
        viewPager.addOnPageChangeListener(changeStateListener)
        tabLayout.setupWithViewPager(viewPager)
        setupToolBar(view, R.id.custom_test_tool_bar)
//        customTestAdapter?.mCustomFragment?.combineCustomTest(mQuestions)
        return view //super.onCreateView(inflater, container, savedInstanceState)
    }
}

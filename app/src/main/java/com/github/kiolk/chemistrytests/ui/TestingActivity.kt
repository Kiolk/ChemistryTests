package com.github.kiolk.chemistrytests.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.models.Test
import com.github.kiolk.chemistrytests.data.TestingPagerAdapter
import getTrainingTets
import kotlinx.android.synthetic.main.activity_testing.*

class TestingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_testing)
        val adapter = TestingPagerAdapter(supportFragmentManager, getTrainingTets())
        testing_view_pager.adapter = adapter
        questions_tab_layout.setupWithViewPager(testing_view_pager)
    }
}

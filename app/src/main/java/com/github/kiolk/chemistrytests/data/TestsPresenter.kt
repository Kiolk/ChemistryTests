package com.github.kiolk.chemistrytests.data

import android.content.Context
import android.support.v7.widget.RecyclerView
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.adapters.TestsPageAdapter.Companion.CUSTOM_TESTS
import com.github.kiolk.chemistrytests.data.adapters.TestsPageAdapter.Companion.LASTS_TEST
import com.github.kiolk.chemistrytests.data.asynctasks.ResultCallback
import com.github.kiolk.chemistrytests.data.asynctasks.SingleAsyncTask
import com.github.kiolk.chemistrytests.data.asynctasks.SingleExecut
import com.github.kiolk.chemistrytests.data.database.DBOperations
import com.github.kiolk.chemistrytests.data.executs.AddCustomTestInUserDB
import com.github.kiolk.chemistrytests.data.executs.GetDataFromDb
import com.github.kiolk.chemistrytests.data.fragments.CustomUserTestsFragment
import com.github.kiolk.chemistrytests.data.fragments.LatestTestsFragment
import com.github.kiolk.chemistrytests.data.models.TestFragmentModel
import com.github.kiolk.chemistrytests.data.models.TestParams

class TestsPresenter {

    companion object {

        fun getAvailableTestsFragments(context : Context): List<TestFragmentModel> {
            val params = DBOperations().getAllTestsParams()
            params.sortBy { it.testInfo.lasModifed }
            val lastsTestFragments = LatestTestsFragment()
            val customUserTestsFragment = CustomUserTestsFragment()
            val testTitles = context.resources.getStringArray(R.array.TEST_TITLES)
            return listOf(TestFragmentModel(testTitles[LASTS_TEST], lastsTestFragments),
                    TestFragmentModel(testTitles[CUSTOM_TESTS], customUserTestsFragment))
        }

        fun saveCustomTest(params: TestParams) {
            SingleAsyncTask().execute(AddCustomTestInUserDB(params, object : ResultCallback {
                override fun <T> onSuccess(any: T?) {
                }

                override fun onError() {
                }

            }))
        }

        fun setAvailableTests(callback :ResultCallback) {
            SingleAsyncTask().execute(GetDataFromDb(callback))
        }
    }
}

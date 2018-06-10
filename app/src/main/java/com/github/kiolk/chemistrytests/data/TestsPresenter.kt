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
import com.github.kiolk.chemistrytests.data.fragments.FavaoriteTestFragment
import com.github.kiolk.chemistrytests.data.fragments.LastModifiedFragment
import com.github.kiolk.chemistrytests.data.fragments.LatestTestsFragment
import com.github.kiolk.chemistrytests.data.models.Test
import com.github.kiolk.chemistrytests.data.models.TestFragmentModel
import com.github.kiolk.chemistrytests.data.models.TestParams

interface OnTestCallback {
    fun <T> onSuccess(any: T? = null)
}

class TestsPresenter {

    companion object {

        fun getAvailableTestsFragments(context: Context): List<TestFragmentModel> {
            val params = DBOperations().getAllTestsParams()
            params.sortBy { it.testInfo.lasModifed }
            val lastsTestFragments = LatestTestsFragment()
            val customUserTestsFragment = CustomUserTestsFragment()
            val lastModifiedFragment = LastModifiedFragment()
            val favoriteTestFragment = FavaoriteTestFragment()
            val testTitles = context.resources.getStringArray(R.array.TEST_TITLES)
            return listOf(TestFragmentModel(testTitles[LASTS_TEST], lastsTestFragments),
                    TestFragmentModel(testTitles[CUSTOM_TESTS], customUserTestsFragment),
                    TestFragmentModel(testTitles[2], lastModifiedFragment),
                    TestFragmentModel(testTitles[3], favoriteTestFragment))
        }

        fun saveCustomTest(params: TestParams) {
            SingleAsyncTask().execute(AddCustomTestInUserDB(params, object : ResultCallback {
                override fun <T> onSuccess(any: T?) {
                }

                override fun onError() {
                }

            }))
        }

        fun setAvailableTests(callback: ResultCallback) {
            SingleAsyncTask().execute(GetDataFromDb(callback))
        }

        fun getLastModifiedTests(context: Context, onTestCallback: OnTestCallback) {
            SingleAsyncTask().execute(GetDataFromDb(object : ResultCallback {
                override fun <T> onSuccess(any: T?) {
                    val tests: MutableList<TestParams> = any as MutableList<TestParams>
                    val actualTime = System.currentTimeMillis()
                    val lastUpdatedTests: MutableList<TestParams> = mutableListOf()
                    val weekAgoUpdate = actualTime - (60 * 1000 * 60 * 24 * 7)
                    tests.forEach {
                        if (it.testInfo.lasModifed > weekAgoUpdate) {
                            lastUpdatedTests.add(it)
                        }
                    }

                    onTestCallback.onSuccess(lastUpdatedTests)
                }

                override fun onError() {
                }
            }))
        }

        fun getFavoriteTests(onTestCallback: OnTestCallback) {
            SingleAsyncTask().execute(GetDataFromDb(object : ResultCallback {
                override fun <T> onSuccess(any: T?) {
                    val tests: MutableList<TestParams> = any as MutableList<TestParams>
                    val lastUpdatedTests: MutableList<TestParams> = mutableListOf()
                    tests.forEach {

                    }

                    onTestCallback.onSuccess(lastUpdatedTests)
                }

                override fun onError() {
                }
            }))
        }
    }
}

package com.github.kiolk.chemistrytests.data.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.adapters.AvailableTestRecyclerAdapter
import com.github.kiolk.chemistrytests.data.database.DBOperations
import com.github.kiolk.chemistrytests.data.fragments.bases.RecyclerTestBaseFragment
import com.github.kiolk.chemistrytests.data.listeners.OnItemClickListener
import com.github.kiolk.chemistrytests.data.models.User
import com.github.kiolk.chemistrytests.ui.activities.TEST_PARAM_INT
import com.github.kiolk.chemistrytests.ui.activities.TestingActivity
import com.google.firebase.auth.FirebaseAuth
import reversSort

class CustomUserTestsFragment : RecyclerTestBaseFragment(){
    override val titleId: Int
        get() = R.string.CUSTOM_TEST


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_custom_user_tests, null)
        val tests = getCurrentUser()?.userCustomTests?.let { reversSort(it) }
        val adapter = context?.let { AvailableTestRecyclerAdapter(it, tests, null) }
        val listener = object  : OnItemClickListener {
            override fun onClick(view: View, position: Int) {
                val intent = Intent(context, TestingActivity::class.java)
                Log.d("MyLogs", "Start test by item $position")
                intent.putExtra(TEST_PARAM_INT, tests?.get(position))
                startActivity(intent)
            }

            override fun onLongClick(view: View, position: Int) {

            }
        }
        attachRecyclerView(view, R.id.custom_user_tests_recycler_view, adapter, listener )


        return view //super.onCreateView(inflater, container, savedInstanceState)
    }
}

fun getCurrentUser() : User? {
    val users = DBOperations().getAllUsers()
    val current = FirebaseAuth.getInstance().currentUser
    return users.find { it.userId == current?.uid }
}
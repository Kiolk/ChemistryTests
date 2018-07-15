package com.github.kiolk.chemistrytests.data.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.adapters.TestsBaseAdapter
import com.github.kiolk.chemistrytests.data.models.TestParams
import com.github.kiolk.chemistrytests.ui.activities.TestingActivity
import com.github.kiolk.chemistrytests.utils.Constants.TESTS_CHILD
import com.github.kiolk.chemistrytests.utils.Constants.TEST_PARAM_INT
import com.google.firebase.database.*

class AvaliableFragments : Fragment(){

    var mTests : MutableList<TestParams> = mutableListOf()
    lateinit var mFirebaseDatabase : FirebaseDatabase
    lateinit var mDatabaseReference : DatabaseReference
    lateinit var mChildEventListener : ChildEventListener
    var adapter : TestsBaseAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewFragment = inflater.inflate(R.layout.fragment_chose_test, null)
        adapter = TestsBaseAdapter(inflater.context, mTests)
        viewFragment.findViewById<ListView>(R.id.test_params_list_view).adapter = adapter
        viewFragment.findViewById<ListView>(R.id.test_params_list_view).onItemClickListener = object : AdapterView.OnItemClickListener{
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val intent = Intent(context, TestingActivity::class.java)
                intent.putExtra(TEST_PARAM_INT, mTests[position])
                startActivity(intent)
                activity?.onBackPressed()
            }
        }
        return viewFragment ?: super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mFirebaseDatabase.reference.child(TESTS_CHILD)
        mChildEventListener = object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                    val testParam = p0?.getValue(TestParams::class.java)
                    testParam?.let { mTests.add(it) }
                adapter?.notifyDataSetChanged()
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
        mDatabaseReference.addChildEventListener(mChildEventListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mDatabaseReference.removeEventListener(mChildEventListener)
    }
}
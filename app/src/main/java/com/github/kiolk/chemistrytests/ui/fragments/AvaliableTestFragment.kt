package com.github.kiolk.chemistrytests.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.adapters.AvailableTestRecyclerAdapter
import com.github.kiolk.chemistrytests.data.database.DBOperations
import com.github.kiolk.chemistrytests.data.listeners.OnItemClickListener
import com.github.kiolk.chemistrytests.data.listeners.RecyclerTouchListener
import com.github.kiolk.chemistrytests.data.models.TestParams
import com.github.kiolk.chemistrytests.ui.activities.TestingActivity
import com.github.kiolk.chemistrytests.utils.Constants.TESTS_CHILD
import com.github.kiolk.chemistrytests.utils.Constants.TEST_PARAM_INT
import com.google.firebase.database.*
import reversSort

class AvaliableTestFragment : Fragment(){

    var mTests : MutableList<TestParams> = mutableListOf()
    lateinit var mFirebaseDatabase : FirebaseDatabase
    lateinit var mDatabaseReference : DatabaseReference
    lateinit var mChildEventListener : ChildEventListener
    var mRecyclerAdapter : AvailableTestRecyclerAdapter? = null
    var mIsTestStart : Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_avaliable_tests, null)
        mTests = DBOperations().getAllTestsParams()
        mTests.sortBy { it.testInfo.lasModifed }
        mTests = reversSort(mTests)
        mRecyclerAdapter = context?.let { AvailableTestRecyclerAdapter(it, mTests) }
        val recycler = view.findViewById<RecyclerView>(R.id.available_tests_recycler_view)
        recycler.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false)
        recycler.adapter = mRecyclerAdapter
        recycler.addOnItemTouchListener(RecyclerTouchListener(context, recycler, object : OnItemClickListener{
            override fun onClick(view: View, position: Int) {
                val intent = Intent(context, TestingActivity::class.java)
                    Log.d("MyLogs", "Start test by item $position")
                    intent.putExtra(TEST_PARAM_INT, mTests[position])
                    startActivity(intent)
                    mIsTestStart = true
            }

            override fun onLongClick(view: View, position: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }))
//        recycler.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener{
//
//            override fun onTouchEvent(rv: RecyclerView?, e: MotionEvent?) {
//                val x : Float? = e?.x
//                val y : Float? = e?.y
//                val child = x?.let { y?.let { it1 -> rv?.findChildViewUnder(it, it1) } }
//                val position = rv?.getChildAdapterPosition(child) ?: 0
//                val intent = Intent(context, TestingActivity::class.java)
////                activity?.onBackPressed()
//                if(!mIsTestStart && position < mTests.size && position >= 0) {
//                    Log.d("MyLogs", "Start test by item $position")
//                    intent.putExtra(TEST_PARAM_INT, mTests[position])
//                    startActivity(intent)
//                    mIsTestStart = true
//                }
//            }
//
//            override fun onInterceptTouchEvent(rv: RecyclerView?, e: MotionEvent?): Boolean {
//                return false
//            }
//
//            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
//
//            }
//        })
        return view ?: super.onCreateView(inflater, container, savedInstanceState)
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
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
//                val testParam = p0?.getValue(TestParams::class.java)
//                testParam?.let { mTests.add(it) }
//                if(testParam?.testId==3) {
//                    mTests = DBOperations().getAllTestsParams()
//                    Log.d("MyLogs", testParam?.testInfo?.testTitle)
//                    mRecyclerAdapter?.notifyDataSetChanged()
//                }
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
        mTests = mutableListOf()
    }

    override fun onResume() {
        super.onResume()
        if(mIsTestStart) mIsTestStart = false
    }
}
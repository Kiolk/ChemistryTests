package com.github.kiolk.chemistrytests.data.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.kiolk.chemistrytests.R
import com.github.kiolk.chemistrytests.data.adapters.recyclers.TheoryRecyclerAdapter
import com.github.kiolk.chemistrytests.data.asynctasks.ResultCallback
import com.github.kiolk.chemistrytests.data.asynctasks.SingleAsyncTask
import com.github.kiolk.chemistrytests.data.executs.GetTheoryFromDB
import com.github.kiolk.chemistrytests.data.listeners.OnItemClickListener
import com.github.kiolk.chemistrytests.data.listeners.RecyclerTouchListener
import com.github.kiolk.chemistrytests.data.models.ChemTheoryModel

class ChemTheoryFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chem_theory, null)
        return view
    }

    fun setTheory(listTheoryId: List<Long>) {
        val listener = object : ResultCallback{
            override fun <T> onSuccess(any: T?) {
                val list = any as List<ChemTheoryModel>
                val adapter = context?.let { TheoryRecyclerAdapter(it, list) }
                val recycler = view?.findViewById<RecyclerView>(R.id.theory_chem_recycler_view)
                recycler?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                recycler?.adapter = adapter
                context.let {
                    val itemTouchListener = recycler?.let { it1 ->
                        RecyclerTouchListener(it, it1, object : OnItemClickListener {
                            override fun onClick(view: View, position: Int) {
                            }

                            override fun onLongClick(view: View, position: Int) {
                            }
                        })
                    }
                    recycler?.addOnItemTouchListener(itemTouchListener)
                }
            }

            override fun onError() {
            }
        }
        SingleAsyncTask().execute(GetTheoryFromDB(listTheoryId, listener))
    }
}

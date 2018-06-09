package com.github.kiolk.chemistrytests.data.listeners

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

interface OnItemClickListener{
    fun onClick(view : View, position : Int)
    fun onLongClick(view: View, position: Int)
}

 class RecyclerTouchListener(var context : Context?, var recycler : RecyclerView, var onItemClickListener: OnItemClickListener) : RecyclerView.OnItemTouchListener{
        var detector : GestureDetector
    init {
        detector = object : GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent?): Boolean {
                return true
            }

            override fun onLongPress(e: MotionEvent?) {
                val view = e?.x?.let { recycler.findChildViewUnder(it, e.y) }
                if (view != null) {
                    onItemClickListener.onLongClick(view, recycler.getChildAdapterPosition(view))
                }
            }
        }){

        }
    }

    override fun onTouchEvent(rv: RecyclerView?, e: MotionEvent?) {
    }

    override fun onInterceptTouchEvent(rv: RecyclerView?, e: MotionEvent?): Boolean {
        val view = e?.x?.let { rv?.findChildViewUnder(it, e?.y) }
        if(view != null && detector.onTouchEvent(e)){
            rv?.getChildAdapterPosition(view)?.let { onItemClickListener.onClick(view, it) }
            return true
        }
        return false
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
    }
}

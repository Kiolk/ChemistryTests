package com.github.kiolk.chemistrytests.ui.customviews

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

class ControlViewPager(context: Context) : ViewPager(context){

    var isPaginationEnabled : Boolean = true

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return isPaginationEnabled && super.onTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return isPaginationEnabled && super.onInterceptTouchEvent(ev)
    }

    fun disablePaging(isPaging : Boolean){
        isPaginationEnabled = isPaging
    }
}

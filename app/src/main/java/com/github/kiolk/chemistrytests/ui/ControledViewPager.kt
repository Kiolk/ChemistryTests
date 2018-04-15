package com.github.kiolk.chemistrytests.ui

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent


class ControledViewPager(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {

    var isEnable: Boolean = true


    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (this.isEnable) {
            super.onTouchEvent(event)
        } else false

    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return if (this.isEnable) {
            super.onInterceptTouchEvent(event)
        } else false

    }

    fun setPagingEnabled(isEnable: Boolean) {
        this.isEnable = isEnable
    }
}
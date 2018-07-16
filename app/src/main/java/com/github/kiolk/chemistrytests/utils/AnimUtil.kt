package com.github.kiolk.chemistrytests.utils

import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPropertyAnimatorListener
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.view.View

fun animOut(view: View) {
    ViewCompat.animate(view)
            .scaleX(0.0F)
            .scaleY(0.0F)
            .alpha(0.0F)
            .setInterpolator(FastOutSlowInInterpolator())
            .withLayer()
            .setListener(object : ViewPropertyAnimatorListener {
                override fun onAnimationEnd(view: View?) {
                    view?.visibility = View.GONE
                }

                override fun onAnimationCancel(view: View?) {
                }

                override fun onAnimationStart(view: View?) {

                }
            })
            .start()
}

fun animIn(view: View) {
    view.visibility = View.VISIBLE
    ViewCompat.animate(view)
            .scaleX(1.0F)
            .scaleY(1.0F)
            .alpha(1.0F)
            .setInterpolator(FastOutSlowInInterpolator())
            .withLayer()
            .setListener(object : ViewPropertyAnimatorListener {
                override fun onAnimationEnd(view: View?) {
                }

                override fun onAnimationCancel(view: View?) {
                }

                override fun onAnimationStart(view: View?) {

                }
            })
            .start()
}